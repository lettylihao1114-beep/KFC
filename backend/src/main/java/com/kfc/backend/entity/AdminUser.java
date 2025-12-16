package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("admin_user")
public class AdminUser {
    private Long id;
    private String username;
    private String password;

    // ⚡️ 必做：右键 -> Generate -> Getter and Setter -> 全选 -> OK

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}