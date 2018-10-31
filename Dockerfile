FROM navikt/java:8-appdynamics

ADD ./VERSION /app/VERSION
COPY ./target/soknad-kontantstotte-api.jar "/app/app.jar"

ADD ./appdynamics-config.sh /init-scripts