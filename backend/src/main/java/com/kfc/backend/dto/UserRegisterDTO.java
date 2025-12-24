package com.kfc.backend.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserRegisterDTO implements Serializable {
    private String username;
    private String password;
    private String phone; // 选填，看你数据库有没有
    private String sex;   // 选填，看你数据库有没有
}