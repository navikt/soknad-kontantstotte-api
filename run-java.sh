#!/bin/bash
if test -r "/app/appdynamics/agent.jar";
then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/app/appdynamics/agent.jar"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.applicationName=soknad-kontantstotte-api"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.nodeName=prod-${HOSTNAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.tierName=prod-soknad-kontantstotte-api"
    export JAVA_OPTS
fi

set -x
exec java \
${DEFAULT_JVM_OPTS} \
${JAVA_OPTS} \
-jar ${APP_JAR} \
${RUNTIME_OPTS} \
$@