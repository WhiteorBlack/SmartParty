package com.qiantang.smartparty.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.module.assistant.view.ActivityDetial;
import com.qiantang.smartparty.module.assistant.view.AdviseActivity;
import com.qiantang.smartparty.module.assistant.view.AdviseRecordActivity;
import com.qiantang.smartparty.module.assistant.view.ApplyPartyActivity;
import com.qiantang.smartparty.module.assistant.view.ApplyPartyDetialActivity;
import com.qiantang.smartparty.module.assistant.view.ApplySuccessActivity;
import com.qiantang.smartparty.module.assistant.view.CharacterActivity;
import com.qiantang.smartparty.module.assistant.view.CharacterDetialActivity;
import com.qiantang.smartparty.module.assistant.view.FeeDetialActivity;
import com.qiantang.smartparty.module.assistant.view.FeeDetialSpecialActivity;
import com.qiantang.smartparty.module.assistant.view.FeeRecordActivity;
import com.qiantang.smartparty.module.assistant.view.MeetingActivity;
import com.qiantang.smartparty.module.assistant.view.MeetingDetialActivity;
import com.qiantang.smartparty.module.assistant.view.MienActivity;
import com.qiantang.smartparty.module.assistant.view.MienDetialActivity;
import com.qiantang.smartparty.module.assistant.view.MsgActivity;
import com.qiantang.smartparty.module.assistant.view.PartyActivity;
import com.qiantang.smartparty.module.assistant.view.PartyfeeActivity;
import com.qiantang.smartparty.module.assistant.view.ReportActivity;
import com.qiantang.smartparty.module.assistant.view.SignListActivity;
import com.qiantang.smartparty.module.assistant.view.StructureActivity;
import com.qiantang.smartparty.module.assistant.view.ThinkingActivity;
import com.qiantang.smartparty.module.assistant.view.ThinkingDetialActivity;
import com.qiantang.smartparty.module.index.view.BookDetialActivity;
import com.qiantang.smartparty.module.index.view.BookRecommendActivity;
import com.qiantang.smartparty.module.index.view.LearningListActivity;
import com.qiantang.smartparty.module.index.view.NewsActivity;
import com.qiantang.smartparty.module.index.view.OnlineListActivity;
import com.qiantang.smartparty.module.index.view.ParagonActivity;
import com.qiantang.smartparty.module.index.view.ParagonDetialActivity;
import com.qiantang.smartparty.module.index.view.RankActivity;
import com.qiantang.smartparty.module.index.view.SignActivity;
import com.qiantang.smartparty.module.index.view.SpeechStudyActivity;
import com.qiantang.smartparty.module.index.view.TestDetialActivity;
import com.qiantang.smartparty.module.index.view.TestDoneInfoActivity;
import com.qiantang.smartparty.module.index.view.TestInfoActivity;
import com.qiantang.smartparty.module.index.view.TestListActivity;
import com.qiantang.smartparty.module.index.view.TestRecordActivity;
import com.qiantang.smartparty.module.index.view.VideoSpeechDetialActivity;
import com.qiantang.smartparty.module.index.view.VideoStudyActivity;
import com.qiantang.smartparty.module.index.view.VideoStudyDetialActivity;
import com.qiantang.smartparty.module.index.view.VoiceSpeechDetialActivity;
import com.qiantang.smartparty.module.login.view.BindPhoneActivity;
import com.qiantang.smartparty.module.login.view.CompeteInfoActivity;
import com.qiantang.smartparty.module.login.view.LoginActivity;
import com.qiantang.smartparty.module.login.view.RegisterActivity;
import com.qiantang.smartparty.module.login.view.SimpleInfoActivity;
import com.qiantang.smartparty.module.main.view.MainActivity;
import com.qiantang.smartparty.module.mine.view.AboutUsActivity;
import com.qiantang.smartparty.module.mine.view.DateDeptActivity;
import com.qiantang.smartparty.module.mine.view.InfoActivity;
import com.qiantang.smartparty.module.mine.view.ModifyPhoneActivity;
import com.qiantang.smartparty.module.mine.view.ModifyPwdActivity;
import com.qiantang.smartparty.module.mine.view.MonthScoreActivity;
import com.qiantang.smartparty.module.mine.view.MyActivity;
import com.qiantang.smartparty.module.mine.view.MyCollectionActivity;
import com.qiantang.smartparty.module.mine.view.MyTestActivity;
import com.qiantang.smartparty.module.mine.view.NamePositionActivity;
import com.qiantang.smartparty.module.mine.view.SettingActivity;
import com.qiantang.smartparty.module.mine.view.ShowHeadPicActivity;
import com.qiantang.smartparty.module.mine.view.TotalScoreActivity;
import com.qiantang.smartparty.module.scan.view.QRCodeActivity;
import com.qiantang.smartparty.module.search.view.SearchActivity;
import com.qiantang.smartparty.module.spalsh.view.GuideActivity;
import com.qiantang.smartparty.module.study.view.PublishActivity;
import com.qiantang.smartparty.module.study.view.StudyMyActivity;
import com.qiantang.smartparty.module.study.view.StudyUnReadMsgActivity;
import com.qiantang.smartparty.module.web.view.HeadWebActivity;
import com.qiantang.smartparty.module.web.view.WebViewNew;
import com.qiantang.smartparty.network.URLs;
import com.umeng.message.PushAgent;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class ActivityUtil {
    private static String TAG = "NETWORK_Exception";

    public static String getChannelName(Activity ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,
                // 因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx
                        .getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static Map<String, String> getExtraMap(Activity activity) {
        String extra = activity.getIntent().getStringExtra("extraMap");
        if (!StringUtil.isEmpty(extra)) {
            try {
                Log.e(TAG, "extra: " + extra);
                Gson gson = new Gson();
                Map<String, String> extraMap = new HashMap<>();
                extraMap = gson.fromJson(extra, extraMap.getClass());

//                JSONObject object = new JSONObject(extra);
//                Map<String, String> extraMap = (Map<String, String>) object;
                Log.e(TAG, "extraMap: " + extraMap.toString());
                return extraMap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void openChrome(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 跳转没有登录要求的网页
     *
     * @param activity
     * @param url
     */
    public static void jumpWeb(Activity activity, String url) {
        Intent intent = new Intent(activity, WebViewNew.class);
        intent.putExtra(WebUtil.URL, url);
        activity.startActivity(intent);
    }

    /**
     * 跳转没有登录要求的网页
     *
     * @param activity
     * @param url
     */
    public static void jumpWeb(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewNew.class);
        intent.putExtra("title", title);
        intent.putExtra(WebUtil.URL, url);
        activity.startActivity(intent);
    }

    /**
     * 通知栏点击跳转
     *
     * @param activity
     * @param url
     */
    public static void jumpNotifyWeb(Context activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewNew.class);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(WebUtil.URL, url);
        activity.startActivity(intent);
    }

    /**
     * 跳转没有登录有要求的网页
     *
     * @param activity
     * @param url
     */
    public static void jumpMyWeb(Activity activity, String url) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        Intent intent = new Intent(activity, WebViewNew.class);
        intent.putExtra(WebUtil.URL, url);
        activity.startActivity(intent);
    }

    /**
     * 首页
     *
     * @param activity
     */
    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 二维码扫描
     *
     * @param activity
     */
    public static void startQRActivity(Activity activity) {
        Intent intent = new Intent(activity, QRCodeActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 引导页
     *
     * @param activity
     */
    public static void startGuideActivity(Activity activity) {
        Intent intent = new Intent(activity, GuideActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 每月学习值
     *
     * @param activity
     */
    public static void startMonthScoreActivity(Activity activity, String date) {
        Intent intent = new Intent(activity, MonthScoreActivity.class);
        intent.putExtra("date", date);
        activity.startActivity(intent);
    }

    /**
     * 累计学习值
     *
     * @param activity
     */
    public static void startTotalScoreActivity(Activity activity) {
        Intent intent = new Intent(activity, TotalScoreActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 设置
     *
     * @param activity
     */
    public static void startSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 个人档案
     *
     * @param activity
     */
    public static void startInfoActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        Intent intent = new Intent(activity, InfoActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 登录
     *
     * @param activity
     */
    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 注册
     *
     * @param activity
     */
    public static void startRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 完善个人信息
     *
     * @param activity
     */
    public static void startCompeteActivity(Activity activity, String phone, String pwd) {
        Intent intent = new Intent(activity, CompeteInfoActivity.class);
        intent.putExtra("pwd", pwd);
        intent.putExtra("phone", phone);
        activity.startActivity(intent);
    }

    /**
     * 修改头像
     *
     * @param activity
     */
    public static void startChangeAvatarActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, ShowHeadPicActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    /**
     * 完善个人信息
     *
     * @param activity
     */
    public static void startSimpleInfoActivity(Activity activity) {
        Intent intent = new Intent(activity, SimpleInfoActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 视频学习
     *
     * @param activity
     */
    public static void startVideoStudyActivity(Activity activity) {
        Intent intent = new Intent(activity, VideoStudyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 系列讲话
     *
     * @param activity
     */
    public static void startSpeechStudyActivity(Activity activity) {
        Intent intent = new Intent(activity, SpeechStudyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 专题学习
     *
     * @param activity
     */
    public static void startLearnListActivity(Activity activity, int id) {
        Intent intent = new Intent(activity, LearningListActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 新闻快报
     *
     * @param activity
     */
    public static void startNewsActivity(Activity activity, int type) {
        Intent intent = new Intent(activity, NewsActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }


    /**
     * 学习排行
     *
     * @param activity
     */
    public static void startRankActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅内部人员可查看");
            return;
        }
        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, RankActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 理论在线
     *
     * @param activity
     */
    public static void startOnlineActivity(Activity activity) {
        Intent intent = new Intent(activity, OnlineListActivity.class);
        activity.startActivity(intent);
    }


    /**
     * 考试评测
     *
     * @param activity
     */
    public static void startTestListActivity(Activity activity) {
        Intent intent = new Intent(activity, TestListActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 考试评测
     *
     * @param activity
     */
    public static void startTestInfoActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, TestInfoActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 考试评测 已完成
     *
     * @param activity
     */
    public static void startTestDoneInfoActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, TestDoneInfoActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 考试评测 详情
     *
     * @param activity
     */
    public static void startTestDeitalInfoActivity(Activity activity, String id, int time, int count, boolean retry) {
        Intent intent = new Intent(activity, TestDetialActivity.class);
        intent.putExtra("time", time);
        intent.putExtra("retry", retry);
        intent.putExtra("count", count);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 考试评测 记录
     *
     * @param activity
     */
    public static void startTestRecordActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, TestRecordActivity.class);

        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 视频学习详情
     *
     * @param activity
     */
    public static void startVideoDetialActivity(Activity activity, String url, String title, String id) {
        Intent intent = new Intent(activity, VideoStudyDetialActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    /**
     * 系列讲话详情
     *
     * @param activity
     */
    public static void startVideoSpeechDetialActivity(Activity activity, String url, String title, String id) {
        Intent intent = new Intent(activity, VideoSpeechDetialActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    /**
     * 系列讲话详情
     *
     * @param activity
     */
    public static void startVoiceSpeechDetialActivity(Activity activity, String url, String title, String id) {
        Intent intent = new Intent(activity, VoiceSpeechDetialActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    /**
     * 党建活动详情
     * 传入状态 以调整底部评论框和报名状态
     *
     * @param activity
     */
    public static void startActivityDetialActivity(Activity activity, String id, int status) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅内部人员可查看");
            return;
        }
        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, ActivityDetial.class);
        intent.putExtra("id", id);
        intent.putExtra("status", status);
        activity.startActivity(intent);
    }

    /**
     * 党费缴纳
     *
     * @param activity
     */
    public static void startPartyFeeActivity(Activity activity) {
        Intent intent = new Intent(activity, PartyfeeActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 党费缴纳记录
     *
     * @param activity
     */
    public static void startFeeRecordActivity(Activity activity) {
        Intent intent = new Intent(activity, FeeRecordActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 入党申请
     *
     * @param activity
     */
    public static void startApplyPartyActivity(Activity activity) {
        Intent intent = new Intent(activity, ApplyPartyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 入党申请成功
     *
     * @param activity
     */
    public static void startApplyPartySuccessActivity(Activity activity) {
        Intent intent = new Intent(activity, ApplySuccessActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 入党申请详情
     *
     * @param activity
     */
    public static void startApplyPartyDeitalActivity(Activity activity) {
        Intent intent = new Intent(activity, ApplyPartyDetialActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 我的活动页面
     *
     * @param activity
     */
    public static void startMyActivity(Activity activity) {
        Intent intent = new Intent(activity, MyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 我的评测
     *
     * @param activity
     */
    public static void startMyTestActivity(Activity activity) {
        Intent intent = new Intent(activity, MyTestActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 我的收藏页面
     *
     * @param activity
     */
    public static void startMyCollectionActivity(Activity activity) {
        Intent intent = new Intent(activity, MyCollectionActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 我的收藏页面
     *
     * @param activity
     */
    public static void startAboutUsActivity(Activity activity) {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 修改手机号页面
     *
     * @param activity
     */
    public static void startModifyPhoneActivity(Activity activity) {
        Intent intent = new Intent(activity, ModifyPhoneActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 修改密码页面
     *
     * @param activity
     */
    public static void startModifyPwdActivity(Activity activity) {
        Intent intent = new Intent(activity, ModifyPwdActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 签到页面
     *
     * @param activity
     */
    public static void startSignActivity(Activity activity, String result) {
        Intent intent = new Intent(activity, SignActivity.class);
        intent.putExtra("result", result);
        activity.startActivity(intent);
    }

    /**
     * 意见反馈页面
     *
     * @param activity
     */
    public static void startAdviseActivity(Activity activity) {
        Intent intent = new Intent(activity, AdviseActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 人物表彰页面
     *
     * @param activity
     */
    public static void startCharacterActivity(Activity activity) {
        Intent intent = new Intent(activity, CharacterActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 意见反馈记录页面
     *
     * @param activity
     */
    public static void startAdviseRecordActivity(Activity activity) {
        Intent intent = new Intent(activity, AdviseRecordActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 会议纪要页面
     *
     * @param activity
     */
    public static void startMeetingActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅内部人员可查看");
            return;
        }
        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, MeetingActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 会议纪要页面
     *
     * @param activity
     */
    public static void startMeetingDetialActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, MeetingDetialActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }


    /**
     * 思想汇报页面
     *
     * @param activity
     */
    public static void startReportActivity(Activity activity) {
        Intent intent = new Intent(activity, ReportActivity.class);
        activity.startActivityForResult(intent, 100);
    }

    /**
     * 思想汇报列表页面
     *
     * @param activity
     */
    public static void startThinkingActivity(Activity activity) {
        Intent intent = new Intent(activity, ThinkingActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 组织架构页面
     *
     * @param activity
     */
    public static void startStructureActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅内部人员可查看");
            return;
        }
        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, StructureActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 思想汇报详情页面
     *
     * @param activity
     */
    public static void startThinkingDetialActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, ThinkingDetialActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 党费缴纳详情
     *
     * @param activity
     */
    public static void startFeeDetialActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, FeeDetialActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 爱心捐款详情
     *
     * @param activity
     */
    public static void startFeeDetialSpecialActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, FeeDetialSpecialActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 学习感悟未读消息
     *
     * @param activity
     */
    public static void startStudyUnreadActivity(Activity activity) {
        Intent intent = new Intent(activity, StudyUnReadMsgActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 我的学习感悟
     *
     * @param activity
     */
    public static void startStudyMyActivity(Activity activity,String id,String name) {
        Intent intent = new Intent(activity, StudyMyActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",name);
        activity.startActivity(intent);
    }

    /**
     * 发布学习感悟
     *
     * @param activity
     */
    public static void startPublishActivity(Activity activity) {
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅供内部人员使用");
            return;
        }

        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, PublishActivity.class);
        activity.startActivityForResult(intent, Config.PUBLISH_REQUEST);
    }

    /**
     * 通知公告
     *
     * @param activity
     */
    public static void startMsgActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅内部人员可查看");
            return;
        }

        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, MsgActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 党建风采
     *
     * @param activity
     */
    public static void startMienActivity(Activity activity) {
        Intent intent = new Intent(activity, MienActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 党建活动
     *
     * @param activity
     */
    public static void startPartyActivity(Activity activity) {
        if (!MyApplication.isLogin()) {
            startLoginActivity(activity);
            return;
        }
        if (TextUtils.equals(MyApplication.mCache.getAsString(CacheKey.DEPT_ID), "1")) {
            ToastUtil.toast("仅内部人员可查看");
            return;
        }
        if (((int) MyApplication.mCache.getAsObject(CacheKey.STATUS)) > 0) {
            ToastUtil.toast("您尚未通过审核，请耐心等待");
            return;
        }
        Intent intent = new Intent(activity, PartyActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 搜索页面
     *
     * @param activity
     */
    public static void startSearchActivity(Activity activity, int type) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    /**
     * 签到记录
     *
     * @param activity
     */
    public static void startSignListActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, SignListActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 党建风采详情
     *
     * @param activity
     */
    public static void startMienDetialActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, MienDetialActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 人物表彰详情
     *
     * @param activity
     */
    public static void startCharacterDetialActivity(Activity activity, String id, String printUrl) {
        Intent intent = new Intent(activity, CharacterDetialActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("printurl", printUrl);
        activity.startActivity(intent);
    }

    /**
     * 用于上部分为H5,下部分为评论原生的页面
     *
     * @param activity
     * @param id
     * @param title
     */
    public static void startHeadWebActivity(Activity activity, String id, String title, String url, int type, String imgUrl) {
        Intent intent = new Intent(activity, HeadWebActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("img", imgUrl);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    /**
     * 先进典范
     *
     * @param activity
     */
    public static void startParagonActivity(Activity activity) {
        Intent intent = new Intent(activity, ParagonActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 先进典范详情
     *
     * @param activity
     */
    public static void startParagonDetialActivity(Activity activity, String id, String printUrl) {
        Intent intent = new Intent(activity, ParagonDetialActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("printurl", printUrl);
        activity.startActivity(intent);
    }

    /**
     * 好书推荐
     *
     * @param activity
     */
    public static void startBookRecommendActivity(Activity activity) {
        Intent intent = new Intent(activity, BookRecommendActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 好书推荐详情
     *
     * @param activity
     */
    public static void startBookDetialActivity(Activity activity, String id) {
        Intent intent = new Intent(activity, BookDetialActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 信息修改
     *
     * @param activity
     */
    public static void startNamePosActivity(Activity activity, int type, String info) {
        Intent intent = new Intent(activity, NamePositionActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("info", info);
        activity.startActivity(intent);
    }

    /**
     * 信息修改
     *
     * @param activity
     */
    public static void startDateDeptActivity(Activity activity, int type, String info) {
        Intent intent = new Intent(activity, DateDeptActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("info", info);
        activity.startActivity(intent);
    }

    /**
     * 绑定微信,qq手机
     *
     * @param activity
     */
    public static void startBindPhoneActivity(Activity activity, int type, String id) {
        Intent intent = new Intent(activity, BindPhoneActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    /**
     * 1在线测评  2通知公告  3特殊党费   4党建风采   5活动/会议通知   6后台推送   7 视频    8讲话    9专题学习 10 唯一登录
     *
     * @param context
     * @param message
     */
    public static void startNotifyActivity(Context context, UMessage message) {

        String type = message.extra.get("type");
        String id = message.extra.get("countId");
        if (!TextUtils.isEmpty(type)) {
            Intent intent = null;
            if (TextUtils.equals(type, "1")) {//1在线测评
                intent = new Intent(context, TestInfoActivity.class);
                intent.putExtra("id", id);
            }

            if (TextUtils.equals(type, "2")) { //2通知公告
                jumpNotifyWeb(context, URLs.MESSAGE_DETIAL + id, "公告详情");
                return;
            }

            if (TextUtils.equals(type, "3")) {//3特殊党费
                intent = new Intent(context, FeeDetialSpecialActivity.class);
                intent.putExtra("id", id);
            }

            if (TextUtils.equals(type, "4")) {//4党建风采
                intent = new Intent(context, HeadWebActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 0);
                intent.putExtra("title", "党建风采");
                intent.putExtra("url", URLs.NOTICE_DETIAL);
            }

            if (TextUtils.equals(type, "5")) {//活动/会议通知

                return;
            }

            if (TextUtils.equals(type, "6")) {//6后台推送
                return;
            }

            if (TextUtils.equals(type, "7")) {//视频
                intent = new Intent(context, VideoStudyDetialActivity.class);
                intent.putExtra("url", "");
                intent.putExtra("id", id);
                intent.putExtra("title", message.title);
            }

            if (TextUtils.equals(type, "8")) {//8讲话
                intent = new Intent(context, VoiceSpeechDetialActivity.class);
                intent.putExtra("url", "");
                intent.putExtra("id", id);
                intent.putExtra("title", message.title);
            }

            if (TextUtils.equals(type, "9")) {//9专题学习
                intent = new Intent(context, HeadWebActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 3);
                intent.putExtra("title", "专题学习");
                intent.putExtra("url", URLs.SPECIALORTHEORY);
            }

            if (TextUtils.equals(type, "10")) {//唯一登录
                logOut(context);
            }

            if (intent == null) {
                return;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }

    /**
     * 退出登录
     */
    private static void logOut(Context context) {
        PushAgent.getInstance(context).deleteAlias(MyApplication.USER_ID, "party", (b, s) -> {

        });
        MyApplication.mCache.remove(CacheKey.USER_ID);
        MyApplication.mCache.remove(CacheKey.INFO);
        MyApplication.mCache.remove(CacheKey.USER_INFO);
        MyApplication.mCache.remove(CacheKey.PHONE);
        MyApplication.USER_ID = "";
        System.gc();
        WebUtil.removeCookie();
        EventBus.getDefault().post(Event.RELOAD);
        EventBus.getDefault().post(Event.LOGOUT);
        MyApplication.isLoginOB.set(false);

    }
}
