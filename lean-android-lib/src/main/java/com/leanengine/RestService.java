package com.leanengine;

import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    protected static void getPrivateEntitiesAsync(final String kind, final NetworkCallback<LeanEntity> networkCallback) {
        if (LeanEngine.getLoginData() == null)
            networkCallback.onFailure(new LeanError(LeanError.Error.NoAccountAuthorized));

        final RestAsyncTask<LeanEntity[]> aTask = new RestAsyncTask<LeanEntity[]>() {

            // executes on background thread
            @Override
            protected LeanEntity[] doInBackground(Void... lists) {
                //todo externalize URLs (and token insertion)
                String url;
                if (kind != null) {
                    url = LeanEngine.getHost() +
                            "/rest/entity/"+kind+"?lean_token=" +
                            LeanEngine.getLoginData().getAuthToken();
                } else {
                    url = LeanEngine.getHost() +
                            "/rest/entity?lean_token=" +
                            LeanEngine.getLoginData().getAuthToken();
                }

                try {
                    JSONObject jsonObject = doGet(url);
                    return fromJsonArray(jsonObject);
                } catch (IOException e) {
                    error = new LeanError(LeanError.Error.ServerNotAccessible);
                } catch (JSONException e) {
                    error = new LeanError(LeanError.Error.ReplyNotJSON);
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

    protected static long putPrivateEntity(final LeanEntity entity) throws LeanException {
        if (LeanEngine.getLoginData() == null)
            throw new LeanException(new LeanError(LeanError.Error.NoAccountAuthorized));
        //todo externalize URLs (and token insertion)
        String url = LeanEngine.getHost() +
                "/rest/entity/" + entity.kind + "?lean_token=" +
                LeanEngine.getLoginData().getAuthToken();
        try {
            JSONObject param = toJson(entity);
            JSONObject jsonObject = doPost(url, param);
            return idFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(new LeanError(LeanError.Error.ServerNotAccessible));
        } catch (JSONException e) {
            throw new LeanException(new LeanError(LeanError.Error.ReplyNotJSON));
        }
    }

    protected static void putPrivateEntity(final LeanEntity entity, final NetworkCallback<Long> networkCallback) {
        if (LeanEngine.getLoginData() == null)
            networkCallback.onFailure(new LeanError(LeanError.Error.NoAccountAuthorized));

        RestAsyncTask<Long> aTask = new RestAsyncTask<Long>() {

            // executes on background thread
            @Override
            protected Long doInBackground(Void... lists) {
                try {
                    return putPrivateEntity(entity);
                } catch (LeanException e) {
                    error = e.getError();
                    return -1l;
                }
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

    protected static abstract class RestAsyncTask<Result> extends AsyncTask<Void, Void, Result> {
        protected LeanError error;
    }

    protected static class RestResponseHandler implements ResponseHandler<JSONObject> {

        @Override
        public JSONObject handleResponse(HttpResponse response) throws LeanException {
            StatusLine statusLine = response.getStatusLine();

            HttpEntity entity = response.getEntity();
            String result;
            try {
                result = entity != null ? EntityUtils.toString(entity) : null;
            } catch (IOException e) {
                throw new LeanException(new LeanError(LeanError.Error.ServerNotAccessible));
            }

            if (statusLine.getStatusCode() >= 300) {
                throw new LeanException(LeanError.fromJSON(result));
            }

            if (result != null) {
                try {
                    return new JSONObject(result);
                } catch (JSONException e) {
                    throw new LeanException(new LeanError(LeanError.Error.ReplyNotJSON));
                }
            } else {
                throw new LeanException(new LeanError(LeanError.Error.ReplyNotJSON));
            }
        }
    }

    private static Long idFromJson(JSONObject json) throws JSONException {
        return json.getLong("id");
    }

    private static LeanEntity fromJsonObject(JSONObject json) throws JSONException {
        LeanEntity entity = new LeanEntity(json.getString("_kind"));
        entity.properties = new HashMap<String, Object>(json.length() - 3);
        Iterator keyIter = json.keys();
        while (keyIter.hasNext()) {
            String key = (String) keyIter.next();
            if (key.equals("_id")) {
                entity.id = Long.decode(json.get(key).toString());
            } else if (key.equals("_kind")) {
                // already handled - skip it
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
        json.put("_kind", entity.kind);
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
