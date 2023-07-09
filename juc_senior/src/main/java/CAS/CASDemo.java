package CAS;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */
public class CASDemo {
    public static void main(String[] args) {
        //CAS仅通过硬件就完成了原子性，不需要像synchronized那样的重锁涉及到用户态到内核态的切换。
        //性能对比重锁性能会好一些。
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5,2022) + "\t" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5,2022) + "\t" + atomicInteger.get());
    }
}
