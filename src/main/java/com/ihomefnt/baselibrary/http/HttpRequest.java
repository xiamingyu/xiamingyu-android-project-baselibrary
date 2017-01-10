package com.ihomefnt.baselibrary.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by shulong on 2014/10/5.
 */
public class HttpRequest extends StringRequest {
    private static final String TAG = HttpRequest.class.getSimpleName();
    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_JSON_CONTENT_TYPE = String
            .format("application/json; charset=%s",
                    PROTOCOL_CHARSET);

    public static enum PARAMETER_TYPE {
        KV_SIMPLE, KV_JSON
    }

    private static String KEY_BASE = "req";
    private Object mRequestParams = new Object();
    private PARAMETER_TYPE mParamType = PARAMETER_TYPE.KV_SIMPLE;

    public HttpRequest(int method, String url,
                       Response.Listener<String> listener,
                       Response.ErrorListener errorListener, PARAMETER_TYPE type) {
        super(method,
                url,
                listener,
                errorListener);
        mParamType = type;
        setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * default constructor, use get method, KV_SIMPLE type.
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    public HttpRequest(String url, Response.Listener<String> listener,
                       Response.ErrorListener errorListener) {
        this(Method.GET,
                url,
                listener,
                errorListener,
                PARAMETER_TYPE.KV_JSON);
    }

    public HttpRequest(int method, String url,
                       Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(method,
                url,
                listener,
                errorListener,
                PARAMETER_TYPE.KV_JSON);
    }

    public void setRequestParams(Object requestParams) {
        mRequestParams = requestParams;
    }

    /**
     * k1=v1&k2=v2...... type, this would be called by getBody method. if the
     * request type is put or post
     *
     * @return
     * @throws com.android.volley.AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() {
        Map<String, String> map = new HashMap<String, String>();
        if (mRequestParams == null) {
            return map;
        }
        try {
            if (mParamType == PARAMETER_TYPE.KV_JSON) {
                String params = JSON.toJSONString(mRequestParams);
                map.put(KEY_BASE,params);
            } else {
                // serialize request object to <String, String> map
                JSONObject jsonObject = (JSONObject) JSON.toJSON(mRequestParams);
                Set<Map.Entry<String, Object>> sets = jsonObject.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = sets.iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    map.put(entry.getKey(),String.valueOf(entry.getValue()));
                }
            }
        } catch (Exception e) {
            map = new HashMap<String, String>();
        }
        return map;
    }

    @Override
    public String getUrl() {
        String url = "";

        switch (getMethod()) {
            case Method.GET:
            case Method.DELETE:
            case Method.PUT:
                url = super.getUrl();
                byte[] body = getBody();
                if (body != null) {
                    url += "?" + new String(body);
                }
                break;
            case Method.POST:
            default:
                return super.getUrl();
        }

        return url;
    }

    /**
     * post or put body data
     *
     * @return
     */
    @Override
    public byte[] getBody() {
        byte[] ret = null;
        try {
            ret = super.getBody();
        } catch (Exception e) {
        }
        return ret;
    }

    @Override
    protected String getParamsEncoding() {
        // default is utf-8
        return super.getParamsEncoding();
    }

    /**
     * http request content type, we can change the return type to json and
     * others to support any type we want
     *
     * @return
     */
    @Override
    public String getBodyContentType() {
        // default is: UTF-8;
        return super.getBodyContentType();
    }

    public void setParamType(PARAMETER_TYPE mParamType) {
        this.mParamType = mParamType;
    }
}
