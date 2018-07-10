package com.qiantang.smartparty.modle;

public class AppVersionJSON {

    /**
     * {
     * "status": 0,
     * "msg": "有新的版本请更新",
     * -"data": {
     * "conUpdate": 1, -----------是否强制更新 0不强制 1强制
     * "versionId": "1.0.2", ------版本号
     * "versionExplain": "新增分享页面",-------更新说明
     * "versionUrl": "www.baiud.com" --------下载链接
     * }
     * }
     */


    private String status;
    private ReturnObjectBean data;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ReturnObjectBean getData() {
        return data;
    }

    public void setData(ReturnObjectBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ReturnObjectBean {
        private String versionExplain;
        private String versionId;
        private String versionUrl;
        private int versionCode;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        private int conUpdate;

        public String getVersionExplain() {
            return versionExplain;
        }

        public void setVersionExplain(String versionExplain) {
            this.versionExplain = versionExplain;
        }

        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public void setVersionUrl(String versionUrl) {
            this.versionUrl = versionUrl;
        }

        public int getConUpdate() {
            return conUpdate;
        }

        public void setConUpdate(int conUpdate) {
            this.conUpdate = conUpdate;
        }
    }
}
