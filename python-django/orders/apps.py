from django.apps import AppConfig
from psycopg2 import OperationalError


class OrdersConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "orders"

    def ready(self):
        from .models import Pizza  
        try:
            Pizza.objects.get_or_create(name='Margherita', price=7.5)
            Pizza.objects.get_or_create(name='Salami', price=8.5)
            Pizza.objects.get_or_create(name='Hawaii', price=9.3)
        except OperationalError:
            pass
