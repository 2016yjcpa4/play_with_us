#!/bin/bash

sudo apt-get build-dep -y vlc

# disable lua https://github.com/axet/play/issues/1

OPTS="--disable-lua --enable-x11 --enable-xvideo --enable-sdl --enable-avcodec --enable-avformat \
 --enable-swscale --enable-mad --enable-libdvbpsi --enable-a52 --enable-libmpeg2 --enable-dvdnav \
 --enable-faad --enable-vorbis --enable-ogg --enable-theora --enable-faac --enable-mkv --enable-freetype \
 --enable-fribidi --enable-speex --enable-flac \
 --enable-caca --enable-skins --enable-skins2 --enable-alsa --enable-ncurses"

rm -rf natives
mkdir -p build
mkdir -p ../../vlc/contrib/linux
(cd ../../vlc/contrib/linux && ../bootstrap) || exit 1
(if ! [ -e "../../vlc/configure" ]; then cd ../../vlc && autoreconf && ./bootstrap; fi) || exit 1
(if [ "../../vlc/configure" -nt "./build/Makefile" ]; then cd ./build/ && ../../../vlc/configure ${OPTS} --prefix=`pwd`/vlc_install_dir; fi) || exit 1
(cd ./build/ && make) || exit 1
(cd ./build/ && make install) || exit 1
(mkdir -p natives) || exit 1
(find build/vlc_install_dir/ -name *plugin*.so -exec cp {} ${PWD}/natives/ \;) || exit 1
(cp -r build/vlc_install_dir/lib/libvlc* natives/) || exit 1
