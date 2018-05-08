package com.diary.check;

import net.tsz.afinal.annotation.sqlite.Id;

import java.io.Serializable;

//import net.tsz.afinal.annotation.sqlite.Id;

/**
 * Created by Administrator on 2016/4/30 0030.
 */
public class User implements Serializable{

    @Id
    private String name;
    private String pwd;
    private int upLimit=80;
    private int daysKeeped=7;
    private int userStatus;//默认为普通用户 管理员为1

    public User() {
    }

    public User(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(int upLimit) {
        this.upLimit = upLimit;
    }

    public int getDaysKeeped() {
        return daysKeeped;
    }

    public void setDaysKeeped(int daysKeeped) {
        this.daysKeeped = daysKeeped;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        return pwd != null ? pwd.equals(user.pwd) : user.pwd == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (pwd != null ? pwd.hashCode() : 0);
        return result;
    }
}
