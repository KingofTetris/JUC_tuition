package votile关键字原理;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/7/1
 */
public class VolatileSeeDemo {

//    static boolean flag = true;
    static volatile boolean flag = true;

    public static void main(String[] args) {
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t come in");
            while (flag){
//                System.out.println("----volatile原理");
            }
            System.out.println(Thread.currentThread().getName() + "\t flag被修改，程序停止");
        },"t1").start();

        //先睡几秒保证t1先启动
        try{ TimeUnit.SECONDS.sleep(2);}catch(InterruptedException e){e.printStackTrace();}

        flag = false;

        System.out.println(Thread.currentThread().getName() + "\t flag状态" + flag);
    }
}
