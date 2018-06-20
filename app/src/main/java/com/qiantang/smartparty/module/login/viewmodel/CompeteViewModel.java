package com.qiantang.smartparty.module.login.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.bigkoo.pickerview.OptionsPickerView;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.modle.HttpResult;
import com.qiantang.smartparty.modle.RxDateDay;
import com.qiantang.smartparty.modle.RxDateMonth;
import com.qiantang.smartparty.modle.RxDateYear;
import com.qiantang.smartparty.modle.RxDeptName;
import com.qiantang.smartparty.modle.RxPartyPos;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class CompeteViewModel extends BaseObservable implements ViewModel {
    private BaseBindActivity activity;
    private ObservableBoolean isBelong = new ObservableBoolean(true);
    private ObservableField<String> name = new ObservableField<>();
    private ObservableField<String> postion = new ObservableField<>();
    private ObservableField<String> org = new ObservableField<>();
    private ObservableField<String> password = new ObservableField<>();
    private ObservableField<String> isParty = new ObservableField<>();
    private ObservableField<String> date = new ObservableField<>("");
    private List<RxDeptName> deptNames = new ArrayList<>();
    private String deptId = "";
    private int posId = 3;
    private OptionsPickerView orgPickerView;
    private OptionsPickerView datePickerView;
    private OptionsPickerView partyPickerView;
    private List<RxDateYear> dateList = new ArrayList<>();
    private List<RxPartyPos> partyPos = new ArrayList<>();
    private String phone;

    public CompeteViewModel(BaseBindActivity activity) {
        this.activity = activity;
        initOrgView();
        initDateView();
        initPartyView();
        phone = activity.getIntent().getStringExtra("phone");
    }

    private void initPartyView() {
        partyPickerView = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                setIsParty(partyPos.get(options1).getName());
                posId = partyPos.get(options1).getCode();
            }
        })
                .setTitleText("身份")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        intPartyData();
    }

    private void intPartyData() {
        partyPos.add(new RxPartyPos("群众", 0));
        partyPos.add(new RxPartyPos("积极分子", 1));
        partyPos.add(new RxPartyPos("预备党员", 2));
        partyPos.add(new RxPartyPos("党员", 3));
        posId = 3;
        setIsParty("党员");
        if (partyPickerView != null) {
            partyPickerView.setPicker(partyPos);
        }
    }

    private void initDateView() {
        datePickerView = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                setDate(dateList.get(options1).getYear() + "-" + dateList.get(options1).getMonth().get(options2).getMonth() + "-" + dateList.get(options1).getMonth().get(options2).getDateDays().get(options3).getDay());
            }
        })
                .setTitleText("入党时间")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        initDateData();
    }

    //所属组织
    private void initOrgView() {
        orgPickerView = new OptionsPickerView.Builder(activity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                setOrg(deptNames.get(options1).getDept_name());
                deptId = deptNames.get(options1).getDept_id();
            }
        })
                .setTitleText("所属组织")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        getOrgData();
    }

    private int year;
    private int month;
    private int day;

    private void initDateData() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        setDate(year + "-" + month + "-" + day);
        new Thread(() -> {
            for (int i = 0; i < 80; i++) {
                year -= i;
                RxDateYear rxDateYear = new RxDateYear();
                rxDateYear.setYear(year);
                List<RxDateMonth> dateMonths = new ArrayList<>();
                int month = 0;
                if (i == 0) {
                    month = this.month;
                } else {
                    month = 12;
                }
                for (int j = 0; j < month; j++) {
                    RxDateMonth rxDateMonth = new RxDateMonth();
                    rxDateMonth.setMonth(j + 1);
                    List<RxDateDay> dateDays = new ArrayList<>();
                    int day = 0;
                    if (i == 0 && j == this.month - 1) {
                        day = this.day;
                    } else {
                        if (j == 1) {
                            //二月
                            if ((year % 100 == 0 && year % 400 == 0) || (year % 100 != 0 && year % 4 == 0)) {  //闰年
                                day = 29;
                            } else {
                                day = 28;
                            }
                        } else if (j == 0 || j == 2 || j == 4 || j == 6 || j == 7 || j == 9 || j == 11) { //1,3,5,7,8,10,12 月
                            day = 31;
                        } else {
                            day = 30;
                        }
                    }
                    for (int k = 0; k < day; k++) {
                        RxDateDay rxDateDay = new RxDateDay();
                        rxDateDay.setDay(k + 1);
                        dateDays.add(rxDateDay);
                    }
                    rxDateMonth.setDateDays(dateDays);
                    dateMonths.add(rxDateMonth);
                }
                rxDateYear.setMonth(dateMonths);
                dateList.add(rxDateYear);
            }
            List<List<RxDateMonth>> monthList = new ArrayList<>();
            List<List<List<RxDateDay>>> dayList = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                monthList.add(dateList.get(i).getMonth());
                List<List<RxDateDay>> days = new ArrayList<>();
                for (int j = 0; j < dateList.get(i).getMonth().size(); j++) {
                    days.add(dateList.get(i).getMonth().get(j).getDateDays());
                }
                dayList.add(days);
            }

            if (datePickerView != null) {
                datePickerView.setPicker(dateList, monthList, dayList);
            }
        }).start();

    }

    @Override
    public void destroy() {
        orgPickerView = null;
    }

    /**
     * 是否本单位人员
     */
    public void nation() {
        ActionSheet.createBuilder(activity, activity.getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("是本单位", "非本单位")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                setIsBelong(true);
                                break;
                            case 1:
                                setIsBelong(false);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 所属组织
     */
    public void orgPop() {
        if (deptNames.size() > 0) {
            if (orgPickerView != null) {
                orgPickerView.show();
            }
        } else {
            getOrgData();
        }
    }

    private void getOrgData() {
        ApiWrapper.getInstance().deptName()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxDeptName>>() {
                    @Override
                    public void onSuccess(List<RxDeptName> data) {
                        deptNames.clear();
                        deptNames.addAll(data);
                        setOrg(deptNames.get(0).getDept_name());
                        deptId = deptNames.get(0).getDept_id();
                        if (orgPickerView != null) {
                            orgPickerView.setPicker(deptNames);
                            orgPickerView.show();
                        }
                    }
                });
    }

    /**
     * 身份
     */
    public void idPop() {
        if (partyPickerView != null) {
            partyPickerView.show();
        }
    }

    /**
     * 入党时间
     */
    public void datePop() {
        if (datePickerView != null) {
            datePickerView.show();
        }
    }

    public void commit() {
        if (TextUtils.isEmpty(getName())) {
            ToastUtil.toast("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(getPostion())) {
            ToastUtil.toast("请输入职务");
            return;
        }
        if (TextUtils.isEmpty(getPassword())) {
            ToastUtil.toast("请输入登录密码");
            return;
        }
        ApiWrapper.getInstance().perfect(phone, getName(), getIsBelong(), deptId, getPostion(), posId, getDate(), getPassword())
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<HttpResult>() {
                    @Override
                    public void onSuccess(HttpResult data) {
                        activity.onBackPressed();
                    }
                });
    }

    @Bindable
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPostion() {
        return postion.get();
    }

    public void setPostion(String postion) {
        this.postion.set(postion);
        notifyPropertyChanged(BR.postion);
    }

    @Bindable
    public String getOrg() {
        return org.get();
    }

    public void setOrg(String org) {
        this.org.set(org);
        notifyPropertyChanged(BR.org);
    }

    @Bindable
    public String getIsParty() {
        return isParty.get();
    }

    public void setIsParty(String isParty) {
        this.isParty.set(isParty);
        notifyPropertyChanged(BR.isParty);
    }

    @Bindable
    public String getDate() {
        return date.get();
    }


    public void setDate(String date) {
        this.date.set(date);
        notifyPropertyChanged(BR.date);
    }


    @Bindable
    public String getPassword() {
        return password.get();
    }


    public void setPassword(String date) {
        this.password.set(date);
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public boolean getIsBelong() {
        return isBelong.get();
    }

    public void setIsBelong(boolean isBelong) {
        this.isBelong.set(isBelong);
        notifyPropertyChanged(BR.isBelong);
    }
}
