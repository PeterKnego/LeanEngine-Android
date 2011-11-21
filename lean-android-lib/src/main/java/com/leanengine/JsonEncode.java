/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonEncode {

    protected static JSONObject entityToJson(LeanEntity entity) throws LeanException {
        try {
            JSONObject json = new JSONObject();
            if (entity.id != 0)
                json.put("_id", entity.id);
            json.put("_kind", entity.kind);
            // no need to put in '_account` as this is automatically set on server from current users accountID
            for (Map.Entry<String, Object> prop : entity.properties.entrySet()) {
                addTypedNode(json, prop.getKey(), prop.getValue());
            }
            return json;
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.CreatingJsonError, " \n\n" + e);
        }
    }

    protected static JSONObject entityListToJson(List<LeanEntity> entityList) throws LeanException {
        JSONObject rootNode = new JSONObject();
        JSONArray resultsArray = new JSONArray();

        for (LeanEntity entity : entityList) {
            resultsArray.put(entityToJson(entity));
        }
        try {
            rootNode.put("result", resultsArray);
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.CreatingJsonError, " \n\n" + e);
        }
        return rootNode;
    }

    protected static JSONObject queryToJson(LeanQuery query) throws LeanException {
        JSONObject json = new JSONObject();
        try {
            json.put("kind", query.getKind());
            JSONObject jsonFilters = new JSONObject();
            for (QueryFilter filter : query.getFilters()) {
                JSONObject jsonFilter;
                if (jsonFilters.has(filter.getProperty())) {
                    jsonFilter = jsonFilters.getJSONObject(filter.getProperty());
                } else {
                    jsonFilter = new JSONObject();
                }
                addTypedNode(jsonFilter, filter.getOperator().toJSON(), filter.getValue());
                jsonFilters.put(filter.getProperty(), jsonFilter);
            }
            json.put("filter", jsonFilters);

            JSONObject jsonSorts = new JSONObject();
            for (QuerySort sort : query.getSorts()) {
                jsonSorts.put(sort.getProperty(), sort.getDirection().toJSON());
            }
            json.put("sort", jsonSorts);

            json.put("limit", query.getLimit());
            if (query.getOffset() != 0) json.put("offset", query.getOffset());
            if (query.getCursor() != null) json.put("cursor", query.getCursor());

            return json;
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.CreatingJsonError, " \n\n" + e);
        }
    }

    protected static void addTypedNode(JSONObject node, String key, Object value) throws LeanException, JSONException {
        if (value instanceof List) {
            List list = (List) value;
            JSONArray arrayNode = new JSONArray();
            for (Object listItem : list) {
                addTypedValueToArray(arrayNode, listItem);
            }
            node.put(key, arrayNode);
        } else {
            addTypedValue(node, key, value);
        }
    }

    private static void addTypedValueToArray(JSONArray node, Object value) throws JSONException {
        if (value instanceof Date) {
            node.put(getDateNode((Date) value));
        } else {
            node.put(value);
        }
    }

    private static void addTypedValue(JSONObject node, String key, Object value) throws JSONException {
        if (value instanceof Date) {
            node.put(key, getDateNode((Date) value));
        } else {
            node.put(key, value);
        }
    }

    private static JSONObject getDateNode(Date date) throws JSONException {
        JSONObject dateNode = new JSONObject();
        dateNode.put("type", "date");
        dateNode.put("value", date.getTime());
        return dateNode;
    }
}
