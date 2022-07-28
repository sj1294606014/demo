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
public class AgeProcessTwo implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        UserModel user = context.getModel();
        if(user.getAge()==20){
            System.out.println("age:"+user.getAge());
        }
    }
}
