#!/bin/bash

zip README.jar README

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dfile=README.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc.pom \
  -Dclassifier=sources \
  -Dpackaging=jar \
  -Dfile=README.jar

mvn gpg:sign-and-deploy-file \
  -DuseAgent=true \
  -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ \
  -DrepositoryId=sonatype-nexus-staging \
  -DpomFile=libvlc.pom \
  -Dclassifier=javadoc \
  -Dpackaging=jar \
  -Dfile=README.jar
