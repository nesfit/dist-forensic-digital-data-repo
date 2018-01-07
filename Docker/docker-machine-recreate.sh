#!/bin/sh

docker-machine rm default
docker-machine create --virtualbox-memory "2048" --driver virtualbox default
eval "$(docker-machine env default)"
