package com.github.yjcpaj4.play_with_us.tool;

import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChecksumBuilderTool {

    private static final File RESOURCE_DIR = new File("res");
    private static final File CHECKSUM_FILE = new File(RESOURCE_DIR, "checksum.json");
    
    private ChecksumBuilderTool() {
    }
    
    public static void main(String args[]) throws IOException {
        
        Map<String, String> m = new HashMap();
        
        for (File f : RESOURCE_DIR.listFiles()) {
            if (f == null || f.equals(CHECKSUM_FILE)) {
                continue;
            }
            m.put(f.getName(), FileUtil.getChecksum(f));
        }
        
        FileUtil.setContents(CHECKSUM_FILE, new Gson().toJson(m));
    }
}
