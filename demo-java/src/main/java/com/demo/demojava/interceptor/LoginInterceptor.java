package com.demo.demojava.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.guyu.common.core.tool.api.R;
import com.guyu.common.core.tool.api.ResultCode;
import com.guyu.common.core.tool.util.RedisUtil;
import com.guyu.common.core.tool.util.ServletUtil;
import com.guyu.manage.constant.ManageConstant;
import com.guyu.manage.entity.TbMenuFunc;
import com.guyu.manage.util.LoginerUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登录拦截器
 */
@Log4j2
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private LoginerUtil loginerUtil;

    @Resource
    private RedisUtil redisUtil;

    @Value("${spring.profiles.active}")
    private String actived;

    private static final String PROPERTIES_DEV = "dev";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StrUtil.equals(actived, PROPERTIES_DEV)) {
            return true;
        }
        String path = request.getServletPath();
        // 免登录校验
        List<TbMenuFunc> menuFuncs = (List<TbMenuFunc>) redisUtil.get(ManageConstant.REDIS_KEY_NOT_LOGIN_CHECK_FUNCS);
        for (TbMenuFunc menuFunc : menuFuncs) {
            if (StrUtil.equals(menuFunc.getFuncUrl(), path)) {
                return true;
            }
        }

        // 登录验证
        String token = request.getHeader(ManageConstant.REQUEST_HEADER_KEY_LOGINER);
        String error = StrUtil.isEmpty(token) ? "未登录" : loginerUtil.get() == null ? "登录失效" : null;
        if (error != null) {
            R r = R.fail(ResultCode.CLIENT_UN_AUTHORIZED);
            ServletUtil.responseTxt(JSON.toJSONString(r));
            return false;
        }

        loginerUtil.refresh();
        return true;
    }

}
