package com.qiantang.smartparty.module.mine.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.BR;

/**
 * Created by zhaoyong bai on 2018/5/31.
 */
public class ModifyPhoneViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableBoolean stepNext = new ObservableBoolean(true);

    public ModifyPhoneViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    @Bindable
    public boolean getStepNext() {
        return stepNext.get();
    }

    public void setStepNext(boolean stepNext) {
        this.stepNext.set(stepNext);
        notifyPropertyChanged(BR.stepNext);
    }

    @Override
    public void destroy() {

    }
}
