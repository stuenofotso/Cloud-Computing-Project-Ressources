#!/bin/bash

########## Stopping VMS: Cette partie sera exécutée par le script d'arrêt du cluster #######################

# stopping container
sudo docker ps -aq --no-trunc | xargs docker rm -f ;

#stopping daemon
#sudo kill $( ps aux | grep '[d]ocker daemon' | awk '{print $2}') 1&2> /dev/null;
sudo service docker stop

tput setaf 2;echo "all done";tput sgr0;


