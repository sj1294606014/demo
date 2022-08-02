package com.demo.demojava.genericity;

/**
 * @ClassName MyGenericMethod
 * @Description
 * @Author sunjie
 * @Date 2022/8/2 16:23
 * @Version 1.0
 */

/**
 * 泛型方法
 * 语法： <T> 返回值类型  就是在返回值的前面+ <T>
 */
public class MyGenericMethod {

    //泛型方法

    public <T>void show(T t){
        System.out.println("泛型方法"+t);
    }

}
