package net.astatis.reddot.lib;

/**
 * Created by asia on 2017/7/13.
 */

public interface DataStore {


    boolean restore(RedDot redDot);

    void save(RedDot redDot);
}
