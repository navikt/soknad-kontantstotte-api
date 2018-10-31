#!/bin/bash
if test -r "/app/appdynamics/javaagent.jar";
then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/app/appdynamics/javaagent.jar"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.applicationName=soknad-kontantstotte-api"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.tierName=${FASIT_ENVIRONMENT_NAME}-soknad-kontantstotte-api"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.reuse.nodeName=true"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.reuse.nodeName.prefix=${FASIT_ENVIRONMENT_NAME}_${APP_NAME}_"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.jvm.shutdown.mark.node.as.historical=true"
    export JAVA_OPTS
fi