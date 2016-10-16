# make clean - clean source vlc dir
# make build - build current working branch
# make master - pull last changes from master repo and build
# make pack - pack native jar from build repo

all: pack

.PHONY: build

clean:
	(cd ../../vlc && git checkout .)
	(cd ../../vlc && git clean -fdx)
	rm -rf ../../vlc/configure
	rm -rf build
	rm -rf natives

master-setup:
	(cd ../../vlc/; git remote add vlc git://git.videolan.org/vlc.git)
	(cd ../../vlc/; git fetch vlc)
	(cd ../../vlc/; git checkout -b vlc vlc/master)

.PHONY: master

# CFLAGS - for debuging, add debug information to the libvlc
# PATH - to ignore macports /opt/local/bin folder
# HOME=/ - to ignore ~/.gitconfig which is not compatible between git 1.8 and git 1.7 (apple default)

master:
	rm -rf master/
	mkdir -p master/
	(cd ../../vlc/ && git fetch vlc)
	(cd ../../vlc/ && git checkout vlc)
	(cd ../../vlc/ && git pull --rebase)
	(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin ./bootstrap)
	(cd ../../vlc/extras/tools && PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin make)
	(cd ./master/ && CFLAGS=-g PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:$PWD/../../vlc/extras/tools/build/bin HOME=/ bash ../../../vlc/extras/package/macosx/build.sh)

pack:
	rm -f vlc-mac.jar

	(cd ./build/VLC.app/Contents/MacOS/lib && jar cf ../../../../../vlc-mac.jar *)
	(cd ./build/VLC.app/Contents/MacOS/plugins && jar uf ../../../../../vlc-mac.jar *)
	
	mvn install:install-file -Dfile=vlc-mac.jar \
	  -DgroupId=org.videolan \
      -DartifactId=vlc \
      -Dversion=1.3.0-SNAPSHOT \
      -Dpackaging=jar \
      -Dclassifier=natives-mac \
      -DgeneratePom=true \
      -DcreateChecksum=true
