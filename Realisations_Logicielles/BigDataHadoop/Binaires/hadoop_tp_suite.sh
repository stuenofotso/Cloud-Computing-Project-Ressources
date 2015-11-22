#!/bin/bash

hadoop fs -D dfs.blocksize=$1 -put $2 input
hadoop jar wc.jar WordCount input output



