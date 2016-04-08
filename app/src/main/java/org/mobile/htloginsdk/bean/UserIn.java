package org.mobile.htloginsdk.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by 郭君华 on 2016/1/18.
 * Email：guojunhua3369@163.com
 */
@Table(name = "UserIn")
public class UserIn {
    @Id
    private int _id;
    @Column(column = "uid")
    private String uid;
    @Column(column = "name")
    private String name;
    @Column(column = "auth_name")
    private String auth_name;
    @Column(column = "bind")
    private int bind;
    @Column(column = "token")
    private String token;
    @Column(column = "divice")
    private int divice;
    @Column(column = "divice_auth_id")
    private String divice_auth_id;
    @Column(column = "divice_auth_name")
    private String divice_auth_name;
    @Column(column = "email")
    private int email;
    @Column(column = "email_auth_id")
    private String email_auth_id;
    @Column(column = "email_auth_name")
    private String email_auth_name;
    @Column(column = "facebook")
    private int facebook;
    @Column(column = "facebook_auth_id")
    private String facebook_auth_id;
    @Column(column = "facebook_auth_name")
    private String facebook_auth_name;

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

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDivice() {
        return divice;
    }

    public void setDivice(int divice) {
        this.divice = divice;
    }

    public String getDivice_auth_id() {
        return divice_auth_id;
    }

    public void setDivice_auth_id(String divice_auth_id) {
        this.divice_auth_id = divice_auth_id;
    }

    public String getDivice_auth_name() {
        return divice_auth_name;
    }

    public void setDivice_auth_name(String divice_auth_name) {
        this.divice_auth_name = divice_auth_name;
    }

    public int getEmail() {
        return email;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public String getEmail_auth_id() {
        return email_auth_id;
    }

    public void setEmail_auth_id(String email_auth_id) {
        this.email_auth_id = email_auth_id;
    }

    public String getEmail_auth_name() {
        return email_auth_name;
    }

    public void setEmail_auth_name(String email_auth_name) {
        this.email_auth_name = email_auth_name;
    }

    public int getFacebook() {
        return facebook;
    }

    public void setFacebook(int facebook) {
        this.facebook = facebook;
    }

    public String getFacebook_auth_id() {
        return facebook_auth_id;
    }

    public void setFacebook_auth_id(String facebook_auth_id) {
        this.facebook_auth_id = facebook_auth_id;
    }

    public String getFacebook_auth_name() {
        return facebook_auth_name;
    }

    public void setFacebook_auth_name(String facebook_auth_name) {
        this.facebook_auth_name = facebook_auth_name;
    }

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }

    @Override
    public String toString() {
        return "UserIn{" +
                "_id=" + _id +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", auth_name='" + auth_name + '\'' +
                ", bind=" + bind +
                ", token='" + token + '\'' +
                ", divice=" + divice +
                ", divice_auth_id='" + divice_auth_id + '\'' +
                ", divice_auth_name='" + divice_auth_name + '\'' +
                ", email=" + email +
                ", email_auth_id='" + email_auth_id + '\'' +
                ", email_auth_name='" + email_auth_name + '\'' +
                ", facebook=" + facebook +
                ", facebook_auth_id='" + facebook_auth_id + '\'' +
                ", facebook_auth_name='" + facebook_auth_name + '\'' +
                '}';
    }
}
