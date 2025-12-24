package com.kfc.backend.interceptor;

import com.kfc.backend.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component; // 👈 1. 必须导入这个
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 加上 @Component 让 Spring 管理这个类
 * 这样 WebConfig 里的 @Autowired 才能生效
 */
@Component // 👈 2. 核心修复：必须加上这个注解
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // 1. 放行 OPTIONS (解决跨域预检报错)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 强制放行 AI、错误页等
        if (requestURI.contains("/ai/") || requestURI.contains("/error")) {
            return true;
        }

        // 3. 检查 Token
        String token = request.getHeader("token");

        // 如果 Token 为空，或者 sessionMap 里没这个 Token
        if (token == null || !LoginController.sessionMap.containsKey(token)) {
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("No Permission! Please Login.");
            return false; // 拦截
        }

        return true; // 放行
    }
}