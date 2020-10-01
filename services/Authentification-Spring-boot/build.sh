#!/bin/bash

mvn clean package
docker build -t akremzerelli/smart-city:authentication-service .
docker login 
docker push akremzerelli/smart-city:authentication-service
