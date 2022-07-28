package com.demo.demobase.config;/**
 * @description:
 * @author: Andy
 * @time: 2022/5/26 15:45
 */

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 *
 *@description:
 *@author: sunjie
 *@time: 2022/5/26 15:45
 *
 */
@Configuration
public class SwaggerConfig
{
    /** 是否开启swagger */
//    @Value("${swagger.enabled}")
    private boolean enabled = true;

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(enabled)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .groupName("公共")
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
//                .apis(RequestHandlerSelectors.basePackage("com.demo"))
                // 扫描所有
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo()
    {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("接口文档2")
                // 描述
                .description("接口文档2")
                // 作者信息
                .contact(new Contact("谷雨", null, null))
                // 版本
                .version("版本号:" +2)
                .build();
    }

    /**
     * 公共api
     *
     * @return
     */
//    @Bean
//    public Docket toolApi()
//    {
//        return new Docket(DocumentationType.SWAGGER_2)
//                // 是否启用Swagger
//                .enable(enabled)
//                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
//                .groupName("公共")
//                .apiInfo(apiInfo())
//                // 设置哪些接口暴露给Swagger展示
//                .select()
//                // 扫描所有有注解的api，用这种方式更灵活
////                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                // 扫描指定包中的swagger注解
//                .apis(RequestHandlerSelectors.basePackage("com.demo"))
//                // 扫描所有
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//                /* 设置安全模式，swagger可以设置访问token */
//        //.pathMapping(pathMapping);
//    }

}
