package 可重入锁_也叫递归锁;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */
public class SyncLockDemo {
    public synchronized  void add(){
        add();
    }
    public static void main(String[] args) {
//        new SyncLockDemo().add();//ion in thread "main" java.lang.StackOverflowError
        //栈溢出说明这方法虽然加上了synchronized关键字，但是这个锁是可重入的(递归)的
        //add里面再调用add也不会出现错误，才会导致栈溢出

        //Lock演示，也是可重入锁
        Lock lock = new ReentrantLock();
        new Thread(()->{
            try{
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "::Lock外层");
                //再套一层
                try{
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + "::Lock内层");
                }finally {
                    lock.unlock();//你自己不释放所，对你自己是无所谓的，你都可以进，但是你把锁占了，别人就进不去了。
                }
            }finally {
                lock.unlock();
            }
        },"B").start();

        new Thread(()->{
            try{
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "::Lock");
            }finally {
                lock.unlock();
            }
        },"C").start();

        //演示synchronized可重入，拿到了o没释放也能进入中层和外层。
      /*  Object o = new Object();
        new Thread(()->{
            synchronized (o){
                System.out.println(Thread.currentThread().getName() + "外层");
                synchronized (o){
                    System.out.println(Thread.currentThread().getName() + "中层");
                    synchronized (o){
                        System.out.println(Thread.currentThread().getName() + "内层");
                    }
                }
            }
        },"A").start();*/
    }

}
