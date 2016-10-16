package com.github.axet.play.vlc;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public interface IMemGet extends StdCallCallback {
    /**
     * in]void *data is a pointer to user-defined data. It can be anything you
     * want. You set it by adding the "--imem-data=<memory_address>" argument
     * when initializing VLC. You set it up in the same manner as the callbacks.
     * 
     * [in]const char *cookie is a user-defined string. Works in the same way as
     * data, but for strings. You set it by adding the
     * "--imem-cookie=<your_string>" argument. your_string can be anything you
     * want. It can be used to identify what instance is calling your callback,
     * should you have multiple VLC instances.
     * 
     * [out]int64_t *dts and int64_t *pts are the Decode Timestamp and
     * Presentation Timestamp values in microseconds, respectively. Usually the
     * same value, but depending on the codec they might be different. In simple
     * words, this tells VLC when to show this frame. This can be a bit
     * confusing, so to to make it a bit more intuitive, in a 30 fps video, each
     * frame is played every 33 milliseconds or so. so the dts and pts for each
     * consecutive call would be 0, 33333, 66666, 99999, 133332 and so forth. In
     * my case I just took'em out of the rtp stream. (rtp streams come with a
     * similar timestamp on a clock of 90kHz)
     * 
     * [out]unsigned *flags: unused, as far as I'm aware. You can ignore this.
     * 
     * [out]size_t * bufferSize: here you should return the size of buffer, in
     * bytes
     * 
     * [out]void ** buffer: a pointer where the video frame data, encoded with
     * your preferred codec is in memory.
     * 
     * @return
     */
    int get(Pointer data, Pointer cookie, Pointer dts, Pointer pts, int flags, Pointer bufferSize, Pointer buffer);
}
