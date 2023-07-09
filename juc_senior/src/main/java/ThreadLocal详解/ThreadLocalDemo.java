package ThreadLocal详解;

/**
 * @author by KingOfTetris
 * @date 2023/7/4
 */


import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 1.为什么要用threadlocal?以前解决线程安全都是每个线程去抢一份，那么就只能用锁解决
 * 2.现在threadlocal每个人有一份自己的资源，就不用再使用锁了。
 * 就像LOL里面10个玩家，各自有各自的英雄，各自有各自的经验，金钱等级等等。每个玩家都是一个threadlocal
 * 但是threadlocal和锁是不能互相代替的，只是应用场景不同。
 *
 * 下面这个场景就适合threadLocal
 * 需求1：总公司根据业绩给分公司分猪肉，只关心分公司的销售额
 * 需求2：现在总公司分完猪肉，分公司再根据每个业务员的业绩给他们继续分猪肉。这就需要记录每个业务员的业绩。
 */

class House{
    int saleCount = 0;
    //演示使用synchronized最方便，效率最低
    //不加锁可以使用cas，AtomicInteger,LongAdder
   /* public synchronized void saleHouse(){
        saleCount++;
    }
    */
    //下面演示longAdder。cas+base+cell快得多。
   /* LongAdder longAdder = new LongAdder();
    public void saleHouseByLongAdder() {
        longAdder.increment();
    }*/
    //每个线程的初始值就是0
    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(()-> 0);
    //自己用自己的，根本不用再考虑加锁的事情。CAS其实也锁，乐观锁。
    public void saleVolumeByThreadLocal(){
        saleVolume.set(saleVolume.get() + 1);//set get 修改/获得supplier
    }
}
public class ThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        House house = new House();
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for(int i = 1; i <= 5; i++) {
            //教学场景都是new出来的线程，实际的线程大部分是使用线程池进行复用。不释放空间的话，可能会造成内存泄漏
            new Thread(()->{
                Random random = new Random();
//                int number = random.nextInt(1, 10);
                try {
                    for (int j = 0; j < random.nextInt(1, 10); j++) {
//                    house.saleHouse();
//                    house.saleHouseByLongAdder();
                        //自定义的ThreadLocal变量必须使用try-finally进行回收
                        //线程池情况下线程经常会被复用，不清理可能会造成内存泄露。因为每个线程的内存大小是有限的。
                        house.saleVolumeByThreadLocal();
                        house.saleCount++;
                    }
                    countDownLatch.countDown();
                    System.out.println(Thread.currentThread().getName() + "号业务员 \t" + "售出" + house.saleVolume.get() + "套");
                }finally {
                    house.saleVolume.remove();//不清除，保不齐哪天暴雷,你就遭殃辣。
                }
//                System.out.println(Thread.currentThread().getName() + "号业务员 \t" + "售出" + number + "套");
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        System.out.println("这家分公司的销售额是" + "\t" + house.saleCount);
//        System.out.println("这家分公司的销售额是" + "\t" + house.longAdder.sum());

    }
}
