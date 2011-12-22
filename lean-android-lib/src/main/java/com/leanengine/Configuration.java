/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * @author Ales Justin
 */
public class Configuration {
    private int port = 80;
    private int sslPort = 443;
    private int connectionTimeout = 30 * 1000;
    private HttpVersion httpVersion = HttpVersion.HTTP_1_1;
    private String contentCharset = "UTF-8";
    private SocketFactory plainFactory;
    private SocketFactory sslFactory;

    /**
     * Create client connection manager.
     * Override in a sub class if needed.
     *
     * Create an HttpClient with the ThreadSafeClientConnManager.
     * This connection manager must be used if more than one thread will
     * be using the HttpClient.
     *
     * @param params the http params
     * @param schemeRegistry the scheme registry
     * @return new client connection manager
     */
    protected ClientConnectionManager createClientConnectionManager(HttpParams params, SchemeRegistry schemeRegistry)
    {
        return new ThreadSafeClientConnManager(params, schemeRegistry);
    }

    /**
     * Create new http client.
     * Override in a sub class if needed.
     *
     * @param ccm the client connection manager
     * @param params the http params
     * @return new http client
     */
    protected HttpClient createClient(ClientConnectionManager ccm, HttpParams params)
    {
        return new DefaultHttpClient(ccm, params);
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public int getSslPort()
    {
        return sslPort;
    }

    public void setSslPort(int sslPort)
    {
        this.sslPort = sslPort;
    }

    public int getConnectionTimeout()
    {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout)
    {
        this.connectionTimeout = connectionTimeout;
    }

    public HttpVersion getHttpVersion()
    {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion)
    {
        this.httpVersion = httpVersion;
    }

    public String getContentCharset()
    {
        return contentCharset;
    }

    public void setContentCharset(String contentCharset)
    {
        this.contentCharset = contentCharset;
    }

    public SocketFactory getPlainFactory()
    {
        if (plainFactory == null)
            return PlainSocketFactory.getSocketFactory();

        return plainFactory;
    }

    public void setPlainFactory(SocketFactory plainFactory)
    {
        this.plainFactory = plainFactory;
    }

    public SocketFactory getSslFactory()
    {
        if (sslFactory == null)
            return SSLSocketFactory.getSocketFactory();

        return sslFactory;
    }

    public void setSslFactory(SocketFactory sslFactory)
    {
        this.sslFactory = sslFactory;
    }
}
