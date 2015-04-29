#!/bin/bash
if [ -z "$1" ]
    then
        echo "Failed to enter training file"
        exit
fi
if [ -z "$2" ]
    then
        echo "Failed to enter testing file"
        exit
fi
if [ -z "$3" ]
    then
        echo "Failed to enter expected results file"
        exit
fi
n=`wc -l $3 | awk '{print $1;}'`
java -cp ./bin SmoothingBayesClassifier $1 $2 > TEMPCOMPARE.txt
diff=`diff -U 0 $3 TEMPCOMPARE.txt | grep -v ^@ | wc -l`
#diff=`sdiff -B -b -s $3 TEMPCOMPARE.txt | wc -l`
rm TEMPCOMPARE.txt
bc <<< "scale = 10; 100 * (1 - (($diff - 2)/ $n))"
