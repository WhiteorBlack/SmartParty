package com.qiantang.smartparty.module.index.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.fullhtml.TextViewForFullHtml;
import com.qiantang.smartparty.widget.dialog.DefaultDialog;
import com.qiantang.smartparty.widget.dialog.OnDialogExecuteListener;

/**
 * Created by zhaoyong bai on 2018/6/13.
 */
public class SpeechPop extends BaseDialog {
    private String title;
    private String content;
    private View view;
    private TextView tvTitle;
    private TextViewForFullHtml tvContent;

    public SpeechPop(Context context) {
        super(context, true);
    }

    @Override
    public View onCreateView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.view_speech_pop, null, false);
        tvContent = view.findViewById(R.id.tv_content);
        tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        AutoUtils.auto(view);
        return view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setUiBeforShow() {
        showAnim(new SlideBottomEnter());
        dismissAnim(new SlideBottomExit());
        tvTitle.setText(title);
        tvContent.loadContent(content);
    }
}
