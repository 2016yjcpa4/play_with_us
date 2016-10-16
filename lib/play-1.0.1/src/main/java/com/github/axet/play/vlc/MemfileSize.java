package com.github.axet.play.vlc;

import com.sun.jna.Callback;
import com.sun.jna.ptr.LongByReference;

public interface MemfileSize extends Callback {
    /**
     * 
     * @param vlc
     * @return VLC_SUCCESS ; VLC_EGENERIC
     */
    int size(LongByReference size);
}
