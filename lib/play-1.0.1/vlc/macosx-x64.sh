#!/bin/bash

export HOST=x86_64-apple-darwin10

./macosx.sh || exit 1

# pack:
rm -f libvlc-mac-x86_64.jar || exit 1

(cd natives && jar cf ../libvlc-mac-x86_64.jar * || exit 1)

mvn install:install-file -Dfile=libvlc-mac-x86_64.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-mac-x86_64 || exit 1
