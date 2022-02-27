#!/bin/bash

# usage: ./runA7Interpreter.sh

# relative path from project directory - A7
#javac -verbose -d ./out/production/A7/ -sourcepath src/ -classpath out/production/A7/ src/controller/Interpreter.java
javac -verbose -d out/production/A7/ -sourcepath src/ src/interpreter/Interpreter.java
COMPILE_RET=$?
[ $COMPILE_RET -ne 0 ] && exit $COMPILE_RET

echo -e "Build done, now starting ...\n"
read -rp "Insert interpreter options: " INTERPETER_OPTIONS
java -classpath out/production/A7 interpreter.Interpreter "$INTERPETER_OPTIONS"
# Interpreter options
#   -d[h] : log program state to stdout
#   -d[h]f : log program state to file
#   -h : omit logging the program state when the top statement on the stack is a CompoundStatement

# for FILE in log/log*.txt; do echo $FILE; cat $FILE | grep "ID"; sleep 1; done

