#!/bin/bash
cd "$(dirname "$0")"
rm -rf build
mkdir -p build
javac -cp ".:lib/*" -d build model/*.java tools/*.java panels/*.java screens/*.java
if [ $? -eq 0 ]; then
    java -cp "build:lib/*" screens.MainFile
else
    echo "Compile hatası!"
fi
