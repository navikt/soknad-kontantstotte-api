FROM navikt/java:8-appdynamics

ENV APPD_NAME=APP_NAME
COPY run-java.sh /init-scripts/

ADD ./VERSION VERSION
COPY ./target/soknad-kontantstotte-api.jar app.jar
