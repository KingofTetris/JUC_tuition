package 辅助工具;

import java.util.concurrent.CountDownLatch;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        //异步任务加上同步条件。
        CountDownLatch countDownLatch = new CountDownLatch(10);//计数器 保证任务顺序 10个同学走完才能锁门。
        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName() + "号同学离开教室");
                countDownLatch.countDown();//计数-1
            },String.valueOf(i)).start();
        }
        countDownLatch.await();//如果计数器不为0 就不会往下执行。
        System.out.println(Thread.currentThread().getName() + "值日生，锁门");
    }
}
