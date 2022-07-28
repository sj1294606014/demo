package com.demo.demoweb.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName testController
 * @Description
 * @Author sunjie
 * @Date 2022/6/17 9:58
 * @Version 1.0
 */
@Api(tags = "controller接口")
@Controller
@RequestMapping("/test-controller")
public class testController {

    @ApiOperationSupport(author = "孙杰")
    @ApiOperation(value = "controller",notes = "controller")
    @GetMapping("/test")
    public String test( ){
        return  "a";
    }
}
