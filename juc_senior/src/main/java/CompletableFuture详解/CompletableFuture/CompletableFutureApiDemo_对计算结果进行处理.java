package CompletableFuture详解.CompletableFuture;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

import java.util.concurrent.*;


/**
 * 第一类比较简单前面都演示过了，就不再演示了
 * ● 获得结果和触发计算
 * ○ 获取结果
 * ■ public T get()
 * ■ public T get(long timeout,TimeUnit unit)
 * ■ public T join() --->和get一样的作用，只是不需要抛出异常
 * ■ public T getNow(T valuelfAbsent) --->计算完成就返回正常值，否则返默认值（传入的参数valuelfAbsent），
 * 立即获取结果不阻塞
 * ○ 主动触发计算
 * ■ public boolean complete(T value) ---->是否打断get/join方法立即返回括号值value
 * complete("我现在就要") + "\t" + get()/join()
 */

/**
 * @author Guanghao Wei
 * @create 2023-04-10 13:43
 */
public class CompletableFutureApiDemo_对计算结果进行处理 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        /**
         * 对计算结果进行处理的分类
         * 1.thenApply 异步任务的计算结果存在依赖，将这两个异步线程串行化
         * 2.handle 作用也是一样，
         * 两者的区别是thenApply出现异常就不会下一步，handle出现异常还是会往下走。
         */
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("11111111");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }, threadPool).thenApply(v -> {
//            int i = 10 / 0;即使第二步就出错，handle还是会处理，这个方法就有点类似finally
            System.out.println("222");
            return v + 2;
        }).thenApply(v -> {
            System.out.println("333333333");
            return v + 3;
        }).handle((f, e) -> {
            System.out.println("44444");
            return f + 4;
            //后面两部whenComplete和exceptionally就和一起之前一样
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("----计算结果" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getCause());
            return null;
        });
        System.out.println(Thread.currentThread().getName() + "------主线程先去做其他事情");
        //创建池子的时候第一件事就是记得把池子关掉，然后去写逻辑代码。不然存在OOM
        threadPool.shutdown();
    }
}

