#!/bin/bash
nbre_tomcats=$(<nbre_tomcats);


########## LAUNCHING VMS: Cette partie sera exécutée par le script de demarrage du cluster #######################

tput setaf 2;echo "launching docker daemon";tput sgr0;  
sudo service docker start

tput setaf 2;echo "Launching VM_MySQL";tput sgr0;
docker run -dti --name vm_mysql  vm_mysql;

launch_apache="docker run ";

for i in $(seq 1 $nbre_tomcats);
do 
tput setaf 2;	echo "Launching VM_Tomcat$i";tput sgr0;
# on aura pris soins à l'avance de configurer chaque serveur tomcat afin qu'il utilise le hostname vm_mysql et le port 3306
# je reste en attente de tegantchouang pour ça
	docker run --link vm_mysql -dti --name "vm_tomcat$i"  "vm_tomcat$i";

	launch_apache="$launch_apache --link vm_tomcat$i ";

done

launch_apache="$launch_apache --name vm_apache -t -i vm_apache ";

tput setaf 2;echo "Launching VM_Apache";tput sgr0;

eval $launch_apache;







