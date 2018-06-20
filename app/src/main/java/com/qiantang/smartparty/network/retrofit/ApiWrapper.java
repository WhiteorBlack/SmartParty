package com.qiantang.smartparty.network.retrofit;


import android.databinding.ObservableField;

import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxActivity;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxAds;
import com.qiantang.smartparty.modle.RxAdviseRecord;
import com.qiantang.smartparty.modle.RxAssientHome;
import com.qiantang.smartparty.modle.RxBookDetial;
import com.qiantang.smartparty.modle.RxBookRecommend;
import com.qiantang.smartparty.modle.RxCharacterDetial;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.modle.RxDeptName;
import com.qiantang.smartparty.modle.RxFeeRecord;
import com.qiantang.smartparty.modle.RxIndex;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.modle.RxIndexSpeak;
import com.qiantang.smartparty.modle.RxLearningClass;
import com.qiantang.smartparty.modle.RxLearningList;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.modle.RxMyStudy;
import com.qiantang.smartparty.modle.RxMyUserInfo;
import com.qiantang.smartparty.modle.RxOnline;
import com.qiantang.smartparty.modle.RxParagonDetial;
import com.qiantang.smartparty.modle.RxPartyFee;
import com.qiantang.smartparty.modle.RxPartyFeeDetial;
import com.qiantang.smartparty.modle.RxPersonalCenter;
import com.qiantang.smartparty.modle.RxRankBranch;
import com.qiantang.smartparty.modle.RxRankPersonal;
import com.qiantang.smartparty.modle.RxRecordDetial;
import com.qiantang.smartparty.modle.RxSignList;
import com.qiantang.smartparty.modle.RxSpeechDetial;
import com.qiantang.smartparty.modle.RxStructureLevelOne;
import com.qiantang.smartparty.modle.RxStructureLevelTwo;
import com.qiantang.smartparty.modle.RxStructurePerson;
import com.qiantang.smartparty.modle.RxStudy;
import com.qiantang.smartparty.modle.RxStudyUnreadMsg;
import com.qiantang.smartparty.modle.RxTest;
import com.qiantang.smartparty.modle.RxTestDetial;
import com.qiantang.smartparty.modle.RxTestDoneInfo;
import com.qiantang.smartparty.modle.RxTestInfo;
import com.qiantang.smartparty.modle.RxThinkDetial;
import com.qiantang.smartparty.modle.RxUploadUrl;
import com.qiantang.smartparty.modle.RxVideoDetial;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.utils.luban.Luban;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
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

    public synchronized Observable<HttpResult> setUpload(final String path) {
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
                            MultipartBody.Part.createFormData("files", fileName, requestFile);
                    return ApiWrapper.getInstance().upload(body);
                })
                .map(uploadUrls -> {
                    HttpResult uploadUrl = uploadUrls;
                    uploadUrl.setImagePath(path);
                    return uploadUrl;
                });
    }

    /**
     * 上传文件
     */
    public Observable<HttpResult> upload(MultipartBody.Part file) {
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
     * 获取学习感悟列表
     *
     * @return
     */
    public Observable<RxMyStudy> getMyStudyList(int pageNo) {
        return getService().getMyStudyList(MyApplication.USER_ID, pageNo).compose(this.applySchedulers());
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
     *
     * @param id
     * @return
     */
    public Observable<String> cancelLike(String id) {
        return getService().cancelLike(MyApplication.USER_ID, id).compose(this.applySchedulers());
    }

    /**
     * 发表感想
     *
     * @return
     */
    public Observable<String> addCommentApp(String content, String image) {
        return getService().addCommentApp(MyApplication.USER_ID, content, image).compose(this.applySchedulers());
    }

    /**
     * 党建助手首页
     */
    public Observable<RxAssientHome> assientHome() {
        return getService().assientHome().compose(this.applySchedulers());
    }

    /**
     * 党建助手首页
     */
    public Observable<List<RxMsg>> tzNotice(int page) {
        return getService().tzNotice(page).compose(this.applySchedulers());
    }

    /**
     * @param type 4党建风采
     * @param page
     * @return
     */
    public Observable<List<RxIndexCommon>> fcNotice(int page, int type) {
        return getService().fcNotice(page, type).compose(this.applySchedulers());
    }

    /**
     * 党建活动
     *
     * @param page
     * @return
     */
    public Observable<List<RxActivity>> djActivity(int page) {
        return getService().djActivity(page).compose(this.applySchedulers());
    }

    /**
     * 我的党建活动
     *
     * @param page
     * @return
     */
    public Observable<List<RxActivity>> myActivity(int page) {
        return getService().djActivity(page, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    public Observable<String> deleteActivity(String id) {
        return getService().djActivityDelete(id).compose(this.applySchedulers());
    }

    /**
     * 活动详情
     *
     * @param page
     * @param id
     * @return
     */
    public Observable<RxActivityDetial> djActivityDetails(int page, String id) {
        return getService().djActivityDetails(page, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 评论
     *
     * @param id
     * @return
     */
    public Observable<String> comment(String content, String id) {
        return getService().comment(id, content, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 报名
     *
     * @param id
     * @return
     */
    public Observable<String> enroll(String id) {
        return getService().enroll(id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 签到记录
     *
     * @param id
     * @param pageNo
     * @return
     */
    public Observable<List<RxSignList>> tzSign(String id, int pageNo) {
        return getService().tzSign(id, pageNo).compose(this.applySchedulers());
    }

    /**
     * 风采详情
     *
     * @param page
     * @param id
     * @return
     */
    public Observable<RxActivityDetial> fcNoticeDetails(int page, String id) {
        return getService().fcNoticeDetails(page, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 风采详情
     *
     * @param page
     * @param id
     * @return
     */
    public Observable<RxCharacterDetial> rwNoticeDetails(int page, String id, String printurl) {
        return getService().rwNoticeDetails(page, printurl, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 思想汇报列表
     *
     * @param page
     * @param type
     * @return
     */
    public Observable<List<RxIndexCommon>> thinking(int page, int type) {
        return getService().thinking(page, type, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 思想汇报详情
     *
     * @return
     */
    public Observable<RxThinkDetial> thinkingDetails(String id) {
        return getService().thinkingDetails(id).compose(this.applySchedulers());
    }

    /**
     * 发表思想汇报
     *
     * @param title
     * @param content
     * @return
     */
    public Observable<String> insertThinking(String title, String content) {
        return getService().insertThinking(title, content, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 发表反馈内容
     *
     * @param content
     * @return
     */
    public Observable<String> insertIdea(String content) {
        return getService().insertIdea(content, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 反馈列表
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxAdviseRecord>> ideaList(int pageNo) {
        return getService().ideaList(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 首页数据
     *
     * @return
     */
    public Observable<List<RxIndex>> showHomePage() {
        return getService().ShowHomePage().compose(this.applySchedulers());
    }

    /**
     * 视频学习列表
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxVideoStudy>> videoList(int pageNo) {
        return getService().videoList(pageNo).compose(this.applySchedulers());
    }

    /**
     * 视频学习列表
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxVideoStudy>> videoUserList(int pageNo) {
        return getService().videoUserList(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }


    /**
     * 视频详情
     *
     * @param pageNo
     * @param id
     * @return
     */
    public Observable<RxVideoDetial> videoDetial(int pageNo, String id) {
        return getService().videoDetails(pageNo, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 视频学习 评论点赞
     *
     * @param id
     * @return
     */
    public Observable<String> videoLike(String id) {
        return getService().videoLike(MyApplication.USER_ID, id).compose(this.applySchedulers());
    }

    /**
     * 视频学习 取消评论点赞
     *
     * @param id
     * @return
     */
    public Observable<String> removeVideoLike(String id) {
        return getService().removeVideoLike(MyApplication.USER_ID, id).compose(this.applySchedulers());
    }


    /**
     * 视频学习评论
     *
     * @param id
     * @param content
     * @return
     */
    public Observable<String> commentVideo(String id, String content) {
        return getService().commentVideo(id, content, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 取消收藏
     *
     * @param id
     * @param type
     * @return
     */
    public Observable<String> collectAbolish(String id, int type) {
        return getService().collectAbolish(type, MyApplication.USER_ID, id).compose(this.applySchedulers());
    }

    /**
     * 收藏
     *
     * @param id
     * @param type
     * @return
     */
    public Observable<String> collectSave(String id, int type) {
        return getService().collectSave(type, MyApplication.USER_ID, id).compose(this.applySchedulers());
    }

    /**
     * 系列讲话
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxIndexSpeak>> speechList(int pageNo) {
        return getService().speakList(pageNo).compose(this.applySchedulers());
    }

    /**
     * 系列讲话 收藏
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxIndexSpeak>> speechUserList(int pageNo) {
        return getService().speakUserList(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 系列讲话详情
     *
     * @param pageNo
     * @param id
     * @return
     */
    public Observable<RxSpeechDetial> speechDetial(int pageNo, String id) {
        return getService().speechDetails(pageNo, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 理论在线类别
     *
     * @return
     */
    public Observable<List<RxLearningClass>> theoryClassify() {
        return getService().theoryClassify().compose(this.applySchedulers());
    }

    /**
     * 理论在线列表
     *
     * @param pageNo
     * @param id
     * @return
     */
    public Observable<RxOnline> theory(int pageNo, int id) {
        return getService().theory(pageNo, id).compose(this.applySchedulers());
    }

    /**
     * 理论在线列表 收藏
     *
     * @param pageNo
     * @return
     */
    public Observable<RxOnline> theoryList(int pageNo) {
        return getService().userTheory(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 获取专题学习类别
     *
     * @return
     */
    public Observable<List<RxLearningClass>> specialClassify() {
        return getService().specialClassify().compose(this.applySchedulers());
    }

    /**
     * 专题学习列表
     *
     * @param pageNo
     * @param id
     * @return
     */
    public Observable<List<RxLearningList>> special(int pageNo, int id) {
        return getService().special(pageNo, id).compose(this.applySchedulers());
    }

    /**
     * 专题学习列表 收藏
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxLearningList>> specialList(int pageNo) {
        return getService().specialList(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 考试评测表
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxTest>> testList(int pageNo) {
        return getService().testList(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 考试评测表
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxTest>> myTest(int pageNo) {
        return getService().userList(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 考试评测信息
     *
     * @param id
     * @return
     */
    public Observable<RxTestInfo> questionnaireDetails(String id) {
        return getService().questionnaireDetails(id).compose(this.applySchedulers());
    }

    /**
     * 考试评测信息
     *
     * @param id
     * @return
     */
    public Observable<RxTestDoneInfo> questionnaireDoneDetails(String id) {
        return getService().questionnaireRecord(id).compose(this.applySchedulers());
    }

    /**
     * 考试评测信息
     *
     * @param id
     * @return
     */
    public Observable<List<RxTestDetial>> questionnairecheck(String id) {
        return getService().questionnairecheck(id).compose(this.applySchedulers());
    }

    /**
     * 考试评测信息
     *
     * @param id
     * @return
     */
    public Observable<List<RxRecordDetial>> questionnaireStatistics(String id) {
        return getService().questionnaireStatistics(id).compose(this.applySchedulers());
    }

    /**
     * 提交评测
     *
     * @param id
     * @param sub
     * @param time
     * @return
     */
    public Observable<HttpResult> questionnaireSave(String id, String sub, int time) {
        return getService().questionnaireSave(id, sub, time, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 先进典范列表
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxIndexCommon>> paragonList(int pageNo) {
        return getService().userParagon(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }


    /**
     * 先进典范列表收藏
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxIndexCommon>> userParagon(int pageNo) {
        return getService().paragonList(pageNo).compose(this.applySchedulers());
    }

    /**
     * 典范详情
     *
     * @param page
     * @param id
     * @return
     */
    public Observable<RxParagonDetial> paragonDetails(int page, String id, String printurl) {
        return getService().paragonDetails(page, printurl, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 好书推荐
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxBookRecommend>> recommend(int pageNo) {
        return getService().recommend(pageNo).compose(this.applySchedulers());
    }

    /**
     * 好书推荐
     *
     * @param pageNo
     * @return
     */
    public Observable<List<RxBookRecommend>> userRecommend(int pageNo) {
        return getService().userRecommend(pageNo, MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 典范详情
     *
     * @param page
     * @param id
     * @return
     */
    public Observable<RxBookDetial> bookDetails(int page, String id) {
        return getService().recommendDetails(page, id, MyApplication.USER_ID).compose(this.applySchedulers());
    }


    /**
     * 轮播图
     *
     * @return
     */
    public Observable<List<RxAds>> advertising() {
        return getService().advertising().compose(this.applySchedulers());
    }

    /**
     * 学习值介绍/关于我们
     *
     * @return
     */
    public Observable<String> lookContent(int type) {
        return getService().lookContent(type).compose(this.applySchedulers());
    }

    /**
     * 组织架构 第一级
     *
     * @return
     */
    public Observable<List<RxStructureLevelOne>> dept1() {
        return getService().dept1().compose(this.applySchedulers());
    }

    /**
     * 组织架构 第2级
     *
     * @return
     */
    public Observable<List<RxStructureLevelTwo>> dept2(String deptId) {
        return getService().dept2(deptId).compose(this.applySchedulers());
    }

    /**
     * 组织架构 第3级
     *
     * @return
     */
    public Observable<List<RxStructurePerson>> dept3(String deptId) {
        return getService().dept3(deptId).compose(this.applySchedulers());
    }

    /**
     * 入党申请
     *
     * @return
     */
    public Observable<String> applyFor(String username, String dept, String phoen, String title, String content, String img) {
        return getService().applyFor(MyApplication.USER_ID, username, dept, phoen, title, content, img).compose(this.applySchedulers());
    }

    public Observable<String> upLoadImage(String base64) {
        return getService().upload(base64).compose(this.applySchedulers());
    }

    /**
     * 登录验证码
     *
     * @param phone
     * @return
     */
    public Observable<HttpResult> loginCode(String phone) {
        return getService().loginCode(phone).compose(this.applySchedulers());
    }


    /**
     * 注册验证码
     *
     * @param phone
     * @return
     */
    public Observable<HttpResult> registerCode(String phone) {
        return getService().registerCode(phone).compose(this.applySchedulers());
    }


    /**
     * 登录
     *
     * @param phone
     * @param code
     * @return
     */
    public Observable<RxMyUserInfo> login(String phone, String code) {
        return getService().login(phone, code).compose(this.applySchedulers());
    }

    /**
     * 登录
     *
     * @param phone
     * @return
     */
    public Observable<RxMyUserInfo> passwordLogin(String phone, String password) {
        return getService().passwordLogin(phone, password).compose(this.applySchedulers());
    }

    /**
     * 注册
     *
     * @param phone
     * @param code
     * @return
     */
    public Observable<HttpResult> userRegister(String phone, String code) {
        return getService().userRegister(phone, code).compose(this.applySchedulers());
    }

    /**
     * 验证
     *
     * @param phone
     * @param code
     * @return
     */
    public Observable<HttpResult> modifyPhoneAfter(String phone, String code) {
        return getService().modifyPhoneAfter(phone, code).compose(this.applySchedulers());
    }

    /**
     * 组织名称
     *
     * @return
     */
    public Observable<List<RxDeptName>> deptName() {
        return getService().deptName().compose(this.applySchedulers());
    }

    /**
     * 完善个人信息
     * @param phone
     * @param username
     * @param bl
     * @param deptId
     * @param position
     * @param member
     * @param joinpatryTime
     * @param password
     * @return
     */
    public Observable<HttpResult> perfect(String phone, String username, boolean bl, String deptId, String position, int member, String joinpatryTime, String password) {
        return getService().perfect(phone, username, bl, deptId, position, member, joinpatryTime, password).compose(this.applySchedulers());
    }

    /**
     * 获取个人信息
     * @return
     */
    public Observable<RxMyUserInfo> center(){
        return getService().center(MyApplication.mCache.getAsString(CacheKey.PHONE)).compose(this.applySchedulers());
    }

    /**
     * 党费列表
     * @return
     */
    public Observable<List<RxPartyFee>> partyMoney(){
        return getService().partyMoney(MyApplication.USER_ID).compose(this.applySchedulers());
    }

    /**
     * 党费列表
     * @return
     */
    public Observable<RxPartyFeeDetial> partyMoneyDetails(String id){
        return getService().partyMoneyDetails(MyApplication.USER_ID,id).compose(this.applySchedulers());
    }

    /**
     * 缴费记录
     * @return
     */
    public Observable<List<RxFeeRecord>> partyMoneyList(int no){
        return getService().partyMoneyList(MyApplication.USER_ID,no).compose(this.applySchedulers());
    }
}
