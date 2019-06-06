#!/bin/bash
if test -r "/opt/appdynamics/javaagent.jar";
then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/opt/appdynamics/javaagent.jar"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.applicationName=${NAIS_APP_NAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.tierName=${ENVIRONMENT_NAME}-${NAIS_APP_NAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.nodeName=${ENVIRONMENT_NAME}-${NAIS_APP_NAME}"
    export JAVA_OPTS
fi
