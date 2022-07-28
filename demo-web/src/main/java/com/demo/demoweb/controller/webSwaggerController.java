package com.demo.demoweb.controller;/**
 * @description:
 * @author: Andy
 * @time: 2022/5/26 15:42
 */

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 *
 *
 *@description:
 *@author: sunjie
 *@time: 2022/5/26 15:42
 *
 */
@Api(tags = "Swagger接口")
@RestController
@RequestMapping("/swagger")
public class webSwaggerController {

    @ApiOperationSupport(author = "孙杰")
    @ApiOperation(value = "第一个TEST",notes = "第一个TEST")
    @PostMapping("test")
    public String test(String str){
        return  str;
    }
}
