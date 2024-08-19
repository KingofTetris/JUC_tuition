package 线程池;

import java.util.concurrent.*;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */
public class ThreadPoolCustomer {
    /**
     * 下面7个核心参数非常重要
     *
     *    public ThreadPoolExecutor(int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue,
     *                               ThreadFactory threadFactory,
     *                               RejectedExecutionHandler handler) {
     */

    public static void main(String[] args) {

        //自定义线程池
        ExecutorService threadPool = new ThreadPoolExecutor(2,
                5,
                2L,
                TimeUnit.SECONDS,
                //长度为3的阻塞队列
                new ArrayBlockingQueue<>(3),
                //默认线程工厂
                Executors.defaultThreadFactory(),
                //超出的不作任何处理
                new ThreadPoolExecutor.AbortPolicy());

            try {
                //10个人来办理业务
                //线程池execute调用线程的Runnable方法 这个时候线程才会创建
                for (int i = 0; i < 6; i++) {
                    threadPool.execute(() -> {
                        System.out.println(Thread.currentThread().getName() + "窗口正在处理业务");
                    });
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                //关闭线程，或者说把线程扔回线程池中
                threadPool.shutdown();
            }
    }
}
