package com.qiantang.smartparty.network;


import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxUploadUrl;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
    //多文件流上传
    @Multipart
    @POST("alibaba/oss/appUploadImages.do")
    Observable<HttpResult<List<RxUploadUrl>>> upload(@PartMap Map<String, RequestBody> params);

    //多文件流上传
    @Multipart
    @POST("alibaba/oss/appUploadImages.do")
    Observable<HttpResult<List<RxUploadUrl>>> upload(@Part MultipartBody.Part file);


}
