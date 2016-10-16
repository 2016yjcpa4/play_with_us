#!/bin/bash

./deploy.sh

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc.pom \
  -Dclassifier=natives-windows-x86 \
  -Dpackaging=jar \
  -Dfile=libvlc-windows-x86.jar
