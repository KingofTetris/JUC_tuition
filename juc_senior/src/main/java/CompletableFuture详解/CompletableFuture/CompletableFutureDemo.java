package CompletableFuture详解.CompletableFuture;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

import java.util.concurrent.*;

/**
 * FutureTask的缺点：
 * get的问题1 是容易阻塞问题，只要你去get结果，
 *              * 如果异步任务已经计算完了那很好你直接取
 *              * 如果没计算完，那么你就要等待futureTask get到结果才能到下一步，
 *              * 所以get一般只放到最后。
 * 解决办法
 * -----1.直接设置get的超时时间，get(3,TimeUtil.SECONDS)
 *   3s以后抛出超时异常，这个方法不建议使用，日志中会产生大量超时异常。
 * -----2.轮询futureTask的完成状态，调用isDone方法查询。
 *     完成后查询。但是轮询也不是特别好，CPU要一直访问异步任务是不是完成了，导致CPU空转。
 *
 * 主要就是由于future get的缺点，以及future api接口的缺乏，人们为了完成复杂的任务就推出了
 * CompletableFuture是JDK8以后推出的。
 * ①所以由于这个缺点,后面就推出了CompletableFuture，调用whenComplete(观察者模式)，可以让执行者通知监听方。
 * ②当然CompletableFuture还有其他优点，多个异步任务组合处理，类似ps -ef|grep tomcat
 * 异步任务之间也存在依赖关系，CompletableFuture就能很好地处理
 * ③对计算速度选最快的，返回计算完成第一名的结果。
 *
 * 对比一下你就会发现Future就只有
 * cancel
 * isCancelled
 * isDone
 * get
 * get(long,TimeUtil)
 * 6个API，CompletableFuture就多了去了。
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //不建议使用空参方法直接new 因为空参是未完成的对象
        /**
         * 建议用下面的runAsync,supplyAsync
         * 两个区别是有没用返回值
         */
//        CompletableFuture completableFuture = new CompletableFuture();

        //Void也是个类。没有返回
       /* CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName());
            //暂停
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });//不指定线程池默认ForkJoinPool.commonPool-worker-1
        System.out.println(completableFuture.get());*/
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
                System.out.println(Thread.currentThread().getName());
                //暂停
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "hello supplyAsync";
        },threadPool);
        //查看是哪个窗口在工作
        /**
         * completableFuture,get和join的区别在于编译时是否报错，get会让你编译时抛出异常，join不会。
         */
        System.out.println(completableFuture.get());
        System.out.println(completableFuture.join());
        threadPool.shutdown();
    }
}
