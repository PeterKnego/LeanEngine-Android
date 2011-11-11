package com.leanengine.query;

import com.leanengine.JsonUtils;
import com.leanengine.LeanError;
import com.leanengine.LeanException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

    public JSONObject toJson() throws LeanException {
        JSONObject json = new JSONObject();
        try {
            json.put("kind", kind);
            JSONObject jsonFilters = new JSONObject();
            for (QueryFilter filter : filters) {
                JSONObject jsonFilter;
                if (jsonFilters.has(filter.getProperty())) {
                    jsonFilter = jsonFilters.getJSONObject(filter.getProperty());
                } else {
                    jsonFilter = new JSONObject();
                }
                JsonUtils.addTypedNode(jsonFilter, filter.getOperator().toJSON(), filter.getValue());
                jsonFilters.put(filter.getProperty(), jsonFilter);
            }
            json.put("filter", jsonFilters);

            JSONObject jsonSorts = new JSONObject();
            for (QuerySort sort : sorts) {
                jsonSorts.put(sort.getProperty(), sort.getDirection().toJSON());
            }
            json.put("sort", jsonSorts);

            return json;
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.CreatingJsonError, " \n\n" + e);
        }
    }

//    public static LeanQuery fromJson(String json) throws LeanException {
//        JSONObject jsonNode;
//        try {
//            jsonNode = new JSONObject(json);
//        } catch (JSONException e) {
//            throw new LeanException(LeanError.Error.QueryJSON, " \n\n" + e);
//        }
//
//        // get the 'kind' of the query
//        LeanQuery query = null;
//        try {
//            query = new LeanQuery(jsonNode.getString("kind"));
//        } catch (JSONException e) {
//            throw new LeanException(LeanError.Error.QueryJSON, " Missing 'kind' property.");
//        }
//
//        // get 'filter'
//        JSONObject filters;
//        try {
//            filters = jsonNode.getJSONObject("filter");
//        } catch (JSONException e) {
//            throw new LeanException(LeanError.Error.QueryJSON, " Property 'filter' must be a JSON object.");
//        }
//
//        if (filters != null) {
//            Iterator<String> filterIterator = filters.getFieldNames();
//            while (filterIterator.hasNext()) {
//                String filterProperty = filterIterator.next();
//                JSONObject filter;
//                try {
//                    filter = filters.getJSONObject(filterProperty);
//                } catch (JSONException cce) {
//                    throw new LeanException(LeanError.Error.QueryJSON, " Filter value must be a JSON object.");
//                }
//                Iterator<String> operatorIterator = filter.getFieldNames();
//                while (operatorIterator.hasNext()) {
//                    String operator = operatorIterator.next();
//                    Object filterValue = JsonUtils.propertyFromJson(filter.get(operator));
//                    query.addFilter(
//                            filterProperty,
//                            QueryFilter.FilterOperator.create(operator),
//                            filterValue);
//                }
//            }
//        }
//
//        // get 'sort'
//        ObjectNode sorts;
//        try {
//            sorts = (ObjectNode) jsonNode.get("sort");
//        } catch (ClassCastException cce) {
//            throw new LeanException(LeanException.Error.QueryJSON, " Property 'sort' must be a JSON object.");
//        }
//        if (sorts != null) {
//            Iterator<String> sortIterator = sorts.getFieldNames();
//            while (sortIterator.hasNext()) {
//                String sortProperty = sortIterator.next();
//                query.addSort(sortProperty, QuerySort.SortDirection.create(sorts.get(sortProperty).getTextValue()));
//            }
//        }
//
//        // get 'options'
//        ObjectNode options;
//        try {
//            options = (ObjectNode) jsonNode.get("options");
//        } catch (ClassCastException cce) {
//            throw new LeanException(LeanException.Error.QueryJSON, " Property 'options' must be a JSON object.");
//        }
//        if (options != null) {
//            query.setQueryOptions(QueryOptions.fromJson(options));
//        }
//
//
//        return query;
//    }

}
