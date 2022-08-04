//package com.demo.demojava.interceptor;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.guyu.manage.constant.ManageConstant;
//import com.guyu.manage.service.ITbRequestLogService;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 请求响应处理
// *
// * @author liuzhimin
// * @version 1.0
// * @date 2021/7/28 0028 10:31
// */
////@ControllerAdvice
////public class LocalResponseBodyAdvice implements ResponseBodyAdvice {
////
////    @Resource
////    ITbRequestLogService logService;
////
////    @Override
////    public boolean supports(MethodParameter methodParameter, Class aClass) {
////        return true;
////    }
////
////    @Override
////    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
////        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
////        HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
////        String uuid = (String) httpServletRequest.getAttribute(ManageConstant.REQUEST_LOG_UUID_KEY);
////        if (StrUtil.isEmpty(uuid)) {
////            return o;
////        }
////        String token = httpServletRequest.getHeader(ManageConstant.REQUEST_HEADER_KEY_LOGINER);
////        String controlName = methodParameter.getExecutable().getDeclaringClass().getName();
////        controlName += "." + methodParameter.getMethod().getName();
////        logService.asyncInsert(token, uuid, controlName, JSON.toJSONString(o));
////        return o;
////    }
////
////}
