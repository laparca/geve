#!/bin/sh
. ./eve.conf

cp=$(echo $GROOVY_HOME/* | tr ' ' ':')
#echo $cp
java -classpath $cp:. org.eve.TestEve
