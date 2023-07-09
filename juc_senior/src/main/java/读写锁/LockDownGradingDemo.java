package 读写锁;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author by KingOfTetris
 * @date 2023/7/6
 */
public class LockDownGradingDemo {

    public static void main(String[] args) {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();

        /**
         * 锁降级
         * 写的时候进行读不会导致数据不一致。是安全的。
         */
        //线程A开始写入
        writeLock.lock();//获取写锁
        System.out.println("-------------写入");
        //假设线程A途中要进行查看,自己获取read锁
        readLock.lock();//获取读锁
        System.out.println("------------查看");
        writeLock.unlock();//释放写锁
        readLock.unlock();//释放读锁

        /**
         * 锁升级？不行 直接卡住。
         * 读的时候进行写 就可能导致脏数据。
         */

        readLock.lock();
        System.out.println("------------查看");
        writeLock.lock();//获取写锁
        System.out.println("-------------写入");
        readLock.unlock();
        writeLock.unlock();
    }
}
