package 集合线程安全;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class ArrayListThreadDemo {

    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();//ArrayList线程不安全

        /**
         * 下面两个是解决ArrayList线程不安全的两个古老方案
         */
//        List<String> list = new Vector<>();//古老的Vector全部加上synchronized关键字，性能低，但是安全
        //因为你刚刚学过嘛，synchronized相当于一个互斥的对象锁，只有拿到对象锁的时候才能执行方法，都相当于串行了。
        //他的底层实现就是Monitor。这个古老的类现在已经不太用了

        //Collecions解决
        //SynchronizedList：使用同步代码块实现同步，
        // 锁定的对象为mutex 就不是synchronized的this了
        // （构造函数可以传入一个Object，
        // 如果在调用的时候显示的传入一个对象，那么锁定的就是用户传入的对象，反之锁定this对象）
//        List<String> list = Collections.synchronizedList(new ArrayList<>());

        //现在一般用这个CopyOnWriteArrayList
        /**
         * 他的解决方案是写时复制技术，保证并发读，独立写
         * 但问题是写时复制技术付出双倍内存的代价，换取了读不加锁的高性能。
         * 因为现在内存都很大，所以才能这样搞。
         */
        List<String> list = new CopyOnWriteArrayList<>();

        /**
         * 三者简单对比
         * Vector对所有操作进行了synchronized关键字修饰，性能最差
         *
         * CopyOnWriteArrayList在写操作时需要进行copy操作，读性能较好，写性能较差
         *
         * Collections.synchronizedList性能较均衡，但是迭代操作并未加锁，所以需要时需要额外注意
         * ————————————————
         * 版权声明：本文为CSDN博主「逆风飞扬z」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/zbs506/article/details/129707209
         */
        for (int i = 0; i < 500; i++) {
            new Thread(()->{
                //向集合添加内容
                //Exception in thread "Thread413" java.util.ConcurrentModificationException
                //当并发量变大的时候，扩容就会出问题。
                /**
                 * 因为我们知道ArrayList首次扩容是在你第一次add操作的时候发生的。
                 * 如果多个线程同时进行扩容，在复制过程中就有可能发生数据丢失的问题。
                 */
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },"Thread" + i).start();
        }
    }
}
