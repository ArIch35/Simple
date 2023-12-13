import json
import requests
BASE_URL = 'https://special-space-garbanzo-694wgrg4w9xc5x57-8000.app.github.dev'

def generate_data_json(request):
        data = dict(request.POST.lists())
        data.pop('csrfmiddlewaretoken', None) 
        for key in data:
            data[key] = data[key][0]  
        return json.dumps(data, indent=4)

def send_post_request(url, cookies, request_body):
        session = requests.Session()
        headers = {'Content-type': 'application/json'}
        session.cookies['csrftoken'] = cookies
        headers['X-CSRFToken'] = session.cookies['csrftoken']
        response = session.post(url, data=request_body, headers=headers)
        print(response.status_code, response.json().get('message'))
        return response.status_code