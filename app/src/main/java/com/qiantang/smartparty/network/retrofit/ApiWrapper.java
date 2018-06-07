package com.qiantang.smartparty.network.retrofit;


import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.modle.RxRankBranch;
import com.qiantang.smartparty.modle.RxRankPersonal;
import com.qiantang.smartparty.modle.RxStudy;
import com.qiantang.smartparty.modle.RxStudyUnreadMsg;
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


    /**
     * 个人排行
     *
     * @param time
     * @return
     */
    public Observable<RxRankPersonal> getRankPersonal(String time) {
        return getService().getRankPersonal(MyApplication.USER_ID, time).compose(this.applySchedulers());
    }


    /**
     * 支部排行
     *
     * @param time
     * @return
     */
    public Observable<RxRankBranch> getRankBranch(String time) {
        return getService().getRankBranch(MyApplication.USER_ID, time).compose(this.applySchedulers());
    }


    /**
     * 获取学习感悟列表
     *
     * @return
     */
    public Observable<RxStudy> getStudyList(int pageNo) {
        return getService().getStudyList(MyApplication.USER_ID, pageNo).compose(this.applySchedulers());
    }

    /**
     * 删除学习感悟
     *
     * @return
     */
    public Observable<String> deleteComment(String id) {
        return getService().deleteComment(id).compose(this.applySchedulers());
    }

    /**
     * 学习感悟未读消息
     */
    public Observable<List<RxStudyUnreadMsg>> getUnreadMsg() {
        return getService().getUnreadMsg(MyApplication.USER_ID).compose(this.applySchedulers());
    }

    public Observable<String> commentLike(int type, String id, String content) {
        return getService().commentLike(MyApplication.USER_ID, type, id, content).compose(this.applySchedulers());
    }

    /**
     * 取消赞
     * @param id
     * @return
     */
    public Observable<String> cancelLike( String id) {
        return getService().cancelLike(MyApplication.USER_ID, id).compose(this.applySchedulers());
    }
}
