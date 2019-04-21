package net.astatis.reddot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.astatis.reddot.lib.RedDot;
import net.astatis.reddot.lib.RedDotManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        RedDotManager.getInstance().addViewsFrom(this);
    }

    private void addNumber(View view) {
        String tag = String.valueOf(view.getTag());
        RedDotManager manager = RedDotManager.getInstance();
        RedDot redDot = manager.findRedDot(tag);
        if (redDot == null) {
            Log.w("SettingActivity", "view not bind red dot:" + tag);
            return;
        }
        manager.setNumber(tag, redDot.getNumber() + 1);
    }

    public void onSetting1Click(View view) {
        addNumber(view);
    }

    public void onSetting2Click(View view) {
        addNumber(view);
    }

    public void onSetting3Click(View view) {
        addNumber(view);
    }

    public void onClearClick(View view) {
        RedDotManager.getInstance().setNumber("setting.setting1", 0);
    }
}
