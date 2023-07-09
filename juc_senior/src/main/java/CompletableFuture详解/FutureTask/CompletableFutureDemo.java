package CompletableFuture详解.FutureTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
/**
 * 在讲CompletableFuture之前，先讲一嘴Future接口。
 * Future接口（FutureTask实现类）定义了操作异步任务执行一些方法，
 * 如获取异步任务的执行结果、取消异步任务的执行、判断任务是否被取消、判断任务执行是否完毕等。
 * 举例：比如主线程让一个子线程去执行任务，子线程可能比较耗时，启动子线程开始执行任务后，
 * 主线程就去做其他事情了，忙完其他事情或者先执行完，
 * 过了一会再才去获取子任务的执行结果或变更的任务状态
 * （老师上课时间想喝水，他继续讲课不结束上课这个主线程，让班长去小卖部帮老师买水完成这个耗时和费力的任务）。
 * 班长就是处理这个异步任务的子线程，老师是主线程。
 * @author by KingOfTetris
 * @date 2023/6/28
 */


/**
 * 2.2.1 Future接口能干什么
 * Future是Java5新加的一个接口，
 * 它提供一种异步并行计算的功能，
 * 如果主线程需要执行一个很耗时的计算任务，
 * 我们会就可以通过Future把这个任务放进异步线程中执行，
 * 主线程继续处理其他任务或者先行结束，再通过Future获取计算结果。
 *
 * 2.2.2 Future接口相关架构
 * ● 目的：异步多线程任务执行且返回有结果，
 * 三个特点：多线程、有返回值、异步任务（班长为老师去买水作为新启动的异步多线程任务且买到水有结果返回）
 * ● 代码实现：Runnable接口+Callable接口+Future接口和FutureTask构造器。
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyThread());//FutureTask使用Callable接口的构造方法
        Thread thread = new Thread(futureTask, "t1");
        thread.start();
        System.out.println(futureTask.get());//  get就可以获取futureTask的返回值
    }
}

class MyThread implements Callable<String>{
    @Override
    public String call() throws Exception {
        System.out.println("------------come in all()");
        return "hello Callale";
    }
}
