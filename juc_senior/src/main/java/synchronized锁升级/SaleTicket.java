package synchronized锁升级;

import java.lang.ref.PhantomReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by KingOfTetris
 * @date 2023/7/5
 */

//演示多线程下，其实一段同步代码大多时候都是同一个线程访问，就没必要切换来切换去。非公平锁
    //引出偏向锁。
class Ticket{
    //票数量
    private int number = 100;
    //锁
    static final Object o = new Object();
    public void sale(){
        //共同资源是票，卖票就需要上锁。
        synchronized (o){
            if (number > 0){
                number--;
                System.out.println(Thread.currentThread().getName() + "卖了一张票,剩余" + number + "张");
            }
            else {
                System.out.println(Thread.currentThread().getName() + "准备卖票，但是余票不足");
            }
        }
    }
}
public class SaleTicket {
    public static void main(String[] args) {
        /**
         * 模拟3个售票员卖这100张票
         */
        //模拟三个卖票机
        Ticket ticket = new Ticket();
        new Thread(()->{for (int i = 0; i < 55; i++) {ticket.sale();}},"AA").start();
        new Thread(()->{for (int i = 0; i < 55; i++) {ticket.sale();}},"BB").start();
        new Thread(()->{for (int i = 0; i < 55; i++) {ticket.sale();}},"CC").start();
    }
}
