package 线程池;

/**
 * @author by KingOfTetris
 * @date 2023/6/29
 */

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池的作用和连接池基本是一样的，你把连接换成线程两个字就完事了。
 * 就是尽可能少地创建线程，尽可能去复用线程。减少开销。
 *
 *
 *  public ThreadPoolExecutor(int corePoolSize,
 *                               int maximumPoolSize,
 *                               long keepAliveTime,
 *                               TimeUnit unit,
 *                               BlockingQueue<Runnable> workQueue，
 *                               ThreadFactory threadFactory,
 *                               RejectedExecutionHandler handler)
 *   corePoolSize 常驻线程数量
 *   maximumPoolSize 最大线程数量
 *
 *   //下面两个是个组合，超出常驻线程数量的线程，超出多久就关闭
 *   keepAliveTime  线程存活时间
 *    TimeUnit unit 存活时间单位
 *
 *    //ThreadFactory threadFactory
 *    线程工厂：线程池创建线程时调用的工厂方法，
 *    通过此方法可以设置线程的优先级、线程命名规则以及线程类型（用户线程还是守护线程）等。
 *
 *  //线程池的拒绝策略
 *  RejectedExecutionHandler handler
 *
 *  线程池的原理：
 *  1.线程池一开始是没用线程的，只有当年使用线程池的execute方法的时候，才会创建线程去调用Runnable接口的run方法
 *  这个时候线程池里面就会创建常驻线程去处理，
 *  2.当需要处理的线程数量超过常驻线程数量的时候就把这些等待的线程扔到阻塞队列里面去。
 *  3.如果阻塞队列也满了，线程池就会马力全开，创建最大线程数量。接待这个时候过来的线程，他们就是VIP(高峰期线程).
 *  3.1 如果能够处理线程需求了，那么经过线程存活时间以后，线程池就会关闭临时创建的线程
 *  4.如果线程池的线程数量已经满了以后还是没法应付现在的需求，那么这个时候就会使用拒绝策略了。
 *  有个前提拒绝策略需要实现 RejectedExecutionHandler 接口，
 *  并实现 rejectedExecution(Runnable r, ThreadPoolExecutor executor) 方法。
 *  不过 Executors 框架已经为我们实现了 4 种拒绝策略
 *  4.1拒绝策略有4种
 *      ----AbortPolicy（默认）：丢弃任务并抛出 RejectedExecutionException 异常。
         * CallerRunsPolicy：由调用线程处理该任务。谁调用的谁处理，哪来的回哪去。
         * DiscardPolicy：丢弃任务，但是不抛出异常。可以配合这种模式进行自定义的处理方式。
         * DiscardOldestPolicy：丢弃阻塞队列种等待最近，也就是最早的未处理任务，
 *       然后重新尝试执行任务。真要骂人辣，等了最久你还不处理，RNM退钱
 *
 */
public class ThreadPoolDemo {
    public static void main(String[] args) {

        /**
         * 但实际开发中下面这三种都不要用
         * 因为newFixedThreadPool和newSingleThreadExecutor的阻塞队列
         * 允许的请求队列长度是Integer.MAX_VALUE 也是21亿。
         * 这可能导致堆积大量的请求，从而OOM
         *
         * newCachedThreadPool也不要用，因为他的maximumPoolSize最大也是Integer.MAX_VALUE
         * 一样会导致OOM。
         *
         * 所以实际上我们都是自定义线程池，自己去写new ThreadPoolExecutor的七个参数
         */
        //一池固定n线程 ---》一个银行固定n个窗口
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);//固定5个窗口
        //一池1线程 ---》一个银行1个窗口
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();//一个池子一个窗口
        //一池可扩容线程 根据线程数决定
        ExecutorService threadPool3 = Executors.newCachedThreadPool();
        try {
            //10个人来办理业务
            //线程池execute调用线程的Runnable方法 这个时候线程才会创建
            for (int i = 0; i < 30; i++) {
                threadPool3.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "窗口正在处理业务");
                });
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            //关闭线程，或者说把线程扔回线程池中
            threadPool3.shutdown();
        }
    }

}
