package com.leanengine.android.mainapp;

import android.app.Application;
import android.content.Intent;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;

public class BroadcastingTestListener implements TestListener {

    public static String action = "LEAN_TEST_ACTION";
    private Application context;

    public BroadcastingTestListener(Application context) {
        this.context = context;
    }

    @Override
    public void addError(Test test, Throwable throwable) {
        TestCase testCase = (TestCase) test;
        Intent intent = new Intent(action);
        intent.putExtra("event", "error");
        intent.putExtra("test", test.getClass().getSimpleName()+"."+testCase.getName());
        intent.putExtra("throwable", throwable.getMessage());
        context.sendBroadcast(intent);
    }

    @Override
    public void addFailure(Test test, AssertionFailedError assertionFailedError) {
        TestCase testCase = (TestCase) test;
        Intent intent = new Intent(action);
        intent.putExtra("event", "failure");
        intent.putExtra("test", test.getClass().getSimpleName()+"."+testCase.getName());
        intent.putExtra("assertion", assertionFailedError.getMessage());
        context.sendBroadcast(intent);
    }

    @Override
    public void endTest(Test test) {
        TestCase testCase = (TestCase) test;
        Intent intent = new Intent(action);
        intent.putExtra("event", "done");
        intent.putExtra("test", test.getClass().getSimpleName()+"."+testCase.getName());
        context.sendBroadcast(intent);
    }

    @Override
    public void startTest(Test test) {
        TestCase testCase = (TestCase) test;
        Intent intent = new Intent(action);
        intent.putExtra("event", "start");
        intent.putExtra("test", test.getClass().getSimpleName()+"."+testCase.getName());
        context.sendBroadcast(intent);
    }
}
