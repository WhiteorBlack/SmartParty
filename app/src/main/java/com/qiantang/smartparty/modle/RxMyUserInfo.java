package com.qiantang.smartparty.modle;


public class RxMyUserInfo {
    private int pageNum;
    private int pageSize;
    private String id;
    private String userId;
    private String avatar;
    private String username;
    private String password;
    private String phone;
    private String deptId;
    private String createTime;
    private String createBy;
    private String updateTime;
    private int cost;
    private String position;
    private int memeber;
    private String openId;
    private int status;
    private String collect;
    private String parentId;
    private String joinpatryTime;
    private String nation;
    private int education;
    private String college;
    private Dept dept;
    private int counts;
    private int learningability;
    private int sta;

    public int getSta() {
        return sta;
    }

    public void setSta(int sta) {
        this.sta = sta;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getLearningability() {
        return learningability;
    }

    public void setLearningability(int learningability) {
        this.learningability = learningability;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getMemeber() {
        return memeber;
    }

    public void setMemeber(int memeber) {
        this.memeber = memeber;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getJoinpatryTime() {
        return joinpatryTime;
    }

    public void setJoinpatryTime(String joinpatryTime) {
        this.joinpatryTime = joinpatryTime;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public static class Dept {
        private String deptId;
        private String deptName;
        private int leader;
        private String phone;
        private String email;
        private int status;

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getLeader() {
            return leader;
        }

        public void setLeader(int leader) {
            this.leader = leader;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    @Override
    public String toString() {
        return "{\"phone\":\"" + getPhone()
                + "\",\"deptName\":" + getDept().getDeptName()
                + ",\"position\":\"" + getPosition()
                + "\",\"deptId\":\"" + getDept().getDeptId()
                + "\",\"memeber\":" + getMemeber()
                + ",\"cost\":\"" + getCost()
                + "\",\"status\":\"" + getStatus()
                + "\",\"userId\":" + getUserId()
                + "\",\"username\":\"" + getUsername() + "\"}";
    }
}
