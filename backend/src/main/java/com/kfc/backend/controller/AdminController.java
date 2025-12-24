package com.kfc.backend.controller;

import com.kfc.backend.common.R;
import com.kfc.backend.dto.AdminLoginDTO;
import com.kfc.backend.entity.AdminUser;
import com.kfc.backend.service.AdminUserService;
import lombok.extern.slf4j.Slf4j; // 如果你有Lombok插件可以用，没有就删掉这行和下面的@Slf4j
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 管理端控制器
 */
@RestController
@RequestMapping("/admin/employee") // 很多教程习惯用这个路径，你也可以用 /admin
// @Slf4j 
public class AdminController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public R<AdminUser> login(HttpServletRequest request, @RequestBody AdminLoginDTO adminLoginDTO) {
        // 1. 调用Service登录
        AdminUser adminUser = adminUserService.login(adminLoginDTO);

        // 2. 登录成功，将员工ID存入 Session (这是一种简单的登录态维持方式)
        //    如果你后面要用 Token/JWT，这里就换成生成 Token
        request.getSession().setAttribute("employee", adminUser.getId());

        return R.success(adminUser);
    }

    /**
     * 管理员退出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理 Session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}