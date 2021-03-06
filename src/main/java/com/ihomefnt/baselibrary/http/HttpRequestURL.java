package com.ihomefnt.baselibrary.http;

import com.android.volley.Request;
import com.ihomefnt.baselibrary.constants.HttpConfig;

public class HttpRequestURL {
    private static final int DEFAULT_TIME_OUT = 15000;
    //接口的相对路径?
    private String mBaseUrl;
    //HTTP请求的方法?GET, POST...)
    private int mHttpMethod;
    //接口的完整地路径?
    private String mUrl;
    private int mTimeout;
    private boolean fromCache = false;

    /**
     * @param baseUrl
     * @param httpMethod
     */
    protected HttpRequestURL(String baseUrl, int httpMethod) {
        this(baseUrl, httpMethod, DEFAULT_TIME_OUT);
    }

    protected HttpRequestURL(String baseUrl) {
        this(baseUrl, Request.Method.GET, DEFAULT_TIME_OUT);
    }

    /**
     * @param baseUrl
     * @param httpMethod
     * @param timeout
     */
    protected HttpRequestURL(String baseUrl, int httpMethod, int timeout) {
        this(baseUrl, httpMethod, timeout, false);
    }

    protected HttpRequestURL(String baseUrl, int httpMethod, boolean fromCache) {
        this(baseUrl, httpMethod, DEFAULT_TIME_OUT, fromCache);
    }

    protected HttpRequestURL(String baseUrl, int httpMethod, int timeout, boolean fromCache) {
        this.mBaseUrl = baseUrl;//server interface
        this.mHttpMethod = httpMethod;
        this.mTimeout = timeout;
        this.mUrl = null; ////full request:relative path with parameters
        this.fromCache = fromCache;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public int getHttpMethod() {
        return mHttpMethod;
    }

    public boolean canFromCache() {
        return fromCache;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("mRelativePath:" + mBaseUrl);
        buffer.append(",mHttpMethod:" + mHttpMethod);
        buffer.append(",mUrl:" + (mUrl == null ? "empty" : mUrl));
        buffer.append(",mTimeout:" + mTimeout);
        return buffer.toString();
    }

    /**
     * all constant url is defined here
     * eg:
     */

    public static final HttpRequestURL LOGIN = new HttpRequestURL(HttpConfig.API_URL + "login", Request.Method.POST, false);


}
