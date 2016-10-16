package com.github.axet.play;

import java.awt.Component;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.Memfile;
import com.github.axet.play.vlc.MemoryFile;
import com.github.axet.play.vlc.MemoryStream;
import com.github.axet.play.vlc.libvlc_callback_t;
import com.github.axet.play.vlc.libvlc_event_manager_t;
import com.github.axet.play.vlc.libvlc_event_type_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class VLC {

    public interface Listener {
        /**
         * start playing after buffering. application may want to show waitnit
         * currsor before actual play starts
         */
        public void start();

        /**
         * end reached
         */
        public void stop();

        /**
         * position changed
         * 
         * @param pos
         */
        public void position(float pos);
    }

    ArrayList<Listener> listeners = new ArrayList<VLC.Listener>();

    Memfile mem;

    VLCInstance vlc;

    VLCMediaPlayer m;

    libvlc_media_t fl;

    libvlc_callback_t evets = new libvlc_callback_t() {
        @Override
        public void libvlc_callback(IntByReference p_event, Pointer p_user_data) {
            switch (p_event.getValue()) {
            case libvlc_event_type_t.libvlc_MediaPlayerVout:
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerOpening:
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerBuffering:
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerPlaying:
                for (Listener l : listeners) {
                    l.start();
                }
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerEndReached:
                for (Listener l : listeners) {
                    l.position(1.0f);
                    l.stop();
                }
                break;
            case libvlc_event_type_t.libvlc_MediaPlayerPositionChanged:
                for (Listener l : listeners) {
                    float pos = LibVlc.INSTANCE.libvlc_media_player_get_position(m.getInstance());
                    l.position(pos);
                }
                break;
            default:
                break;
            }
        }
    };

    public VLC() {
        vlc = new VLCInstance();
        m = new VLCMediaPlayer();
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) {
        listeners.remove(l);
    }

    void create(String mrl) {
        fl = LibVlc.INSTANCE.libvlc_media_new_location(vlc.getInstance(), mrl);

        LibVlc.INSTANCE.libvlc_media_player_set_media(m.getInstance(), fl);

        libvlc_event_manager_t ev = LibVlc.INSTANCE.libvlc_media_player_event_manager(m.getInstance());
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerVout, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerOpening, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerBuffering, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerPlaying, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerEndReached, evets, null);
        LibVlc.INSTANCE.libvlc_event_attach(ev, libvlc_event_type_t.libvlc_MediaPlayerPositionChanged, evets, null);
        
        setVolume(100);
    }

    public void open(URL f) {
        if (fl != null)
            throw new RuntimeException("close first");

        create(f.toString());
    }

    public void open(File f) {
        mem = new MemoryFile(f);

        create(mem.getMrl());
    }

    public void open(InputStream is) {
        mem = new MemoryStream(is);

        create(mem.getMrl());
    }

    public void play() {
        LibVlc.INSTANCE.libvlc_media_player_play(m.getInstance());
    }

    public void stop() {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m.getInstance(), 0);
        LibVlc.INSTANCE.libvlc_media_player_stop(m.getInstance());
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        if (m != null) {
            m.close();
            m = null;
        }
        if (fl != null) {
            LibVlc.INSTANCE.libvlc_media_release(fl);
            fl = null;
        }
        if (vlc != null) {
            vlc.close();
            vlc = null;
        }
    }

    public void setVolume(int v) {
        LibVlc.INSTANCE.libvlc_audio_set_volume(m.getInstance(), v);
    }

    public void pause(boolean pause) {
        LibVlc.INSTANCE.libvlc_media_player_set_pause(m.getInstance(), pause);
    }

    public void setPosition(float f) {
        LibVlc.INSTANCE.libvlc_media_player_set_position(m.getInstance(), f);
    }

    public float getPosition() {
        return LibVlc.INSTANCE.libvlc_media_player_get_position(m.getInstance());
    }

    /**
     * Get the current movie length (in ms).
     * 
     * @return
     */
    public long getLength() {
        long t;

        t = LibVlc.INSTANCE.libvlc_media_get_duration(fl);
        if (t != -1)
            return t;

        t = LibVlc.INSTANCE.libvlc_media_player_get_time(m.getInstance());
        if (t != -1)
            return t;

        t = LibVlc.INSTANCE.libvlc_media_player_get_length(m.getInstance());
        if (t != -1)
            return t;

        return -1;
    }

    public void setVideoCanvas(Component c) {
        if (Platform.isMac())
            LibVlc.INSTANCE.libvlc_media_player_set_nsobject(m.getInstance(), Native.getComponentID(c));

        if (Platform.isLinux())
            LibVlc.INSTANCE.libvlc_media_player_set_xwindow(m.getInstance(), Native.getComponentID(c));

        if (Platform.isWindows())
            LibVlc.INSTANCE.libvlc_media_player_set_hwnd(m.getInstance(), Native.getComponentID(c));
    }
}
