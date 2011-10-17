package com.leanengine.rest;

import java.io.IOException;
import java.util.List;

public interface NetworkCallback<T> {

    public void onResult(T... result);

    public void onFailure(RestException restException);
}
