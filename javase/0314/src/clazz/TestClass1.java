package clazz;

public class TestClass1 {
    public static void main(String[] args) throws ClassNotFoundException {
        //方式1   类名.class
        Class clazz1 = TestClass1.class;
        System.out.println(clazz1);

        //方式2   对象名.getClass()
        TestClass1 tc1 = new TestClass1();
        Class clazz2 = tc1.getClass();

        //方式3   Class.forName()
        String className = "clazz.TestClass1";
        Class clazz3 = Class.forName(className);
        System.out.println(clazz3);

        //方式4   通过类加载器
        Class clazz4 =  ClassLoader.getSystemClassLoader().loadClass("clazz.TestClass1");
        System.out.println(clazz4);



    }
}
