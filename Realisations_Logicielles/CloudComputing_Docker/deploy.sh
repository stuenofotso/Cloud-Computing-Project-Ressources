#!/bin/bash

# le script suppose que pwd contient le chemin absolue vers le repertoire contenant ce script

nbre_tomcats=$1;

if [[ $nbre_tomcats < 1 ]]
	then
		
		tput setaf 1;echo "vous devez declarer au moins un serveur tomcat";tput sgr0;
		exit;
	fi

echo $nbre_tomcats > nbre_tomcats;

tput setaf 2;echo "Checking docker installing...";tput sgr0;

if ! type "docker" > /dev/null;
then 
	echo "docker not found; installation...";
#	sudo apt-get -y install docker.io; 
	sudo echo "deb http://get.docker.io/ubuntu docker main" >> /etc/apt/sources.list;
	sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys D8576A8BA88D21E9;
	sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9;
	sudo apt-get -y update;
	sudo apt-get -y install lxc-docker;
	sudo addgroup $USER docker;
else
	tput setaf 2;echo "docker found...";tput sgr0;
fi


tput setaf 2;echo "launching docker daemon";tput sgr0;  
sudo service docker start


tput setaf 2;echo "Building VM_MySQL";tput sgr0;
sudo docker build -t vm_mysql MySQL;



#on procedera a la configuration dans le dockerfile des tomcats de l'hostname de mysql comme etant vm_mysql
tput setaf 2;echo "Building VM_Tomcat1";tput sgr0;
sudo docker build -t vm_tomcat1 Tomcat;





for i in $(seq 2 $nbre_tomcats);
do 
	tput setaf 2;echo "Building VM_Tomcat$i";tput sgr0;
	sudo docker build -t "vm_tomcat$i" Tomcat_clone;
done


tput setaf 2;echo "Generating workers.properties for $nbre_tomcats vm_tomcats";tput sgr0;


sudo cp -f Apache/config_files/workers1.properties Apache/config_files/workers.properties

i=0;

servers="worker.loadbalancer.balance_workers=server1";

for i in $(seq 1 $nbre_tomcats);
do 
	sudo echo "" >> Apache/config_files/workers.properties;
	sudo echo "#server$i configuration" >> Apache/config_files/workers.properties;
	sudo echo "worker.server$i.port=8009" >> Apache/config_files/workers.properties;
	sudo echo "worker.server$i.host=vm_tomcat$i" >> Apache/config_files/workers.properties;
	sudo echo "worker.server$i.type=ajp13" >> Apache/config_files/workers.properties;
	sudo echo "worker.server$i.lbfactor=1" >> Apache/config_files/workers.properties;
	if [[ $i > 1 ]]
	then
			
		servers="$servers, server$i";
	fi

done

sudo echo "$servers" >> Apache/config_files/workers.properties;

tput setaf 2;echo "Building VM_Apache";tput sgr0;
sudo docker build -t vm_apache Apache;


#demarrage du cluster
sudo ./start.sh

