FROM navikt/java:8-appdynamics

COPY run-java.sh /init-scripts/

ADD ./VERSION VERSION
COPY ./target/soknad-kontantstotte-api.jar app.jar
