#!/bin/sh

docker-machine rm default
docker-machine create --virtualbox-memory "2048" --virtualbox-share-folder "C:\\Docker:c/Docker" --driver virtualbox default
eval "$(docker-machine env default)"

docker-machine ssh default "mkdir /home/docker/Docker"
docker-machine ssh default "sudo mount -t vboxsf -o uid=1000,gid=50 c/Docker /home/docker/Docker"
