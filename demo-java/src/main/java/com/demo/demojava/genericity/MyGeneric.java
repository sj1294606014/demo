package com.demo.demojava.genericity;

/**
 * @ClassName MyGeneric
 * @Description
 * @Author sunjie
 * @Date 2022/8/2 16:22
 * @Version 1.0
 */

/**
 * 泛型类
 * 语法：类名<T>
 * T是类型占位符,表示一种引用类型，如果编写多个使用逗号隔开
 */
public class MyGeneric<T> {
    //使用泛型T
    //1.创建变量
    T t;

    //2.泛型作为方法的参数
    public void show(T t) {
        System.out.println(t);
    }

    //3.泛型作为方法的返回值
    public T getT() {
        return t;
    }

}