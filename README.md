## Subscription-App

Subscription-App cover several topics: 

- Java16
- Spring boot 2.6
- Postgres 
- RabbitMq 
- Lombok
- Testcontainers
- Awaitility
- JavaFaker
- Junit5
- Mockito
- Simple Monitoring with Prometheus and Grafana
- Docker
- Docker Compose
- Python

## usage
Consider it is running from the root path(globo-hansmelo)

To run tests:

```shell
$ cd subscription-app && mvn test
```

To run this service:

```shell
$ docker-compose build && docker-compose up -d && cd subscription-app && mvn spring-boot:run
```

To run the generator of http post requests:

```shell
$ cd request-app && pip3 install -r requirements.txt --no-index  && python3 request_sender.py
```


## Sample logs

```shell
2021-09-01 18:15:30.397  INFO 28195 --- [nio-8080-exec-1] c.d.g.controller.NotificationController  : The request is accepted Notification[type=SUBSCRIPTION_PURCHASED, subscription=5793cf6b3fd833521db8c420955e6f01].
2021-09-01 18:15:30.415  INFO 28195 --- [nio-8080-exec-3] c.d.globo.service.NotificationSender     : Sending Notification[type=SUBSCRIPTION_PURCHASED, subscription=5793cf6b3fd833521db8c420955e6f02]
2021-09-01 18:15:30.430  INFO 28195 --- [ntContainer#0-1] c.d.globo.service.NotificationListener   : Received Notification[type=SUBSCRIPTION_PURCHASED, subscription=5793cf6b3fd833521db8c420955e6f01]
2021-09-01 18:15:30.598  INFO 28195 --- [ntContainer#0-1] c.d.globo.service.SubscriptionService    : Saved Subscription(id=5793cf6b3fd833521db8c420955e6f01, status=ACTIVE, createdAt=Wed Sep 01 18:15:30 BRT 2021, updatedAt=Wed Sep 01 18:15:30 BRT 2021)
2021-09-01 18:15:30.662  INFO 28195 --- [ntContainer#0-1] c.d.globo.service.EventHistoryService    : Saved EventHistory(id=1, type=SUBSCRIPTION_PURCHASED, subscription=Subscription(id=5793cf6b3fd833521db8c420955e6f01, status=ACTIVE, createdAt=Wed Sep 01 18:15:30 BRT 2021, updatedAt=Wed Sep 01 18:15:30 BRT 2021), createdAt=Wed Sep 01 18:15:30 BRT 2021)
```


## Simple Monitoring
Grafana: http://localhost:3000

User: admin
Pass:admin

[Graphs from docker containers](http://localhost:3000/dashboard/db/docker-containers?refresh=10s&orgId=1)

[Graphs from docker host](http://localhost:3000/dashboard/db/docker-host?refresh=10s&orgId=1)

[Graphs from monitor service](http://localhost:3000/dashboard/db/monitor-services?refresh=10s&orgId=1)


## Project Structure
- [subscription-app]
	 - [main/java]
	    - [/controller] : API Rest
	    - [/configuration] : RabbitMQ configuration.
	    - [/service] : Services for the business logic, the listerner and the sender.
	    - [/repository] : Respository from POJOs.
	    - [/domain]: POJOs.
	 - [test/java]
	    - [/integration] : Integration tests.
	    - [/service] : Unit tests for service.
- [grafana] - confs of dashboard and datasources, setup.sh the script of import of confs.
- [prometheus] - confs of the prometheus.
- [request-app] - python service that execute http post notification.

# Desafio Backend Java


## O Desafio
### O desafio se trata de atualiza????o das assinatura do usu??rio de acordo com notifica????es.
<img src="DesafioFila.png" width="500" height="500">


#### Tipos de Notifica????es:

-  SUBSCRIPTION_PURCHASED - A Compra foi realizada e a assinatura deve estar com status ativa.

-  SUBSCRIPTION_CANCELED - A Compra foi cancelada e a assinatura deve estar com status cancelada.

-  SUBSCRIPTION_RESTARTED - A Compra foi recuperada e a assinatura deve estar com status ativa.


### Etapas

- Recebimento Notifica????o HTTP
- Enfileiramento
- Processamento e Persistencia

## Considera????es Gerais
Voc?? dever?? usar este reposit??rio como o repo principal do projeto, i.e.,
todos os seus commits devem estar registrados aqui, pois queremos ver como
voc?? trabalha.

Esse problema tem algumas constraints:

1. Linguagem : Java 8 ou mais atualizada

2. Framework: Spring

3. Database: Qualquer database relacional

4. Deve ser poss??vel conseguir rodar seu c??digo em um Mac OS X OU no Ubuntu;

5. O RabbitMQ deve ficar dentro de um compose(Docker)

6. Devemos ser capazes de executar o seu c??digo em um ambiente local de alguma forma automatizada:

   ``` git clone seu-repositorio
    cd seu-repositorio
    ./configure (ou algo similar)
    docker-compose up -d
    make run (ou algo similar como java -jar desafio.jar)
    make sendNotifications 
    
    
    obs: N??o necessariamente deve ser dessa forma, mas precisa estar automatizada e documentado.
   ```

7. Devemos ter automatizadas as chamadas para a api com as notifica????es em anexo no arquivo [notificacoes.txt], obrigatoriamente seguindo a ordem do arquivo (OBS: Pode ser feito em qualquer linguagem de programa????o). 

Esses comandos devem ser o suficiente para inicializar o RabbitMQ, a aplica????o Java e as chamadas para a API. Pode se considerar que temos instalado no meu sistema: Java, Python e Ruby e Docker.
Qualquer outra depend??ncia que eu precisar voc?? tem que prover.

**Registre tudo**: testes que forem executados, id??ias que gostaria de
implementar se tivesse tempo (explique como voc?? as resolveria, se houvesse
tempo), decis??es que forem tomadas e seus porqu??s, arquiteturas que forem
testadas, os motivos de terem sido modificadas ou abandonadas, instru????es de
deploy e instala????o, etc. Crie um ??nico arquivo COMMENTS.md ou HISTORY.md no
reposit??rio para isso.


## Modelo do Banco de Dados

![Modelo](database_model.png)


## Execu????o
Ap??s as inicializa????o do ambiente(Docker/Rabbit/Aplica????o), executar o script de envio das Notifica????es e espera-se que o status da assinatura esteja conforme as notifica????es recebidas, al??m disso deve ter conter todo o hist??rico de notifica????es para cada assinatura para um poss??vel auditoria.


### O que ser?? avaliado na sua solu????o?

-  Seu c??digo ser?? observado por uma equipe de desenvolvedores que avaliar??o a
   implementa????o do c??digo, simplicidade e clareza da solu????o, a arquitetura,
   estilo de c??digo, testes unit??rios, testes funcionais, n??vel de automa????o
   dos testes, o design da interface e a documenta????o.
   
- Consist??ncia dos dados persistidos.


### Dicas

- Use qualquer ferramenta dentro framework Spring

- Automatize o m??ximo poss??vel;

- Em caso de d??vidas, pergunte.
