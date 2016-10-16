package com.github.axet.play.vlc;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

public interface IMemRelease extends StdCallCallback {
    /**
     * void *data and const char *cookie are exactly the same as above
     * 
     * [in]size_t bufferSize is the size of buffer, in bytes
     * 
     * [in]void * buffer is the buffer you must deallocate. Basically you
     * release the memory allocated for the video frame in your "get" function.
     * bufferSize and buffer should be the same you passed to the get function.
     */
    int myImemReleaseCallback(Pointer data, String cookie, int bufferSize, Pointer buffer);
}
