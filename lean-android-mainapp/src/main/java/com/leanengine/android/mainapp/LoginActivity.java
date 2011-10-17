package com.leanengine.android.mainapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.leanengine.LeanAccount;

public class LoginActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

    }

    @Override
    protected void onResume() {
        super.onResume();


        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final Button logoutButton = (Button) findViewById(R.id.logoutButton);
        final MainTabWidget tabHost = (MainTabWidget) getParent();

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//
                LeanAccount.loginFacebookInBrowser();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.INVISIBLE);
                tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.INVISIBLE);
                tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.INVISIBLE);

                LeanAccount.logout();
            }
        });

        if (LeanAccount.isLoggedIn()) {
            loginButton.setVisibility(View.INVISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.VISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.INVISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.INVISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.INVISIBLE);
        }
    }
}
