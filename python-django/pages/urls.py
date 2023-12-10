from django.urls import path
from .views import HomepageView, UserInformationView, MenuView, OrderView


urlpatterns = [
    path('', HomepageView.as_view(), name='home'),
    path('kundendaten/', UserInformationView.as_view(), name='user-info'),
    path('speisekarte/', MenuView.as_view(), name='menu'),
    path('bestellung/', OrderView.as_view(), name='order'),
]