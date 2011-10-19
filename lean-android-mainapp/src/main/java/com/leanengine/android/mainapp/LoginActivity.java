package com.leanengine.android.mainapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.leanengine.FbDialog;
import com.leanengine.LeanAccount;
import com.leanengine.LeanEngine;
import com.leanengine.LoginListener;
import com.leanengine.rest.RestException;

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

                Uri loginUri = LeanEngine.getFacebookLoginUri();

                FbDialog fbDialog = new FbDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess(String token) {
                        Log.d("FbDialog", "success!");
                        LeanEngine.setAuthData(token);
                        checkLogin(loginButton, logoutButton, tabHost);
                    }

                    @Override
                    public void onCancel() {
                        Log.d("FbDialog", "cancelled");
                        checkLogin(loginButton, logoutButton, tabHost);
                    }

                    @Override
                    public void onError(RestException exception) {
                        Log.d("FbDialog", "Error: " + exception.getMessage());
                        checkLogin(loginButton, logoutButton, tabHost);
                    }
                });

                fbDialog.show();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                loginButton.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.INVISIBLE);
                tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.INVISIBLE);
                tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.INVISIBLE);

                LeanAccount.logout();
                checkLogin(loginButton, logoutButton, tabHost);

            }
        });

        checkLogin(loginButton, logoutButton, tabHost);

    }

    private void checkLogin(View loginButton, View logoutButton, MainTabWidget tabHost) {
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
