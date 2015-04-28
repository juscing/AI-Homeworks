#!/bin/bash
awk '{print $NF}' trainingData/census.train | tee TEMPCOMPARE.txt
n=`wc -l TEMPCOMPARE.txt | awk '{print $1;}'`
#diff=java BayesClassifier | diff -U 0 TEMPCOMPARE.txt - | grep -v ^@ | wc -l
#x=$diff/$n
echo "$n%"
