package com.qiantang.smartparty.modle;


import com.qiantang.smartparty.network.retrofit.RetrofitUtil;

public class HttpResult<T> {

    public String getErrorMessage() {
        return msg;
    }

    public void setErrorMessage(String errorMessage) {
        this.msg = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getErrorCode() {
        return status;
    }

    public void setErrorCode(int errorCode) {
        this.status = errorCode;
    }

    private String msg;
    private String token;
    private int status;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //用来模仿Data
    private T data;

    public T getReturnObject() {
        return data;
    }

    public void setReturnObject(T returnObject) {
        this.data = returnObject;
    }

    public void getReturnObjectClass(T t) {
        t.getClass();
    }

    public boolean isSuccess() {
        return status == RetrofitUtil.APIException.OK;
    }

}
