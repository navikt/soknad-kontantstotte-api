#!/bin/bash
# Denne filen må ha LF som line separator.

# Stop scriptet om en kommando feiler
set -e

# Usage string
usage="Script som bygger prosjektet
Bruk:
./$(basename "$0") OPTIONS
Gyldige OPTIONS:
    -h  | --help        - printer denne hjelpeteksten
"

# Default verdier
IMAGE_NAME="soknad-kontantstotte-api"
DOCKER_REGISTRY="docker.adeo.no:5000"
DOCKER_REPOSITORY="soknad"
TAG="${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/${IMAGE_NAME}:${versjon}"

# Hent ut argumenter
for arg in "$@"
do
case $arg in
    -h|--help)
    echo "$usage" >&2
    exit 1
    ;;
    *) # ukjent argument
    printf "Ukjent argument: %s\n" "$1" >&2
    echo ""
    echo "$usage" >&2
    exit 1
    ;;
esac
done


function build_container() {
    docker build \
        --tag ${TAG} \
        .
}

function publish_container() {
    docker push ${TAG} # Pusher siste bygg til soknad-builder:$VERSJON
}

function create_version_file() {
    echo ${versjon} > VERSION
}

create_version_file
build_container
publish_container