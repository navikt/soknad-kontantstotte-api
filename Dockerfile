FROM navikt/java:8-appdynamics

ADD ./VERSION VERSION
COPY ./target/soknad-kontantstotte-api.jar app.jar
