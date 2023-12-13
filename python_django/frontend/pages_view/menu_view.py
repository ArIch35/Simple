import requests
from django.shortcuts import render
from django.views.generic import TemplateView
from .common_functions import BASE_URL, generate_data_json, send_post_request

class MenuView(TemplateView):
    template_name = 'pages/speisekarte.html'
    selected_pizzas = []
    
    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        response = requests.get(f'{BASE_URL}/pizzas/')
        context['pizzas'] = response.json() if response.status_code == 200 else []
        return context
    
    def post(self, request, *args, **kwargs):
        context = self.get_context_data(**kwargs)
        self.get_selected_pizzas(request)
        context["data"] = generate_data_json(request)
        context["message"] = self.get_alert_message(request, context)
        return render(request, 'pages/speisekarte.html', context)
    
    def get_alert_message(self, request, context):
        if request.POST.get('time') == '':
            return "Bitte geben Sie eine Lieferzeit ein"
        if request.POST.get('email') == '':
            return "Bitte geben Sie eine gültige Email ein"
        if len(self.selected_pizzas) == 0:
            return "Bitte wählen Sie mindestens eine Pizza aus"
        status_code = send_post_request(f'{BASE_URL}/orders/',request.COOKIES.get('csrftoken'), self.generate_request_body(request))
        if status_code != 200:
            return self.get_post_result_message(status_code)
        context["data"] = {}
        return "Vielen Dank für Ihre Bestellung"
    
    def get_post_result_message(self, status_code):
        if status_code == 404:
            return "Nutzer existiert nicht, bitte registrieren Sie sich zuerst"
        if status_code == 400:
            return "Fehlerhafte Bestelldaten, bitte überprüfen Sie Ihre Eingaben"
        return "Bestellung konnten nicht gespeichert werden"
    
    def get_selected_pizzas(self, request):
        invalid_keys = ['csrfmiddlewaretoken', 'time', 'email']
        self.selected_pizzas = [(key, value) for key, value in request.POST.items() if key not in invalid_keys and value != '']

    def generate_request_body(self, request):
        request_body = '{\n'
        request_body += f'"time": "{request.POST.get("time")}",\n'
        request_body += f'"email": "{request.POST.get("email")}",\n"pizzas": [\n'
        for index,pizza in enumerate(self.selected_pizzas):
            request_body += f'{{"{pizza[0]}": "{pizza[1]}"}},\n' if index != len(self.selected_pizzas) - 1 else f'{{"{pizza[0]}": "{pizza[1]}"}}\n]\n}}'
        return request_body