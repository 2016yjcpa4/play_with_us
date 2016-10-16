package com.github.axet.play.vlc;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface MemfileRead extends Callback {
    /**
     * 
     * @param vlc
     * @return VLC_SUCCESS ; VLC_EGENERIC
     */
    int read(Pointer buf, int bufSize);
}
