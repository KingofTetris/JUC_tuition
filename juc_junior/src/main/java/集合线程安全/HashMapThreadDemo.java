package 集合线程安全;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class HashMapThreadDemo {
    public static void main(String[] args) {
        //这些容器，ArrayList,HashSet HashMap都一样 都是扩容，要复制数据的时候，出问题
        //如果多个线程同时扩容就有问题了，
        /**
         * ArrayList第一个线程A 插入数据时 ，发现需要扩容 ， 线程A 去扩容去了
         * 线程A扩容过程还没完成的时候，线程B过来了。
         *线程B也觉得他要扩容。那么这个时候就出现了A B一起扩容的情况
         * 然后你会发现，A要早一点，于是A先把size++的位置给占了。用于填充他add的元素
         * B晚一点扩容，但是他的size++位置和上面是一样的，这就会把A的add的元素改为B要添加的元素，这就导致数据被覆盖掉。
         * 直接 报错位置就是实际修改次数和预计修改次数不一致。当然add只是其中一种情况，remove也有可能引发线程不安全。
         *  if (modCount != expectedModCount) 三个集合类并发错误原理都是因为同时扩容而导致数据丢失。
         * HashSet和HashMap也是一个道理。
         * 但是细节上有些许不同，HashMap在1.7之前使用头插法，当出现高并发下出现同时扩容
         * 就有可能导致循环引用出现死循环，在JDK8已经改成尾插法解决了问题
         *
         * 当然除了这个next引用死循环的高并发问题以外。
         * 还有就是在多线程插入的时候也会造成链表中数据的覆盖导致数据丢失。和ArrayList一样。
         * 这个解决就不是简单的头插法改尾插法就完事了。
         * 这个解决就是下面Hashtable和ConcurrentHashMap的事情了。
         *
         * JDK1.7版本的ConcurrentHashMap采用分段锁的形式，
         * 每一段分一个Segment类，他内部类似HashMap的结构，内部有一个Entry数组，
         * 数组的每一个元素是一个链表，同时Segment继承自ReentrantLock。
         * 在HashEntry中采用volatile来修饰，HashEntry的当前值和next元素的值。
         * 所以get方法在获取数据的时候是不需要加锁的，这样就大大提高了执行效率
         * 在执行put()方法的时候先尝试获取锁（tryLock()）,
         * 如果获取失败，说明存在竞争，那么通过scanAndLockForPut()方法自旋，
         * 当自旋次数达到MAX_SCAN_RETRIES时会执行阻塞锁，直到获取锁成功。
         * JDK1.8采用了CAS+synchronized的方法来保证并发，线程安全
         */
//        HashMap<String,Integer> map = new HashMap<>();
        //Hashtable和Vector一样也是给所有方法都加上了synchronized锁，性能当然低得吓人。
        //就相当于同时只能有一个线程put
//        Hashtable<String,Integer> map = new Hashtable<>();

        //现在1.5以后 道格李 帮你写了ConcurrentHashMap 解决了HashMap线程不安全的问题。
        //他的底层实现也是synchronized 但是不像Hashtable那么亏贼，直接在方法上全部加上synchronized
        //而是通过分段锁的方式来实现线程安全。
        ConcurrentHashMap<String,Integer> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                int j = 0;
                String s1 = UUID.randomUUID().toString().substring(0, 8);
                map.put(s1,j);
                System.out.println(map);
            }, "thread" + i).start();
        }
    }
}
