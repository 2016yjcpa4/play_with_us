package com.github.axet.play;

import java.io.File;

import com.github.axet.mavennatives.MavenNatives;
import com.github.axet.play.vlc.LibC;
import com.github.axet.play.vlc.LibVlc;
import com.github.axet.play.vlc.libvlc_instance_t;
import com.github.axet.play.vlc.libvlc_media_t;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;

public class VLCInstance {
    static String vlc_args[] = { "-I", "dumy", "--ignore-config", "--no-xlib", "--no-video-title-show" };

    static libvlc_instance_t inst;

    static int count = 0;

    static Object lock = new Object();

    boolean close = false;

    static String VLC_CORE = "vlccore";
    static String VLC = "vlc";

    static {
        preloadLibrary(VLC_CORE, MavenNatives.mavenNative(VLC_CORE));

        File vlc = preloadLibrary(VLC, MavenNatives.mavenNative(VLC));

        setPluginPath(vlc.getParentFile());
    }

    static File preloadLibrary(String lib, File path) {
        NativeLibrary.addSearchPath(lib, path.getParent());
        NativeLibrary.getInstance(lib);

        return path;
    }

    public static void setPluginPath(File path) {
        if (Platform.isLinux() || Platform.isMac()) {
            LibC.INSTANCE.setenv("VLC_PLUGIN_PATH", path.toString(), 1);
        }

        if (Platform.isWindows()) {
            Kernel32.INSTANCE.SetEnvironmentVariable("VLC_PLUGIN_PATH", path.toString());
        }
    }

    public VLCInstance() {
        synchronized (lock) {
            if (count == 0) {
                inst = LibVlc.INSTANCE.libvlc_new(vlc_args.length, vlc_args);

                if (inst == null)
                    throw new RuntimeException("Unable to instantiate VLC");

                VLCWarmup.warmup(this);
            }

            count++;
        }
    }

    public libvlc_instance_t getInstance() {
        synchronized (lock) {
            return inst;
        }
    }

    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        synchronized (lock) {
            if (!close) {
                close = true;
                count--;
                if (count == 0) {
                    LibVlc.INSTANCE.libvlc_release(inst);
                    inst = null;
                }
            }
        }
    }

    public libvlc_media_t createMedia(String uri) {
        return LibVlc.INSTANCE.libvlc_media_new_path(getInstance(), uri);
    }
}
