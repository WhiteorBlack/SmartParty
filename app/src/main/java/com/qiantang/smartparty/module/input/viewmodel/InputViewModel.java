package com.qiantang.smartparty.module.input.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;

/**
 * Created by zhaoyong bai on 2018/5/27.
 */
public class InputViewModel extends BaseObservable implements ViewModel {
    private ObservableBoolean isPop = new ObservableBoolean(false);
    public ObservableField<String> hint = new ObservableField<>("");
    private BaseBindActivity activity;
    private ObservableField<String> textString = new ObservableField<>("");

    public InputViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Bindable
    public boolean getIsPop() {
        return isPop.get();
    }

    public void setIsPop(boolean isPop) {
        this.isPop.set(isPop);
        notifyPropertyChanged(BR.isPop);
    }

    @Bindable
    public String getHint() {
        return hint.get();
    }

    public void setHint(String hint) {
        this.hint.set(hint);
    }

    @Bindable
    public String getTextString() {
        return textString.get();
    }

    public void setTextString(String hint) {
        this.textString.set(hint);
        notifyPropertyChanged(BR.textString);
    }

    public BaseBindActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }
}
