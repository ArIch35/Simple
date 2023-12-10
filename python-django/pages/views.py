from datetime import datetime
import json
import requests
from django.shortcuts import render
from django.views.generic import TemplateView
from itertools import groupby
from .models import Order
# Change the backend URL to the Base URL of your backend
BASE_URL = 'https://special-space-garbanzo-694wgrg4w9xc5x57-8000.app.github.dev'

def generate_data_json(request):
        data = dict(request.POST.lists())
        data.pop('csrfmiddlewaretoken', None) 
        for key in data:
            data[key] = data[key][0]  
        return json.dumps(data, indent=4)

def send_post_request(url, cookies, request_body):
        session = requests.Session()
        headers = {'Content-type': 'application/json'}
        session.cookies['csrftoken'] = cookies
        headers['X-CSRFToken'] = session.cookies['csrftoken']
        response = session.post(url, data=request_body, headers=headers)
        print(response.status_code, response.json().get('message'))
        return response.status_code

class HomepageView(TemplateView):
    template_name = 'pages/home.html'

class UserInformationView(TemplateView):
    template_name = 'pages/kundendaten.html'

    def post(self, request, *args, **kwargs):
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

class OrderView(TemplateView):
    template_name = 'pages/bestellung.html'
    
    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        response = requests.get(f'{BASE_URL}/orders-get/')
        context['orders'] = self.generate_orders(response.json()) if response.status_code == 200 else []
        return context
    
    def generate_orders(self, orders):
        orders.sort(key=lambda x: x['customer'])
        grouped_orders = {k: list(v) for k, v in groupby(orders, key=lambda x: x['customer'])}
        print(orders)
        valid_orders = []
        for customer, orders in grouped_orders.items():
            date = datetime.strptime(orders[0]['date'], "%Y-%m-%dT%H:%M:%S.%fZ").strftime("%B %d, %Y")
            pizzas = ""
            total_price = 0
            for index, order in enumerate(orders):
                if order['pizza_name'] == None:
                    continue
                pizzas += f"{order['pizza_name']} ({order['amount']}x), " if index != len(orders) - 1 else f"{order['pizza_name']} ({order['amount']}x)"
                total_price += order['total_price']
            valid_orders.append(Order(customer, pizzas, total_price, date, orders[0]['time']))
        return valid_orders