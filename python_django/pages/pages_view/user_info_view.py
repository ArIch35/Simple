from django.shortcuts import render
from django.views.generic import TemplateView
from .common_functions import BASE_URL, generate_data_json, send_post_request

class UserInformationView(TemplateView):
    template_name = 'pages/kundendaten.html'

    def post(self, request, _, **kwargs):
        context = super().get_context_data(**kwargs)
        context["data"] = generate_data_json(request)
        context["message"] = self.get_alert_message(request, context)
        return render(request, 'pages/kundendaten.html', context)

    def get_alert_message(self, request, context):
        if request.POST.get('firstname') == '':
            return "Bitte geben Sie Ihren Vornamen ein"
        if request.POST.get('lastname') == '':
            return "Bitte geben Sie Ihren Nachnamen ein"
        if request.POST.get('email') == '':
            return "Bitte geben Sie eine gültige Email ein"
        if request.POST.get('postcode') == '':
            return "Bitte geben Sie Ihre Postleitzahl ein"
        if request.POST.get('city') == '':
            return "Bitte geben Sie Ihre Stadt ein"
        if request.POST.get('address') == '':
            return "Bitte geben Sie Ihre Adresse ein"
        status_code = send_post_request(f'{BASE_URL}/users/',request.COOKIES.get('csrftoken'), self.generate_request_body(request))
        if status_code != 201:
            return self.get_post_result_message(status_code)
        context["data"] = {}
        return "Nutzerdaten erfolgreich gespeichert"
    
    def get_post_result_message(self, status_code):
        if status_code == 401:
            return "Email bereits vergeben, bitte wählen Sie eine andere Email"
        if status_code == 400:
            return "Fehlerhafte Nutzerdaten, bitte überprüfen Sie Ihre Eingaben"
        return "Nutzerdaten konnten nicht gespeichert werden"
    
    def generate_request_body(self, request):
        request_body = '{\n'
        items = list(request.POST.items())
        for index, (key, value) in enumerate(items):
            if key != 'csrfmiddlewaretoken':
                request_body += f'"{key}": "{value}",\n' if index != len(items) - 1 else f'"{key}": "{value}"\n}}'
        return request_body