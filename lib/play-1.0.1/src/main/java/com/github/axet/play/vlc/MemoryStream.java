package com.github.axet.play.vlc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

/**
 * 
 */
public class MemoryStream extends Memfile {

    public MemoryStream(final InputStream is) {
        open = new MemfileOpen() {
            @Override
            public int open() {
                return LibVlc.VLC_SUCCESS;
            }
        };

        close = new MemfileClose() {
            @Override
            public int close() {
                return LibVlc.VLC_SUCCESS;
            }
        };

        size = new MemfileSize() {
            @Override
            public int size(LongByReference size) {
                size.setValue(-1);
                return LibVlc.VLC_EGENERIC;
            }
        };

        seek = new MemfileSeek() {
            @Override
            public int seek(long pos) {
                return LibVlc.VLC_EGENERIC;
            }
        };

        read = new MemfileRead() {
            @Override
            public int read(Pointer buf, int bufSize) {
                byte[] b = new byte[bufSize];
                try {
                    int len = is.read(b);

                    if (len == -1)
                        return 0;

                    buf.write(0, b, 0, len);

                    return len;
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
            }
        };

        write();
    }

}
