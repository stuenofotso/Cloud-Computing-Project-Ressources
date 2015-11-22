#!/bin/bash


if [[ $# < 4 ]]
then
	
	tput setaf 1;echo "vous devez préciser le pas, la taille min des bloc et la taille max des bloc. ";tput sgr0;
exit;
fi

pas=$2;
min=$1;
max=$3;
val=$min;
mtime=$5;
	stop-dfs.sh
	sudo rm -R -f /tmp/*
	hdfs namenode -format
	start-dfs.sh
#	sudo -u hdfs hadoop fs -chown $USER /
	hdfs dfs -mkdir /user
	hdfs dfs -mkdir /user/$USER
	hdfs dfs -mkdir input
while [  $val -le $max ] 
do
	echo "hadoop fs -D dfs.blocksize=$val -put $4 input"
	/usr/bin/time -f "$val\t%e" -o "stats-$mtime.csv" --append ./hadoop_tp_suite.sh $val $4
	let val=$val*$pas;
	if  [  $val -le $max ]
	then
		hdfs dfs -rm -R output
		hdfs dfs -rm -R input/*
	fi
    	clear

done


