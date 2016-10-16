package com.github.axet.play.vlc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

/**
 * 
 */
public class MemoryFile extends Memfile {
    RandomAccessFile file;
    FileChannel fc;

    public MemoryFile(final File f) {
        open = new MemfileOpen() {
            @Override
            public int open() {
                try {
                    file = new RandomAccessFile(f, "r");
                    fc = file.getChannel();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        close = new MemfileClose() {
            @Override
            public int close() {
                try {
                    fc.close();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        size = new MemfileSize() {
            @Override
            public int size(LongByReference size) {
                try {
                    size.setValue(file.length());
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        seek = new MemfileSeek() {
            @Override
            public int seek(long pos) {
                try {
                    fc.position(pos);
                } catch (IOException e) {
                    e.printStackTrace();
                    return LibVlc.VLC_EGENERIC;
                }
                return LibVlc.VLC_SUCCESS;
            }
        };

        read = new MemfileRead() {
            @Override
            public int read(Pointer buf, int bufSize) {
                ByteBuffer b = ByteBuffer.allocate(bufSize);
                try {
                    int len = fc.read(b);

                    if (len == -1)
                        return 0;

                    byte[] bb = b.array();
                    buf.write(0, bb, 0, len);

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
