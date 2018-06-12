FROM docker.adeo.no:5000/soknad/soknad-builder:1.2.0 AS builder
ADD / /workspace

RUN chmod +x /workspace/build.sh
RUN /workspace/build.sh