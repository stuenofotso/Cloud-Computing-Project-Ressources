#!/bin/bash

if [[ $# < 4 ]]
then
	
	tput setaf 1;echo "vous devez préciser la valeur min, le pas et la valeur max pour chaque parametre (1 parametre), plus le nom du fichier ";tput sgr0;
exit;
fi

mtime=$(date --rfc-3339 seconds)

echo " Starting generation data to HADOOP... "

mtime=${mtime//[[:blank:]]/}


./hadoop_tp.sh $1 $2 $3 $4 $mtime

echo "End !"

echo " Starting to drawn curve... "

java -jar launch_hadoop_perf.jar "stats-$mtime.csv"

echo " Generation finished ! "
