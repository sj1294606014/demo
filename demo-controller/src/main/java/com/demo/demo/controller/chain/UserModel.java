package com.demo.demo.controller.chain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UserModel
 * @Description
 * @Author sunjie
 * @Date 2022/6/23 17:12
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    private int age;

    private String name;
}
