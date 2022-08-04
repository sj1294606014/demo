//package com.demo.demojava.interceptor;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.guyu.common.core.tool.api.R;
//import com.guyu.common.core.tool.api.ResultCode;
//import com.guyu.common.core.tool.util.RedisUtil;
//import com.guyu.common.core.tool.util.ServletUtil;
//import com.guyu.manage.constant.ManageConstant;
//import com.guyu.manage.entity.TbMenuFunc;
//import com.guyu.manage.entity.TbUser;
//import com.guyu.manage.util.LoginerUtil;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
///**
// * 权限控制拦截器
// */
//@Component
//public class PermissionInterceptor implements HandlerInterceptor {
//
//    @Resource
//    LoginerUtil loginerUtil;
//
//    @Resource
//    RedisUtil redisUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String path = request.getServletPath();
//
//        if (true) {
//            return true;
//        }
//        // 未登录不进行权限校验
//        String token = request.getHeader(ManageConstant.REQUEST_HEADER_KEY_LOGINER);
//        if (StrUtil.isEmpty(token)) {
//            return true;
//        }
//
//        // 免权限校验
//        List<TbMenuFunc> menuFuncs = (List<TbMenuFunc>) redisUtil.get(ManageConstant.REDIS_KEY_NOT_AUTH_CHECK_FUNCS);
//        for (TbMenuFunc menuFunc : menuFuncs) {
//            if (StrUtil.equals(menuFunc.getFuncUrl(), path)) {
//                return true;
//            }
//        }
//
//        // 登录失效不进行权限校验
//        TbUser loginer = loginerUtil.get();
//        if (loginer == null) {
//            return true;
//        }
//
//        if (loginer.getAuths().contains(path)) {
//            return true;
//        }
//        R r = R.fail(ResultCode.UN_AUTHORIZED);
//        ServletUtil.responseTxt(JSON.toJSONString(r));
//        return false;
//    }
//
//}
