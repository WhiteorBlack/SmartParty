package com.qiantang.smartparty.module.assistant.viewmodel;

import android.Manifest;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.PublishImgAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.utils.permissions.EasyPermission;
import com.qiantang.smartparty.utils.permissions.PermissionCode;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.qiantang.smartparty.utils.StringUtil.getString;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class ApplyPartyViewModel implements ViewModel {
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

    public ApplyPartyViewModel(BaseBindActivity activity, PublishImgAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        initFunctionConfig();
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
                    if (isReady) {
//                        uploadImage();
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                ToastUtil.toast(errorMsg);
            }
        };
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
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
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
        };
    }
}
