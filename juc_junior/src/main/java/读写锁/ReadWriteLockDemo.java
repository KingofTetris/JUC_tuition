package 读写锁;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */

//读写锁：一个资源可以被多个读线程访问，一个写进程访问。
    //读写互斥，（读写互斥是对其他线程互斥，对你自己当然不互斥。你都可以写了，当然可以读）
// 写写阻塞。
    //读写锁可能导致读线程饥饿，如果来了多个写线程，可能就没法读了。
public class ReadWriteLockDemo {
    static Mycache mycache;
    /**
     * 从结果上就很明显能看出来写锁是排它锁，读锁是共享锁。
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        mycache = new Mycache();
        for (int i = 1; i <= 5; i++) {
            final int finalI = i;
            new Thread(()->{
                mycache.put(finalI,UUID.randomUUID().toString().substring(0,8));
            },"Thread" + finalI).start();
        }
        //睡眠是为了效果明显，不然读线程可能先于写线程就看不出效果了。
        TimeUnit.SECONDS.sleep(1);

        for (int i = 1; i <= 5; i++) {
            final int finalI = i;
            new Thread(()->{
                mycache.get(finalI);
            },"Thread" + finalI).start();
        }

    }
}
class Mycache{

    //volatile使得共享资源的变化对多个线程可见。
    private volatile HashMap<Integer,String> map = new HashMap<>();
    //读写锁对象
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();//读写锁的实现类重入读写锁


    public void put(Integer key,String val){
        //加上写锁
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "----正在进行写操作" + key);
            TimeUnit.SECONDS.sleep(1);
            map.put(key,val);
            System.out.println(Thread.currentThread().getName() + "---写完了" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public String get(Integer key) {
        String res = null;
        readWriteLock.readLock().lock();
        //暂停
        try {
            System.out.println(Thread.currentThread().getName() + "正在进行读操作" + key);
            TimeUnit.SECONDS.sleep(1);
            res = map.get(key);
            System.out.println(Thread.currentThread().getName() + "---读完了" + key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            readWriteLock.readLock().unlock();
        }
        return res;
    }
}
