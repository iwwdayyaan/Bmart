package com.bart.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.bart.api.interfaces.AppComponent;
import com.bart.api.interfaces.DaggerAppComponent;


public class AppController extends Application {
    private static AppController instance;
    private static SharedPreferences spBart;
    private AppComponent component;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        component = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();

        spBart = getSharedPreferences("SpBart", MODE_PRIVATE);

    }

    public AppComponent getComponent() {
        return component;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static SharedPreferences getSharedPref() {
        return spBart;
    }
    public static synchronized AppController getInstance() {
        return instance;
    }
}
