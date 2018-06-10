package com.qiantang.smartparty.network;


import com.qiantang.smartparty.config.Config;

/**
 * 用于HMTL 地址管理
 */
public class URLs {
    public static final String RESPONSE_OK = "0";
    public static final int PAGE_SIZE = 8;
    //版本检测
    public static final String GET_VERSION = Config.SERVER_HOST + "";
    public static final String NOTICE_DETIAL=Config.SERVER_HOST+"app/partyBuild/shareTZ?noticeId=";
}

