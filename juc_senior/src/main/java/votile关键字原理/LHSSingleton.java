package votile关键字原理;

/**
 * @Author:wjy
 */

/**
 * 成员变量 instance 为什么是私有的？
 * 最直接的原因就是 防止外部直接访问，如果不声明为 private ，其他类将能直接访问和修改它，而这就违反了单例模式的原则，因为单例模式要求类只有一个实例化
 * 成员变量 instance 为什么是静态的？
 * 使用 static 修饰后，意味着该变量是属于类的，而不是类的实例
 * 你可以通过 类名.变量名 的方式直接访问
 * 同时，将只存在一个 instance
 * 你可以像
 * Singleton s1 = Singleton.getInstance();
 * Singleton s2 = Singleton.getInstance();
 * 这样创建两个实例化对象，但他们都共享一个 instance 对象，确保只有一个单例
 * 构造方法 Singleton() 为什么是私有的？
 * 与成员变量相似，是为了防止外部实例化 ，防止其他类通过 new 关键字直接创建该类的实例（new 关键字本质是调用了类的构造方法），而应该通过特定途径（通常是类提供的静态方法，也就是 getInstance()
 * getInstance() 方法为什么是公有的？
 * 很显然，这是我们暴露给其他类调用来创建实例的方法，因此必须是公有的，如果是私有那不就是成黑盒搁这里圈地自萌了~
 * getInstance() 方法为什么是静态的？
 * 与成员变量相似，因为构造方法被私有化了，我们无法通过 new 关键字来实例化对象，而通过 类名.方法名 的方式可以直接访问
 * ————————————————
 *
 *                             版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
 *
 * 原文链接：https://blog.csdn.net/m0_63653444/article/details/140007103
 */
public class LHSSingleton {

    //在该类中创建一个该类的对象供外界去使用
    private static LHSSingleton instance;

    // 构造方法 private 化
    private LHSSingleton(){

    }

    // 得到 Singleton 的实例(唯一途径）
    public static LHSSingleton getInstance(){
        if (instance == null){
            instance = new LHSSingleton();
        }
        return instance;
    }
}

