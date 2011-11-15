package com.leanengine.android.mainapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.leanengine.android.mainapp.tests.SimpleEntityTest;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class TestsActivity extends Activity {

    private BroadcastReceiver receiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tests_layout);
        Button loginButton = (Button) findViewById(R.id.startButton);
        final TextView text = (TextView) findViewById(R.id.testsView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                text.setText("");
                allTests();
            }
        });

    }

    private void allTests() {
        TestSuite suite = new TestSuite();

//        suite.addTest(new SimpleEntityTest("testCreateEntity"));
//        suite.addTest(new SimpleEntityTest("testLoadEntity"));
        suite.addTest(new SimpleEntityTest("testQueryEntity"));
//        suite.addTest(new SimpleEntityTest("testDeleteEntity"));

        TestResult results = new TestResult();
        results.addListener(new BroadcastingTestListener(getApplication()));
        suite.run(results);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastingTestListener.action);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String event = intent.getStringExtra("event");
                String testname = intent.getStringExtra("test");
                final TextView text = (TextView) TestsActivity.this.findViewById(R.id.testsView);
                if (event.equals("start")) {
                    text.append(testname);
                }    else {
                    text.append(": " + event + "\n");
                }
            }
        };

        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }


}
