package com.demo.demojava.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuzhimin
 * @title ControllerInterceptor
 * @create 2022/5/12 19:00
 */
@Component
public class ControllerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        String override = "/override" + path;

        RequestMappingHandlerMapping mapping = SpringUtil.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            for (String pattern : mappingInfo.getPatternsCondition().getPatterns()) {
                if (StrUtil.equals(override, pattern)) {
                    request.getRequestDispatcher(override).forward(request, response);
                    return false;
                }
            }
        }

        HandlerMethod handlerMethod = handlerMethods.get(override);
        if (Objects.nonNull(handlerMethod)) {
            response.sendRedirect(request.getContextPath() + override);
            return false;
        }
        return true;
    }
}
