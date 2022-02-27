#!/bin/bash


# compile
javafxc -d out/production/A7/ -sourcepath src/ src/view/Main.java

# exit if compilation fails
COMPILE_RET=$?
[ $COMPILE_RET -ne 0 ] && exit $COMPILE_RET

# run
javafx -classpath out/production/A7 view.Main