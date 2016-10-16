package com.github.axet.play.vlc;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class Memfile extends Structure {
    public MemfileOpen open;
    public MemfileClose close;
    public MemfileSize size;
    public MemfileSeek seek;
    public MemfileRead read;

    @Override
    protected List<?> getFieldOrder() {
        return Arrays.asList(new String[] { "open", "close", "size", "seek", "read" });
    }

    public Memfile() {
    }

    public long field(String name) {
        return Pointer.nativeValue(getPointer().getPointer((fieldOffset(name))));
    }

    public long getOpen() {
        return field("open");
    }

    public long getClose() {
        return field("close");
    }

    public long getSize() {
        return field("size");
    }

    public long getSeek() {
        return field("seek");
    }

    public long getRead() {
        return field("read");
    }

    public String getMrl() {
        return "memfile://" + getOpen() + "/" + getClose() + "/" + getSize() + "/" + getSeek() + "/" + getRead();
    }
}
