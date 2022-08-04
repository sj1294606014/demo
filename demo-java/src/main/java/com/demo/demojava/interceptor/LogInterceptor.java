package com.demo.demojava.interceptor;

import com.alibaba.fastjson.JSON;
import com.guyu.common.core.tool.util.ServletUtil;
import com.guyu.manage.constant.ManageConstant;
import com.guyu.manage.service.ITbRequestLogService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 互联网收件-日志拦截器
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Resource
    private ITbRequestLogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        if (ManageConstant.LOG_EXCLUDE_URLS.contains(path)) {
            return true;
        }
        String uuid = logService.putLog(ServletUtil.getIpAddress(), path, JSON.toJSONString(request.getParameterMap()));
        request.setAttribute(ManageConstant.REQUEST_LOG_UUID_KEY, uuid);
        return true;
    }

}
