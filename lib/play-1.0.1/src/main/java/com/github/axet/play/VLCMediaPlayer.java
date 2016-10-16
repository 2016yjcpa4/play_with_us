package com.github.axet.play;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_media_player_t;

public class VLCMediaPlayer {
    VLCInstance vlc = new VLCInstance();

    libvlc_media_player_t m = LibVlc.INSTANCE.libvlc_media_player_new(vlc.getInstance());

    public VLCMediaPlayer() {
    }

    public libvlc_media_player_t getInstance() {
        return m;
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        if (m != null) {
            LibVlc.INSTANCE.libvlc_media_player_release(m);
            m = null;
        }
        if (vlc != null) {
            vlc.close();
            vlc = null;
        }
    }
}
