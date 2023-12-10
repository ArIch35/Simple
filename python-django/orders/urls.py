from django.urls import path
from . import views

urlpatterns = [
    path('pizzas/', views.get_all_pizza, name='get_all_pizza'),
    path('users/', views.add_user, name='add_user'),
    path('orders/', views.add_order, name='add_order'),
    path('orders-get/', views.get_all_orders, name='get_all_orders'),
]