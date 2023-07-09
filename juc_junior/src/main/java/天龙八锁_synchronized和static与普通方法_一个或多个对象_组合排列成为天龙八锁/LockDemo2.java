package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Person2 person = new Person2();
        new Thread(()-> {
            try {
                person.game();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(()-> person.study(),"BB").start();
    }
}
//两个同步方法，但是其中一个先睡1s，但无所谓，锁的对象还是一样的this，谁先拿到谁执行，所以执行顺序还是game ,study
class Person2{
    public synchronized void game() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("game");
    }
    public synchronized void study(){
        System.out.println("study");
    }
}
