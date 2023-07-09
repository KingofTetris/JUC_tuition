package 中断详解.interruptDemo;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */
public class InterruptDemo3_中断阻塞进程发生死循环 {
    public static void main(String[] args) throws InterruptedException {
       Thread t1 = new Thread(()->{
           while (true){
               if (Thread.currentThread().isInterrupted()){
                   System.out.println("中断标志位为true，终止线程");
                   break;
               }
               System.out.println("------------hello InterruptDemo3");
           }
           try {
               //加入睡眠，模拟中断处于阻塞状态的线程，这个时候就会抛出中断异常了。
               //JDK17 已经帮你做了优化了，不会无限循环，会立即停止。
               //JDK8 还是无限循环。
               /**
                * -----1.至于为什么阻塞方法会把中断标志给情况，是因为
                * 这是因为这些方法都被设计为可以响应线程的中断请求，
                * 所以才会将线程的中断标志位清除，即重置为false。
                *
                * -----2、如果阻塞方法（如wait()、sleep()和join()）不能响应中断请求，最主要的原因就是可能导致死锁。
                * 即在阻塞期间不会抛出InterruptedException异常，而是继续等待或休眠，会导致以下情况发生：
                *    ------2.1延迟响应中断：如果线程在阻塞期间不能被中断，
                *    那么中断请求将会延迟到线程恢复运行后才会被处理。这可能会导致中断请求的响应时间延迟，影响系统的性能和响应性。
                *    -----2.2 资源占用：如果线程在阻塞期间不能被中断且没有明确的终止条件，
                *    那么该线程将一直占用某些资源，导致资源无法被其他线程使用或释放，从而影响系统的整体运行效率。
                *    -----2.3 死锁：如果线程在阻塞期间无法响应中断，
                *    且其他线程在等待该线程释放某个共享资源时被阻塞，
                *    就可能导致死锁的发生。因为无法中断的线程无法继续执行并释放所占用的资源，其他线程也无法继续执行，
                *    导致所有相关线程都无法继续运行下去。
                */
               Thread.sleep(200);//因为你调用了阻塞方法以后，线程的中断状态将被清除，所以才会无限循环。
           } catch (InterruptedException e) {
               //JDK8 需要自己在catch里面处理。
               //方法也很简单，再设置一次状态为true，线程就会停下来了。
               Thread.currentThread().interrupt();
               e.printStackTrace();
           }
       },"A");
       t1.start();

        TimeUnit.MILLISECONDS.sleep(1);

        new Thread(()->{
            t1.interrupt();
        }).start();

    }
}
