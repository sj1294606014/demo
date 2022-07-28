package com.demo.demo.controller.chain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName ProcessChain
 * @Description 责任链的流程控制器（整个责任链的执行流程通用控制）
 * @Author sunjie
 * @Date 2022/6/23 17:04
 * @Version 1.0
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessChain {
    private  Map<String, ProcessTemplate> templateConfig = new HashMap<>();
    private static ProcessChain processChain = new ProcessChain();

    public void process(ProcessContext context) {
        //根据上下文的Code 执行不同的责任链
        String businessCode = context.getCode();
        ProcessTemplate processTemplate = templateConfig.get(businessCode);
        List<BusinessProcess> actionList = processTemplate.getProcessList();
        //遍历某个责任链的流程节点
        for (BusinessProcess action : actionList) {
            try {
                action.process(context);
                if (context.getNeedBreak()) {
                    break;
                }
            } catch (Exception e2) {
                //...
            }
        }
    }


    public ProcessChain addProcess(String code, BusinessProcess businessProcess) {
        ProcessTemplate template = templateConfig.get(code);

        if(Objects.isNull(template)) {
            ProcessTemplate template2 = new ProcessTemplate();
            if(!template2.getProcessList().contains(businessProcess)){
                template2.addProcess(businessProcess);
                templateConfig.put(code, template2);
            }
        }else {
            if(!template.getProcessList().contains(businessProcess)){
                template.addProcess(businessProcess);
            }
        }
        return this;
    }


}
