package 中断详解.interruptDemo;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */
public class InterruptDemo4_Thread中断静态方法 {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println("----------------1");
        Thread.currentThread().interrupt();//设置主线程中断标志位为true
        System.out.println("----------------2");
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "\t" + Thread.interrupted());

        /**
         * 静态方法Thread.interrupted(); 在返回线程中断状态位后，还会清除中断状态
         * 实例方法Thread.currentThread().isInterrupted(); 只会返回中断位信息
         */
//        Thread.interrupted();
//        Thread.currentThread().isInterrupted();
    }
}
