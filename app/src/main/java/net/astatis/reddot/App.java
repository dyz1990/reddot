package net.astatis.reddot;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by asia on 2019/4/21
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
