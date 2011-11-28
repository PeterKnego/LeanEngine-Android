/*
 * This software is released under the BSD license. For full license see License-library.txt file.
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine.android.example;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.leanengine.*;

public class LoginActivity extends Activity {
    private static String DEFAULT_HOST_URL = "http://demo.lean-engine.com";


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
        final EditText urlEditText = (EditText) findViewById(R.id.serverUrl);
        final Button resetButton = (Button) findViewById(R.id.resetButton);

        SharedPreferences preferences = getSharedPreferences("lean-android-prefs", 0);
        String hostUrl = preferences.getString("url", null);

        if (hostUrl == null || hostUrl.length() == 0) {
            hostUrl = DEFAULT_HOST_URL;
        }
        urlEditText.setText(hostUrl);

        loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                initializeLeanEngineAndSaveUrl(urlEditText.getText().toString());
                Uri loginUri = LeanEngine.getFacebookLoginUri();

                LoginDialog fbDialog = new LoginDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("LoginDialog", "success!");
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
                        Toast toast = Toast.makeText(LoginActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG);
                        toast.show();
                        checkLogin();
                    }
                });

                fbDialog.show();
            }
        });

        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                initializeLeanEngineAndSaveUrl(urlEditText.getText().toString());
                Uri loginUri = LeanEngine.getGoogleLoginUri();

                LoginDialog fbDialog = new LoginDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("LoginDialog", "success!");
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
                        Toast toast = Toast.makeText(LoginActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG);
                        toast.show();
                        checkLogin();
                    }
                });

                fbDialog.show();
            }
        });

        loginYahooButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                initializeLeanEngineAndSaveUrl(urlEditText.getText().toString());
                Uri loginUri = LeanEngine.getYahooLoginUri();

                LoginDialog fbDialog = new LoginDialog(LoginActivity.this, loginUri.toString(), new LoginListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("LoginDialog", "success!");
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
                        Toast toast = Toast.makeText(LoginActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG);
                        toast.show();
                        checkLogin();
                    }
                });

                fbDialog.show();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlEditText.setText(DEFAULT_HOST_URL);
            }
        });
    }

    private void initializeLeanEngineAndSaveUrl(String host) {
        saveUrl(host);
        LeanEngine.init(getApplicationContext(), host);
    }

    private void enableLogoutButton() {
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LeanAccount.logoutInBackground(new NetworkCallback<Boolean>() {
                    @Override
                    public void onResult(Boolean... result) {
                        Toast.makeText(LoginActivity.this, "Successfully logged out.", Toast.LENGTH_LONG).show();
                        checkLogin();
                    }

                    @Override
                    public void onFailure(LeanError error) {
                        Log.d("LoginDialog", "Error: " + error.getErrorMessage());
                        Toast.makeText(LoginActivity.this, error.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void checkLogin() {

        if (LeanAccount.isUserLoggedIn()) {
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

    private void saveUrl(String url) {
        SharedPreferences preferences = getSharedPreferences("lean-android-prefs", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("url", url);
        editor.commit();
    }
}
