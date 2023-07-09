package 读写锁;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * @author by KingOfTetris
 * @date 2023/7/6
 */
public class StampLockDemo {

    static int number = 99;
    static StampedLock stampedLock = new StampedLock();
    public void write(){
        long stamp = stampedLock.writeLock();//和以前一样也是获得锁，无非是多了个戳记
        System.out.println(Thread.currentThread().getName()  + "\t 写线程准备修改");
        try {
            number = number + 13;
        }finally {
            stampedLock.unlockWrite(stamp);//比较stamp
        }
        System.out.println(Thread.currentThread().getName() + "\t 写操作完成");
    }
    //悲观读，不允许写操作饥介入
    public void read(){
        long stamp = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName() + "\t come in readLock codeblock 4 seconds ");
        for (int i = 0; i < 4; i++) {
            try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t 读取中");
        }
        try {
            int result = number;
            System.out.println(Thread.currentThread().getName() + "\t" + "获得成员变量值result:" + result);
            System.out.println("写线程没有修改成功，普通读写互斥");
        }finally {
            stampedLock.unlockRead(stamp);
        }
    }

    public void OptimisticRead(){
        long stamp = stampedLock.tryOptimisticRead();
        int result = number;
        //故意间隔4s钟，乐观认为读取中没有线程修改number值，
        System.out.println("4s前stampedLock.validate方法值(true无修改，false有修改):" + stampedLock.validate(stamp));
        for (int i = 0; i < 4; i++) {
            try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t" +"正在读取..." + (i+1) + "秒后" +
                    "stampedLock.validate方法值(true无修改，false有修改):" + "\t" + stampedLock.validate(stamp));
        }
        if (!stampedLock.validate(stamp)){
            System.out.println("有人修改---有人修改");
            stamp = stampedLock.readLock();
            try {
                //
                System.out.println("从乐观读升级为悲观读");
                result = number;
                System.out.println("悲观读后result:" + result);
            }finally {
                stampedLock.unlockRead(stamp);
            }
        }
        System.out.println(Thread.currentThread().getName() + "\t finally value" + result);
    }
    public static void main(String[] args) {
//        TranditionalPessimisticRead();
        StampLockDemo resource = new StampLockDemo();
        new Thread(()->{
            resource.OptimisticRead();
        },"readThread").start();

        //2s演示写操作介入
//        try{ TimeUnit.SECONDS.sleep(2);}catch(InterruptedException e){e.printStackTrace();}
        //6s读操作完毕，互不干扰。
        try{ TimeUnit.SECONDS.sleep(6);}catch(InterruptedException e){e.printStackTrace();}
        //写线程想进去修改
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() +"\t come in");
            resource.write();
        },"writeThread").start();

//        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
//        System.out.println(Thread.currentThread().getName() + "number:" + StampLockDemo.number);
    }

    private static void TranditionalPessimisticRead() {
        StampLockDemo resource = new StampLockDemo();
        new Thread(()->{
        resource.read();
        },"readThread").start();

        try{ TimeUnit.SECONDS.sleep(3);}catch(InterruptedException e){e.printStackTrace();}
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() +"\t come in");
            resource.write();
        },"writeThread").start();

        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        System.out.println(Thread.currentThread().getName() + "number:" + StampLockDemo.number);
    }
}
