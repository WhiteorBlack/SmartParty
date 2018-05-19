package com.qiantang.smartparty.network.retrofit;


import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.modle.RxUploadUrl;
import com.qiantang.smartparty.utils.luban.Luban;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ChenHengQuan on 2016/11/23.
 * Email nullpointerchan@163.com
 */
public class ApiWrapper extends RetrofitUtil {

    private volatile static ApiWrapper apiWrapper;

    private ApiWrapper() {
    }

    public static ApiWrapper getInstance() {
        if (apiWrapper == null) {
            synchronized (ApiWrapper.class) {
                if (apiWrapper == null) {
                    apiWrapper = new ApiWrapper();
                }
            }
        }
        return apiWrapper;
    }

    public synchronized Observable<RxUploadUrl> setUpload(final String path) {
        final File file = new File(path);
        return Luban.get(MyApplication.getContext())
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .onErrorResumeNext(throwable -> {
                    return Observable.empty();
                })
                .flatMap(bytes -> {
                    RequestBody requestFile = RequestBody.create(MediaType.parse
                            ("multipart/form-data"), bytes);
                    String fileName = file.getName().replaceFirst("gif$", "png");
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", fileName, requestFile);
                    return ApiWrapper.getInstance().upload(body);
                })
                .map(uploadUrls -> {
                    RxUploadUrl uploadUrl = null;
                    if (uploadUrls.size() > 0) {
                        uploadUrl = uploadUrls.get(0);
                        uploadUrl.setFilePath(path);
                    }
                    return uploadUrl;
                });
    }

    /**
     * 上传文件
     */
    public Observable<List<RxUploadUrl>> upload(MultipartBody.Part file) {
        return getService().upload(file)
                .compose(this.apply());
    }


}
