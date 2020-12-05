#!/bin/sh


tagVersion=$1


docker build -t luke19/lernplattform-backend --file Dockerfile_prod .

docker tag luke19/lernplattform-backend:latest luke19/lernplattform-backend:$tagVersion


docker push luke19/lernplattform-backend:latest
docker push luke19/lernplattform-backend:$tagVersion