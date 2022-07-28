package com.demo.demo.controller.chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProcessTemplate
 * @Description
 * @Author sunjie
 * @Date 2022/6/23 17:03
 * @Version 1.0
 */
public class ProcessTemplate{
    private List<BusinessProcess> processList = new ArrayList<>();
    public List<BusinessProcess> getProcessList() {
        return processList;
    }
    public void setProcessList(List<BusinessProcess> processList) {
        this.processList = processList;
    }

    public void addProcess(BusinessProcess process) {
        this.processList.add(process);
    }
}
