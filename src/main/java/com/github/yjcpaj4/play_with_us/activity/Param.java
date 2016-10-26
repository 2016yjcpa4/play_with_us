package com.github.yjcpaj4.play_with_us.activity;

import java.util.HashMap;
import java.util.Map;

public class Param {

    private Map<String, Object> mParams = new HashMap<String, Object>();

    public void putInt(String k, int v) { mParams.put(k, v); }
    public void putString(String k, String v) { mParams.put(k, v); }
    
    public int getInt(String k) { return getInt(k, -1); }
    public int getInt(String k, int n) { return mParams.containsKey(k) ? (int) mParams.get(k) : n; }
    public String getString(String k) { return getString(k, null); }
    public String getString(String k, String d) { return mParams.containsKey(k) ? (String) mParams.get(k) : d; }
}
