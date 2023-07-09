package ThreadLocal详解;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author by KingOfTetris
 * @date 2023/7/4
 */

class MyData{
    ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(()->0);
    public void add(){
        threadLocal.set(threadLocal.get() + 1);
    }
}
public class ThreadLocalDemo2 {

    public static void main(String[] args) {
        MyData myData = new MyData();
        //创建固定大小为3的线程池，实际工作中都是自定义线程池。为什么3个自带线程池有问题 你如果忘了请复习
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            for(int i = 1; i <= 10; i++) {
                threadPool.submit(()->{
                    try {
                        //线程复用就可能导致里面的内存溢出，线程池搭配threadLocal要记得remove
                        Integer before = myData.threadLocal.get();
                        myData.add(); //让10个线程++
                        Integer after = myData.threadLocal.get();
                        System.out.println(Thread.currentThread().getName() + "\t before:" + before +
                                "\t after:" + after);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        myData.threadLocal.remove();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //创建线程池就要记得复用。
            threadPool.shutdown();
//            myData.threadLocal.remove();
        }
    }
}
