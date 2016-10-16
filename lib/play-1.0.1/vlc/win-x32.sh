#!/bin/bash

export HOST=i686-w64-mingw32
export VLC=vlc-2.0.6

# minimum mingw-w64-3.0
#
# mingw-w64-i686-dev                    3.0~svn5496-1  
# mingw-w64-tools                       3.0~svn5496-1  
#
sudo apt-get -t experimental install -y binutils-mingw-w64-i686 gcc-mingw-w64-i686 g++-mingw-w64-i686 mingw-w64-tools mingw-w64-i686-dev || exit  1

./win.sh || exit 1

# pack:
rm -f libvlc-windows-x86.jar || exit 1

rm -rf natives || exit 1
mkdir -p natives || exit 1

cp build/$VLC/*.dll natives/ || exit 1
cp -r build/$VLC/plugins natives/ || exit 1

(cd natives && jar cf ../libvlc-windows-x86.jar *) || exit 1

mvn install:install-file -Dfile=libvlc-windows-x86.jar \
  -DgroupId=com.github.axet.play \
  -DpomFile=libvlc.pom \
  -Dpackaging=jar \
  -Dclassifier=natives-windows-x86 || exit 1

