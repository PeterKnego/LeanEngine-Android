package com.leanengine.android.mainapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.leanengine.*;

public class LoginActivity extends Activity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void enableLoginButtons() {
        final Button loginFacebookButton = (Button) findViewById(R.id.loginFacebookButton);
        final Button loginGoogleButton = (Button) findViewById(R.id.loginGoogleButton);
        final Button loginYahooButton = (Button) findViewById(R.id.loginYahooButton);

        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Uri loginUri = LeanEngine.getFacebookLoginUri();

                LoginDialog fbDialog = new LoginDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess(String token) {
                        Log.d("LoginDialog", "success!");
                        LeanEngine.setAuthData(token);
                        checkLogin();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("LoginDialog", "cancelled");
                        checkLogin();
                    }

                    @Override
                    public void onError(LeanError error) {
                        Log.d("LoginDialog", "Error: " + error.getErrorMessage());
                        checkLogin();
                    }
                });

                fbDialog.show();
            }
        });

        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Uri loginUri = LeanEngine.getGoogleLoginUri();

                LoginDialog fbDialog = new LoginDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess(String token) {
                        Log.d("LoginDialog", "success!");
                        LeanEngine.setAuthData(token);
                        checkLogin();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("LoginDialog", "cancelled");
                        checkLogin();
                    }

                    @Override
                    public void onError(LeanError error) {
                        Log.d("LoginDialog", "Error: " + error.getErrorMessage());
                        checkLogin();
                    }
                });

                fbDialog.show();
            }
        });

        loginYahooButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Uri loginUri = LeanEngine.getYahooLoginUri();

                LoginDialog fbDialog = new LoginDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess(String token) {
                        Log.d("LoginDialog", "success!");
                        LeanEngine.setAuthData(token);
                        checkLogin();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("LoginDialog", "cancelled");
                        checkLogin();
                    }

                    @Override
                    public void onError(LeanError error) {
                        Log.d("LoginDialog", "Error: " + error.getErrorMessage());
                        checkLogin();
                    }
                });

                fbDialog.show();
            }
        });


    }

    private void enableLogoutButton() {
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LeanAccount.logout();
                checkLogin();
            }
        });
    }

    private void checkLogin() {

        if (LeanAccount.isLoggedIn()) {
            setContentView(R.layout.logout_layout);
            enableLogoutButton();
            MainTabWidget tabHost = (MainTabWidget) getParent();

            ((TextView) tabHost.getTabWidget().getChildTabViewAt(0).findViewById(android.R.id.title)).setText("Sign out");
            tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.VISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.VISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(3).setVisibility(View.VISIBLE);
        } else {
            setContentView(R.layout.login_layout);
            enableLoginButtons();
            MainTabWidget tabHost = (MainTabWidget) getParent();

            ((TextView) tabHost.getTabWidget().getChildTabViewAt(0).findViewById(android.R.id.title)).setText("Sign in");
            tabHost.getTabWidget().getChildTabViewAt(1).setVisibility(View.INVISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(2).setVisibility(View.INVISIBLE);
            tabHost.getTabWidget().getChildTabViewAt(3).setVisibility(View.INVISIBLE);
        }
    }
}
