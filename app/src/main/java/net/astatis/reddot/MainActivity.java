package net.astatis.reddot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.astatis.reddot.lib.RedDotManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RedDotManager redDotManager = RedDotManager.getInstance();
        redDotManager.addViewsFrom(this);

    }

    public void onSettingClick(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
