package 中断详解.interruptDemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

//哪种方法不重要，重要的是思想，监听状态，然后中断
public class InterruptDemo_中断线程 {

    //volatile有一个非常重要的功能 ---- 可见性
    static volatile boolean isStop = false;

    //原子布尔型
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "\t 中断状态位interrupt被修改，程序停止");
                    break;
                }
                System.out.println("t1的中断状态" + Thread.currentThread().isInterrupted());
            }
            System.out.println("t1的中断状态" + Thread.currentThread().isInterrupted());
            System.out.println("-------hello Interrupted");
        }, "t1");//要用Thread类自带的API就不能直接start了
        t1.start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
           t1.interrupt();//t2尝试修改t1的中断状态，自己中断自己也可以。
        },"t2").start();
    }

    private static void m2_AtomicBoolean() throws InterruptedException {
        new Thread(()->{
            while (true){
                if (atomicBoolean.get()){
                    System.out.println(Thread.currentThread().getName() + "\t atomicBoolean被修改为true，程序停止");
                    break;
                }
            }
            System.out.println("-------hello AtomicBoolean");
        },"t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            atomicBoolean.set(true);
        },"t2").start();
    }

    private static void m1_volatile() throws InterruptedException {
        new Thread(()->{
            while (true){
                if (isStop){
                    System.out.println(Thread.currentThread().getName() + "\t volatile停止符号isStop被修改，程序停止");
                    break;
                }
            }
            System.out.println("-------hello volatile");
        },"t1").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            isStop = true;
        },"t2").start();
    }
}
