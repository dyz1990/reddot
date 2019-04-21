package net.astatis.reddot.lib;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asia on 2017/7/12.
 */
public class RedDotManager {
    private static final String TAG = "RedDotManager";
    private RedDotTree mDotTree;
    private Map<Class, DotViewToggle> mViewToggles;
    private DataStore mDataCache;
    private Map<String, List<View>> mRedDotViews;
    private SparseArray<String> mViewTagMap = new SparseArray<>();

    private Pattern mTagPattern;

    private static RedDotManager sInstance;
    private View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            Log.i(TAG, "onViewDetachedFromWindow:" + v);
            int key = v.hashCode();
            String tag = mViewTagMap.get(key);
            mViewTagMap.remove(key);
            List<View> views = mRedDotViews.get(tag);
            if (views == null) {
                return;
            }
            boolean remove = views.remove(v);
            v.removeOnAttachStateChangeListener(this);
            Log.i(TAG, "onViewDetachedFromWindow: removed:" + remove);

        }
    };

    public static RedDotManager getInstance() {
        if (sInstance == null) {
            synchronized (RedDotManager.class) {
                if (sInstance == null) {
                    synchronized (RedDotManager.class) {
                        sInstance = new RedDotManager();
                    }
                }
            }
        }

        return sInstance;
    }

    private RedDotManager() {
        mRedDotViews = new HashMap<>();
        mViewToggles = new HashMap<>();
        mTagPattern = Pattern.compile("reddot:(.+)");
        mDotTree = new RedDotTree();
        mDataCache = new MemDataStore();
    }

    public void show(String tag) {
        final RedDot redDot = findRedDot(tag);
        if (redDot != null) {
            redDot.setVisible(true);
            performVisibleChange(redDot, true);
        } else {
            throw new RuntimeException("red dot not found with tag:" + tag);
        }
    }

    public boolean setNumber(String tag, int n) {
        final RedDot redDot = findRedDot(tag);
        if (redDot == null) {
            return false;
        }
        redDot.setNumber(n);
        performNumberChange(redDot);
        return true;
    }

    @Nullable
    public RedDot findRedDot(String tag) {
        return mDotTree.find(tag, false);
    }

    public RedDot findOrCreateRedDot(String tag) {
        return mDotTree.find(tag, true);
    }

    public void hide(String tag) {
        final RedDot redDot = findRedDot(tag);
        if (redDot != null) {
            redDot.setVisible(false);
            performVisibleChange(redDot, false);
        } else {
            throw new RuntimeException("red dot not found with tag:" + tag);
        }
    }

    private void performVisibleChange(RedDot redDot, boolean visble) {
        RedDot parent = redDot;
        if (mDataCache != null) {
            mDataCache.save(redDot);
        }
        while (parent != null) {
            final List<View> views = mRedDotViews.get(parent.getPathString());
            if (views != null) {
                for (int i = 0; i < views.size(); i++) {
                    setDotViewVisible(parent, views.get(i));
                }
            }
            parent = parent.getParent();
        }
    }

    private void setDotViewVisible(RedDot redDot, View view) {
        final DotViewToggle dotViewToggle = mViewToggles.get(view.getClass());
        boolean visible = redDot.isVisible();
        if (dotViewToggle == null
                || !dotViewToggle.toggleView(redDot, view, visible)
                ) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }


    private void performNumberChange(RedDot redDot) {
        RedDot parent = redDot;
        if (mDataCache != null) {
            mDataCache.save(redDot);
        }
        while (parent != null) {
            final List<View> views = mRedDotViews.get(parent.getPathString());
            if (views != null) {

                for (int i = 0; i < views.size(); i++) {
                    setDotViewNumber(parent, views.get(i));
                }
            }
            parent = parent.getParent();
        }
    }

    private void setDotViewNumber(RedDot redDot, View view) {
        final DotViewToggle dotViewToggle = mViewToggles.get(view.getClass());
        if (dotViewToggle == null || !dotViewToggle.showNumber(redDot, view)) {
            int number = redDot.getNumber();
            if (view instanceof TextView) {
                ((TextView) view).setText(String.valueOf(number));
            }
            view.setVisibility(number > 0 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public RedDot addView(String path, View view) {
        final RedDot redDot = findOrCreateRedDot(path);
        cacheView(view, path);
        if (mDataCache != null) {
            mDataCache.restore(redDot);
        }
        setDotViewVisible(redDot, view);
        setDotViewNumber(redDot, view);
        mViewTagMap.put(view.hashCode(), path);
        return redDot;
    }

    private void cacheView(View view, String tagString) {
        List<View> views = mRedDotViews.get(tagString);
        if (views == null) {
            views = new ArrayList<>();
            mRedDotViews.put(tagString, views);
        }
        views.add(view);
        view.addOnAttachStateChangeListener(mAttachStateChangeListener);
    }


    public void addViewsFrom(Activity activity) {
        addViewsFrom((ViewGroup) activity.getWindow().getDecorView());
    }

    public void addViewsFrom(ViewGroup viewGroup) {
        final int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            RedDot redDot = checkRedDotView(view);
            if (redDot == null && view instanceof ViewGroup) {
                addViewsFrom((ViewGroup) view);
            }
        }
    }

    private RedDot checkRedDotView(View view) {
        Object tag = view.getTag();
        if (tag instanceof String) {
            String tagStr = (String) tag;
            final Matcher matcher = mTagPattern.matcher(tagStr);
            if (matcher.matches()) {
                final String path = matcher.group(1);
                return addView(path, view);
            }
        }
        return null;
    }

    public void addViewToggle(Class<? extends View> clazz, DotViewToggle toggle) {
        mViewToggles.put(clazz, toggle);
    }

    public RedDot getRoot() {
        return mDotTree.getRoot();
    }

    public void load() {

    }

    /**
     * 查找指定节点下的所有叶子节点
     *
     * @param root
     * @return
     */
    public static List<RedDot> findLeafRedDots(RedDot root) {
        List<RedDot> redDots = new ArrayList<>();
        Stack<RedDot> stack = new Stack<>();
        stack.push(root);
        RedDot parent = null;
        while (!stack.isEmpty()) {
            parent = stack.pop();
            final int childCount = parent.getChildCount();
            if (childCount > 0) {
                for (RedDot redDot :
                        parent.mChildren.values()) {
                    stack.push(redDot);
                }

            } else {
                redDots.add(parent);
            }
        }
        return redDots;
    }

    public DataStore getDataCache() {
        return mDataCache;
    }

    public void setDataCache(DataStore dataCache) {
        mDataCache = dataCache;
    }
}
