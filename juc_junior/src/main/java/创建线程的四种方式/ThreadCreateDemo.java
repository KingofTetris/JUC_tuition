package 创建线程的四种方式;

import java.util.concurrent.*;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */


/**
 * Runnable和Callable
 * 不同点
 *  (1)Callable规定的方法是call()，Runnable规定的方法是run()。
 *  其中Runnable可以提交给Thread来包装下，直接启动一个线程来执行，而Callable则一般都是提交给ExecuteService来执行。
 *  (2)Callable的任务执行后可返回值，而Runnable的任务是不能返回值得
 *  (3)call方法可以抛出异常，run方法不可以
 *  (4)运行Callable任务可以拿到一个Future对象，c表示异步计算的结果。
 *
 *  注：Callalbe接口支持返回执行结果，需要调用FutureTask.get()得到，
 *  此方法会阻塞主进程的继续往下执行，如果不调用不会阻塞。
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
        //callable的返回值需要用futuretask对象.get()获得
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


