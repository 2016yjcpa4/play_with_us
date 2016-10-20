package org.game.util;

import java.io.File;

public class FileUtil {

    private FileUtil() {
    }
    
    public static String getNameWithoutExtension(File f) {
        try {
            String s = f.getName();
            return s.substring(0, s.lastIndexOf("."));
        } 
        catch (Exception e) {
            return "";
        }
    }
    
    public static String getExtension(File f) {
        try {
            String s = f.getName();
            return s.substring(s.lastIndexOf(".") + 1).toLowerCase();
        } 
        catch (Exception e) {
            return "";
        }
    }
}
