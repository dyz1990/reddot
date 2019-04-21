package net.astatis.reddot.lib;

import android.view.View;
import android.widget.TextView;

/**
 * Created by asia on 2017/7/12.
 */

public class DefaultViewToggle implements DotViewToggle {
    @Override
    public boolean toggleView(RedDot redDot, View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return true;
    }

    @Override
    public boolean showNumber(RedDot redDot, View view) {
        int number = redDot.getNumber();
        if (view instanceof TextView) {
            ((TextView) view).setText(number);
            return true;
        }
        view.setVisibility(number > 0 ? View.VISIBLE : View.INVISIBLE);
        return true;
    }
}
