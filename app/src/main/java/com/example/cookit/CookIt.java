package com.example.cookit;

import android.app.Application;
import android.content.Context;

public class CookIt extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

}
