package com.demo.demo.controller.chain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProcessContext
 * @Description 责任链上下文
 * @Author sunjie
 * @Date 2022/6/23 17:02
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessContext {
    // 标识责任链的code
    private String code;
    // 存储上下文的真正载体
    private UserModel model;
    // 责任链中断的标识
    private Boolean needBreak = false;

}
