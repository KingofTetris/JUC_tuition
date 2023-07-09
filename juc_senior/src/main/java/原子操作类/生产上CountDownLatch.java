package 原子操作类;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

class MyNumber{
    //这些原子类的底层都是CAS
    AtomicInteger atomicInteger = new AtomicInteger();
    public void addPlusPlus(){
        atomicInteger.getAndIncrement();
    }
}
public class 生产上CountDownLatch {
    public static final int SIZE = 50;

    public static void main(String[] args) throws InterruptedException {
        MyNumber myNumber = new MyNumber();
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);
        for (int i = 0; i < SIZE; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    myNumber.addPlusPlus();//50个线程i++;
                }
                countDownLatch.countDown();//计数器-1
            },String.valueOf(i)).start();
        }
        countDownLatch.await();//如果计数器不为0 就不会往下执行。
        //教学或者平常你写着完，睡2s是无所谓的。但是生产上最好用CyclicBarrier和CountDownLatch
//        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        //主线程获取速度快于其他线程，就结束了，到不了5W
        System.out.println(Thread.currentThread().getName() + "\t" + "result:" + myNumber.atomicInteger.get());
    }
}
