package com.leanengine;

public class QuerySort {
    private String property;
    private LeanQuery.SortDirection direction;

    public QuerySort(String property, LeanQuery.SortDirection direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public LeanQuery.SortDirection getDirection() {
        return direction;
    }

}
