package com.kfc.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 旧的登录控制器
 * * 修改说明：
 * 1. 核心业务逻辑（登录、注册）已经迁移到了 UserController.java。
 * 2. 为了解决 "Ambiguous mapping" (接口冲突) 报错，删除了这里的所有 @PostMapping 方法。
 * 3. 删除了 @RequestMapping("/user") 注解，防止路径冲突。
 * 4. 必须保留 sessionMap，因为拦截器 (LoginCheckFilter) 需要引用 LoginController.sessionMap。
 */
@RestController
public class LoginController {

    // =======================================================
    // ⚠️ 严禁删除！拦截器 (Interceptor) 正在引用此变量
    // =======================================================
    public static Map<String, Object> sessionMap = new ConcurrentHashMap<>();

}