package com.qiantang.smartparty.network;


import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxActivity;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxAds;
import com.qiantang.smartparty.modle.RxAdviseRecord;
import com.qiantang.smartparty.modle.RxAssientHome;
import com.qiantang.smartparty.modle.RxBookDetial;
import com.qiantang.smartparty.modle.RxBookRecommend;
import com.qiantang.smartparty.modle.RxCharacterDetial;
import com.qiantang.smartparty.modle.RxIndex;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.modle.RxIndexSpeak;
import com.qiantang.smartparty.modle.RxLearningClass;
import com.qiantang.smartparty.modle.RxLearningList;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.modle.RxMyStudy;
import com.qiantang.smartparty.modle.RxOnline;
import com.qiantang.smartparty.modle.RxParagonDetial;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {
    //多文件流上传
    @Multipart
    @POST("alibaba/oss/appUploadImages.do")
    Observable<HttpResult<List<RxUploadUrl>>> upload(@PartMap Map<String, RequestBody> params);

    //多文件流上传
    @Multipart
    @POST("alibaba/oss/appUploadImages.do")
    Observable<HttpResult<List<RxUploadUrl>>> upload(@Part MultipartBody.Part file);

    //个人排行
    @FormUrlEncoded
    @POST("app/learningability/peopleRanking")
    Observable<HttpResult<RxRankPersonal>> getRankPersonal(@Field("userId") String userId,
                                                           @Field("time") String time);

    //支部排行
    @FormUrlEncoded
    @POST("app/learningability/branchRanking")
    Observable<HttpResult<RxRankBranch>> getRankBranch(@Field("userId") String userId,
                                                       @Field("time") String time);

    //学习感悟
    @FormUrlEncoded
    @POST("app/comment/commenList")
    Observable<HttpResult<RxStudy>> getStudyList(@Field("userId") String userId,
                                                 @Field("pageNum") int pageNum);

    //我的学习感悟
    @FormUrlEncoded
    @POST("app/comment/myCommentAppList")
    Observable<HttpResult<RxMyStudy>> getMyStudyList(@Field("userId") String userId,
                                                     @Field("pageNum") int pageNum);

    //删除学习感悟
    @FormUrlEncoded
    @POST("app/comment/deleteCommentApp")
    Observable<HttpResult<String>> deleteComment(@Field("commentId") String commentId);

    //学习感悟未读消息
    @FormUrlEncoded
    @POST("app/comment/unLookMessage")
    Observable<HttpResult<List<RxStudyUnreadMsg>>> getUnreadMsg(@Field("userId") String userId);

    //点赞 评论
    @FormUrlEncoded
    @POST("app/comment/zanCommentAppOp")
    Observable<HttpResult<String>> commentLike(@Field("userId") String userId,
                                               @Field("type") int type,
                                               @Field("commentId") String commentId,
                                               @Field("content") String content);

    //取消赞
    @FormUrlEncoded
    @POST("app/comment/deleteZanApp")
    Observable<HttpResult<String>> cancelLike(@Field("userId") String userId,
                                              @Field("commentId") String commentId);

    //发表感想
    @FormUrlEncoded
    @POST("app/comment/addCommentApp")
    Observable<HttpResult<String>> addCommentApp(@Field("userId") String userId,
                                                 @Field("content") String content,
                                                 @Field("image") String image);

    //党建助手首页
    @POST("app/partyBuild/homePage")
    Observable<HttpResult<RxAssientHome>> assientHome();

    @FormUrlEncoded
    //消息列表
    @POST("app/partyBuild/tzNotice")
    Observable<HttpResult<List<RxMsg>>> tzNotice(@Field("pageNum") int page);

    @FormUrlEncoded
    //党建风采
    @POST("app/partyBuild/fcNotice")
    Observable<HttpResult<List<RxIndexCommon>>> fcNotice(@Field("pageNum") int page,
                                                         @Field("type") int type);

    @FormUrlEncoded
    //党建活动详情
    @POST("app/partyBuild/djActivityDetails")
    Observable<HttpResult<RxActivityDetial>> djActivityDetails(@Field("pageNum") int page,
                                                               @Field("activityId") String activityId,
                                                               @Field("userId") String userId);

    @FormUrlEncoded
    //党建活动
    @POST("app/partyBuild/djActivity")
    Observable<HttpResult<List<RxActivity>>> djActivity(@Field("pageNum") int page);

    //评论
    @FormUrlEncoded
    @POST("app/partyBuild/comment")
    Observable<HttpResult<String>> comment(@Field("contentId") String contentId,
                                           @Field("content") String content,
                                           @Field("userId") String userId);

    //活动报名
    @FormUrlEncoded
    @POST("app/partyBuild/enroll")
    Observable<HttpResult<String>> enroll(@Field("activityNum") String activityNum,
                                          @Field("userId") String userId);

    //活动报名
    @FormUrlEncoded
    @POST("app/partyBuild/tzSign")
    Observable<HttpResult<List<RxSignList>>> tzSign(@Field("noticeId") String noticeId,
                                                    @Field("pageNum") int pageNum);

    //党建风采详情
    @FormUrlEncoded
    @POST("app/partyBuild/fcNoticeDetails")
    Observable<HttpResult<RxActivityDetial>> fcNoticeDetails(@Field("pageNum") int page,
                                                             @Field("contentId") String contentId,
                                                             @Field("userId") String userId);

    //人物表彰详情
    @FormUrlEncoded
    @POST("app/partyBuild/rwNoticeDetails")
    Observable<HttpResult<RxCharacterDetial>> rwNoticeDetails(@Field("pageNum") int page,
                                                              @Field("printurl") String printurl,
                                                              @Field("contentId") String contentId,
                                                              @Field("userId") String userId);

    //思想汇报列表
    @FormUrlEncoded
    @POST("app/partyBuild/thinking")
    Observable<HttpResult<List<RxIndexCommon>>> thinking(@Field("pageNum") int page,
                                                         @Field("type") int type,
                                                         @Field("userId") String userId);

    //思想汇报详情
    @FormUrlEncoded
    @POST("app/partyBuild/thinkingDetails")
    Observable<HttpResult<RxThinkDetial>> thinkingDetails(@Field("contentId") String contentId);

    //思想汇报发表
    @FormUrlEncoded
    @POST("app/partyBuild/insertThinking")
    Observable<HttpResult<String>> insertThinking(@Field("title") String title,
                                                  @Field("content") String content,
                                                  @Field("userId") String userId);

    //反馈发表
    @FormUrlEncoded
    @POST("app/partyBuild/insertIdea")
    Observable<HttpResult<String>> insertIdea(@Field("content") String content,
                                              @Field("userId") String userId);

    //反馈列表
    @FormUrlEncoded
    @POST("app/partyBuild/ideaList")
    Observable<HttpResult<List<RxAdviseRecord>>> ideaList(@Field("pageNum") int page,
                                                          @Field("userId") String userId);

    //反馈列表
    @POST("app/home/ShowHomePage")
    Observable<HttpResult<List<RxIndex>>> ShowHomePage();

    //视频学习列表
    @FormUrlEncoded
    @POST("app/video/list")
    Observable<HttpResult<List<RxVideoStudy>>> videoList(@Field("pageNum") int page);

    //视频学习详情
    @FormUrlEncoded
    @POST("app/video/details")
    Observable<HttpResult<RxVideoDetial>> videoDetails(@Field("pageNum") int page,
                                                       @Field("videoId") String videoId,
                                                       @Field("userId") String userId);

    //系列讲话详情
    @FormUrlEncoded
    @POST("app/speak/details")
    Observable<HttpResult<RxSpeechDetial>> speechDetails(@Field("pageNum") int page,
                                                         @Field("speakId") String videoId,
                                                         @Field("userId") String userId);

    //视频学习点赞
    @FormUrlEncoded
    @POST("app/video/like")
    Observable<HttpResult<String>> videoLike(@Field("userId") String userId,
                                             @Field("comment_id") String comment_id);

    //视频学习点赞取消
    @FormUrlEncoded
    @POST("app/video/remove")
    Observable<HttpResult<String>> removeVideoLike(@Field("userId") String userId,
                                                   @Field("comment_id") String comment_id);


    //收藏
    @FormUrlEncoded
    @POST("app/questionnaire/collectSave")
    Observable<HttpResult<String>> collectSave(@Field("type") int type,
                                               @Field("userId") String userId,
                                               @Field("contentId") String contentId);

    //取消收藏
    @FormUrlEncoded
    @POST("app/questionnaire/collectAbolish")
    Observable<HttpResult<String>> collectAbolish(@Field("type") int type,
                                                  @Field("userId") String userId,
                                                  @Field("contentId") String contentId);

    //视频学习评论
    @FormUrlEncoded
    @POST("app/video/comment")
    Observable<HttpResult<String>> commentVideo(@Field("essay_id") String essay_id,
                                                @Field("content") String content,
                                                @Field("userId") String userId);

    //视频学习列表
    @FormUrlEncoded
    @POST("app/speak/list")
    Observable<HttpResult<List<RxIndexSpeak>>> speakList(@Field("pageNum") int page);

    //专题学习类别
    @POST("app/content/specialClassify")
    Observable<HttpResult<List<RxLearningClass>>> specialClassify();

    //理论在线类别
    @POST("app/content/theoryClassify")
    Observable<HttpResult<List<RxLearningClass>>> theoryClassify();

    //专题学习列表
    @FormUrlEncoded
    @POST("app/content/theory")
    Observable<HttpResult<RxOnline>> theory(@Field("pageNum") int pageNum,
                                            @Field("classifyId") int classifyId);

    //专题学习列表
    @FormUrlEncoded
    @POST("app/content/special")
    Observable<HttpResult<List<RxLearningList>>> special(@Field("pageNum") int pageNum,
                                                         @Field("classifyId") int classifyId);

    //评测列表
    @FormUrlEncoded
    @POST("app/questionnaire/list")
    Observable<HttpResult<List<RxTest>>> testList(@Field("pageNum") int pageNum,
                                                  @Field("userId") String userId);

    //评测详情
    @FormUrlEncoded
    @POST("app/questionnaire/questionnaireDetails")
    Observable<HttpResult<RxTestInfo>> questionnaireDetails(@Field("questionnaire_id") String questionnaire_id);

    //评测详情
    @FormUrlEncoded
    @POST("app/questionnaire/questionnaireRecord")
    Observable<HttpResult<RxTestDoneInfo>> questionnaireRecord(@Field("userquestionnaire_id") String userquestionnaire_id);

    //评测详情
    @FormUrlEncoded
    @POST("app/questionnaire/questionnairecheck")
    Observable<HttpResult<List<RxTestDetial>>> questionnairecheck(@Field("questionnaire_id") String questionnaire_id);

    //答题记录
    @FormUrlEncoded
    @POST("app/questionnaire/questionnaireStatistics")
    Observable<HttpResult<List<RxRecordDetial>>> questionnaireStatistics(@Field("userquestionnaire_id") String userquestionnaire_id);

    //评测详情
    @FormUrlEncoded
    @POST("app/questionnaire/questionnaireSave")
    Observable<HttpResult<HttpResult>> questionnaireSave(@Field("questionnaire_id") String questionnaire_id,
                                                         @Field("subject") String subject,
                                                         @Field("quizTime") int quizTime,
                                                         @Field("userId") String userId);

    //先进典范
    @FormUrlEncoded
    @POST("app/content/paragon")
    Observable<HttpResult<List<RxIndexCommon>>> paragonList(@Field("pageNum") int pageNum);

    //先进典范详情
    @FormUrlEncoded
    @POST("app/content/paragonDetails")
    Observable<HttpResult<RxParagonDetial>> paragonDetails(@Field("pageNum") int page,
                                                           @Field("printurl") String printurl,
                                                           @Field("contentId") String contentId,
                                                           @Field("userId") String userId);

    //好书推荐
    @FormUrlEncoded
    @POST("app/content/recommend")
    Observable<HttpResult<List<RxBookRecommend>>> recommend(@Field("pageNum") int pageNum);

    //好书推荐详情
    @FormUrlEncoded
    @POST("app/content/recommendDetails")
    Observable<HttpResult<RxBookDetial>> recommendDetails(@Field("pageNum") int page,
                                                          @Field("contentId") String contentId,
                                                          @Field("userId") String userId);

    //轮播图
    @POST("app/agreement/advertising")
    Observable<HttpResult<List<RxAds>>> advertising();

    //学习值介绍/关于我们
    @FormUrlEncoded
    @POST("app/agreement/lookContent")
    Observable<HttpResult<String>> lookContent(@Field("type") int type);

    //组织架构 第一级
    @POST("app/user/dept1")
    Observable<HttpResult<List<RxStructureLevelOne>>> dept1();

    //组织架构 第e二级
    @FormUrlEncoded
    @POST("app/user/dept2")
    Observable<HttpResult<List<RxStructureLevelTwo>>> dept2(@Field("deptId") String deptId);

    //组织架构 第三级
    @FormUrlEncoded
    @POST("app/user/dept3")
    Observable<HttpResult<List<RxStructurePerson>>> dept3(@Field("deptId") String deptId);
}
