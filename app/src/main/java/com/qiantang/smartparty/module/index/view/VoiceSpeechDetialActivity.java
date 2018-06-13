package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.databinding.ActivityVoiceSpeechDetialBinding;
import com.qiantang.smartparty.databinding.ViewVoiceSpeechHeadBinding;
import com.qiantang.smartparty.module.index.popwindow.SpeechPop;
import com.qiantang.smartparty.module.index.viewmodel.VoiceSpeechDetialViewMdoel;
import com.qiantang.smartparty.module.input.viewmodel.InputViewModel;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.Player;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/5/25.
 */
public class VoiceSpeechDetialActivity extends BaseBindActivity implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, Player.TimeChangerListener {
    private ActivityVoiceSpeechDetialBinding binding;
    private ViewVoiceSpeechHeadBinding headBinding;
    private VoiceSpeechDetialViewMdoel viewMdoel;
    private CommentAdapter adapter;
    private InputViewModel inputViewModel;
    private Player player;
    private SpeechPop speechPop;

    @Override
    protected void initBind() {
        adapter = new CommentAdapter(R.layout.item_comment);
        viewMdoel = new VoiceSpeechDetialViewMdoel(this, adapter);
        inputViewModel = new InputViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voice_speech_detial);
        binding.input.setViewModel(inputViewModel);
        binding.setViewModel(viewMdoel);
        headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_voice_speech_head, null, false);
        headBinding.setViewModel(viewMdoel);
    }

    @Override
    public void initView() {
        speechPop=new SpeechPop(this);
        player = new Player(headBinding.seekbar);
        player.setTimeChangerListener(this);
        headBinding.seekbar.setOnSeekBarChangeListener(this);
        headBinding.chbPlay.setOnCheckedChangeListener(this);
        binding.toolbar.setTitle("系列讲话");
        inputViewModel.setHint("发表学习感悟...");
        viewMdoel.initData();
        initRv(binding.rv);
        viewMdoel.testData();
    }

    private void initRv(RecyclerView rv) {
        AutoUtils.auto(headBinding.getRoot());
        adapter.addHeaderView(headBinding.getRoot());
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        adapter.setOnLoadMoreListener(() -> viewMdoel.loadMore(), rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = MyApplication.heightPixels - rv.getHeight() - MyApplication.widthPixels * 9 / 16; //计算键盘占用高度
            if (heightDiff > MyApplication.heightPixels / 3) { //键盘弹出
                inputViewModel.setIsPop(true);
            } else { //键盘收起的时候判断是否有文字输入,如果有则继续展示发送按钮
                if (TextUtils.isEmpty(inputViewModel.getTextString())) {
                    inputViewModel.setIsPop(false);
                } else {
                    inputViewModel.setIsPop(true);
                }
            }
        });
    }

    public void startVideo(String url) {
        player.playUrl(url);
        viewMdoel.setTotalTime(player.mediaPlayer.getDuration());
    }

    public void setPopInfo(String title,String content){
        if (speechPop!=null){
            speechPop.setTitle(title);
            speechPop.setContent(content);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.tv_send:
                viewMdoel.comment(inputViewModel.getTextString());
                inputViewModel.setTextString("");
                closeInput();
                break;
            case R.id.iv_share:

                break;
            case R.id.iv_collect:
                viewMdoel.cancelPrase();
                break;
            case R.id.iv_uncollect:
                viewMdoel.prase();
                break;
            case R.id.ll_info:
                speechPop.show();
                break;
        }
    }

    public void updateCollect(boolean isCollect) {
        inputViewModel.setIsCollect(isCollect);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (player!=null){
//            player.play();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void viewModelDestroy() {
        player.stop();
        viewMdoel.destroy();
        inputViewModel.destroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            player.pause();
        } else {
            player.play();
        }
    }

    private int progress;
    private boolean isTracking = false;

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        this.progress = i * player.mediaPlayer.getDuration() / seekBar.getMax();
        if (isTracking) {
            viewMdoel.setPlayTime(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTracking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isTracking = false;
        player.mediaPlayer.seekTo(progress);
    }

    @Override
    public void onTimeChange(long time) {
        viewMdoel.setPlayTime(time);
    }
}
