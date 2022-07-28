package com.demo.demo.controller.chain;

import org.springframework.stereotype.Service;

/**
 * @ClassName WhiteListProcess
 * @Description
 * @Author sunjie
 * @Date 2022/6/23 17:08
 * @Version 1.0
 */

@Service
public class WhiteListProcess implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        UserModel user = context.getModel();
        if ("3y".equals(user.getName())) {
            context.setNeedBreak(true);
        }
    }
}
