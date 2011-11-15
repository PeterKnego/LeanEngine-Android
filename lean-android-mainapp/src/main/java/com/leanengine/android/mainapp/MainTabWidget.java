package com.leanengine.android.mainapp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import com.leanengine.LeanEngine;
import com.leanengine.LoginData;

public class MainTabWidget extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LoginData loginData = LeanEngine.handleLogin(this);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, LoginActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("signin").setIndicator("Sign in",
                res.getDrawable(R.drawable.ic_tab_signin))
                .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, EditActivity.class);
        spec = tabHost.newTabSpec("edit").setIndicator("Edit",
                res.getDrawable(R.drawable.ic_tab_edit))
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ViewActivity.class);
        spec = tabHost.newTabSpec("view").setIndicator("View",
                res.getDrawable(R.drawable.ic_tab_view))
                .setContent(intent);
        tabHost.addTab(spec);

         intent = new Intent().setClass(this, TestsActivity.class);
        spec = tabHost.newTabSpec("tests").setIndicator("Test",
                res.getDrawable(R.drawable.ic_tab_test))
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.INVISIBLE);
        tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.INVISIBLE);
        tabHost.getTabWidget().getChildTabViewAt(3).setVisibility(View.INVISIBLE);

    }
}