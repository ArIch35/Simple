from django.urls import path
from .pages_view.homepage_view import HomepageView
from .pages_view.user_info_view import UserInformationView
from .pages_view.menu_view import MenuView
from .pages_view.order_view import OrderView

urlpatterns = [
    path('', HomepageView.as_view(), name='home'),
    path('kundendaten/', UserInformationView.as_view(), name='user-info'),
    path('speisekarte/', MenuView.as_view(), name='menu'),
    path('bestellung/', OrderView.as_view(), name='order'),
]