package net.astatis.reddot.lib;

import android.view.View;

/**
 * Created by asia on 2017/7/12.
 */

public interface DotViewToggle {

    boolean toggleView(RedDot redDot, View view, boolean visible);

    boolean showNumber(RedDot redDot, View view);
}
