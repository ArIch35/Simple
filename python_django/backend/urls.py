from django.urls import path
from . import views

urlpatterns = [
    path('pizzas/', views.PizzaAPIView.as_view(), name='get_all_pizza'),
    path('users/', views.UserAPIView.as_view(), name='user_api_view'),
    path('orders/', views.OrderAPIView.as_view(), name='order_api_view'),
]