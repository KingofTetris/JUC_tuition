package 阻塞队列;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */

/**
 * 阻塞队列的意义在于方便程序员管理多线程，极大可能地避免程序员自己操作wait/notify,await/signal，
 * 造成死锁或者其他预期之外的效果。
 * 在线程池中有大用
 *
 * 这个接口有很多实现，最主要的是ArrayBlockingQueue和LinkedBlockingQueue
 *
 * 他们俩的主要区别是：
 * 1.队列大小不同；
 * arrayBlockingQueue在初始化的时候，必须指定队列的大小；
 * 而LinkedBlockingQueue在初始化的时候，如果你没有指定大小，
 * 则会默认Integer.MAX_VALUE，是一个很大的值，这样就会导致数据在一个不可控范围，
 * 一旦添加速度远大于移除的速度时，可能会有内存泄漏的风险。
 * 底层实现不同；
 * arrayBlockingQueue的底层是一个数组，而LinkedBlockingQueue底层是一个链表结构。
 * 官方文档介绍中，LinkedBlockingQueue的吞吐行是高于arrayBlockingQueue；
 * 但是在添加或移除元素中，LinkedBlockingQueue则会生成一个额外的Node对象，对GC可能存在影响；
 * 至于为什么说LinkedBlockingQueue的吞吐性是高于arrayBlockingQueue：
 * 吞吐性能强是因为有两个锁，试想一下，Array里面使用的是一个锁，
 * 不管put还是take行为，都可能被这个锁卡住，
 * 而Linked里面put和take是两个锁，
 * put只会被put行为卡住，而不会被take卡住，
 * 因此吞吐性能自然强于Array。
 * 而“less predictable performance”这个也是显而易见的，
 * Array采用的时固定内存，而Linked采用的时动态内存，
 * 无论是分配内存还是释放内存（甚至GC）动态内存的性能自然都会比固定内存要差。
 *
 * 锁机制不一样；
 * arrayBlockingQueue使用的一个锁来控制，
 * LinkedBlockingQueue使用了2个锁来控制，一个名为putLock，另一个是takeLock，
 * 但是锁的本质都是ReentrantLock。
 *
 *
 * ————————————————
 * 版权声明：本文为CSDN博主「Zz罗伯特」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/zz18435842675/article/details/107310130
 */
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        //创建阻塞队列
        //定长阻塞队列用Array
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        //第一组add / remove 队满队空直接异常
     /*   blockingQueue.add("A");
        blockingQueue.add("B");
        blockingQueue.add("C");
//        System.out.println(blockingQueue.element()); //输出队头
//        blockingQueue.add("D");
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());
        System.out.println(blockingQueue.remove());*/

        //第二组offer / poll  offer超过队长返回false，poll返回null
      /*  System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.offer("B"));
        System.out.println(blockingQueue.offer("C"));
        System.out.println(blockingQueue.offer("A"));

        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());*/

        // 第三组put/take。 会出现阻塞状态。
//        blockingQueue.put("A");
//        blockingQueue.put("B");
//        blockingQueue.put("C");
        //第四个元素放不进去了，他就会一直等待，直到空间能放出来
//        blockingQueue.put("D");
//        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take());
//        System.out.println(blockingQueue.take());

        //第四组 offer poll设置阻塞时间
        System.out.println(blockingQueue.offer("a", 3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("b", 3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("c", 3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("d", 3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(3, TimeUnit.SECONDS));



    }
}
