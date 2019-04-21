package net.astatis.reddot.lib;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asia on 2017/7/12.
 */

public class RedDot {
    private String mName;//c
    private String mTagString;//a.b.c

    private boolean mVisible;
    private RedDot mParent;
    private int mNumber;

//    private Set<View> mViews;

    //    private List<RedDot> mChildren;
    Map<String, RedDot> mChildren;

    RedDot(@NonNull String name) {
        this(name, null);
    }

    private RedDot(@NonNull String name, RedDot parent) {
        this.mName = name;
        mParent = parent;
        mChildren = new HashMap<>();
//        mViews = new HashSet<>();

        if (mParent != null) {
            mParent.addChild(this);
        }
    }

    public String getName() {
        return mName;
    }

    RedDot getParent() {
        return mParent;
    }

    int getChildCount() {
        return mChildren.size();
    }

    RedDot getChildAt(int i) {
        return mChildren.get(i);
    }

    RedDot addChild(RedDot child) {
        mChildren.put(child.mName, child);
        child.mParent = this;
//        if (this.mPath.length > 0) {
//            child.mPath = new String[this.mPath.length + 1];
//            System.arraycopy(this.mPath, 0, child.mPath, 0, this.mPath.length);
//        } else {
//            child.mPath = new String[1];
//        }
//        child.mPath[child.mPath.length - 1] = child.mName;
        child.mTagString = null;
        return this;
    }

    RedDot addChildren(RedDot... children) {
        for (RedDot child :
                children) {
            addChild(child);
        }
        return this;
    }

    /**
     * 获取指定名称的子节点
     *
     * @param name
     * @return
     */
    public RedDot getByName(@NonNull String name) {
        return getByName(name, false);
    }

    /**
     * 获取指定名称的子节点，如果不存在，则创建
     *
     * @param name
     * @param create
     * @return
     */
    RedDot getByName(@NonNull String name, boolean create) {
        RedDot child = mChildren.get(name);
        if (child != null) return child;
        if (!create) return null;

        RedDot redDot = new RedDot(name, this);
        addChild(redDot);
        return redDot;
    }

    /**
     * 查找指点路径的节点
     *
     * @param path
     * @return
     */
    public RedDot findByPath(@NonNull String path) {
        return findOrCreateByPath(path, false);
    }

    RedDot findOrCreateByPath(@NonNull String path, boolean create) {

        if (path.equals(mTagString)) {
            return this;
        }

        String[] names = path.split("\\.");
//        if (names.length < mPath.length) return null;
        RedDot result = this;
        for (String name : names) {
            RedDot child = result.getByName(name, create);
            if (child != null) {
                result = child;
            } else {
                return null;
            }
        }

        return result;
    }

    public boolean isVisible() {
        if (mChildren.size() > 0) {
            Collection<RedDot> values = mChildren.values();
            for (RedDot redDot :
                    values) {
                if (redDot.isVisible()) {
                    return true;
                }
            }
        }
        return mVisible;
    }

    public RedDot setVisible(boolean visible) {
        mVisible = visible;

        return this;
    }

    /**
     * 获取红点的数字
     *
     * @return
     */
    public int getNumber() {
        return mNumber;
    }

    RedDot setNumber(int number) {
        int diff = number - mNumber;
        mNumber = number;
        final RedDot parent = mParent;
        if (parent != null) {
            parent.setNumber(parent.mNumber + diff);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{name=").append(mName)
                .append(",path=").append(getPathString());
        sb.append(",children=[");
        for (RedDot child :
                mChildren.values()) {
            sb.append(child);
            sb.append("\n,");
        }
        sb.append("]");

        return sb.append('}').toString();
    }

    public String getPathString() {
        if (mTagString == null) {
            StringBuilder sb = new StringBuilder(mName);
            RedDot parent = mParent;
            while (parent != null) {
                if (parent.mParent != null)
                    sb.insert(0, ".");
                sb.insert(0, parent.mName);
                parent = parent.mParent;
            }

            mTagString = sb.toString();
        }
        return mTagString;
    }
}
