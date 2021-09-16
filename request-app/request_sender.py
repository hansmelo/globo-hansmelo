import requests
import json
import time

lines = filter(None, open("notificacoes.txt", "r").read().splitlines())
 

#url = 'http://globo-subscription-app:8080/notifications' #to run in the docker-compose-full-services.yml

url = 'http://localhost:8080/notifications' #to run without docker


headers = {"Content-type": "application/json",
          "Accept": "application/json"}

#time.sleep(5) #to run in the docker-compose-full-services.yml
for line in lines:
    r = requests.post(url, json=json.loads(line.replace("\'", "\"")), headers=headers)
    print(line)