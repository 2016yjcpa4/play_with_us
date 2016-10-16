package com.github.axet.play.vlc;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface LibC extends Library {
    static LibC INSTANCE = (LibC) Native.loadLibrary("c", LibC.class);

    public int setenv(String name, String value, int overwrite);

    public int unsetenv(String name);
}
