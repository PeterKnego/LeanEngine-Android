package com.leanengine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class JsonDecode {

    public static LeanAccount accountFromJson(JSONObject json) throws LeanException {

        Long id;
        try {
            id = json.getLong("id");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field 'id'.");
        }

        String providerId;
        try {
            providerId = json.getString("providerId");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field 'providerId'.");
        }

        String nickName;
        try {
            nickName = json.getString("nickName");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field 'nickName'.");
        }

        String provider;
        try {
            provider = json.getString("provider");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field 'provider'.");
        }

        JSONObject jsonProviderProperties;
        Map<String, Object> providerProperties;
        try {
            jsonProviderProperties = json.getJSONObject("providerProperties");
            providerProperties = accountPropsFromJson(jsonProviderProperties);
            return new LeanAccount(id, nickName, providerId, provider, providerProperties);
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field 'providerProperties'.");
        }
    }

    private static Map<String, Object> accountPropsFromJson(JSONObject jsonNode) throws LeanException, JSONException {
        Map<String, Object> props = new HashMap<String, Object>(jsonNode.length());

        // must have some properties
        if (jsonNode.length() == 0) throw new LeanException(LeanError.Error.JsonMissingField,
                " JSON object 'providerProperties' must not be empty.");

        Iterator fieldNames = jsonNode.keys();
        while (fieldNames.hasNext()) {
            String field = (String) fieldNames.next();
            props.put(field, jsonNode.get(field));
        }
        return props;
    }


    public static LeanEntity[] entityListFromJson(JSONObject json) throws LeanException {
        try {
            JSONArray array = json.getJSONArray("result");
            LeanEntity[] result = new LeanEntity[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                result[i] = entityFromJson(item);
            }
            return result;
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.CreatingJsonError, " \n\n" + e);
        }
    }

    public static LeanEntity entityFromJson(JSONObject json) throws LeanException {

        String kind;
        try {
            kind = json.getString("_kind");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field '_kind'.");
        }
        Long id;
        try {
            id = json.getLong("_id");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field '_id'.");
        }
        Long accountId;
        try {
            accountId = json.getLong("_account");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.JsonMissingField, " Entity JSON missing field '_account'.");
        }

        LeanEntity entity = new LeanEntity(kind, id, accountId);

        try {
            entity.properties = entityPropertiesFromJson(json);
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.ErrorParsingJSON, " " + e);
        }

        return entity;

    }

    public static Map<String, Object> entityPropertiesFromJson(JSONObject jsonNode) throws LeanException, JSONException {
        Map<String, Object> props = new HashMap<String, Object>(jsonNode.length());

        // must have some properties
        if (jsonNode.length() == 0) throw new LeanException(LeanError.Error.EmptyEntity);

        Iterator fieldNames = jsonNode.keys();
        while (fieldNames.hasNext()) {
            String field = (String) fieldNames.next();

            // skip LeanEngine system properties (starting with underscore '_')
            if (field.startsWith("_")) continue;
            Object subNode = jsonNode.get(field);
            props.put(field, propertyFromJson(subNode));
        }
        return props;
    }

    public static Object propertyFromJson(Object jsonNode) throws LeanException, JSONException {
        if (jsonNode instanceof JSONObject) {
            return typedObjectFromJson((JSONObject) jsonNode);
        } else if (jsonNode instanceof JSONArray) {
            return typedArrayFromJson((JSONArray) jsonNode);
        }
        return jsonNode;
    }

    private static List<Object> typedArrayFromJson(JSONArray arrayNode) throws LeanException, JSONException {
        List<Object> result = new ArrayList<Object>(arrayNode.length());
        for (int i = 0; i < arrayNode.length(); i++) {
            Object node = arrayNode.getJSONObject(i);
            if (node instanceof JSONObject) {
                result.add(typedObjectFromJson((JSONObject) node));
            } else {
                result.add(node);
            }
        }
        return result;
    }

    private static Object typedObjectFromJson(JSONObject node) throws LeanException, JSONException {
        // must have 'type' field
        String type = node.getString("type");
        if (type == null) throw new LeanException(LeanError.Error.ValueToJSON, " Missing 'type' field.");

        if ("date".equals(type)) {
            return new Date(getLongFromValueNode("value", node));
        } else if ("geopt".equals(type)) {
            throw new IllegalArgumentException("Value nodes of type 'geopt' are not yet implemented.");
        } else if ("geohash".equals(type)) {
            throw new IllegalArgumentException("Value nodes of type 'geohash' are not yet implemented.");
        } else if ("blob".equals(type)) {
            throw new IllegalArgumentException("Value nodes of type 'blob' are not yet implemented.");
        } else if ("shortblob".equals(type)) {
            throw new IllegalArgumentException("Value nodes of type 'shortblob' are not yet implemented.");
        } else if ("text".equals(type)) {
            throw new IllegalArgumentException("Value nodes of type 'text' are not yet implemented.");
        } else if ("reference".equals(type)) {
            throw new IllegalArgumentException("Value nodes of type 'reference' are not yet implemented.");
        } else {
            //unknown node type
            throw new LeanException(LeanError.Error.ValueToJSON, " Unknown type '" + type + "'.");
        }
    }

    private static long getLongFromValueNode(String fieldName, JSONObject node) throws LeanException {
        try {
            return node.getLong(fieldName);
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.ValueToJSON, " Missing '" + fieldName + "' field.");
        }
    }


//   public static LeanQuery fromJson( JSONObject jsonNode) throws LeanException {
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
//                    Object filterValue = JsonDecode.propertyFromJson(filter.get(operator));
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
