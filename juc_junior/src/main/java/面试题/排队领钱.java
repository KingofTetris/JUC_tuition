package 面试题;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/9/15
 */


//多线程模拟，5个窗口，每个窗口20人取钱，取完后进行汇总
public class 排队领钱 {
    static int money;
    final static Object lock = new Object();

    public static void main(String[] args) {
        List<Integer> moneys = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                //每个窗口20人领钱
                Random random = new Random();
                int moneyForThread = 0;
                for (int j = 0; j < 20; j++) {
                    int r = random.nextInt(101);
                    moneyForThread += r;
                    //你加上synchronized效率太低了。
                    synchronized (lock) {
                        money += r;
                    }
                }
                System.out.println(Thread.currentThread().getName() + ":moneyForThread = " + moneyForThread);
                moneys.add(moneyForThread);
            }, "t" + i).start();
        }
        System.out.println(moneys.stream().mapToInt(x -> x).sum());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("领钱总和是" + money);
    }
}
