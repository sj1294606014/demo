文章目录
一、泛型类用法
二、泛型方法用法
三、泛型通配符 <?>
四、泛型安全检查
五、完整代码示例
1、泛型类 / 方法
2、main 函数




一、泛型类用法

泛型类用法 : 使用时先声明泛型 , 如果不声明泛型 , 则表示该类的泛型是 Object 类型 ;


泛型类代码 :

public class Student<T> {

    private String name;
    private int age;
    /**
     * 该数据的类型未知
     *  使用泛型表示 , 运行时确定该类型
     */
    private T data;

    public Student(String name, int age, T data) {
        this.name = name;
        this.age = age;
        this.data = data;
    }
}

指明泛型类型 : 指定 泛型类 的泛型为 String 类型 , 那么在该类中凡是使用到 T 类型的位置 , 必须是 String 类型 , 泛型类的 泛型声明 , 使用时在 类名后面 声明 ;

        // 指定 泛型类 的泛型为 String 类型
        //      那么在该类中凡是使用到 T 类型的位置 , 必须是 String 类型
        //      泛型类的 泛型声明 , 使用时在 类名后面 声明
        Student<String> student = new Student("Tom", 16, "Cat");
        String data = student.getData();

不指明泛型类型 : 如果不 指明泛型类型 , 则 泛型类型 默认为 Object 类型 ; 如下面的示例 , 获取的 泛型类型的变量也是 Object 类型 , 需要强转为 String 类型 ;

        // 如果不 指明泛型类型
        //      则 泛型类型 默认为 Object 类型
        Student student1 = new Student("Tom", 16, "Cat");
        // 获取的 泛型类型的变量也是 Object 类型 , 需要强转为 String
        String data1 = (String) student1.getData();



二、泛型方法用法

泛型方法 : 给下面的泛型方法 , 传入 String 作为参数 , 则 泛型方法中的 T 的类型就是 String 类型 ;

    public <T, A> T getData2(T arg){
        T data = arg;
        return data;
    }

指定泛型的方法 : 指定 泛型方法 的泛型类 , 泛型方法 的泛型声明 , 在调用时 方法名的前面 声明 ; 这种用法很少见 ;

        // 指定 泛型方法 的泛型类
        //      泛型方法 的泛型声明 , 在调用时 方法名的前面 声明
        student.<String, Integer>getData2("Mouse");

不指定泛型的方法 : 泛型方法 中 也可以不声明泛型类型 , 传入的参数是 泛型 T 类型 , 如果给传入参数设置 String , 隐含将泛型 T 设置为 String 类型 ;

        // 泛型方法 中 也可以不声明泛型类型
        //      传入的参数是 泛型 T 类型
        //      如果给传入参数设置 String , 隐含将泛型 T 设置为 String 类型
        String data2 = student.getData2("Mouse");


三、泛型通配符 <?>

如果现在的泛型类型不确定 , 则使用 ? 作为通配符 , 该用法与将泛型类型指定为 Object 类型用法相同 ;

? 通配符用法示例 :

        // 使用 <?> 通配符作为泛型
        //      默认将泛型指定为 Object 类型
        Student<?> student2 = new Student("Tom", 16, "Cat");
        String data2 = (String) student1.getData();

上述 ? 通配符用法等效于下面的不指定泛型的用法 :

        // 如果不 指明泛型类型
        //      则 泛型类型 默认为 Object 类型
        Student student1 = new Student("Tom", 16, "Cat");
        // 获取的 泛型类型的变量也是 Object 类型 , 需要强转为 String
        String data1 = (String) student1.getData();

四、泛型安全检查

注意下面 2 22 种泛型的用法 , 推荐使用第一种方法 ;

        // 泛型的安全检查
        // 推荐写法
        Student<String> student3 = new Student<>("Tom", 16, "Cat");
        // 不推荐写法
        Student student4 = new Student<String>("Tom", 16, "Cat");

使用 new 关键字创建对象 , 是发生在运行时 , 也就是 new Student<String>("Tom", 16, "Cat") 代码是在运行时才会执行 , 根本起不到 编译时 安全检查 的作用 , 从 安全检查 方面考虑 , 这种写法没有意义 ;


以 List 泛型为例 :

编译期进行安全检查示例 :
        // 编译器 在 编译期 进行检查
        List<String> list1 = new ArrayList<>();
        list1.add(1);

会报如下错误 :


编译期不进行安全检查示例 :
        // 编译器 在 编译期 不进行检查
        List list2 = new ArrayList<String>();
        list2.add(1);

该代码没有任何报错 ;

五、完整代码示例


1、泛型类 / 方法

/**
 * 泛型类
 *  该 T 类型作为参数使用
 *  T 是参数化类型 , 可以由外部传入
 *
 * @param <T>
 */
public class Student<T> {

    private String name;
    private int age;
    /**
     * 该数据的类型未知
     *  使用泛型表示 , 运行时确定该类型
     */
    private T data;

    public Student(String name, int age, T data) {
        this.name = name;
        this.age = age;
        this.data = data;
    }

    /**
     * 该方法不是泛型方法
     *  该方法是普通方法 , 返回值类型是 T 类型
     * @return
     */
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 泛型方法 , 是将某个类型作为参数传入
     *      方法指定泛型 , 写法如下 ;
     *
     * 该方法是泛型方法
     *      方法指定了 2 个泛型
     *      泛型个数 , 泛型的个数可以有很多个
     *      多个泛型之间 , 使用逗号隔开
     *
     * 泛型方法指定的泛型 T 与类中的泛型 T 没有任何关系
     *      这两个 T 可以是不同的类型
     *
     * 泛型方法中定义的泛型 T
     *      与参数类型的 T
     *      返回值类型的 T
     *      方法内部的 T
     *      都是同一个类型
     *
     * 与泛型类中的 T 完全没有关系
     *
     * @param <T>
     * @param <A>
     * @return
     */
    public <T, A> T getData2(T arg){
        T data = arg;
        return data;
    }

    public <T> T getData4(T arg){
        T data = arg;
        return data;
    }

    /**
     * 如果静态方法中使用类 类中的泛型
     *      这种使用时错误的
     *
     * 如果必须在 静态方法 中使用泛型 T
     *      则该泛型 T 必须是静态方法的泛型
     *      不能是类的泛型
     *
     * @param arg
     * @return
     */
    public static <T> T getData3(T arg){
        T data = arg;
        return data;
    }
}



2、main 函数

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 指定 泛型类 的泛型为 String 类型
        //      那么在该类中凡是使用到 T 类型的位置 , 必须是 String 类型
        //      泛型类的 泛型声明 , 使用时在 类名后面 声明
        Student<String> student = new Student("Tom", 16, "Cat");
        String data = student.getData();


        // 如果不 指明泛型类型
        //      则 泛型类型 默认为 Object 类型
        Student student1 = new Student("Tom", 16, "Cat");
        // 获取的 泛型类型的变量也是 Object 类型 , 需要强转为 String
        String data1 = (String) student1.getData();

        // 使用 <?> 通配符作为泛型
        //      默认将泛型指定为 Object 类型
        Student<?> student2 = new Student("Tom", 16, "Cat");
        String data2 = (String) student1.getData();

        // 泛型的安全检查
        // 推荐写法
        Student<String> student3 = new Student<>("Tom", 16, "Cat");
        // 不推荐写法
        Student student4 = new Student<String>("Tom", 16, "Cat");

        // 指定 泛型方法 的泛型类
        //      泛型方法 的泛型声明 , 在调用时 方法名的前面 声明
        student.<String, Integer>getData2("Mouse");

        // 泛型方法 中 也可以不声明泛型类型
        //      传入的参数是 泛型 T 类型
        //      如果给传入参数设置 String , 隐含将泛型 T 设置为 String 类型
        String data3 = student.getData2("Mouse");


        // 编译器 在 编译期 进行检查
        List<String> list1 = new ArrayList<>();
        list1.add(1);

        // 编译器 在 编译期 不进行检查
        List list2 = new ArrayList<String>();
        //list2.add(1);
    }
}
