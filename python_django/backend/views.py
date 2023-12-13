import json
from django.http import JsonResponse
from django.db.models import F
from .models import Pizza, Customer, Order

def get_all_pizza(_):
    pizzas = Pizza.objects.all()
    return JsonResponse(list(pizzas.values('name', 'price')), safe=False)

def get_all_orders(_):
    orders = Order.objects.annotate(pizza_name=F('pizzas__name')).values('customer', 'pizza_name', 'amount', 'total_price', 'date', 'time')
    return JsonResponse(list(orders), safe=False)

def add_user(request):
    if request.method != 'POST':
        return JsonResponse({'message': 'Invalid request method'}, status=400)
    data = json.loads(request.body)
    firstname = data.get('firstname')
    lastname = data.get('lastname')
    username = data.get('username')
    email = data.get('email')
    password = data.get('password')
    postcode = data.get('postcode')
    city = data.get('city')
    address = data.get('address')
    if not firstname or not lastname or not username or not email or not password or not postcode or not city or not address:
        return JsonResponse({'message': 'Invalid request body'}, status=400)
    try:
        user = Customer.objects.create(firstname=firstname, lastname=lastname, username=username, email=email, password=password, postcode=postcode, city=city, address=address)
        user.save()
        return JsonResponse({'message': 'User successfully created'}, status=201)
    except Exception as e:
        print(e)
        return JsonResponse({'message': 'User already exists'}, status=401)

def add_order(request):
    if request.method != 'POST':
        return JsonResponse({'message': 'Invalid request method'}, status=400)
    data = json.loads(request.body)
    time = data.get('time')
    email = data.get('email')
    pizzas = data.get('pizzas')
    if not time or not email or not pizzas:
        return JsonResponse({'message': 'Invalid request body'}, status=400)
    pizzas = [(list(pizza.keys())[0], int(list(pizza.values())[0])) for pizza in pizzas]
    try:
        user = Customer.objects.get(email=email)
        for pizza_name, amount in pizzas:
            pizza = Pizza.objects.get(name=pizza_name)
            order = user.order_set.create(amount=amount, total_price=round(pizza.price * amount, 2), time=time)
            order.pizzas.set([pizza])
            order.save()
        return JsonResponse({'message': 'Order successfully created'}, status=200)
    except Customer.DoesNotExist:
        return JsonResponse({'message': 'User does not exist'}, status=404)
    except Exception as e:
        print(e)
        return JsonResponse({'message': 'Order could not be created'}, status=401)
    
def delete_order(request):
    if request.method != 'DELETE':
        return JsonResponse({'message': 'Invalid request method'}, status=400)
    data = json.loads(request.body)
    customer = data.get('customer')
    date = data.get('date')
    time = data.get('time')
    if not time or not date or not customer:
        return JsonResponse({'message': 'Invalid request body'}, status=400)
    try:
        orders = Order.objects.filter(customer=customer,date=date, time=time)
        orders.delete()
        return JsonResponse({'message': 'Order successfully deleted'}, status=200)
    except Order.DoesNotExist:
        return JsonResponse({'message': 'Order does not exist'}, status=404)
    