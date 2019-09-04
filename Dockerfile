FROM navikt/java:11-appdynamics

ENV APPD_ENABLED=TRUE

COPY ./target/soknad-kontantstotte-api.jar "app.jar"
