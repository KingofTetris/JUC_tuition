package CompletableFuture详解.CompletableFuture;

import java.sql.Time;
import java.util.concurrent.*;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */
public class CompletableFutureUseDemo {


    /**
     * 不一样的用法进行改进。
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "come in");
            int result = ThreadLocalRandom.current().nextInt(100);
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            System.out.println("1秒钟后出结果，结果是:" + result);
            //模拟异常
//            int i = 1 / 0;
            //supplyAsync有返回值
            return result;
            //优点1.异步任务结束，whenComplete完成通知观察者，自动回调get方法
            //优点2，主线程不再关系异步任务的执行，异步任务之间可以whenComplete完成通知观察者，去拿结果。
        },threadPool).whenComplete((v,e)->{
            //没有异常
            if (e == null){System.out.println("-------计算完成，更新系统中某个值为" + v);}
            //出现异常
            //优点3, 异步任务出错会自动回调exceptionally方法
        }).exceptionally(e->{
            e.printStackTrace();
            System.out.println("出现异常");
            System.out.println("异常情况"+e.getCause() + "\t" + e.getMessage());
            return null;//异常返回null
        });

        System.out.println("主线程去忙其他任务了---------------");
        //手动关闭线程池
        threadPool.shutdown();
        //主线程阻塞3s，防止主线程太快结束，默认线程池就关闭了，异步任务用不到。
//        TimeUnit.SECONDS.sleep(3);
    }

    /**
     * 和Future完全一样的用法
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void CompletableFutureUseLikeFuture() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "come in");
            int result = ThreadLocalRandom.current().nextInt(100);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1秒钟后出结果，结果是:" + result);
            //supplyAsync有返回值
            return result;
        });
        System.out.println("主线程去忙其他任务了---------------");
        System.out.println(completableFuture.get());
    }
}
