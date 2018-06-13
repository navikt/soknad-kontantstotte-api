FROM navikt/java:8

ADD ./VERSION /app/VERSION
COPY ./target/soknad-kontantstotte-api.jar "/app/app.jar"