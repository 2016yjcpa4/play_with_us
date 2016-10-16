package com.github.axet.play.vlc;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * String vlc_args[] = { "--imem-get=" + get.getIMemGet(), "--imem-release=" +
 * get.getIMemRelease(), "--imem-cat=1" };
 * 
 * libvlc_instance_t inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length,
 * vlc_args);
 * 
 * @author axet
 * 
 */
public class IMem extends Structure {
    public IMemGet get;
    public IMemRelease release;

    @Override
    protected List<?> getFieldOrder() {
        return Arrays.asList(new String[] { "get", "release" });
    }

    public IMem() {
        get = new IMemGet() {
            @Override
            public int get(Pointer data, Pointer cookie, Pointer dts, Pointer pts, int flags, Pointer bufferSize,
                    Pointer buffer) {
                return 0;
            }
        };

        release = new IMemRelease() {
            @Override
            public int myImemReleaseCallback(Pointer data, String cookie, int bufferSize, Pointer buffer) {
                return 0;
            }
        };

        write();
    }

    public long getIMemGet() {
        return getPointer().getLong(fieldOffset("get"));

    }

    public long getIMemRelease() {
        return getPointer().getLong(fieldOffset("release"));

    }
}
