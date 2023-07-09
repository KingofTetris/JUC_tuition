package Java琐事;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 题目：实现一个自旋锁，借鉴CAS思想
 * 通过CAS完成自旋锁，A线程先进来调用myLock方法自己持有锁5秒钟，
 * B随后进来后发现当前有线程持有锁，所以只能通过自旋等待，直到A释放锁后B随后抢到。
 */
public class SpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    //还是一样拿锁，解锁
    public void lock(){
        Thread thread = Thread.currentThread();
        //自旋，do while
        System.out.println(thread.getName() + "\t come in");
        while (!atomicReference.compareAndSet(null, thread)) {}
    }

    //解锁
    public void unlock(){
        Thread thread = Thread.currentThread();
        //释放
        atomicReference.compareAndSet(thread,null);
        System.out.println(thread.getName() + "\t ------task over,unlock");
    }
    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(()->{
          spinLockDemo.lock();
          try{ TimeUnit.SECONDS.sleep(5);}catch(InterruptedException e){e.printStackTrace();}
          spinLockDemo.unlock();
        },"A").start();

        //暂停500ms，让线程A先启动去拿锁
        try{ TimeUnit.MILLISECONDS.sleep(500);}catch(InterruptedException e){e.printStackTrace();}

        new Thread(()->{
            spinLockDemo.lock();
            System.out.println("B业务处理中");
            spinLockDemo.unlock();
        },"B").start();
    }
}
