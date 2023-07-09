package votile关键字原理;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */


class MyNumber{

    //即使加上volatile 也不能保证number最后一定是10000，没有原子性
    volatile int number;

    //原子性还是得加锁。
    public void addPlusPlus(){
        //复合操作不具有原子性 数据加载number,number+1，数据赋值
        /**
         *原子性是指一个操作是不可中断的，要么全做，要么全不做，一个操作一旦开始就不会受其他线程的影响。
         *
         * 因为volatile不对主内存加锁所有人都可以去主内存里面读写数据，
         * 对同一份数据进行操作时，如果前面的线程提交并发出通知
         * 那么后面的线程就会将当前操作作废，去读主内存的数据，出现写丢失的情况。
         * 这就是volatile只能保证可见性和有序性 不能保证原子性的原因。
         * 对于多线程下共享变量的写操作还是要加锁
         *
         * 因此啊，volatile对于啊才能遇到依赖当前值的计算最好不要用。比如i++,i=i+1;
         * volatile依赖可见性一般用在保存某个状态的boolean值或者int值
         */
        number++;
    }
}
public class VolatileNoAtomicDemo {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    myNumber.addPlusPlus();
                }
            }).start();
        }
        //不睡这1s 10个线程没运行完 主线程就结束了，
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}

        System.out.println(myNumber.number);
    }
}

