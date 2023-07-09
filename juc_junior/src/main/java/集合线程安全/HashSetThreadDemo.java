package 集合线程安全;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class HashSetThreadDemo {
    public static void main(String[] args) {
        //这些容器，ArrayList,HashSet HashMap都一样 都是扩容，要复制数据的时候，出问题
        //如果多个线程同时扩容就有问题了，
        /**
         * ArrayList第一个线程A 插入数据时 ，发现需要扩容 ， 线程A 去扩容去了
         * 线程A扩容过程还没完成的时候，线程B过来了。
         *线程B也觉得他要扩容。那么这个时候就出现了A B一起扩容的情况
         * 然后你会发现，A要早一点，于是A先把size++的位置给占了。用于填充他add的元素
         * B晚一点扩容，但是他的size++位置和上面是一样的，这就会把A的add的元素改为B要添加的元素，这就导致数据被覆盖掉。
         * 直接 报错位置就是实际修改次数和预计修改次数不一致。当然add只是其中一种情况，remove也要可能引发线程不安全。
         *
         * HashSet和HashMap也是一个道理。
         */
//        Set<String> set = new HashSet<>();

        //他的改造和ArrayList类似
        //原理也是一样的，写时复制。
        CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 300; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, "thread" + i).start();
        }
    }
}
