package com.ihomefnt.baselibrary.http;

/**
 * the server response protocol defined by below java bean
 */
public class HttpBaseResponse {
    private Long code; //ret code, 0x00: business success; others: error code;
    private Object ext;//Java bean(must conform Java bean standard), extra data; will be converted to json string when return to client
    private Object obj;//Java bean, extra data

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
