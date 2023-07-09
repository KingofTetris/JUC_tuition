package synchronized锁升级;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/7/5
 */
public class synchronizedUpDemo {
    public static void main(String[] args) {
//        nolock();
//        biasdLock();
        //softLock();
        Object o = new Object();
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
            System.out.println("这就是轻量级锁");
        }
    }

    private static void biasdLock() {
        //biasedLock
        //JDK8是默认开启偏向锁，但是有延迟4s，4s后才生效，你要立即看到的话，
        //可以使用参数-XX:BiasedLockingStartupDelay=0 也就是偏向锁启动延迟设置为0
        //或者你直接程序延迟5S钟
        //JDK17是默认是关闭偏向锁的，他的延迟已经直接改成0了
        //因为现在的场景并发量几乎不可能再出现 一直是同一个线程访问同步代码了。
        try{ TimeUnit.SECONDS.sleep(5);}catch(InterruptedException e){e.printStackTrace();}
        Object o = new Object();
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
            System.out.println("这就是偏向锁");
        }
    }

    private static void nolock() {
        Object o = new Object();
        //调用hashCode才会生成
        System.out.println("十进制hashcode:" + o.hashCode());
        //从结果的value上看，可以知道java对象内存布局是使用的小端对齐
        //低位数据放低地址，高位数据放高地址 对于计算机来说，这才是顺序，对于人来说是逆序的。
        //但是机器码又不是给人看的，为了高效就使用小端对齐。
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        //所以就证明了 无锁状态下后面三位固定是0 01 01代表无锁，前面的0/1代表是否是偏向锁
        //至于为什么hashcode明明是int类型应该是32位，是因为hashcode是正整数，不会存在负数的情况
        //把符号位去掉也就是31位了。
    }
}
