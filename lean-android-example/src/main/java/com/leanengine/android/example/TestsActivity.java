/*
 * This software is released under the BSD license. For full license see License-library.txt file.
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine.android.example;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.leanengine.android.example.tests.AccountTest;
import com.leanengine.android.example.tests.LongTextTest;
import com.leanengine.android.example.tests.QueryTest;
import com.leanengine.android.example.tests.UnicodeTest;
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

        AsyncTask<Void, Void, Void> testTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                TestSuite suite = new TestSuite();

                suite.addTest(new AccountTest("account present"));
                suite.addTest(new QueryTest("query test"));
                suite.addTest(new LongTextTest("long text properties"));
                suite.addTest(new UnicodeTest("unicode text properties"));

                TestResult results = new TestResult();
                results.addListener(new BroadcastingTestListener(getApplication()));
                suite.run(results);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(TestsActivity.this, "Tests finished.", Toast.LENGTH_SHORT).show();
            }
        };

        testTask.execute((Void)null);

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
                } else {
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
