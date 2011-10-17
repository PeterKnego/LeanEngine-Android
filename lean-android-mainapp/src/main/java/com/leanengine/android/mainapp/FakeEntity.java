package com.leanengine.android.mainapp;

import java.util.Map;

public class FakeEntity {
    public String name;
    public Map<String, Object> propertyValueMap;

    public FakeEntity(String name, Map<String, Object> propertyValueMap) {
        this.name = name;
        this.propertyValueMap = propertyValueMap;
    }

}