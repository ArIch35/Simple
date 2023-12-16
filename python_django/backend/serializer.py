from rest_framework import serializers
from django.db.models import F
from .models import Customer, Order, Pizza

class PizzaSerializer(serializers.Serializer):
    class Meta:
        model = Pizza

    def get_all_pizza(self):
        return Pizza.objects.all().values('name', 'price')
    
    name = serializers.CharField(max_length=100)
    price = serializers.FloatField()
    
class UserSerializer(serializers.Serializer):
    class Meta:
        model = Customer

    def create(self, validated_data):
        return Customer.objects.create(**validated_data)
    
    def exists(self, validated_data):
        return Customer.objects.filter(email=validated_data['email']).exists()
    
    firstname = serializers.CharField(max_length=100)
    lastname = serializers.CharField(max_length=100)
    username = serializers.CharField(max_length=100)
    email = serializers.CharField(max_length=100)
    password = serializers.CharField(max_length=100)
    postcode = serializers.IntegerField()
    city = serializers.CharField(max_length=100)
    address = serializers.CharField(max_length=100)

class OrderSerializer(serializers.Serializer):
    class Meta:
        model = Order

    def get_all_orders(self):
        return Order.objects.annotate(pizza_name=F('pizzas__name')).values('customer', 'pizza_name', 'amount', 'total_price', 'date', 'time')

    def create(self, validated_data):
        user = Customer.objects.get(email=validated_data['email'])
        pizzas = [(list(pizza.keys())[0], int(list(pizza.values())[0])) for pizza in validated_data['pizzas']]
        for pizza_name, amount in pizzas:
            pizza = Pizza.objects.get(name=pizza_name)
            order = user.order_set.create(amount=amount, total_price=round(pizza.price * amount, 2), time=validated_data['time'])
            order.pizzas.set([pizza])
            order.save()
    
    def customer_exists(self, validated_data):
        return Customer.objects.filter(email=validated_data['email']).exists()
    
    email = serializers.CharField(max_length=100)
    time = serializers.TimeField()
    pizzas = serializers.ListField()

class OrderDeleteSerializer(serializers.Serializer):
    class Meta:
        model = Order

    def delete(self, validated_data):
        Order.objects.filter(customer=validated_data['customer'],date=validated_data['date'], time=validated_data['time']).delete()

    def order_exists(self, validated_data):
        return Order.objects.filter(customer=validated_data['customer'],date=validated_data['date'], time=validated_data['time']).exists()
    
    customer = serializers.CharField(max_length=100)
    date = serializers.DateField()
    time = serializers.TimeField()