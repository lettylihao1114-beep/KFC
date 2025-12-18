package com.kfc.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("user")
public class User {
    private Long id;
    private String openid;   // 微信唯一ID
    private String nickname; // 昵称
    private String phone;    // 手机号
    private String avatar;   // 头像
    private Integer isVip;   // 👈 重点：0=普通，1=金卡
    private BigDecimal balance; // 余额

    // --- 手动 Getter/Setter (防报错) ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Integer getIsVip() { return isVip; }
    public void setIsVip(Integer isVip) { this.isVip = isVip; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}