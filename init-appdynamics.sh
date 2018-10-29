#!/bin/bash

if test -r "/opt/appdynamics/agent.jar";
then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/opt/appdynamics/agent.jar"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.applicationName=soknad-kontantstotte-api"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.nodeName=prod-${HOSTNAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.tierName=prod-soknad-kontantstotte-api"
    export JAVA_OPTS
fi