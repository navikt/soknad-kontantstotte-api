#!/bin/bash
if test -r "/opt/appdynamics/javaagent.jar";
then
    export APPD_HOSTNAME="$FASIT_ENVIRONMENT_NAME-$HOSTNAME"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.tierName=${FASIT_ENVIRONMENT_NAME}-${APP_NAME}"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.reuse.nodeName=true"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.agent.reuse.nodeName.prefix=${FASIT_ENVIRONMENT_NAME}_${APP_NAME}_"
    JAVA_OPTS="${JAVA_OPTS} -Dappdynamics.jvm.shutdown.mark.node.as.historical=true"
    export JAVA_OPTS
fi
exec java ${DEFAULT_JVM_OPTS} ${JAVA_OPTS} -jar app.jar ${RUNTIME_OPTS} $@
