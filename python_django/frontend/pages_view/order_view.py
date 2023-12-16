import os
from ..models import Order
import requests
from django.views.generic import TemplateView
from itertools import groupby


class OrderView(TemplateView):
    template_name = 'pages/bestellung.html'
    
    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        response = requests.get(f'{os.environ["BASE_BACKEND_URL"]}/orders/')
        context['orders'] = self.generate_orders(response.json()) if response.status_code == 200 else []
        context['url'] = os.environ["BASE_BACKEND_URL"]
        return context
    
    def generate_orders(self, orders):
        orders.sort(key=lambda x: (x['customer'], x['time']))
        grouped_orders = {k: list(v) for k, v in groupby(orders, key=lambda x: (x['customer'], x['time']))}
        valid_orders = []
        for (customer, _), orders in grouped_orders.items():
            pizzas = ""
            total_price = 0
            for index, order in enumerate(orders):
                if order['pizza_name'] == None:
                    continue
                pizzas += f"{order['pizza_name']} ({order['amount']}x), " if index != len(orders) - 1 else f"{order['pizza_name']} ({order['amount']}x)"
                total_price += order['total_price']
            valid_orders.append(Order(customer, pizzas, total_price, orders[0]["date"], orders[0]['time']))
        return valid_orders