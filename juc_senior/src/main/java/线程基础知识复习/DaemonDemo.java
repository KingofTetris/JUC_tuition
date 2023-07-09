package 线程基础知识复习;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author KinfOfTetris
 * @create 2023-06-28 14:36
 */
public class DaemonDemo {

    private String test;

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaemonDemo that = (DaemonDemo) o;
        return Objects.equals(test, that.test);
    }*/

   /* @Override
    public int hashCode() {
        //hashCode的计算 底层是JVM虚拟机完成的，C++代码
        //空对象hashcode 为0。
        return Objects.hash(test);
    }*/

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 开始运行,当前线程的类别:"
                    + (Thread.currentThread().isDaemon() ? "守护线程" : "用户线程"));
            while (true) {

            }
        }, "t1");
        t1.setDaemon(true);//通过设置属性Daemon来设置当前线程是否为守护线程
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 主线程结束");
    }
}
