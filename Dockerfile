FROM navikt/java:8-appdynamics

ENV APPD_NAME=APP_NAME
ADD run-java.sh run-java.sh

ADD ./VERSION VERSION
COPY ./target/soknad-kontantstotte-api.jar app.jar
