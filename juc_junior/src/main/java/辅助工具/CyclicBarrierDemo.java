package 辅助工具;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */

/**
 * 所以目前看来你可以理解为
 * CountDownLatch是-1，CyclicBarrier是+1
 */
public class CyclicBarrierDemo {
    //创建固定值
    private final static int NUMBER = 7;
    public static void main(String[] args) {
        //创建循环栅栏
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER,()->{
            System.out.println("集齐龙珠就可以召唤神龙");
        });
        //让七个线程去集齐七颗龙珠
        for (int i = 1; i <= 7 ; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "星龙珠被找到了");
                //龙珠没集齐就一直等待，集齐以后就会执行栅栏的任务
                /**
                 * 原理就是执行一次await就有一个线程等待，当等待线程数等于你设置的Number时候，
                 * 就自动执行Runnable也就是barrierAction的run方法
                 */
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
