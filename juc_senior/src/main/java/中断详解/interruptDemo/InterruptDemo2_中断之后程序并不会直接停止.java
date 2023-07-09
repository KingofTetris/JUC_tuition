package 中断详解.interruptDemo;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */
public class InterruptDemo2_中断之后程序并不会直接停止 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            for (int i = 0; i < 300; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("t1线程被打断了~~");
                    break; //如果被中断就手动停止。
                }
                System.out.println(i);
            }
//            System.out.println("t1线程默认标识为" + Thread.currentThread().isInterrupted());
        });
        t1.start();

        //1ms后中断
        /**
         * 结果发现确实他打印过程中标记就已经是true了，但是却没有中断。
         * 说明interrupt方法仅仅只是给个标记位，这个标记为为true以后怎么处理
         * 是程序员的事情
         */
        TimeUnit.MILLISECONDS.sleep(1);
        t1.interrupt();
        System.out.println("t1线程默认标识为" + t1.isInterrupted());

        //2s后这300个数一定打印完了，这个时候我们测试线程的状态
        TimeUnit.SECONDS.sleep(2);
        t1.interrupt(); //对于已经死亡的线程不会产生任何影响。还是false
        System.out.println("t1线程默认标识为" + t1.isInterrupted());
    }
}
