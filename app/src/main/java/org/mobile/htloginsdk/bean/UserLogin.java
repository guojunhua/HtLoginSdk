package org.mobile.htloginsdk.bean;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 郭君华 on 2016/1/18.
 * Email：guojunhua3369@163.com
 */
@Table(name = "login")
public class UserLogin {
    @Column(name = "id")
    private int id;
    //设备id，邮箱，FacebookID username 为主键
    @Column(name = "username",isId = true)
    private String username;
    //设备名称，账号密码，Facebook名称
    @Column(name = "password")
    private String password;
    //登录状态设备为1，Facebook登录为3,邮箱登录为2
    @Column(name = "loginStats")
    private int loginStats;
    //绑定状态未绑定为1绑定为2
    @Column(name = "isBind")
    private int isBind;
    //登录返回的token
    @Column(name = "token")
    private String token;
    //登录返回的uid
    @Column(name = "uid")
    private String uid;
    //登录返回的name
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "emailPassword")
    private String emailPassword;

    public UserLogin(String username, String password, int loginStats, int isBind, String uid, String token, String name, String email, String emailPassword) {
        this.username = username;
        this.password = password;
        this.loginStats = loginStats;
        this.isBind = isBind;
        this.uid = uid;
        this.token = token;
        this.name = name;
        this.email = email;
        this.emailPassword = emailPassword;
    }

    public UserLogin() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", loginStats=" + loginStats +
                ", isBind=" + isBind +
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", emailPassword='" + emailPassword + '\'' +
                '}';
    }
}
