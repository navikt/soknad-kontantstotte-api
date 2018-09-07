#!/bin/bash
# Denne filen må ha LF som line separator.

# Stop scriptet om en kommando feiler
set -e

# Enabler extglob, for å kunne bruke parantesoperator i rm
shopt -s extglob

# Usage string
usage="Script som bygger prosjektet

Bruk:
./$(basename "$0") OPTIONS

Gyldige OPTIONS:
    -h  | --help        - printer denne hjelpeteksten
    --publish           - publiserer dockerimaget
"


# Default verdier
v=${versjon}
IMAGE_NAME="soknad-kontantstotte-api"
DOCKER_REGISTRY="repo.adeo.no:5443"
DOCKER_REPOSITORY="soknad"
TAG="${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}/${IMAGE_NAME}:${v:="unversioned"}"
BUILDER_IMAGE="repo.adeo.no:5443/soknad/soknad-docker-builder:1.0.0"


# Hent ut argumenter
for arg in "$@"
do
case $arg in
    -h|--help)
        echo "$usage" >&2
        exit 1
        ;;
    --publish)
        PUBLISH=true
        ;;
    *) # ukjent argument
        printf "Ukjent argument: %s\n" "$1" >&2
        echo ""
        echo "$usage" >&2
        exit 1
    ;;
esac
done


function build_command {
    docker run \
        --rm \
        --volume $(pwd):/var/workspace \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        --env NPM_TOKEN=${NPM_AUTH} \
        $BUILDER_IMAGE \
        "$@"
}

function build_pdf_template {
    CURRENT_DIR=$(pwd)

    cd src/main/resources/react-pdf/
    mkdir -p dist

    BABEL_PRESETS=@babel/preset-env,@babel/preset-react
    BABEL_PLUGINS=@babel/plugin-transform-react-constant-elements,@babel/plugin-transform-react-inline-elements

    echo "Run npm"
    build_command npm install

    echo "Run babel"
    build_command npx babel src --out-file dist/bundle.js --presets=$BABEL_PRESETS --plugins=$BABEL_PLUGINS

    echo "Remove all node modules except react & dom"
    chmod -R 777 node_modules/*
    rm -rf node_modules/!(react|react-dom)

    cd $CURRENT_DIR
}

function build_target {
    build_command mvn clean verify
}


function build_container {
    docker build \
        --tag ${TAG} \
        .
}

function create_version_file {
    echo ${versjon} > VERSION
}

function publish_container() {
    docker push ${TAG}
}

build_pdf_template
build_target
create_version_file
build_container

if [[ $PUBLISH ]]; then
    if [ -z ${versjon+x} ]; then
        echo "versjon er ikke satt - publiserer ikke!"
        exit 1;
        else publish_container;
    fi
fi