package org.mobile.htloginsdk.bean;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 郭君华 on 2016/1/18.
 * Email：guojunhua3369@163.com
 */
@Table(name = "login")
public class UserLogin {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "loginStats")
    private int loginStats;
    @Column(name = "isBind")
    private int isBind;
    @Column(name = "diviceId")
    private String diviceId;
    @Column(name = "brand")
    private String brand;
    @Column(name = "facebookId")
    private String facebookId;
    @Column(name = "facebookName")
    private String facebookName;
    @Column(name = "token")
    private String token;
    @Column(name = "uid")
    private String uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLoginStats() {
        return loginStats;
    }

    public void setLoginStats(int loginStats) {
        this.loginStats = loginStats;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public String getDiviceId() {
        return diviceId;
    }

    public void setDiviceId(String diviceId) {
        this.diviceId = diviceId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
