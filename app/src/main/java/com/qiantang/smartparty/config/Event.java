package com.qiantang.smartparty.config;

/**
 * 事件总线 事件编码
 */
public interface Event {
    int RELOAD = 2000;
    int GO_HOME = 2002;
    int FINISH = 2003;
    int RELOAD_WEB = 2004;
    int WEB_BACK = 2007;
    int KILL_WEB = 2010;
    int GO_MALL = 2013;
    int LOGOUT = 2021;
    int RELOAD_STUDY = 2026;

    //搜索跳转类型
    int SEARCH_NEWS=8;  //新闻快报
    int SEARCH_MEETING=6;  //会议纪要
    int SEARCH_MIEN=4;  //党建风采
    int SEARCH_STUDY_STATE=9; //学习动态
    int SEARCH_CHARACTER=5; //人物表彰
}
