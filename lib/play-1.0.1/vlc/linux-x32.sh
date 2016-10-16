#!/bin/bash

./linux.sh

# pack:
rm -f libvlc-linux-x86.jar

(cd natives && jar cf ../libvlc-linux-x86.jar *)

mvn install:install-file -Dfile=libvlc-linux-x86.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-linux-x86

