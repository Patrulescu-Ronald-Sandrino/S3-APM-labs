#!/bin/bash

# usage: ./runA6.sh

# relative path from project directory - A6
#javac -verbose -d ./out/production/A6/ -sourcepath src/ -classpath ./out/production/A6/ src/controller/Interpreter.java
[ -d "log" ] || mkdir log
javac -verbose -d ./out/production/A6/ -sourcepath src/ src/controller/Interpreter.java
COMPILE_RET=$?
[ $COMPILE_RET -ne 0 ] && exit $COMPILE_RET

echo -e "Build done, now starting ...\n"
read -rp "Insert interpreter options: " INTERPETER_OPTIONS
java -classpath ./out/production/A6 controller.Interpreter "$INTERPETER_OPTIONS"
# Interpreter options
#   -d[h] : log program state to stdout
#   -d[h]f : log program state to file
#   -h : omit logging the program state when the top statement on the stack is a CompoundStatement

# for FILE in log/log*.txt; do echo $FILE; cat $FILE | grep "ID"; sleep 1; done

