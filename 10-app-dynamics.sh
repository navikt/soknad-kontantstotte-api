#!/bin/bash
if test -r "/app/appdynamics/javaagent.jar";
then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/app/appdynamics/javaagent.jar"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.applicationName=${APP_NAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.reuse.nodeName=true"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.reuse.nodeName.prefix=${FASIT_ENVIRONMENT_NAME}_${APP_NAME}_"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.nodeName=${HOSTNAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.jvm.shutdown.mark.node.as.historical=true"
    JAVA_OPTS="${JAVA_OPTS} -D-Dappdynamics.agent.tierName=${FASIT_ENVIRONMENT_NAME}_${APP_NAME}"
    export JAVA_OPTS
fi