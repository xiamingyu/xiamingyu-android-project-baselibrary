package com.ihomefnt.baselibrary.http;

/**
 * Http request call back, implement this would make user get the response data
 * that server returned
 *
 * @param <T>
 */
public interface HttpRequestCallBack<T> {
    public void onRequestSuccess(T response, boolean cached);

    public void onRequestFailed(Long code, Object ext);
}
