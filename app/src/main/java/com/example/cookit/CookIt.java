package com.example.cookit;

import android.app.Application;
import android.content.Context;

public class CookIt extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
