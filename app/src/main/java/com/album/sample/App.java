package com.album.sample;

import android.app.Application;

/**
 * by y on 15/08/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
    }
}
