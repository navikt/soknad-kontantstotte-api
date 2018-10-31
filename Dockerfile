FROM navikt/java:8

RUN mkdir -p /app/appdynamics \
    && wget -O /app/appdynamics/appdynamicsagent.zip http://maven.adeo.no/nexus/service/local/repositories/m2internal/content/com/appdynamics/appdynamics-appagent/4.5.1/appdynamics-appagent-4.5.1.zip \
    && unzip /app/appdynamics/appdynamicsagent.zip -d /app/appdynamics
COPY appdynamics.sh /init-scripts/

ADD ./VERSION /app/VERSION
COPY ./target/soknad-kontantstotte-api.jar "/app/app.jar"