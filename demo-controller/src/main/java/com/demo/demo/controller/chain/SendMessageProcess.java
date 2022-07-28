package com.demo.demo.controller.chain;

import org.springframework.stereotype.Service;

/**
 * @ClassName SendMessageProcess
 * @Description
 * @Author sunjie
 * @Date 2022/6/23 17:08
 * @Version 1.0
 */
@Service
public class SendMessageProcess implements BusinessProcess{

    @Override
    public void process(ProcessContext context) {
        UserModel user = (UserModel) context.getModel();
        System.out.println("给"+user.getName()+"发消息");
    }

}

