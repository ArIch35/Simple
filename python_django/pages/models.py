from django.db import models

class Order:
    def __init__(self, customer, pizzas, total_price, date, time):
        self.pizzas = pizzas
        self.customer = customer
        self.total_price = total_price
        self.date = date
        self.time = time
