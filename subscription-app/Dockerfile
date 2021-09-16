FROM openjdk:16-slim
RUN apt update
RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app
COPY target/subscription-app-0.0.3-SNAPSHOT.jar $PROJECT_HOME/subscription-app-0.0.3-SNAPSHOT.jar
WORKDIR $PROJECT_HOME
CMD ["java","-jar","./subscription-app-0.0.3-SNAPSHOT.jar"]