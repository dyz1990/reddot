package net.astatis.reddot.lib;

/**
 * Created by asia on 2019/4/21
 */
public class RedDotTree {
    private RedDot mRoot = new RedDot("");

    public RedDot getRoot() {
        return mRoot;
    }

    public RedDot add(String tags) {
        String[] split = tags.split("\\.");
        RedDot cur = mRoot;
        for (int i = 0; i < split.length; i++) {
            String name = split[i];
            RedDot redDot = cur.mChildren.get(name);
            if (redDot == null) {
                cur.mChildren.put(name, redDot = new RedDot(name));
            }
            cur = redDot;
        }
        return cur;
    }

    /**
     * 查找指定的tag
     *
     * @param tags
     * @param create 没有的时候是否创建
     * @return
     */
    public RedDot find(String tags, boolean create) {
        RedDot cur = mRoot;
        String[] split = tags.split("\\.");
        for (int i = 0; i < split.length; i++) {
            String name = split[i];
            RedDot redDot = cur.getByName(name, create);
            if(redDot==null) return null;
            cur = redDot;
        }
        return cur;
    }
}
