package me.leefeng.rxjava.login;

import cn.bmob.v3.BmobObject;

/**
 * Created by limxing on 2016/11/15.
 */

public class User extends BmobObject {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
}
