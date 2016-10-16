package com.github.axet.play.vlc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface LibVlc extends Library {

    public static final int VLC_SUCCESS = 0;
    public static final int VLC_EGENERIC = -1;

    static LibVlc INSTANCE = (LibVlc) Native.loadLibrary("vlc", LibVlc.class);

    // media

    libvlc_media_t libvlc_media_new_as_node(libvlc_instance_t p_instance, String psz_name);

    libvlc_media_t libvlc_media_new_fd(libvlc_instance_t p_instance, int fd);

    libvlc_media_t libvlc_media_new_path(libvlc_instance_t p_instance, String path);

    libvlc_media_t libvlc_media_new_location(libvlc_instance_t p_instance, String psz_mrl);

    long libvlc_media_get_duration(libvlc_media_t p_md);

    void libvlc_media_release(libvlc_media_t p_md);

    // instance
    libvlc_instance_t libvlc_new(int argc, String[] argv);

    void libvlc_release(libvlc_instance_t p_instance);

    libvlc_media_player_t libvlc_media_player_new(libvlc_instance_t p_libvlc_instance);

    void libvlc_media_player_release(libvlc_media_player_t p_mi);

    // events

    libvlc_event_manager_t libvlc_media_player_event_manager(libvlc_media_player_t p_mi);

    int libvlc_event_attach(libvlc_event_manager_t p_event_manager, int i_event_type, libvlc_callback_t f_callback,
            Pointer user_data);

    float libvlc_media_player_get_position(libvlc_media_player_t p_mi);

    float libvlc_media_player_set_position(libvlc_media_player_t p_mi, float pos);

    // player

    long libvlc_media_player_get_length(libvlc_media_player_t p_mi);

    long libvlc_media_player_get_time(libvlc_media_player_t p_mi);

    // vlc video

    void libvlc_media_player_set_media(libvlc_media_player_t p_mi, libvlc_media_t p_md);

    void libvlc_media_player_set_hwnd(libvlc_media_player_t p_mi, long drawable);

    void libvlc_media_player_set_nsobject(libvlc_media_player_t p_mi, long drawable);

    void libvlc_media_player_set_xwindow(libvlc_media_player_t p_mi, long drawable);

    void libvlc_media_player_set_agl(libvlc_media_player_t p_mi, Pointer drawable);

    // controls

    int libvlc_media_player_play(libvlc_media_player_t p_mi);

    int libvlc_media_player_stop(libvlc_media_player_t p_mi);

    void libvlc_media_player_set_pause(libvlc_media_player_t mp, int do_pause);

    void libvlc_audio_set_volume(libvlc_media_player_t mp, int i_volume);

    int libvlc_audio_get_volume(libvlc_media_player_t mp);

    void libvlc_media_player_set_pause(libvlc_media_player_t mp, boolean pause);

    boolean libvlc_media_player_is_playing(libvlc_media_player_t mp);
}
