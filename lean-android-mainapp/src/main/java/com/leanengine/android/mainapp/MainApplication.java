package com.leanengine.android.mainapp;

import android.app.Application;
import com.leanengine.LeanEngine;

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LeanEngine.init(this, "http://lean-engine.appspot.com");

        //todo check here that login scheme contains the right hostname
    }
}
