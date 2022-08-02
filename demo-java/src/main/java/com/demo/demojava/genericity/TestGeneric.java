package com.demo.demojava.genericity;

import lombok.Data;

/**
 * @ClassName genericity
 * @Description
 * @Author sunjie
 * @Date 2022/8/2 16:19
 * @Version 1.0
 */
@Data
public class TestGeneric {

    public static void main(String[] args) {
        //使用泛型创建对象
        //注意：1.泛型只能使用引用类型  2.不用泛型对象不能相互赋值
        MyGeneric<String> myGeneric = new MyGeneric<String>();
        myGeneric.t = "hello";
        myGeneric.show("大家好！我是XXX");
        String string = myGeneric.getT();

        MyGeneric<Integer> myGeneric2 = new MyGeneric<Integer>();
        myGeneric2.t = 200;
        myGeneric2.show(200);
        Integer integer = myGeneric2.getT();


        //泛型方法可以不给具体的返回值 根据赋给的值 判断是什么类型
        MyGenericMethod mfm=new MyGenericMethod();
        mfm.show("1231");
        mfm.show(3.14);
        mfm.show('c');


    }


}
