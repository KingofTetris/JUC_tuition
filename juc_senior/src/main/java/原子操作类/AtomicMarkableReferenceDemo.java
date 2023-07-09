package 原子操作类;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author Guanghao Wei
 * @create 2023-04-12 16:55
 */

/**
 * CAS---->Unsafe--->cmpxchg--->do while/ABA---->AtomicStampedReference + AtomicMarkableReference
 *
 * AtomicStampedReference 这兄弟带邮戳的解决改了几次的问题，每次要设置预期修改次数 1,2,3,4...
 *
 * AtomicMarkableReference 带标志位的解决有没有修改的问题。就是一次性，你有没有改过，改过几次不重要。
 * 每次设置的是预期是否修改。true|false
 */
public class AtomicMarkableReferenceDemo {
    static AtomicMarkableReference markableReference = new AtomicMarkableReference(100, false);
    public static void main(String[] args) {
        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t" + "默认标识: " + marked);//t1	默认标识: false
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            markableReference.compareAndSet(100, 1000, marked, !marked);//t2	默认标识: false

        }, "t1").start();

        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t" + "默认标识: " + marked);//t2	t2线程CASResult：false
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean b = markableReference.compareAndSet(100, 2000, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t" + "t2线程CASResult：" + b);
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.isMarked());//t2	true
            System.out.println(Thread.currentThread().getName() + "\t" + markableReference.getReference());//t2	1000

        }, "t2").start();
    }
}
