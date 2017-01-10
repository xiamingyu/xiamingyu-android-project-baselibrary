package com.ihomefnt.baselibrary.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ihomefnt.baselibrary.ScanApplication;
import com.ihomefnt.baselibrary.baseutil.*;
import org.apache.http.HttpStatus;

import java.util.List;

/**
 * HttpRequestManager
 * with one static generic static public method, user can call it directly
 * with input parameters, call back listener, and response java bean class.
 */
public class HttpRequestManager {
    private static final String TAG = HttpRequestManager.class.getSimpleName();

    /**
     * @param url
     * @param params
     * @param listener
     * @param clz
     * @param <T>
     */
    public static <T> void sendRequest(HttpRequestURL url, Object params, HttpRequestCallBack listener, Class<T> clz) {
        try {
            /**
             * get volley request queue
             */
            final RequestQueue queue = ScanApplication.getInstance().getRequestQueue();

            /**
             * parse raw data to json object
             */
            JSONObject raw = JSON.parseObject(JSON.toJSONString(params));
            String a =raw.toString();

            /**
             *  init success listener
             */
            VolleySuccessListener<T> successListener = new VolleySuccessListener<T>(url, listener, raw, clz);
            /**
             * init failed listener
             */
            VolleyFailedListener failedListener = new VolleyFailedListener(url, listener, raw, clz);
            /**
             * init http request
             */
            HttpRequest request = new HttpRequest(url.getHttpMethod(),
                    url.getBaseUrl(),
                    successListener,
                    failedListener);

            request.setTag(url);
            request.setShouldCache(false);

            request.setRequestParams(raw);
            /**
             * Add data to request queue
             */
            queue.add(request);
        } catch (Exception e) {
        }
    }
    /**
     * @param url
     * @param params
     * @param listener
     * @param clz
     * @param <T>
     */
    public static <T> void sendRequest(HttpRequestURL url, Object params, HttpRequestCallBack listener, Class<T> clz, HttpRequest.PARAMETER_TYPE paramType) {
        try {
            /**
             * get volley request queue
             */
            final RequestQueue queue = ScanApplication.getInstance().getRequestQueue();

            /**
             * parse raw data to Tjson object
             */
            JSONObject raw = JSON.parseObject(JSON.toJSONString(params));

            /**
             *  init success listener
             */
            VolleySuccessListener<T> successListener = new VolleySuccessListener<T>(url, listener, raw, clz);
            /**
             * init failed listener
             */
            VolleyFailedListener failedListener = new VolleyFailedListener(url, listener, raw, clz);
            /**
             * init http request
             */
            HttpRequest request = new HttpRequest(url.getHttpMethod(),
                    url.getBaseUrl(),
                    successListener,
                    failedListener);
            request.setParamType(paramType);
            request.setTag(url);
            request.setShouldCache(false);
            request.setRequestParams(raw);
            /**
             * Add data to request queue
             */
            queue.add(request);
        } catch (Exception e) {
        }
    }

    /**
     * Response success listener
     *
     * @param <T>, is the response data that meaningful to user
     */
    public static class VolleySuccessListener<T> implements Response.Listener<String> {
        public HttpRequestURL mRequestUrl;
        public HttpRequestCallBack mRequestListener;
        private Class<T> clz;// type class
        private Object rawParams;//

        public VolleySuccessListener(HttpRequestURL requestUrl, HttpRequestCallBack listener, Object params, Class<T> clz) {
            this.mRequestUrl = requestUrl;
            mRequestListener = listener;
            this.clz = clz;
            rawParams = params;
        }

        /**
         * save content to cache based on the request as
         * the file name
         *
         * @param content
         */
        private void saveToCache(String content) {
            try {
                JSONObject object = (JSONObject) JSON.toJSON(rawParams);
                //remove sessionKey
                String fileName = object.toJSONString();
                fileName = mRequestUrl.getBaseUrl() + fileName;
                //get the MD5 file name
                fileName = FileUtils.createMD5(fileName);
                //save to cache
                CacheUtils.saveStringToCache(ScanApplication.getInstance(), fileName, content);
            } catch (Exception e) {
                //do nothing
                return;
            }
        }

        @Override
        public void onResponse(String response) {
            LoggerUtil.d(TAG, "response=" + response.toString());
            try {
                HttpBaseResponse baseResponse = JSON.parseObject(response, HttpBaseResponse.class);
                if (baseResponse != null && baseResponse.getCode() == ResponseCode.HTTP_SUCCESS) {
                    /**
                     * get business data
                     */
                    JSON json = (JSON) baseResponse.getObj();
                    if (json != null) {
                        /**
                         * judge whether the response data is array list or just pure java bean
                         */
                        if (json instanceof JSONObject) {
                            /**
                             * parse single object
                             */
                            T dataResponse = JSON.parseObject(json.toJSONString(), clz);
                            if (mRequestListener != null) {
                                mRequestListener.onRequestSuccess(dataResponse, false);
                            }
                        } else if (json instanceof JSONArray) {
                            /**
                             * parse array list
                             */
                            //parse json array to array list
                            String jsonArrayString = json.toJSONString();
                            List<T> javaObjects = JSON.parseArray(jsonArrayString, clz);
                            if (mRequestListener != null) {
                                mRequestListener.onRequestSuccess(javaObjects, false);
                            }
                        }
                        if (mRequestUrl.canFromCache()) {
                            saveToCache(json.toJSONString());
                        }
                    } else {
                        /**
                         * response data is null.
                         */
                        if (mRequestListener != null) {
                            mRequestListener.onRequestSuccess(ResponseCode.HTTP_SUCCESS, false);
                        }
                    }
                } else {
                    //response code is not 200
                    if (mRequestListener != null) {
                        if (mRequestListener != null) {
                            //send not success code to listener, let caller to deal with the code
                            mRequestListener.onRequestFailed(baseResponse!=null ? baseResponse.getCode() : ResponseCode.HTTP_FAILED, (baseResponse != null ? baseResponse.getExt() : null));

                        }
                    }
                }
            } catch (Exception e) {
                if (mRequestListener != null) {
                    mRequestListener.onRequestFailed(ResponseCode.HTTP_FAILED, null);
                }
            }
        }
    }

    public static class VolleyFailedListener<T> implements Response.ErrorListener {
        public HttpRequestURL mRequestUrl;
        public HttpRequestCallBack mRequestListener;
        private Class<T> clz;// type class
        private Object rawParams;//

        public VolleyFailedListener(HttpRequestURL requestUrl, HttpRequestCallBack listener, Object params, Class<T> clz) {
            this.mRequestUrl = requestUrl;
            mRequestListener = listener;
            rawParams = params;
            this.clz = clz;
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

            /**
             * whether should fetch data from cache
             */
            if (mRequestUrl != null && mRequestUrl.canFromCache()) {
                try {
                    /**
                     * need discuss whether should return
                     */
                    if (volleyError.networkResponse == null || volleyError.networkResponse.statusCode == HttpStatus.SC_REQUEST_TIMEOUT
                            || volleyError.networkResponse.statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                        String content = readFromCache();
                        if (!StringUtil.isNullOrEmpty(content)) {
                            /**
                             * 1. parse string to JSON, it either JSONObject or JSONArray;
                             */
                            JSON obj = (JSON) JSON.parse(content);
                            Object ret = null;
                            /**
                             * 2. if json object, parse to java object with given clz
                             */
                            if (obj instanceof JSONObject) {
                                ret = JSON.parseObject(content, clz);
                            } else if (obj instanceof JSONArray) {
                                /**
                                 * if json array, parse to array list with clz type element entry;
                                 */
                                ret = JSON.parseArray(content, clz);
                            }
                            /**
                             * call success listener with from cache = true;
                             */
                            if (mRequestListener != null) {
                                mRequestListener.onRequestSuccess(ret, true);//from cache
                                return;//return;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (mRequestListener != null) {
                mRequestListener.onRequestFailed(ResponseCode.HTTP_FAILED, null);
            }
            /**
             * cancel queued
             */
            final RequestQueue queue = ScanApplication.getInstance().getRequestQueue();
            //cancel request with the given request url
            queue.cancelAll(mRequestUrl);
        }

        /**
         * read data from cache
         *
         * @return
         */
        private String readFromCache() {
            try {
                JSONObject object = (JSONObject) JSON.toJSON(rawParams);
                String fileName = object.toJSONString();
                fileName = mRequestUrl.getBaseUrl() + fileName;
                //get the MD5 file name
                fileName = FileUtils.createMD5(fileName);
                //get content from cache
                CacheFile file = CacheUtils.getStringCache(fileName, ScanApplication.getInstance());
                return file != null ? file.getFileContent() : "";
            } catch (Exception e) {

            }
            return "";
        }
    }
}