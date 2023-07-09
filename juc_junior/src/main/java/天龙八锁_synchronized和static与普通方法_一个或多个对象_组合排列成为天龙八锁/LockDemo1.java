package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo1 {
    public static void main(String[] args) throws InterruptedException {
        Person1 person = new Person1();
        new Thread(()-> person.game(),"AA").start();
        TimeUnit.SECONDS.sleep(1);
        new Thread(()-> person.study(),"BB").start();
    }
}
//两个同步方法，锁住的就是this对象，锁是同一个，先调用的先执行
class Person1{

    public synchronized void game(){
        System.out.println("game");
    }

    public synchronized void study(){
        System.out.println("study");
    }
}
