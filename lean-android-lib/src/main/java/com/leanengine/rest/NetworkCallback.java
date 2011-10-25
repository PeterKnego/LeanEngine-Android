package com.leanengine.rest;

import com.leanengine.LeanError;

public interface NetworkCallback<T> {

    public void onResult(T... result);

    public void onFailure(LeanError error);
}
