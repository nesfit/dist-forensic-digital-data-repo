#!/bin/sh

# Docker Toolbox with VirtualBox

# Settings for high-performance machine

docker-machine rm default
docker-machine create --virtualbox-memory "24576" --virtualbox-cpu-count "4" --virtualbox-disk-size "45000" --virtualbox-share-folder "C:\\Users:c/Users" --driver virtualbox default
eval "$(docker-machine env default)"

docker-machine ssh default "mkdir /home/docker/Users"
docker-machine ssh default "sudo mount -t vboxsf -o uid=1000,gid=50 c/Users /home/docker/Users"

#-------------------------------------------

# Old settings for low-performance machine

#docker-machine rm default
#docker-machine create --virtualbox-memory "4096" --virtualbox-share-folder "C:\\Docker:c/Docker" --driver virtualbox default
#eval "$(docker-machine env default)"

#docker-machine ssh default "mkdir /home/docker/Docker"
#docker-machine ssh default "sudo mount -t vboxsf -o uid=1000,gid=50 c/Docker /home/docker/Docker"
