package com.kfc.backend.dto;

import java.io.Serializable;

/**
 * 管理员登录数据传输对象
 */
public class AdminLoginDTO implements Serializable {

    private String username;
    private String password;

    // === 手动 Getter / Setter ===

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