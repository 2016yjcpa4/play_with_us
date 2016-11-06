package com.github.yjcpaj4.play_with_us.resource;

import java.io.File;
import java.io.IOException;

public interface Resource {

    void load(File f, String s) throws IOException;

    void release();
}
