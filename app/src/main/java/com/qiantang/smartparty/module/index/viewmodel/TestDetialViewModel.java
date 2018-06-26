package com.qiantang.smartparty.module.index.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.config.CacheKey;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxAddScore;
import com.qiantang.smartparty.modle.RxQuestion;
import com.qiantang.smartparty.modle.RxTestDetial;
import com.qiantang.smartparty.modle.RxTestSave;
import com.qiantang.smartparty.module.index.adapter.TestDetialAdapter;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.network.retrofit.RetrofitUtil;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.utils.AppUtil;
import com.qiantang.smartparty.utils.LoadingWindow;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.widget.NoScrollRecycleView;
import com.qiantang.smartparty.widget.loading.LoadingView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDetialViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private TestDetialAdapter detialAdapter;
    private ObservableField<String> countTime = new ObservableField<>();
    private int timeSeconde = 0;
    private int coastTime = 0; //答题花费的时间
    private int questionCount = 0;
    private String id;
    private List<RxTestSave> testSaves = new ArrayList<>();
    private int currentPos = 0;
    private List<RxTestDetial> questionList = new ArrayList<>();
    private ObservableInt maxProgress = new ObservableInt(10);
    private ObservableInt currentProgress = new ObservableInt(0);
    private ObservableField<String> progressTest = new ObservableField<>("");
    private ObservableField<String> buttonText = new ObservableField<>("下一题");
    private ObservableInt resultGrad = new ObservableInt(0);
    private String resultId = "";
    private ObservableBoolean isCommit = new ObservableBoolean(false);
    private LoadingWindow loadingView;
    private boolean retry;

    public TestDetialViewModel(BaseBindActivity activity, TestDetialAdapter detialAdapter) {
        this.activity = activity;
        this.detialAdapter = detialAdapter;
        initData();
    }

    private void initData() {
        retry = activity.getIntent().getBooleanExtra("retry", false);
        loadingView = new LoadingWindow(activity);
        id = activity.getIntent().getStringExtra("id");
        timeSeconde = activity.getIntent().getIntExtra("time", 0);
        questionCount = activity.getIntent().getIntExtra("count", 0);
        setMaxProgress(questionCount - 1);
        setProgressTest("1/" + questionCount);
        countDownTime();
        getData();
    }

    private void countDownTime() {
        new CountDownTimer(timeSeconde * 1000, 1000) {

            @Override
            public void onTick(long l) {
                if (getIsCommit()) {
                    return;
                }
                coastTime++;
                setCountTime("倒计时" + AppUtil.stringForTime(timeSeconde - coastTime));
            }

            @Override
            public void onFinish() {
                if (!getIsCommit()) {
                    setCountTime("测试已结束");
                    commitData();
                }
            }
        }.start();

    }

    private void getData() {
        ApiWrapper.getInstance().questionnairecheck(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxTestDetial>>() {
                    @Override
                    public void onSuccess(List<RxTestDetial> data) {
                        questionList.addAll(data);
                        detialAdapter.setNewData(questionList);
                    }
                });
    }

    @Bindable
    public String getCountTime() {
        return countTime.get();
    }

    public void setCountTime(String countTime) {
        this.countTime.set(countTime);
        notifyPropertyChanged(BR.countTime);
    }

    @Bindable
    public int getMaxProgress() {
        return maxProgress.get();
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress.set(maxProgress);
        notifyPropertyChanged(BR.maxProgress);
    }

    @Bindable
    public int getCurrentProgress() {
        return currentProgress.get();
    }

    @Bindable
    public String getProgressTest() {
        return progressTest.get();
    }

    public void setProgressTest(String progressTest) {
        this.progressTest.set(progressTest);
        notifyPropertyChanged(BR.progressTest);
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress.set(currentProgress);
        notifyPropertyChanged(BR.currentProgress);
    }

    @Bindable
    public String getButtonText() {
        return buttonText.get();
    }

    public void setButtonText(String buttonText) {
        this.buttonText.set(buttonText);
        notifyPropertyChanged(BR.buttonText);
    }

    @Bindable
    public int getResultGrad() {
        return resultGrad.get();
    }

    public void setResultGrad(int resultGrad) {
        this.resultGrad.set(resultGrad);
        notifyPropertyChanged(BR.resultGrad);
    }

    @Bindable
    public boolean getIsCommit() {
        return isCommit.get();
    }

    public void setIsCommit(boolean isCommit) {
        this.isCommit.set(isCommit);
        notifyPropertyChanged(BR.isCommit);
    }

    @Override
    public void destroy() {

    }

    public void next(NoScrollRecycleView rv) {
        if (getIsCommit()) {
            return;
        }
        List<RxQuestion> questions = questionList.get(currentPos).getOption();
        String id = "";
        int grade = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).isSelect()) {
                id = "," + questions.get(i).getOption_id();
            }
            if (questions.get(i).isSelect() && questions.get(i).isAnswer()) {
                grade += questionList.get(currentPos).getGrade();
            }
        }
        if (TextUtils.isEmpty(id)) {
            ToastUtil.toast("请选择答案");
            return;
        }
        id = id.substring(1);
        RxTestSave rxTestSave = new RxTestSave();
        rxTestSave.setGrade(grade);
        rxTestSave.setOption(id);
        rxTestSave.setQuestionnaire_id(questionList.get(currentPos).getSubject_id());
        testSaves.add(rxTestSave);
        currentPos++;
        if (currentPos == questionList.size()) {
            //最后一题
            commitData();
            return;
        }
        if (currentPos == questionList.size() - 1) {
            setButtonText("提交");
        }
        setProgressTest(currentPos + 1 + "/" + questionCount);
        setCurrentProgress(currentPos);
        rv.scrollToPosition(currentPos);
    }

    private void commitData() {
        loadingView.showWindow();
        String object = new Gson().toJson(testSaves);
        ApiWrapper.getInstance().questionnaireSave(id, object, coastTime)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(() -> loadingView.hidWindow())
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onFail(RetrofitUtil.APIException e) {
                        super.onFail(e);
                        setIsCommit(false);
                        ToastUtil.toast(e.getMessage());
                    }

                    @Override
                    public void onSuccess(HttpResult data) {
                        setIsCommit(true);
                        detialAdapter.setNewData(null);
                        resultId = data.getUserquestionnaire_id();
                        setResultGrad(data.getGrade());
                        setCountTime("考试评测");
//                        if (!retry) {
//                            EventBus.getDefault().post(new RxAddScore(CacheKey.TEST, coastTime * 1000, id));
//                        }
                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                ActivityUtil.startTestRecordActivity(activity, resultId);
                break;
        }
    }
}
