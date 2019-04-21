package net.astatis.reddot.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asia on 2019/4/21
 */
public class MemDataStore implements DataStore {
    private Map<String, Boolean> mVisbleMap = new HashMap<>();
    private Map<String, Integer> mNumberMap = new HashMap<>();

    public boolean setVisible(String key, boolean visible) {
        mVisbleMap.put(key, visible);
        return true;
    }

    public boolean setNumber(String key, int number) {
        mNumberMap.put(key, number);
        return true;
    }

    @Override
    public boolean restore(RedDot redDot) {
        String key = redDot.getPathString();
        Boolean visible = mVisbleMap.get(key);
        if (visible != null) {
            redDot.setVisible(visible);
        }
        Integer integer = mNumberMap.get(key);
        if (integer != null) {
            redDot.setNumber(integer);
        }

        return true;
    }

    @Override
    public void save(RedDot redDot) {
        String pathString = redDot.getPathString();
        setNumber(pathString,redDot.getNumber());
        setVisible(pathString, redDot.isVisible());
    }
}
