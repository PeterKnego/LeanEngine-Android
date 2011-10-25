package com.leanengine;

import com.leanengine.LeanError;

import java.util.concurrent.ExecutionException;

public interface NetworkCallback<T> {

    public abstract void onResult(T... result);

    public abstract void onFailure(LeanError error);

}
