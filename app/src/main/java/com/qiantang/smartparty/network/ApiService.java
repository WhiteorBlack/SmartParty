package com.qiantang.smartparty.network;


import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxActivity;
import com.qiantang.smartparty.modle.RxActivityDetial;
import com.qiantang.smartparty.modle.RxAssientHome;
import com.qiantang.smartparty.modle.RxCharacterDetial;
import com.qiantang.smartparty.modle.RxIndexCommon;
import com.qiantang.smartparty.modle.RxMsg;
import com.qiantang.smartparty.modle.RxMyStudy;
import com.qiantang.smartparty.modle.RxRankBranch;
import com.qiantang.smartparty.modle.RxRankPersonal;
import com.qiantang.smartparty.modle.RxSignList;
import com.qiantang.smartparty.modle.RxStudy;
import com.qiantang.smartparty.modle.RxStudyUnreadMsg;
import com.qiantang.smartparty.modle.RxThinkDetial;
import com.qiantang.smartparty.modle.RxUploadUrl;

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
}
