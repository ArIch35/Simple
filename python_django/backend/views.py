import json
from rest_framework.views import APIView
from rest_framework.response import Response
from .serializer import PizzaSerializer, UserSerializer, OrderSerializer, OrderDeleteSerializer

class UserAPIView(APIView):
    def post(self, request):
        serializer = UserSerializer(data=request.data)
        if serializer.is_valid():
            if serializer.exists(request.data):
                return Response({'message': 'User already exists'}, status=401)
            serializer.create(request.data)
            return Response({'message': 'User successfully created'}, status=201)
        return Response({'message': 'Invalid request body'}, status=400)
    
class OrderAPIView(APIView):
    def get(self, _):
        return Response(OrderSerializer().get_all_orders(), status=200)

    def post(self, request):
        serializer = OrderSerializer(data=request.data)
        if serializer.is_valid():
            if not serializer.customer_exists(request.data):
                return Response({'message': 'User does not exist'}, status=404)
            serializer.create(request.data)
            return Response({'message': 'Order successfully created'}, status=201)
        return Response({'message': 'Invalid request body'}, status=400)
    
    def delete(self, request):
        data = json.loads(list(request.data.keys())[0])
        serializer = OrderDeleteSerializer(data=data)
        if serializer.is_valid():
            if not serializer.order_exists(data):
                return Response({'message': 'Order does not exist'}, status=404)
            serializer.delete(data)
            return Response({'message': 'Order successfully deleted'}, status=200)
        return Response({'message': 'Invalid request body'}, status=400)
    
class PizzaAPIView(APIView):
    def get(self, _):
        return Response(PizzaSerializer().get_all_pizza(), status=200)
