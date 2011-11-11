package com.leanengine.query;

import com.leanengine.LeanError;
import com.leanengine.LeanException;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryOptions {
    private Integer limit;
    private Integer offset;
    private Integer prefetchSize;
    private String startCursor;
    private String endCursor;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getPrefetchSize() {
        return prefetchSize;
    }

    public void setPrefetchSize(Integer prefetchSize) {
        this.prefetchSize = prefetchSize;
    }

    public String getStartCursor() {
        return startCursor;
    }

    public void setStartCursor(String cursor) {
        this.startCursor = cursor;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(String cursor) {
        this.endCursor = cursor;
    }

    public static QueryOptions fromJson(JSONObject node) throws LeanException {
        try {
            QueryOptions options = new QueryOptions();
            if (node.get("startCursor") != null) options.setStartCursor(node.getString("startCursor"));
            if (node.get("endCursor") != null) options.setEndCursor(node.getString("endCursor"));
            if (node.get("limit") != null) options.setLimit(node.getInt("limit"));
            if (node.get("offset") != null) options.setOffset(node.getInt("offset"));
            if (node.get("prefetchSize") != null) options.setPrefetchSize(node.getInt("prefetchSize"));
            return options;
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.CreatingJsonError, " \n\n"+e.getMessage());
        }
    }
}
