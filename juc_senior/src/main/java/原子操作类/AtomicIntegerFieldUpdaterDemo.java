package 原子操作类;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Atomic FieldUpdater解决了synchronized的问题，
 * 之前你要改变一个非现场安全的对象中某个值你就需要给获取方法加上锁
 * 现在你只需要修改这个字段为public volatile就可以了，不再需要加锁。
 *
 * ● 更新的对象属性必须使用public volatile修饰符
 * ● 因为对象的属性修改类型原子类都是抽象类，
 * 所以每次使用都必须使用静态方法newUpdater()创建一个更新器，并且需要设置想要更新的类和属性
 */

class BankAccount{
    public volatile int money = 0;
    AtomicIntegerFieldUpdater<BankAccount> fieldUpdater =
            //非现场安全对象类和字段名
            AtomicIntegerFieldUpdater.newUpdater(BankAccount.class,"money");

    //不加synchronized 保证高性能原子性
    public void transferMoney(BankAccount bankAccount){
        fieldUpdater.getAndIncrement(bankAccount);
    }

    public synchronized void transferSyncMoney(){
        money++;
    }
}
public class AtomicIntegerFieldUpdaterDemo {

    public static void main(String[] args) throws InterruptedException {
        synchronizedTransfer();
        fieldUpdater();
    }

    private static void synchronizedTransfer() throws InterruptedException {
        BankAccount bankAccount = new BankAccount();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    bankAccount.transferSyncMoney();
                }
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("costTime：" + (endTime - startTime) + "ms");
        System.out.println(Thread.currentThread().getName() + "\t" +"result:" + bankAccount.money);
    }

    private static void fieldUpdater() throws InterruptedException {
        BankAccount bankAccount = new BankAccount();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    bankAccount.transferMoney(bankAccount);
                }
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        System.out.println("costTime：" + (endTime - startTime) + "ms");
        System.out.println(Thread.currentThread().getName() + "\t" +"result:" + bankAccount.money);
    }
}
