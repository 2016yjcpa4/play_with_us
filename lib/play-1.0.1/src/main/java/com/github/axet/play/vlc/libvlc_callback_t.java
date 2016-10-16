package com.github.axet.play.vlc;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface libvlc_callback_t extends Callback {
    void libvlc_callback(IntByReference p_event, Pointer p_user_data);
}
