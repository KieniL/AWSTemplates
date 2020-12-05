if [ ! -f openapi-generator.jar ]; then
    curl http://central.maven.org/maven2/org/openapitools/openapi-generator-cli/3.3.4/openapi-generator-cli-3.3.4.jar --output openapi-generator.jar
fi
java -jar openapi-generator.jar generate -i courses-api.yml -l spring -o ../ -c generator.json
java -jar openapi-generator.jar generate -i users-api.yml -l spring -o ../ -c generator.json
java -jar openapi-generator.jar generate -i database-api.yml -l spring -o ../ -c generator.json

$SHELL
