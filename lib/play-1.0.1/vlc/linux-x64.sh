#!/bin/bash

./linux.sh

# pack:
rm -f libvlc-linux-x86_64.jar

(cd natives && jar cf ../libvlc-linux-x86_64.jar *)

mvn install:install-file -Dfile=libvlc-linux-x86_64.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-linux-x86_64
