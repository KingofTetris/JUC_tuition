package 死锁;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 *
 * 面试：请你快速写一个死锁代码，解释一下
 */
public class deadLock {

    /**
     * 程序是写完了，因为你是自己故意写的死锁程序，只有你知道，其他人是不知道的。
     * 实际开发过程中，到底是网络不好，端口没开放导致的请求超时，连接超时，还是因为死锁很难说
     * 检查死锁有两个步骤。
     * 1.jps -l 查看可能是死锁的进程号
     * 2.jstack 进程号.就知道了，如果是死锁 会报Found 1 deadlock.
     */

    //两个锁对象a,b
    //A线程争抢锁B，再抢锁A。B线程先抢锁A，再抢锁B。造成死锁。
    static final Object a = new Object();
    static final Object b = new Object();

    public static void main(String[] args)  {
        new Thread(()->{
            synchronized (a){
                System.out.println("A线程占用锁a，请求锁b");
                //第一个线程要稍微等待一下，保证这个死锁程序生效
                //否则第二个线程还没创建完，第一个线程就执行完了，就不会发生死锁了
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (b){
                    System.out.println("A线程得到锁b");
                }
            }
        },"A").start();

        new Thread(()->{
            synchronized(b){
                System.out.println("B线程占用锁b，请求锁a");
                synchronized (a){
                    System.out.println("B线程得到锁a");
                }
            }
        },"B").start();
    }
}
