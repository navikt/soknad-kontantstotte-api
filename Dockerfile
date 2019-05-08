FROM navikt/java:11-appdynamics

COPY 20-appdynamics.sh /init-scripts/

ADD ./VERSION VERSION
COPY ./target/soknad-kontantstotte-api.jar "app.jar"
