package com.leanengine.rest;

import android.os.AsyncTask;
import com.leanengine.LeanEngine;
import com.leanengine.LeanEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class RestService {

    private static JSONObject doGet(String uri) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(uri);
        httpget.addHeader("Content-Type", "application/json");

        return httpclient.execute(httpget, new RestResponseHandler());
    }

    private static JSONObject doPost(String uri, JSONObject json) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(json.toString()));

        return httpclient.execute(httpPost, new RestResponseHandler());
    }

//    public static void putPrivateEntity()

    public static void getPrivateEntitiesAsync(final NetworkCallback<LeanEntity> networkCallback) {
        if (LeanEngine.getLoginData() == null) networkCallback.onFailure(new RestException(0, "User not logged in"));

        RestAsyncTask<LeanEntity[]> aTask = new RestAsyncTask<LeanEntity[]>(networkCallback) {

            // executes on background thread
            @Override
            protected LeanEntity[] doInBackground(Void... lists) {
                //todo externalize URLs (and token insertion)
                String url = LeanEngine.getHost() +
                        "/rest/entity?lean_token=" +
                        LeanEngine.getLoginData().getAuthToken();
                try {
                    JSONObject jsonObject = doGet(url);
                    return fromJsonArray(jsonObject);
                } catch (IOException e) {
                    error = new RestException(e);
                } catch (JSONException e) {
                    error = new RestException(e);
                }
                return null;
            }

            // executes on UI thread
            @Override
            protected void onPostExecute(LeanEntity[] leanEntities) {
                if (error != null) {
                    networkCallback.onFailure(error);
                    return;
                }
                networkCallback.onResult(leanEntities);
            }
        };

        aTask.execute((Void) null);
    }

    public static void putPrivateEntity(final LeanEntity entity, final NetworkCallback<Long> networkCallback) {
        if (LeanEngine.getLoginData() == null) networkCallback.onFailure(new RestException(0, "User not logged in"));

        RestAsyncTask<Long> aTask = new RestAsyncTask<Long>(networkCallback) {

            // executes on background thread
            @Override
            protected Long doInBackground(Void... lists) {
                //todo externalize URLs (and token insertion)
                String url = LeanEngine.getHost() +
                        "/rest/entity/" + entity.kind + "?lean_token=" +
                        LeanEngine.getLoginData().getAuthToken();
                try {
                    JSONObject param = toJson(entity);
                    JSONObject jsonObject = doPost(url, param);
                    return idFromJson(jsonObject);
                } catch (IOException e) {
                    error = new RestException(e);
                } catch (JSONException e) {
                    error = new RestException(e);
                }
                return null;
            }

            // executes on UI thread
            @Override
            protected void onPostExecute(Long entityID) {
                if (error != null) {
                    networkCallback.onFailure(error);
                    return;
                }
                networkCallback.onResult(entityID);
            }
        };

        aTask.execute((Void) null);
    }

    public static abstract class RestAsyncTask<Result> extends AsyncTask<Void, Void, Result> {

        private NetworkCallback callback;
        protected RestException error;

        protected RestAsyncTask(NetworkCallback callback) {
            this.callback = callback;
        }

    }

    public static class RestResponseHandler implements ResponseHandler<JSONObject> {

        @Override
        public JSONObject handleResponse(HttpResponse response) throws RestException {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() >= 300) {
                throw new RestException(statusLine.getStatusCode(),
                        statusLine.getReasonPhrase());
            }

            HttpEntity entity = response.getEntity();
            String result;
            try {
                result = entity != null ? EntityUtils.toString(entity) : null;
            } catch (IOException e) {
                throw new RestException(0, e.getMessage());
            }

            if (result != null) {
                try {
                    return new JSONObject(result);
                } catch (JSONException e) {
                    throw new RestException(0, "Invalid reply from REST server: content not JSON.");
                }
            } else {
                throw new RestException(0, "Empty response from REST service");
            }
        }
    }

    private static Long idFromJson(JSONObject json) throws JSONException {
        return json.getLong("id");
    }

    private static LeanEntity fromJsonObject(JSONObject json) throws JSONException {
        LeanEntity entity = new LeanEntity();
        entity.properties = new HashMap<String, Object>(json.length() - 3);
        Iterator keyIter = json.keys();
        while (keyIter.hasNext()) {
            String key = (String) keyIter.next();
            if (key.equals("_id")) {
                entity.id = Long.decode(json.get(key).toString());
            } else if (key.equals("_entity")) {
                entity.kind = json.get(key).toString();
            } else if (key.equals("_account")) {
                entity.accountID = Long.parseLong(json.get(key).toString());
            } else {
                // todo Implement proper property typing (with prefixes)
                entity.properties.put(key, json.get(key));
            }
        }
        return entity;
    }

    private static LeanEntity[] fromJsonArray(JSONObject json) throws JSONException {
        JSONArray array = json.getJSONArray("list");
        LeanEntity[] result = new LeanEntity[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            result[i] = fromJsonObject(item);
        }
        return result;
    }

    private static JSONObject toJson(LeanEntity entity) throws JSONException {
        JSONObject json = new JSONObject();
        if (entity.id != 0)
            json.put("_id", entity.id);
        json.put("_entity", entity.kind);
        // no need to put in '_account` as this is automatically set on server from current users accountID
        Map<String, Object> props = entity.properties;
        for (Map.Entry<String, Object> prop : props.entrySet()) {
            // todo handle proper typing of properties
            json.put(prop.getKey(), prop.getValue());
        }
        return json;
    }

    private static JSONObject toJson(List<LeanEntity> entityList) throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        for (LeanEntity entity : entityList) {
            array.put(toJson(entity));
        }

        json.put("list", array);
        return json;
    }
}
