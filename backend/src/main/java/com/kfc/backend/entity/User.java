package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
// === 1. 新增这两个关键导入 ===
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类 (合并了 账号密码登录 和 微信登录 字段)
 */
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // === 基础字段 ===

    // === 2. 核心修改：加上这个注解，解决前端 ID 精度丢失导致“用户不存在”的问题 ===
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String username; // 账号
    private String password; // 密码 (MD5加密)
    private String phone;    // 手机号
    private String sex;      // 性别 (0:女, 1:男)
    private Integer status;  // 状态 (0:禁用, 1:正常)

    // === 微信/会员字段 ===
    private String openid;   // 微信OpenID
    private String nickname; // 昵称
    private String avatar;   // 头像
    private Integer isVip;   // 0=普通，1=金卡
    private BigDecimal balance; // 余额
    private LocalDateTime vipExpireTime; // 会员过期时间

    // ==========================================
    //       以下是手动生成的 Getter 和 Setter
    // ==========================================

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getVipExpireTime() {
        return vipExpireTime;
    }

    public void setVipExpireTime(LocalDateTime vipExpireTime) {
        this.vipExpireTime = vipExpireTime;
    }
}