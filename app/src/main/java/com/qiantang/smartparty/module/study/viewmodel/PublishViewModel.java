package com.qiantang.smartparty.module.study.viewmodel;

import android.Manifest;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxStudy;
import com.qiantang.smartparty.modle.RxStudyList;
import com.qiantang.smartparty.module.study.adapter.PublishAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Function;

import static com.qiantang.smartparty.utils.StringUtil.getString;


public class PublishViewModel implements ViewModel {
    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private final BaseBindActivity activity;
    private final PublishAdapter adapter;
    private final LoadingWindow loadingWindow;
    private final PhotoInfo nullPhoto = new PhotoInfo();
    private FunctionConfig.Builder functionConfigBuilder;
    private List<PhotoInfo> list = new ArrayList<>();
    private GalleryFinal.OnHanlderResultCallback resultCallback;
    private boolean isMax;
    public String content = "";
    private boolean uploadOK = false;
    private boolean isHasNew = false;
    private boolean isReady = true;
    public ObservableBoolean isPublishing = new ObservableBoolean();

    public PublishViewModel(BaseBindActivity activity, PublishAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        initAdapterData(adapter);
        initFunctionConfig();
        loadingWindow = new LoadingWindow(activity);
    }

    /**
     * 初始化图片展示Adapter
     *
     * @param adapter
     */
    private void initAdapterData(PublishAdapter adapter) {
        list.add(nullPhoto);
        adapter.setNewData(list);
    }


    /**
     * 获取相册图片
     */
    private void getPic() {
        ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        FunctionConfig functionConfig;
                        functionConfigBuilder.setSelected(list);
                        functionConfig = functionConfigBuilder.build();
                        switch (index) {
                            case 0:
                                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,
                                        functionConfig, resultCallback);
                                break;
                            case 1:
                                EasyPermission.with(activity)
                                        .rationale(getString(R.string.rationale_camera))
                                        .addRequestCode(PermissionCode.RG_CAMERA_PERM)
                                        .permissions(Manifest.permission.CAMERA)
                                        .request();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        functionConfigBuilder.setSelected(list);
        FunctionConfig functionConfig = functionConfigBuilder.build();
        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, resultCallback);
    }

    /**
     * 初始化图片选择器
     */
    private void initFunctionConfig() {
        if (functionConfigBuilder == null) {
            functionConfigBuilder = new FunctionConfig.Builder()
                    .setMutiSelectMaxSize(9)
                    .setSelected(list)
                    .setEnableCamera(false)
                    .setEnablePreview(true);
        }
        resultCallback = new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList != null) {
                    if (reqeustCode == REQUEST_CODE_CAMERA) {
                        int index = list.size() - 1;
                        list.add(index > 0 ? index : 0, resultList.get(0));
                        if (list.size() < 10) {
                            isMax = false;
                        } else {
                            list.remove(list.size() - 1);
                            isMax = true;
                        }
                    } else {
                        list.clear();
                        list.addAll(resultList);
                        if (resultList.size() < 9) {
                            list.add(nullPhoto);
                            isMax = false;
                        } else {
                            isMax = true;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    isHasNew=true;
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtil.toast(errorMsg);
            }
        };
    }

    /**
     * 过滤已上传的图片
     *
     * @return
     */
    private List<String> checkPhotoUrl() {
        if (!isHasNew) return null;
        uploadOK = false;
        isReady = false;
        isHasNew = false;
        List<String> photoPathList = new ArrayList<>();
        int size = isMax ? list.size() : list.size() - 1;
        for (int i = 0; i < size; i++) {
            String photoPath = list.get(i).getPhotoPath();
            if (!TextUtils.isEmpty(photoPath)) {
                photoPathList.add(photoPath);
            }
        }
        return photoPathList;
    }

    /**
     * 上传图片
     */
    public void uploadImage() {
        loadingWindow.showWindow();
        upload(checkPhotoUrl());
    }

    /**
     * 图片上传
     *
     * @param photoPathList 图片路径集合
     */
    private void upload(List<String> photoPathList) {
        if (photoPathList == null) {
            return;
        }
        Observable.fromIterable(photoPathList)
                .flatMap((Function<String, Observable<HttpResult>>) photoPath -> rxUpload(photoPath))
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    isReady = true;
                    uploadImage();
                })
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (!isHasNew) {
                            uploadOK = true;
                            if (!isPublishing.get()) {
                                publish();
                            }
                        }
                    }

                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        uploadOK = false;
                        isPublishing.set(false);
                        loadingWindow.hidWindow();
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(HttpResult uploadUrl) {
                        if (uploadUrl.getImgId() != null) {
                            urlSb.append(uploadUrl.getImgId()).append(",");
                        }
                    }
                });
    }


    /**
     * 图片进行压缩并进行请求包装
     *
     * @param path
     * @return
     */
    @Nullable
    private Observable<HttpResult> rxUpload(final String path) {
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        return ApiWrapper.getInstance().setUpload(path);
    }

    private StringBuilder urlSb = new StringBuilder("");
    private String image = "";

    /**
     * 发帖请求
     */
    public void publish() {
        isPublishing.set(true);
        if (!uploadOK) {
            return;
        }

        urlSb.deleteCharAt(urlSb.length() - 1);
        image = urlSb.toString();

        commitData();
    }


    /**
     * 发帖请求
     */
    public void commitData() {
        if (list.size() == 0 && TextUtils.isEmpty(content)) {
            ToastUtil.toast("感想内容不能为空");
            return;
        }

        ApiWrapper.getInstance().addCommentApp(content, image)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(loadingWindow::hidWindow)
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        EventBus.getDefault().post(Event.RELOAD);
                        activity.onBackPressed();
                    }
                });
    }


    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int
                    i) {
                switch (view.getId()) {
                    case R.id.publish_item_lose:
                        if (isMax) {
                            list.add(nullPhoto);
                            isMax = false;
                        }
                        baseQuickAdapter.remove(i);
                        break;
                    case R.id.sdv_item_pic:
                        String photoPath = ((PhotoInfo) baseQuickAdapter.getData().get(i)).getPhotoPath();
                        if (TextUtils.isEmpty(photoPath)) {
                            getPic();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {
        WeakReference<GalleryFinal.OnHanlderResultCallback> wrf = new WeakReference<>(
                resultCallback);
        WeakReference<GalleryFinal.OnHanlderResultCallback> wr = new WeakReference<>(
                GalleryFinal.getCallback());
        resultCallback = null;
        loadingWindow.dismiss();
        GalleryFinal.clearCallback();
        GalleryFinal.cleanCacheFile();
    }
}
