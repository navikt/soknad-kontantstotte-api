FROM navikt/java:11

ENV APPD_ENABLED=TRUE
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Djava.awt.headless=true"

COPY ./target/soknad-kontantstotte-api.jar "app.jar"
