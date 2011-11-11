package com.leanengine.android.mainapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.leanengine.LeanEntity;

public class TestsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tests_layout);
        Button loginButton = (Button) findViewById(R.id.startButton);
        final TextView text = (TextView) findViewById(R.id.testsView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               text.append("tests starting..");
            }
        });
    }
}
