FROM navikt/java:8

ARG version
ARG app_name

COPY target/soknad-api.jar "/app/app.jar"
