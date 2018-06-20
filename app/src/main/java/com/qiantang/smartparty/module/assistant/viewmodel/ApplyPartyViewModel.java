package com.qiantang.smartparty.module.assistant.viewmodel;

import android.Manifest;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.PublishImgAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxApplyPartyInfo;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.StringUtil;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;
import com.trello.rxlifecycle2.android.ActivityEvent;
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

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class ApplyPartyViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private PublishImgAdapter adapter;
    private FunctionConfig.Builder functionConfigBuilder;
    private List<PhotoInfo> list = new ArrayList<>();
    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_GALLERY = 1001;
    private GalleryFinal.OnHanlderResultCallback resultCallback;
    private boolean isMax;
    private final PhotoInfo nullPhoto = new PhotoInfo();
    private boolean isHasNew = false;
    private boolean isReady = true;
    private ObservableField<RxApplyPartyInfo> applyInfo = new ObservableField<>();
    private ObservableInt inputCount = new ObservableInt(0);
    private String image = "";
    private Map<String, String> picMap = new HashMap<>();
    public ObservableBoolean isPublishing = new ObservableBoolean();
    private final LoadingWindow loadingWindow;
    private boolean uploadOK = false;
    private String currentPath = "";

    public ApplyPartyViewModel(BaseBindActivity activity, PublishImgAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        loadingWindow = new LoadingWindow(activity);
        initAdapterData(adapter);
        initFunctionConfig();
        RxApplyPartyInfo rxApplyPartyInfo = new RxApplyPartyInfo();
        rxApplyPartyInfo.setUsername(MyApplication.mCache.getAsString(CacheKey.USER_NAME));

        setApplyInfo(rxApplyPartyInfo);
    }

    /**
     * 初始化图片展示Adapter
     *
     * @param adapter
     */
    private void initAdapterData(PublishImgAdapter adapter) {
        list.add(nullPhoto);
        adapter.setNewData(list);
    }

    public void commitData() {
        ApiWrapper.getInstance().applyFor(applyInfo.get().getUsername(), applyInfo.get().getDept(), applyInfo.get().getPhone(), applyInfo.get().getTitle(), applyInfo.get().getContent(), "")
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> loadingWindow.hidWindow())
                .subscribe(new NetworkSubscriber<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtil.toast(data);
                        activity.onBackPressed();
                    }
                });
    }

    /**
     * 上传图片
     */
    public void uploadImage() {
        if (TextUtils.isEmpty(applyInfo.get().getContent())) {
            ToastUtil.toast("请输入入党申请内容");
            return;
        }

        if (TextUtils.isEmpty(applyInfo.get().getDept())) {
            ToastUtil.toast("请输所在部门信息");
            return;
        }

        if (TextUtils.isEmpty(applyInfo.get().getPhone())) {
            ToastUtil.toast("请输手机号码");
            return;
        }

        if (TextUtils.isEmpty(applyInfo.get().getTitle())) {
            ToastUtil.toast("请输入入党申请标题");
            return;
        }

        if (TextUtils.isEmpty(applyInfo.get().getUsername())) {
            ToastUtil.toast("请输入您的姓名");
            return;
        }
        loadingWindow.showWindow();
        upload(checkPhotoUrl());
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
                            currentPath="";
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
        if (StringUtil.isEmpty(path) || picMap.get(path) != null) {
            return null;
        }
        currentPath = path;
        return ApiWrapper.getInstance().setUpload(path);
    }

    private StringBuilder urlSb = new StringBuilder("");

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
                    isHasNew = true;
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtil.toast(errorMsg);
            }
        };
    }

    @Bindable
    public RxApplyPartyInfo getApplyInfo() {
        return applyInfo.get();
    }

    public void setApplyInfo(RxApplyPartyInfo applyInfo) {
        this.applyInfo.set(applyInfo);
        notifyPropertyChanged(BR.applyInfo);

    }

    @Bindable
    public int getInputCount() {
        return inputCount.get();
    }

    public void setInputCount(int inputCount) {
        this.inputCount.set(inputCount);
        notifyPropertyChanged(BR.inputCount);
    }

    @Override
    public void destroy() {
        resultCallback = null;
        GalleryFinal.clearCallback();
        GalleryFinal.cleanCacheFile();
    }

    public RecyclerView.OnItemTouchListener itemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.publish_item_lose:
                        if (isMax) {
                            list.add(nullPhoto);
                            isMax = false;
                        }
                        baseQuickAdapter.remove(position);
                        break;
                    case R.id.sdv_item_pic:
                        String photoPath = ((PhotoInfo) baseQuickAdapter.getData().get(position)).getPhotoPath();
                        if (TextUtils.isEmpty(photoPath)) {
                            getPic();
                        }
                        break;
                }
            }

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }
}
