#!/bin/bash

if test -r "/opt/appdynamics/agent.jar";
then
    JAVA_OPTS="${JAVA_OPTS} -javaagent:/opt/appdynamics/agent.jar"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.applicationName=my_app"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.nodeName=prod-${HOSTNAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.tierName=prod-my_app"
    export JAVA_OPTS
fi