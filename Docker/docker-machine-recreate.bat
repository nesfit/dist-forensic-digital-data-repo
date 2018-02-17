
docker-machine rm default
docker-machine create --virtualbox-memory "4096" --virtualbox-share-folder "C:\Users:c/Users" --driver virtualbox --virtualbox-no-vtx-check default
eval "$(docker-machine env default)"

docker-machine ssh default "mkdir /home/docker/Users"
docker-machine ssh default "sudo mount -t vboxsf -o uid=1000,gid=50 c/Users /home/docker/Users"
