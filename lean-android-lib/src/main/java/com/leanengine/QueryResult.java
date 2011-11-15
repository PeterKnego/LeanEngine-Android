package com.leanengine;

import com.leanengine.LeanEntity;

import java.util.List;

public class QueryResult {
    private List<LeanEntity> result;
    private String cursor;

    public QueryResult(List<LeanEntity> result, String cursor) {
        this.result = result;
        this.cursor = cursor;
    }

    public List<LeanEntity> getResult() {
        return result;
    }

    public String getCursor() {
        return cursor;
    }
}
