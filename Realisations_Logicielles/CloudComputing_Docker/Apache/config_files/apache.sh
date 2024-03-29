#!/bin/bash

#lancement du service apache
service apache2 restart;

# détermination de l'ip de la vm_apache
line=$(head -n 1 /etc/hosts);

OIFS="$IFS";
line1=${line//[[:blank:]]/\*};
echo "$str1";
IFS='* ';
read -a tab_apache_host_name <<< $(echo "$line1");
IFS="$OIFS";

tput setaf 2;
echo "";
echo "";
echo "";

echo "Le cluster s'est lancé avec succès.";
echo "";
tput setaf 4;
echo " Rendez vous à l'adresse http://$tab_apache_host_name/Annuaire/add pour tester le cluster...";
tput setaf 2;
echo "";
echo "";
echo "Saisissez svp exit pour fermer le serveur apache et n'oubliez pas d'exécuter le script stop.sh pour stopper le cluster avant si nécessaire d'exécuter le script start.sh pour le relancer à nouveau...";
tput sgr0;

#lancement du bash
/bin/bash;
