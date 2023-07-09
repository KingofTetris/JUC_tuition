package 读写锁;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */
public class 读写锁降级 {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();//读锁
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();//写锁
        //可以从写锁降级为读锁，不能从读锁升级为写锁
        writeLock.lock(); //
        System.out.println("写xxxxxxxxxxxxx");

        readLock.lock();
        System.out.println("读xxxxxxxxxxxxx");

//        writeLock.lock(); //
//        System.out.println("写xxxxxxxxxxxxx");
    }
}
