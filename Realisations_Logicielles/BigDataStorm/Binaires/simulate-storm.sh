#!/bin/bash

if [[ $# < 10 ]]
then
	
	tput setaf 1;echo "vous devez préciser la valeur min, le pas et la valeur max pour chaque parametre (3 parametres), plus le nom du fichier ";tput sgr0;
exit;
fi

mtime=$(date --rfc-3339 seconds)

re="results_excel-$mtime.csv"

doc="doc-$mtime"

mkdir "$doc"
echo " Starting generation data... " 

for i in $(seq $1 $2 $3);
do
	mkdir "$doc/$i"
	for j in $(seq $4 $5 $6);
	do
		mkdir "$doc/$i/$j"
		for k in $(seq $7 $8 $9);
		do
		/usr/bin/time -f "$k\t%e" -o "tmp" java -Xms256m -Xmx1024m -jar launch_wordcount_topology.jar ${10} results 2 $i $j $k
		cat "tmp" >> "$doc/$i/$j/results.csv"
		echo "$i	$j	$(cat tmp)" >> "$re"

		sleep 10

		done
	done
done

echo " End ! "

mkdir "img-$mtime"
echo " Starting to drawn curves... "
java -jar launch_storm_perf.jar "$doc"
echo " Generation finished ! "
rm "tmp"
rm "results"

