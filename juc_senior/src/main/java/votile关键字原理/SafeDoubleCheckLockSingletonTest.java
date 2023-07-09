package votile关键字原理;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */
class SafeDoubleCheckLockSingleton{
    private volatile static SafeDoubleCheckLockSingleton singleton;
    //单例模型私有化构造器，new只能我自己来用
    //告诉别人只能用static方法获取，不能new
    private SafeDoubleCheckLockSingleton(){

    }
    public static SafeDoubleCheckLockSingleton getInstance(){
        if(singleton == null){
            //加类锁，注意你不能加对象锁,保证只有一个线程能创建单例对象
            //因为不同的服务可能都是一个对象，对象锁没有意义。类锁保证JVM中只有一份
            synchronized(SafeDoubleCheckLockSingleton.class){
                //双端锁，隐患就在这里，用volatile解决
                if(singleton == null){
                    singleton = new SafeDoubleCheckLockSingleton();
                }
            }
        }
        return singleton;
    }
}

public class SafeDoubleCheckLockSingletonTest{
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            //假设10000个线程都去创建实例，如果你不给单例对象加上volatile对象
            //那么有可能有线程会获得空对象null
            new Thread(SafeDoubleCheckLockSingleton::getInstance).start();
        }
    }
}
