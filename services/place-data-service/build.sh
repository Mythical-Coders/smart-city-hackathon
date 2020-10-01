#!/bin/bash

mvn clean package
docker build -t akremzerelli/smart-city:places-data-service .
docker login 
docker push akremzerelli/smart-city:places-data-service
