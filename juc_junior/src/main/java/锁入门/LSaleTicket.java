package 锁入门;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */

class LTicket{
    //票数量
    private int number = 300;

    //可重入锁 无参默认是非公平锁
    //非公平锁能者多劳，其他人可能"饿死"。
    //synchronized只支持非公平锁
    /**
     * 执行效率高，但是可能出现线程饥饿.
     * 只要锁空闲了就去争抢，就有可能导致某几个线程一直抢到，其他线程抢不到的情况。
     */
    private final ReentrantLock lock = new ReentrantLock();

    //改为true就是公平锁
    /**
     * 执行效率相对要低，但是线程都会拿到资源，不会饥饿。
     * 出现锁了会排队等待。一定会轮流到所有的线程
     */
//    private final ReentrantLock lock = new ReentrantLock(true);

    public void sale(){
        //上锁
        lock.lock();
        try {
            if (number > 0){
                number--;
                System.out.println(Thread.currentThread().getName() + "卖了一张票,剩余" + number + "张");
            }
            else {
                System.out.println(Thread.currentThread().getName() + "准备卖票，但是余票不足");
            }
        }
        catch (Exception e){
            System.out.println(e);//有异常就输出一下
        }
        finally {
            //和synchronized不一样，关键字会自动释放锁，LOCK你得手动保证Lock最终一定会解锁
            lock.unlock();
        }
    }
}
public class LSaleTicket {
    public static void main(String[] args) {

        //模拟三个卖票机
        LTicket lTicket = new LTicket();
        new Thread(()->{
            for (int i = 0; i < 200; i++) {
                lTicket.sale();
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                lTicket.sale();
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 100; i++) {
                lTicket.sale();
            }
        },"CC").start();
    }
}
