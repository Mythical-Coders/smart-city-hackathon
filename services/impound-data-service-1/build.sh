#!/bin/bash

mvn clean package
docker build -t akremzerelli/smart-city:impound-data-service .
docker login 
docker push akremzerelli/smart-city:impound-data-service
