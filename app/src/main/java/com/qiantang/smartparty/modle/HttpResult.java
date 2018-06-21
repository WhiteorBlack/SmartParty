package com.qiantang.smartparty.modle;


import com.qiantang.smartparty.network.retrofit.RetrofitUtil;

public class HttpResult<T> {
    private String avatar;
    private String msg;
    private String token;
    private int status;
    private String title;
    private int grade;
    private String userquestionnaire_id;
    private String imgId;
    private String imagePath;
    private String id;

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


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getUserquestionnaire_id() {
        return userquestionnaire_id;
    }

    public void setUserquestionnaire_id(String userquestionnaire_id) {
        this.userquestionnaire_id = userquestionnaire_id;
    }

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
