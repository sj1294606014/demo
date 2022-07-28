package com.demo.demo.controller;/**
 * @description:
 * @author: Andy
 * @time: 2022/5/27 9:43
 */

import com.demo.demo.controller.chain.*;
import com.demo.demo.controller.utils.FileUtils;
import com.demo.demobase.ReentrantLock.SynchronizedByKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: sunjie
 * @time: 2022/5/27 9:43
 */
@Api(tags = "controller接口")
@RestController
@RequestMapping("/controller")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class demoController {
    private final ProcessChain processChain;
    // 不同的code 对应不同的责任链
    private final WhiteListProcess whiteListProcess;
    private final SendMessageProcess sendMessageProcess;
    private final AgeProcessOne ageProcessOne;
    private final AgeProcessTwo  ageProcessTwo;
    private final AgeProcessThree ageProcessThree;
    private final SynchronizedByKey synchronizedByKey;


    @ApiOperation("demo")
    @PostMapping("demo")
    public String test(String str) {
        return str;
    }

    @ApiOperation("a")
    @GetMapping("a")
    public String testa() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        Integer test = 3;
        System.out.println(test);
        test=null;
        System.out.println(test);
        returnString("asd");
        return "index";
    }
    public String returnString(String s){
        return s+"123";
    }


    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public void send(Integer age) {
        processChain.addProcess("sendMessage", whiteListProcess)
                    .addProcess("sendMessage", sendMessageProcess)
                    .addProcess("age",ageProcessOne)
                    .addProcess("age",ageProcessTwo)
                    .addProcess("age",ageProcessThree)
                    .process(ProcessContext.builder()
                        .code("age")
                        .model(UserModel.builder()
                                .age(age)
                                .name("张三")
                                .build())
                        .build());
    }

    @RequestMapping(value = "/lock", method = RequestMethod.GET)
    public String send(String name) {
        String md5 = FileUtils.md5HashCode32(name);
        String fileName = md5 + ".text";
        File templateFile = new File(System.getProperty("user.dir") + File.separator + "TextTest", fileName);
        synchronizedByKey.exec(md5, () -> {
            if (!templateFile.exists()) {
                //用于存储html字符串
                try {
                    FileWriter writer = new FileWriter(templateFile);
                    writer.write(name);
                    System.out.println("生成text"+templateFile.getAbsolutePath());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        return md5;


    }


}
