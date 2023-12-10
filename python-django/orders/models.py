from django.db import models
from django.contrib.auth.models import AbstractBaseUser, UserManager

# Create your models here.
class Pizza(models.Model):
    name = models.CharField(max_length=100)
    price = models.FloatField()

class Customer(AbstractBaseUser):
    objects = UserManager()
    firstname = models.CharField(max_length=250)
    lastname = models.CharField(max_length=250)
    username = models.CharField(max_length=250, default='user')
    email = models.EmailField(max_length=250, primary_key=True, db_column='email')
    password = models.CharField(max_length=100)
    postcode = models.IntegerField()
    city = models.CharField(max_length=250)
    address = models.CharField(max_length=250)

class Order(models.Model):
    customer = models.ForeignKey(Customer, on_delete=models.CASCADE)
    pizzas = models.ManyToManyField(Pizza)
    amount = models.IntegerField(default=1)
    total_price = models.FloatField()
    date = models.DateTimeField(auto_now_add=True)
    time = models.TimeField(default='00:00')