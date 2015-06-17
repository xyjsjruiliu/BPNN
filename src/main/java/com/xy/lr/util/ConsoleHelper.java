package com.xy.lr.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xylr on 15-6-5.
 * 命令行读取参数程序
 */
public class ConsoleHelper {
    private Map<String, String> mArgMap;

    public ConsoleHelper(String[] args) {
        mArgMap = new HashMap<String, String>();
        for (int i = 0; i < args.length; i += 2) {
            mArgMap.put(args[i], args[i + 1]);
        }
    }

    public String getArg(String argName, String defaultValue) {
        if (mArgMap.containsKey(argName)) {
            return mArgMap.get(argName);
        } else
            return defaultValue;
    }

    public int getArg(String argName, int defaultValue) {
        if (mArgMap.containsKey(argName)) {
            return Integer.valueOf(mArgMap.get(argName));
        } else
            return defaultValue;
    }

    public float getArg(String argName, float defaultValue) {
        if (mArgMap.containsKey(argName)) {
            return Float.valueOf(mArgMap.get(argName));
        } else
            return defaultValue;
    }
}
