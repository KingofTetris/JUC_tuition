package 锁入门;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */


/**
 *首先锁是由管程(Monitor)实现的
 * TODO synchronized底层就是使用了monitorenter和monitorexit两个指令来实现锁
 * 一个拿锁，两个放锁，一个是正常情况，一个是异常情况。保证你一定会释放锁。
 * 同步方法底层在jvm层面会有一个ACC_SYNCHRONIZED符号位记录是否是同步方法
 * 如果是就会去抢对象锁，一样是一个进入，两个出去。
 * 如果是静态同步方法就会多一个ACC_STATIC符号位，去抢类锁。
 */
public class TicketDemo {
    public static void main(String[] args) {
        //模拟201人抢票200张票
        for (int i = 0; i < 201; i++) {
            Thread thread = new Thread(new MyThread());
            thread.start();
        }
    }
}


class MyThread implements Runnable{
    static final Object obj = new Object();
    static int ticketNum = 200; //资源也是贡献的，你要用static修饰，不然就变成普通成员变量了。
    @Override
    public void run() {
        synchronized (obj){
            if (ticketNum <= 0){
                System.out.println("余票不足");
            }
            else {
                ticketNum--;
                System.out.println(Thread.currentThread().getName() + "买了一张票,剩余" + ticketNum + "张");
            }
        }
    }
}
