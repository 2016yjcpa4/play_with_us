package com.github.axet.play.vlc;

import com.sun.jna.Callback;

public interface MemfileClose extends Callback {
    /**
     * 
     * @param vlc
     * @return VLC_SUCCESS ; VLC_EGENERIC
     */
    int close();
}
