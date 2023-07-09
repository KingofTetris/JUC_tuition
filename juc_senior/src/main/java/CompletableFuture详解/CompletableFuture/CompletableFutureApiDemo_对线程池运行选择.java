package CompletableFuture详解.CompletableFuture;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

import java.util.concurrent.*;

/**
 * @author Guanghao Wei
 * @create 2023-04-10 13:43
 *   ○ CompletableFuture和线程池说明
 *     ■ 如果没有传入自定义线程池，都用默认线程池ForkJoinPool
 *     ■ 传入一个线程池，如果你执行第一个任务时，传入了一个自定义线程池
 *       1.
 *       thenRun使用线程池原则是跟随原则，跟随上一个对结果处理方法。
 *       如果你上一个使用自定义，下一个也会使用自定义。
 *       2.但是thenRunAsync会自动切换到ForkJoinPool默认线程池。因为他的源码就是当年的内核大于1时候，选择默认线程池
 *     ■ 备注：可能是线程处理太快，系统优化切换原则，
 *     直接使用main线程处理，thenAccept和thenAcceptAsync，thenApply和thenApplyAsync等，之间的区别同理。
 * ● 对计算速度进行选用
 *   ○ 谁快用谁
 *   ○ applyToEither
 */
public class CompletableFutureApiDemo_对线程池运行选择 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture.supplyAsync(() -> {
            System.out.println("1号任务" + Thread.currentThread().getName());
            return 1;
        }, threadPool).thenApply(v -> {
            System.out.println("2号任务" + Thread.currentThread().getName());
            return v + 2;
        }).thenApplyAsync(v -> {
            System.out.println("3号任务" + Thread.currentThread().getName());
            return v + 3;
        }).whenComplete((v,e)->{
            System.out.println(v);
            threadPool.shutdown();
        }).exceptionally(e->{
            threadPool.shutdown();
            //最后记得加上return
            return null;
        });
//        System.out.println(Thread.currentThread().getName() + "------主线程先去做其他事情");
        //创建池子的时候第一件事就是记得把池子关掉，然后去写逻辑代码。不然存在OOM
//        threadPool.shutdown();
    }
}
