#!/bin/bash
if [ ! -f openapi-generator-cli.jar ]; then
    curl http://central.maven.org/maven2/org/openapitools/openapi-generator-cli/3.3.4/openapi-generator-cli-3.3.4.jar --output openapi-generator-cli.jar
fi

cd "$(dirname "$0")"
mkdir temp && cd temp

git clone https://FSTU5-WS19bb-Lernplattform@dev.azure.com/FSTU5-WS19bb-Lernplattform/Lernplattform/_git/Lernplattform
java -jar ./../openapi-generator-cli.jar generate -i  ./Lernplattform/courses-service/openapi/courses-api.yml -g typescript-angular -o ./../courses
java -jar ./../openapi-generator-cli.jar generate -i  ./Lernplattform/courses-service/openapi/users-api.yml -g typescript-angular -o ./../users
java -jar ./../openapi-generator-cli.jar generate -i  ./Lernplattform/courses-service/openapi/database-api.yml -g typescript-angular -o ./../database

cd .. && rm -rf temp

$SHELL
