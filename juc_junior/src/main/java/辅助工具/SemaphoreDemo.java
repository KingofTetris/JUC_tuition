package 辅助工具;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */
//信号量模拟6辆车 3个停车位
public class SemaphoreDemo {
    //3个信号量
    static Semaphore semaphore = new Semaphore(3);
    public static void main(String[] args) {
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                try {
                    //抢信号 acquire和release
                    semaphore.acquire();//抢车位
                    System.out.println(Thread.currentThread().getName() + "号车抢到了车位");
                    //设置随机停车时间 5s以内
                    int parkTime = new Random().nextInt(1,5);
                    TimeUnit.SECONDS.sleep(parkTime);
                    System.out.println(parkTime+ "s后，" +Thread.currentThread().getName() + "号车------离开了车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //最终信号释放
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
