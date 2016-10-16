#!/bin/bash

./macosx.sh

# pack:
rm -f libvlc-mac-x86.jar

(cd natives && jar cf ../libvlc-mac-x86.jar *)

mvn install:install-file -Dfile=libvlc-mac-x86.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-mac-x86
