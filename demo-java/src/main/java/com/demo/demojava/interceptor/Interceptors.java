package com.demo.demojava.interceptor;

/**
 * @ClassName Interceptors
 * @Description
 * @Author sunjie
 * @Date 2022/8/4 11:14
 * @Version 1.0
 */

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 */
@Configuration
@AllArgsConstructor
public class Interceptors implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private PermissionInterceptor permissionInterceptor;

    @Resource
    private LogInterceptor logInterceptor;

    @Resource
    private ControllerInterceptor controllerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 拦截器按照顺序执行
         *addPathPatterns 用于添加拦截规则
         *excludePathPatterns 用于排除拦截
         */
        registry.addInterceptor(loginInterceptor);
        registry.addInterceptor(logInterceptor);
        registry.addInterceptor(permissionInterceptor);
        registry.addInterceptor(controllerInterceptor).addPathPatterns("/admin/**");
    }

}

