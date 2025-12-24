package com.kfc.backend.config;

import com.kfc.backend.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 注入拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 默认拦截所有
                .excludePathPatterns(
                        // === 1. 核心放行：C端顾客接口 ===
                        // 这一行非常关键！解决了 buyVip 报 401 的问题
                        "/user/**",

                        // === 2. 管理端登录 ===
                        "/admin/employee/login",
                        "/admin/employee/logout",

                        // === 3. 静态资源与公共接口 ===
                        "/product/**",
                        "/category/**",
                        "/common/**",
                        "/shop/status",
                        "/shop/info", // 如果有店铺信息接口也建议放行

                        // === 4. Swagger 文档 ===
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources",
                        "/v3/api-docs/**"
                );
    }
}