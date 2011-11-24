/*
 * This software is released under the BSD license. For full license see License-library.txt file.
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine.android.example;

import android.app.Application;
import com.leanengine.LeanEngine;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeanEngine.init(this, "http://demo.lean-engine.com");

    }
}
