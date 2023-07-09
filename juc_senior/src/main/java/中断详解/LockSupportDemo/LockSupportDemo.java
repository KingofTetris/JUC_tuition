package 中断详解.LockSupportDemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

/**
 * 1.通过synchronized锁来写多线程，你得先获得锁，
 * 也就是synchronized把代码包起来，才能wait和notify
 * 2.notify一得在wait后面，不然就卡死
 * 当然其实这两个细节，没什么必要
 * 因为synchronized是隐式锁，会自动加锁和释放锁
 * 3.同样对ReentrantLock.newCondition 也是一样，你得先获得锁，释放锁，
 * 保证await在signal之前。也就是必须先等待后唤醒。不然就报错。
 *
 * 正是因为这两个条件的限制才出现了LockSupport，突破了这些限制
 * 关心业务，不再关心这些细节。
 */
public class LockSupportDemo {

    /**
     * LockSupport连锁都不用关心了,再也不用关心到底要锁住哪块代码
     * 唤醒和等待顺序也无所谓了。
     * 这就是许可证机制，只要你有许可证就让你运行,
     * **而且每个线程最多只有一个许可证***!
     * 但是park和unpark还是要成对出现
     * @param args
     */
    public static void main(String[] args) {
        //LockSupport就是个工具类，全是静态方法，不需要你new出来
        Thread t1 = new Thread(() -> {
            try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t" + "come in");
            LockSupport.park();//阻塞没有许可证的线程
            System.out.println(Thread.currentThread().getName() + "\t" + "被唤醒");
        }, "t1");
        t1.start();
//        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        new Thread(()->{
            LockSupport.unpark(t1);//给线程t1发许可证
            System.out.println(Thread.currentThread().getName() + "\t" + "发出唤醒通知");
        },"t2").start();
    }

    private static void syncAwaitSignal() {
        Lock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        new Thread(()->{
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " come in");
                c1.await();
                System.out.println(Thread.currentThread().getName() + "被唤醒");
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"t1").start();

        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}

        new Thread(()->{
            /**
             * t2要使用c1这把锁，也得先获得lock
             */
            lock.lock();
            try {
                c1.signal();
                System.out.println(Thread.currentThread().getName() + "发出唤醒通知");
            }finally {
                lock.unlock();
            }
        },"t2").start();
    }

    private static void syncWaitNotify() {
        Object objLock = new Object();
        new Thread(()->{
        synchronized (objLock){
            System.out.println(Thread.currentThread().getName() + "\t" + "come in");
            try {
                objLock.wait();//释放锁
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() +"\t" + "被唤醒");
        }
        },"t1").start();

        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}

        new Thread(()->{
            synchronized (objLock){
                objLock.notify();
                System.out.println(Thread.currentThread().getName() + "\t ---发出通知");
            }
        },"t2").start();
    }
}
