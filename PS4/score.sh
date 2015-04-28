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
awk '{print $NF}' trainingData/census.train > TEMPCOMPARE1.txt
n=`wc -l TEMPCOMPARE1.txt | awk '{print $1;}'`
java -cp ./bin BayesClassifier $1 $2 > TEMPCOMPARE2.txt
diff=`sdiff -B -b -s TEMPCOMPARE1.txt TEMPCOMPARE2.txt | wc -l`
rm TEMPCOMPARE1.txt
rm TEMPCOMPARE2.txt
bc <<< "scale = 10; 100 * ($diff / $n)"
