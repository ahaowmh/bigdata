package reflect;

public class TestReflect {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //得到配置文件的Class路径
        Class clazz = Class.forName("reflect.Student");
        //从Class对象中得到该类的无参构造方法Constructor,并创建对象
        Object stu1 = clazz.newInstance();
        System.out.println(stu1);
    }
}
