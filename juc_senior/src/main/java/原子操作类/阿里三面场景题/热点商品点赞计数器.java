package 原子操作类.阿里三面场景题;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * 点赞数加加统计，不要求实时精确，因为你统计别人也在点，做不到实时精确 就是明示你LongAdder了。
 * 需求：50个线程，每个线程100w次，总点赞数出来
 */

class ClickNumber {
    int number = 0;

    //加锁统计
    public synchronized void clickBySynchronized() {
        number++;
    }

    //传统AtomicLong
    AtomicLong atomicLong = new AtomicLong(0);
    public void clickByAtomicLong() {
        atomicLong.getAndIncrement();
    }

    //longAdder只能算加法，而且是从0开始
    LongAdder longAdder = new LongAdder();
    public void clickByLongAdder() {
        longAdder.increment();
    }

    //你要加减乘除，自定义初始值就要用LongAccumulator
    LongAccumulator longAccumulator = new LongAccumulator((x, y) -> x + y, 0);

    public void clickByLongAccumulator() {
        longAccumulator.accumulate(1);
    }
}

public class 热点商品点赞计数器 {

    public static final int _1W = 10000;
    public static final int threadNum = 50;

    public static void main(String[] args) throws InterruptedException {
        ClickNumber clickNumber = new ClickNumber();
        long startTime;
        long endTime;
        /**
         * 从下面的数量级，
         * 你当然就明白了为什么大数据高并发下要用LongAdder
         */
        //sync costTime：1830ms
        CountDownLatch countDownLatch1 = new CountDownLatch(threadNum);
        //AtomicLong costTime：628ms
        CountDownLatch countDownLatch2 = new CountDownLatch(threadNum);
        //LongAdder costTime：199ms
        CountDownLatch countDownLatch3 = new CountDownLatch(threadNum);
        //LongAccumulator costTime：161ms
        CountDownLatch countDownLatch4 = new CountDownLatch(threadNum);
        startTime = System.currentTimeMillis();
        //50个线程
        for (int i = 1; i <= threadNum; i++) {
            new Thread(() -> {
                //1百万次
                for (int j = 1; j <= 100 * _1W; j++) {
                    clickNumber.clickByLongAccumulator();
                }
                countDownLatch4.countDown();
            }, String.valueOf(i)).start();
        }
        countDownLatch4.await();
        endTime = System.currentTimeMillis();
        System.out.println("costTime：" + (endTime - startTime) + "ms");
//        System.out.println(clickNumber.number);
        //如果是原子类 你就要从原子类去get了
//        System.out.println(clickNumber.atomicLong.get());
//        System.out.println(clickNumber.longAdder.sum());
        System.out.println(clickNumber.longAccumulator.get());
    }
}
