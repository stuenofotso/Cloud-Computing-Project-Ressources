#!/bin/bash

if [[ $# < 2 ]]
then
	
	tput setaf 1;echo "Vous devez préciser le nombre d'itérations et le nom du fichier' ";tput sgr0;
exit;
fi

mtime=$(date --rfc-3339 seconds)


echo " Starting generation data... " 

for i in $(seq 1 1 $1);
do
	/usr/bin/time -f "$2\t$1\t%e" --append -o "results_sequentiel-$mtime.csv" java -Xms256m -Xmx1024m -jar wordCountSequencial.jar $2
done

echo " End ! "



