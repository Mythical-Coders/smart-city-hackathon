#!/bin/bash

mvn clean package
docker build -t akremzerelli/smart-city:discovery-server .
docker login 
docker push akremzerelli/smart-city:discovery-server
