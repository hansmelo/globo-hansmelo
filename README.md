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
### O desafio se trata de atualização das assinatura do usuário de acordo com notificações.
<img src="DesafioFila.png" width="500" height="500">


#### Tipos de Notificações:

-  SUBSCRIPTION_PURCHASED - A Compra foi realizada e a assinatura deve estar com status ativa.

-  SUBSCRIPTION_CANCELED - A Compra foi cancelada e a assinatura deve estar com status cancelada.

-  SUBSCRIPTION_RESTARTED - A Compra foi recuperada e a assinatura deve estar com status ativa.


### Etapas

- Recebimento Notificação HTTP
- Enfileiramento
- Processamento e Persistencia

## Considerações Gerais
Você deverá usar este repositório como o repo principal do projeto, i.e.,
todos os seus commits devem estar registrados aqui, pois queremos ver como
você trabalha.

Esse problema tem algumas constraints:

1. Linguagem : Java 8 ou mais atualizada

2. Framework: Spring

3. Database: Qualquer database relacional

4. Deve ser possível conseguir rodar seu código em um Mac OS X OU no Ubuntu;

5. O RabbitMQ deve ficar dentro de um compose(Docker)

6. Devemos ser capazes de executar o seu código em um ambiente local de alguma forma automatizada:

   ``` git clone seu-repositorio
    cd seu-repositorio
    ./configure (ou algo similar)
    docker-compose up -d
    make run (ou algo similar como java -jar desafio.jar)
    make sendNotifications 
    
    
    obs: Não necessariamente deve ser dessa forma, mas precisa estar automatizada e documentado.
   ```

7. Devemos ter automatizadas as chamadas para a api com as notificações em anexo no arquivo [notificacoes.txt], obrigatoriamente seguindo a ordem do arquivo (OBS: Pode ser feito em qualquer linguagem de programação). 

Esses comandos devem ser o suficiente para inicializar o RabbitMQ, a aplicação Java e as chamadas para a API. Pode se considerar que temos instalado no meu sistema: Java, Python e Ruby e Docker.
Qualquer outra dependência que eu precisar você tem que prover.

**Registre tudo**: testes que forem executados, idéias que gostaria de
implementar se tivesse tempo (explique como você as resolveria, se houvesse
tempo), decisões que forem tomadas e seus porquês, arquiteturas que forem
testadas, os motivos de terem sido modificadas ou abandonadas, instruções de
deploy e instalação, etc. Crie um único arquivo COMMENTS.md ou HISTORY.md no
repositório para isso.


## Modelo do Banco de Dados

![Modelo](database_model.png)


## Execução
Após as inicialização do ambiente(Docker/Rabbit/Aplicação), executar o script de envio das Notificações e espera-se que o status da assinatura esteja conforme as notificações recebidas, além disso deve ter conter todo o histórico de notificações para cada assinatura para um possível auditoria.


### O que será avaliado na sua solução?

-  Seu código será observado por uma equipe de desenvolvedores que avaliarão a
   implementação do código, simplicidade e clareza da solução, a arquitetura,
   estilo de código, testes unitários, testes funcionais, nível de automação
   dos testes, o design da interface e a documentação.
   
- Consistência dos dados persistidos.


### Dicas

- Use qualquer ferramenta dentro framework Spring

- Automatize o máximo possível;

- Em caso de dúvidas, pergunte.
