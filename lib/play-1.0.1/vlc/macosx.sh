#!/bin/bash

export PATH=$PWD/../../vlc/extras/tools/build/bin:/bin:/sbin:/usr/bin:/usr/sbin/
export CC=/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc
export CXX=/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/c++
export OBJC=/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/cc 
export OSX_VERSION=10.8
export SDKROOT=$(xcode-select -print-path)/Platforms/MacOSX.platform/Developer/SDKs/MacOSX$OSX_VERSION.sdk

rm -rf build/VLC.app || exit 1
rm -rf natives || exit 1
mkdir -p build || exit 1

(cd ../../vlc/extras/tools && ./bootstrap) || exit 1
(cd ../../vlc/extras/tools && make) || exit 1

mkdir -p ../../vlc/contrib/osx || exit 1
(if [ ! -e ../../vlc/contrib/$HOST ]; then cd ../../vlc/contrib/osx && ../bootstrap --host=$HOST --build=$HOST && make prebuilt; fi) || exit 1
(cd ../../vlc && ./bootstrap) || exit 1

(cd ./build/ && ../../../vlc/extras/package/macosx/configure.sh --host=$HOST --build=$HOST) || exit 1
(cd ./build/ && make -j4) || exit 1

(cd ./build/ && make install) || exit 1
(mkdir -p natives) || exit 1
(find build/vlc_install_dir/ -name *plugin.dylib -exec cp {} $PWD/natives/ \;) || exit 1
(cp build/vlc_install_dir/lib/*.dylib natives/) || exit 1

# replace all absolute paths to natives librarys with @loader_path prefix
for i in natives/*.dylib; do
  echo $i
  for ii in natives/*.dylib; do
    ii=$(basename $ii)
    install_name_tool -change $PWD/build/vlc_install_dir/lib/$ii @loader_path/$ii $i
  done
done
