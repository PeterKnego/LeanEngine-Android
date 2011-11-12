package com.leanengine.query;

import com.leanengine.JsonDecode;
import com.leanengine.LeanError;
import com.leanengine.LeanException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeanQuery {
    private final String kind;
    private List<QueryFilter> filters = new ArrayList<QueryFilter>();
    private List<QuerySort> sorts = new ArrayList<QuerySort>();
    private QueryOptions queryOptions;

    public LeanQuery(String kind) {
        this.kind = kind;
    }

    public void addFilter(String property, QueryFilter.FilterOperator operator, Object value) {
        filters.add(new QueryFilter(property, operator, value));
    }

    public void addSort(String property, QuerySort.SortDirection direction) {
        sorts.add(new QuerySort(property, direction));
    }

    public String getKind() {
        return kind;
    }

    public List<QuerySort> getSorts() {
        return sorts;
    }

    public List<QueryFilter> getFilters() {
        return filters;
    }

    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    public void setQueryOptions(QueryOptions queryOptions) {
        this.queryOptions = queryOptions;
    }

}
