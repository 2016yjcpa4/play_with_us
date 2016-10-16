package com.github.axet.play;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.MemoryStream;
import com.github.axet.play.vlc.libvlc_callback_t;
import com.github.axet.play.vlc.libvlc_event_manager_t;
import com.github.axet.play.vlc.libvlc_event_type_t;
import com.github.axet.play.vlc.libvlc_media_player_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

/**
 * prevent broken playing at startup if here two simultenious media players
 * running.
 * 
 * 
 * [0x7fe3e1d79530] auhal audio output error: No audio output devices were
 * found.
 * 
 * [0x7fe3e1d79530] auhal audio output error: audio-device var does not exist.
 * device probe failed.
 * 
 * [0x7fe3e1d79530] auhal audio output error: opening the auhal output failed
 * 
 * [0x7fe3e1d79530] main audio output error: module not functional
 * 
 * [0x7fe3e1060eb0] main decoder error: failed to create audio output
 * 
 * @author axet
 * 
 */
public class VLCWarmup {

    static Object lock = new Object();

    static libvlc_callback_t evets = new libvlc_callback_t() {
        @Override
        public void libvlc_callback(IntByReference p_event, Pointer p_user_data) {
            switch (p_event.getValue()) {
            case libvlc_event_type_t.libvlc_MediaPlayerEndReached:
                synchronized (lock) {
                    lock.notifyAll();
                }
                break;
            default:
                break;
            }
        }
    };

    static public void warmup(VLCInstance vlc) {
        libvlc_media_player_t m = LibVlc.INSTANCE.libvlc_media_player_new(vlc.getInstance());

        MemoryStream mem = new MemoryStream(VLCWarmup.class.getResourceAsStream("empty.ogg"));
        libvlc_media_t fl = LibVlc.INSTANCE.libvlc_media_new_location(vlc.getInstance(), mem.getMrl());

        LibVlc.INSTANCE.libvlc_media_player_set_media(m, fl);

        libvlc_event_manager_t ev = LibVlc.INSTANCE.libvlc_media_player_event_manager(m);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerEndReached, evets, null);

        LibVlc.INSTANCE.libvlc_media_player_play(m);

        // wait until initial file played. then continue
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        LibVlc.INSTANCE.libvlc_media_player_release(m);
        
        LibVlc.INSTANCE.libvlc_media_release(fl);
    }

}
