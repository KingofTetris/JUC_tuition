package CompletableFuture详解.CompletableFuture;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

import java.util.concurrent.*;
/**
 * @author Guanghao Wei
 * @create 2023-04-10 13:43
 */
public class CompletableFutureApiDemo_对计算结果进行消费 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        /**
         * 对计算结果进行消费的分类
         * 1.thenApply 异步任务的计算结果存在依赖，将这两个异步线程串行化
         * 2.handle 作用也是一样，
         * 两者的区别是thenApply出现异常就不会下一步，handle出现异常还是会往下走。
         */
      /*  ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture.supplyAsync(() -> {
            return 1;
            //thenRun 不需要返回结果
            //thenApply 任务串行化，需要上一步的结果，自己也有返回值
        }, threadPool).thenApply(v -> {
            return v + 2;
        }).thenApply(v -> {
            return v + 3;
            //前三步完成以后，Accept拿着上一步的结果消费一下，自己没有无返回结果
        }).thenAccept(r ->{
            System.out.println(r);
        });
        System.out.println("主线程忙碌中");
        threadPool.shutdown();
*/
    /*      ○ 对比补充
    ■ thenRun(Runnable runnable) :任务A执行完执行B，并且不需要A的结果
    ■ thenAccept(Consumer action): 任务A执行完执行B，B需要A的结果，但是任务B没有返回值
    ■ thenApply(Function fn): 任务A执行完执行B，B需要A的结果，同时任务B有返回值*/
        System.out.println(CompletableFuture.supplyAsync(() -> "result")
                        .thenRun(() -> {})
                        .join());//null

        System.out.println("---------------------------");
        System.out.println(CompletableFuture.supplyAsync(() -> "result")
                .thenAccept(System.out::println)
                .join());//result null
        System.out.println("----------------------------");

        System.out.println(CompletableFuture.supplyAsync(() -> "result")
                .thenApply(f -> f + 2)
                .join());//result2
    }
}

