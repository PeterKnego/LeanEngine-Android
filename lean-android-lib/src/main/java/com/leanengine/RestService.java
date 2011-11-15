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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    protected static LeanEntity getPrivateEntity(final String kind, final long id) throws LeanException, IllegalArgumentException {
        if (LeanEngine.getLoginData() == null)
            throw new LeanException(LeanError.Error.NoAccountAuthorized);

        String url;
        if (kind != null) {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity/" + kind + "/" + id + "?lean_token=" +
                    LeanEngine.getLoginData().getAuthToken();

        } else {
            throw new IllegalArgumentException("Parameter 'kind' must not be null.");
        }
        try {
            JSONObject jsonObject = doGet(url);
            return JsonDecode.entityFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Error.ServerNotAccessible);
        }
    }

    protected static void getPrivateEntityAsync(final String kind, final long id, final NetworkCallback<LeanEntity> networkCallback) {

        final RestAsyncTask<LeanEntity[]> aTask = new RestAsyncTask<LeanEntity[]>() {

            // executes on background thread
            @Override
            protected LeanEntity[] doInBackground(Void... lists) {
                try {
                    return new LeanEntity[]{getPrivateEntity(kind, id)};
                } catch (LeanException e) {
                    error = e.getError();
                    return null;
                }
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

    protected static LeanEntity[] getPrivateEntities(final String kind) throws LeanException {
        if (LeanEngine.getLoginData() == null)
            throw new LeanException(LeanError.Error.NoAccountAuthorized);

        String url;
        if (kind != null) {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity/" + kind + "?lean_token=" +
                    LeanEngine.getLoginData().getAuthToken();
        } else {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity?lean_token=" +
                    LeanEngine.getLoginData().getAuthToken();
        }

        try {
            JSONObject jsonObject = doGet(url);
            return JsonDecode.entityListFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Error.ServerNotAccessible);
        }
    }

    protected static void getPrivateEntitiesAsync(final String kind, final NetworkCallback<LeanEntity> networkCallback) {

        final RestAsyncTask<LeanEntity[]> aTask = new RestAsyncTask<LeanEntity[]>() {

            // executes on background thread
            @Override
            protected LeanEntity[] doInBackground(Void... lists) {
                try {
                    return getPrivateEntities(kind);
                } catch (LeanException e) {
                    error = e.getError();
                    return null;
                }
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
            throw new LeanException(LeanError.Error.NoAccountAuthorized);
        //todo externalize URLs (and token insertion)
        String url = LeanEngine.getHostURI() +
                "/rest/v1/entity/" + entity.kind + "?lean_token=" +
                LeanEngine.getLoginData().getAuthToken();
        try {
            JSONObject param = JsonEncode.entityToJson(entity);
            JSONObject jsonObject = doPost(url, param);
            return idFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Error.ServerNotAccessible);
        } catch (JSONException e) {
            throw new LeanException(LeanError.Error.ErrorParsingJSON);
        }
    }

    protected static void putPrivateEntityAsync(final LeanEntity entity, final NetworkCallback<Long> networkCallback) {
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

    protected static LeanEntity[] queryPrivate(final LeanQuery query) throws LeanException {
        if (LeanEngine.getLoginData() == null)
            throw new LeanException(LeanError.Error.NoAccountAuthorized);

        String url = LeanEngine.getHostURI() +
                "/rest/v1/query?lean_token=" +
                LeanEngine.getLoginData().getAuthToken();

        try {
            JSONObject queryJson = JsonEncode.queryToJson(query);
            JSONObject jsonObject = doPost(url, queryJson);

            // update the Query's cursor - used in fetching next results
            String cursor = jsonObject.optString("cursor");
            if(cursor.length() != 0)
                query.setCursor(cursor);

            return JsonDecode.entityListFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Error.ServerNotAccessible);
        }
    }

    protected static void queryPrivateAsync(final LeanQuery query, final NetworkCallback<LeanEntity> networkCallback) {

        final RestAsyncTask<LeanEntity[]> aTask = new RestAsyncTask<LeanEntity[]>() {

            // executes on background thread
            @Override
            protected LeanEntity[] doInBackground(Void... lists) {
                try {
                    return queryPrivate(query);
                } catch (LeanException e) {
                    error = e.getError();
                    return null;
                }
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
                throw new LeanException(LeanError.Error.ServerNotAccessible);
            }

            if (statusLine.getStatusCode() >= 300) {
                throw new LeanException(LeanError.fromJSON(result));
            }

            if (result != null) {
                try {
                    return new JSONObject(result);
                } catch (JSONException e) {
                    throw new LeanException(LeanError.Error.ErrorParsingJSON);
                }
            } else {
                throw new LeanException(LeanError.Error.ErrorParsingJSON);
            }
        }
    }

    private static Long idFromJson(JSONObject json) throws JSONException {
        return json.getLong("id");
    }
}
