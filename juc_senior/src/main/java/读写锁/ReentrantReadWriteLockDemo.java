package 读写锁;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

/**
 * @author by KingOfTetris
 * @date 2023/7/6
 */

//和基础篇一样，我们也是用缓存来模拟缓存，演示读写锁
class MyResource{
    Map<String,String> map = new HashMap<>();
    //普通的非公平可重入锁，相当于synchronized关键字
    Lock lock = new ReentrantLock();
    //普通的读写锁，读写互斥，读读共享
    //读写锁的缺点就是
    //1.写锁饥饿
    //2.锁降级 把写🔒降级为读🔒。
    //也就是一个线程获得了写锁的情况下，他还可以获得读锁，这就是写锁的降级。
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    //
    StampedLock stampedLock = new StampedLock();

    public void write(String key,String value){
        //ReentrantLock 不管是读还是写每次只能有一个线程获得🔒。
//        lock.lock();
        //readWriteLock 使用read/writeLock获取锁对象，然后用lock/unlock上锁和解锁
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t 正在进行写操作");
            map.put(key,value);
            //假设写要500ms
            try{ TimeUnit.MILLISECONDS.sleep(500);}catch(InterruptedException e){e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t 写操作完成");
        }finally {
//            lock.unlock();
        readWriteLock.writeLock().unlock();
        }
    }

    public void read(String key){
//        lock.lock();

        //读锁共享，写锁互斥.
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t 正在进行读操作");
            String s = map.get(key);
            //假设读要100ms
//            try{ TimeUnit.MILLISECONDS.sleep(100);}catch(InterruptedException e){e.printStackTrace();}
            //假设要2000ms，读没完成，写锁无法获得，那么写现场就会饥饿
            try{ TimeUnit.MILLISECONDS.sleep(2000);}catch(InterruptedException e){e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t 读操作完成,读取到" + s);
        }finally {
//            lock.unlock();
            readWriteLock.readLock().unlock();
        }
    }
}
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) {
        MyResource resource = new MyResource();

        /**
         * ReentrantLock不管读写同时只能有一个线程。
         */
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(()->{
                String temp = String.valueOf(finalI);
                String value = UUID.randomUUID().toString().substring(0, 8);
                resource.write(temp,value);
            },String.valueOf(i)).start();
        }
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(()->{
               resource.read(String.valueOf(finalI));
            },String.valueOf(i)).start();
        }
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        //再来3个写线程，演示读操作未完成，写是无法获得的。
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            new Thread(()->{
                String temp = String.valueOf(finalI);
                String value = UUID.randomUUID().toString().substring(0, 8);
                resource.write(temp,value);
            },"新" + i).start();
        }


    }
}
