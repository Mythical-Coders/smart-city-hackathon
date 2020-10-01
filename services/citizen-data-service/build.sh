#!/bin/bash

mvn clean package
docker build -t akremzerelli/smart-city:citizen-data-service .
docker login 
docker push akremzerelli/smart-city:citizen-data-service
