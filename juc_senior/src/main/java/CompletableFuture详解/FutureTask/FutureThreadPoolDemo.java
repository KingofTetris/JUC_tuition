package CompletableFuture详解.FutureTask;

import java.util.concurrent.*;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class FutureThreadPoolDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        long startTime = System.currentTimeMillis();
        //多个异步任务 一池固定3线程
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //异步任务1
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "asyn task1 over";
        });

        //异步任务2
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "asyn task2 over";
        });

        //线程池提交2个异步任务
        executorService.submit(futureTask1);
        executorService.submit(futureTask2);

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
         */
        System.out.println(futureTask1.get(3,TimeUnit.SECONDS));
        System.out.println(futureTask2.get(3,TimeUnit.SECONDS));
        //主线程自己运行第三个任务
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //把线程扔回线程池，保持复用。
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        System.out.println("-----------------异步任务耗时------------------");
        System.out.println("costTime：" + (endTime - startTime) + "ms");
        System.out.println();
        System.out.println("-----------------同步任务耗时------------------");
        m1();

    }

    public static void m1() {
        //同步任务 一件一件做
        long startTime = System.currentTimeMillis();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("costTime：" + (endTime - startTime) + "ms");
        System.out.println(Thread.currentThread().getName() + "\t-----------结束");
    }
}
