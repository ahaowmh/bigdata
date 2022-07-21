package reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TestReflect2 {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //得到配置文件的Class路径
        Class clazz = Class.forName("reflect.Student");
        //从Class对象中得到该类的无参构造方法
        Constructor constructor = clazz.getConstructor(String.class,int.class);
        //用Constructor创建对象
        Object stu2 = constructor.newInstance("张三",80);
        System.out.println(stu2);

    }

}
