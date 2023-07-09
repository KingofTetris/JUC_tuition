package 创建线程的四种方式;

import java.util.concurrent.*;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */
public class ThreadCreateDemo {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //方法1 继承Thread类 重写run方法
        new Thread(new MyThread1(),"AA").start();

        //方法2 实现Runnable接口，重写run方法
        new Thread(new MyThread2(),"BB").start();

        //方法三要配合FutureTask使用，不然就没意义了
        MyThread3 thread3 = new MyThread3();
        FutureTask<String> futureTask = new FutureTask<>(thread3);
        new Thread(futureTask,"线程3").start();
        System.out.println(futureTask.get());

        //方法4 线程池
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(new Thread(new MyThread1(),"AA"));
        pool.submit( new Thread(new MyThread2(),"BB"));
        pool.submit(new Thread(futureTask,"CC"));
        pool.shutdown();
    }

}
class MyThread1 extends Thread{
    //为什么run方法没有返回值？
    /**
     * 由于线程执行是异步的，没有直接的返回值可以返回给调用者。
     * 如果在run方法中定义了返回值,那么调用者在调用线程的start方法后就无法获取到该返回值，
     * 因为线程的执行是在后台进行的。
     */
    @Override
    public void run() {
        System.out.println("方法1");
    }
}

class MyThread2 implements Runnable{

    @Override
    public void run() {
        System.out.println("方法2");
    }
}


class MyThread3 implements Callable<String>{
    //call方法无法正常取得返回值是会报异常的。
    @Override
    public String call() throws Exception {
        return "方法三";
    }
}


