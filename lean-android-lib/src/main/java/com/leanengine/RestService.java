/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class RestService {

    private static RestService INSTANCE = new RestService();
    
    private volatile HttpClient client;
    private Configuration configuration = new Configuration(); // default

    public static RestService getInstance() {
        return INSTANCE;
    }

    private HttpClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    if (configuration != null) {
                        final HttpParams params = new BasicHttpParams();
                        HttpConnectionParams.setConnectionTimeout(params, configuration.getConnectionTimeout());
                        HttpProtocolParams.setVersion(params, configuration.getHttpVersion());
                        HttpProtocolParams.setContentCharset(params, configuration.getContentCharset());

                        final SchemeRegistry schemeRegistry = new SchemeRegistry();
                        schemeRegistry.register(new Scheme("http", configuration.getPlainFactory(), configuration.getPort()));
                        schemeRegistry.register(new Scheme("https", configuration.getSslFactory(), configuration.getSslPort()));

                        final ClientConnectionManager ccm = configuration.createClientConnectionManager(params, schemeRegistry);

                        client = configuration.createClient(ccm, params);
                    } else {
                        client = new DefaultHttpClient();
                    }
                }
            }
        }
        return client;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * Dispose http client / its connection manager once done.
     */
    public void dispose() {
        final HttpClient temp = client;
        client = null;
        if (temp != null) {
            final ClientConnectionManager manager = temp.getConnectionManager();
            if (manager != null)
                manager.shutdown();
        }
    }
    
    private JSONObject doGet(String uri) throws IOException {
        HttpGet httpget = new HttpGet(uri);
        httpget.addHeader("Accept", "application/json");
        httpget.addHeader("Accept-Charset", "UTF-8");

        return getClient().execute(httpget, new RestResponseHandler());
    }

    private JSONObject doPost(String uri, JSONObject json) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Accept-Charset", "UTF-8");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));

        return getClient().execute(httpPost, new RestResponseHandler());
    }

    private void doDelete(String uri) throws IOException {
        HttpDelete httpget = new HttpDelete(uri);
        httpget.addHeader("Content-Type", "application/json");

        getClient().execute(httpget, new DeleteResponseHandler());
    }

    protected LeanEntity getPrivateEntity(final String kind, final Long id) throws LeanException, IllegalArgumentException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);

        String url;
        if (kind != null && id != null) {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity/" + kind + "/" + id + "?lean_token=" +
                    LeanEngine.getAuthToken();

        } else {
            throw new IllegalArgumentException("Parameters 'kind' and 'id' must not be null.");
        }
        try {
            JSONObject jsonObject = doGet(url);
            return JsonDecode.entityFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    protected void getPrivateEntityAsync(final String kind, final long id,
                                                final NetworkCallback<LeanEntity> networkCallback) {

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


    public void deletePrivateEntity(String kind, Long id) throws LeanException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);

        String url;
        if (kind != null && id != null) {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity/" + kind + "/" + id + "?lean_token=" +
                    LeanEngine.getAuthToken();
        } else {
            throw new IllegalArgumentException("Parameters 'kind' and 'id' must not be null.");
        }

        try {
            doDelete(url);
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    public void deletePrivateEntityAsync(final String kind, final long id, final NetworkCallback<Void> networkCallback)
            throws LeanException {

        final RestAsyncTask<Void> aTask = new RestAsyncTask<Void>() {

            // executes on background thread
            @Override
            protected Void doInBackground(Void... lists) {
                try {
                    deletePrivateEntity(kind, id);
                    return null;
                } catch (LeanException e) {
                    error = e.getError();
                    return null;
                }
            }

            // executes on UI thread
            @Override
            protected void onPostExecute(Void anything) {
                if (error != null) {
                    networkCallback.onFailure(error);
                    return;
                }
                networkCallback.onResult((Void) null);
            }
        };

        aTask.execute((Void) null);

    }

    protected LeanEntity[] getPrivateEntities(final String kind) throws LeanException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);

        String url;
        if (kind != null) {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity/" + kind + "?lean_token=" +
                    LeanEngine.getAuthToken();
        } else {
            url = LeanEngine.getHostURI() +
                    "/rest/v1/entity?lean_token=" +
                    LeanEngine.getAuthToken();
        }

        try {
            JSONObject jsonObject = doGet(url);
            return JsonDecode.entityListFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    protected void getPrivateEntitiesAsync(final String kind, final NetworkCallback<LeanEntity> networkCallback) {

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

    protected long putPrivateEntity(final LeanEntity entity) throws LeanException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);
        //todo externalize URLs (and token insertion)
        String url = LeanEngine.getHostURI() +
                "/rest/v1/entity/" + entity.kind + "?lean_token=" +
                LeanEngine.getAuthToken();
        try {
            JSONObject param = JsonEncode.entityToJson(entity);
            JSONObject jsonObject = doPost(url, param);
            return idFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    protected void putPrivateEntityAsync(final LeanEntity entity, final NetworkCallback<Long> networkCallback) {
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

    protected LeanEntity[] queryPrivate(final LeanQuery query) throws LeanException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);

        String url = LeanEngine.getHostURI() +
                "/rest/v1/query?lean_token=" +
                LeanEngine.getAuthToken();

        try {
            JSONObject queryJson = JsonEncode.queryToJson(query);
            JSONObject jsonObject = doPost(url, queryJson);

            // update the Query's cursor - used in fetching next results
            String cursor = jsonObject.optString("cursor");
            if (cursor.length() != 0)
                query.setCursor(cursor);

            return JsonDecode.entityListFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    protected void queryPrivateAsync(final LeanQuery query, final NetworkCallback<LeanEntity> networkCallback) {

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

    protected Boolean logout() throws LeanException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);

        String url = LeanEngine.getHostURI() +
                "/rest/v1/public/logout?lean_token=" +
                LeanEngine.getAuthToken();

        try {
            JSONObject jsonObject = doGet(url);
            boolean result = resultFromJson(jsonObject);

            // after the request is made we must also clear local data
            LeanEngine.resetAuthToken();
            LeanEngine.clearCookies();
            return result;
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    public void logoutAsync(final NetworkCallback<Boolean> callback) {
        final RestAsyncTask<Boolean[]> aTask = new RestAsyncTask<Boolean[]>() {

            // executes on background thread
            @Override
            protected Boolean[] doInBackground(Void... lists) {
                try {
                    return new Boolean[]{logout()};
                } catch (LeanException e) {
                    error = e.getError();
                    return null;
                }
            }

            // executes on UI thread
            @Override
            protected void onPostExecute(Boolean[] result) {
                if (error != null) {
                    callback.onFailure(error);
                    return;
                }
                callback.onResult(result);
            }
        };

        aTask.execute((Void) null);
    }

    public LeanAccount getCurrentAccountData() throws LeanException {
        if (!LeanAccount.isUserLoggedIn())
            throw new LeanException(LeanError.Type.NotAuthorizedError);

        String url = LeanEngine.getHostURI() +
                "/rest/v1/public/account?lean_token=" +
                LeanEngine.getAuthToken();

        try {
            JSONObject jsonObject = doGet(url);
            return JsonDecode.accountFromJson(jsonObject);
        } catch (IOException e) {
            throw new LeanException(LeanError.Type.NetworkError);
        }
    }

    public void getCurrentAccountDataAsync(final NetworkCallback<LeanAccount> networkCallback) throws LeanException {
        final RestAsyncTask<LeanAccount[]> aTask = new RestAsyncTask<LeanAccount[]>() {

            // executes on background thread
            @Override
            protected LeanAccount[] doInBackground(Void... lists) {
                try {
                    return new LeanAccount[]{getCurrentAccountData()};
                } catch (LeanException e) {
                    error = e.getError();
                    return null;
                }
            }

            // executes on UI thread
            @Override
            protected void onPostExecute(LeanAccount[] leanAccounts) {
                if (error != null) {
                    networkCallback.onFailure(error);
                    return;
                }
                networkCallback.onResult(leanAccounts);
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
                result = entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
            } catch (IOException e) {
                throw new LeanException(LeanError.Type.NetworkError);
            }

            if (statusLine.getStatusCode() >= 300) {
                throw new LeanException(LeanError.fromJSON(result));
            }

            if (result != null) {
                try {
                    return new JSONObject(result);
                } catch (JSONException e) {
                    throw new LeanException(LeanError.Type.ServerError, "Malformed JSON reply: " + e.getMessage());
                }
            } else {
                throw new LeanException(LeanError.Type.ServerError, "Malformed JSON reply.");
            }
        }
    }

    protected static class DeleteResponseHandler implements ResponseHandler<Void> {

        @Override
        public Void handleResponse(HttpResponse response) throws LeanException {
            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() >= 300) {
                throw new LeanException(LeanError.Type.ServerError);
            }

            return null;
        }
    }

    private static Long idFromJson(JSONObject json) throws LeanException {
        try {
            return json.getLong("id");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Type.ServerError, "Malformed JSON reply: missing field 'id'.");
        }
    }

    private static boolean resultFromJson(JSONObject json) throws LeanException {
        try {
            return json.getBoolean("result");
        } catch (JSONException e) {
            throw new LeanException(LeanError.Type.ServerError, "Malformed JSON reply: missing field 'result'.");
        }
    }
}
