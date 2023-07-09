package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo4 {
    public static void main(String[] args) throws InterruptedException {

        Person4 person1 = new Person4();
        Person4 person2 = new Person4();

        new Thread(()-> {
            person2.game();
        },"AA").start();
        new Thread(()-> person1.study(),"BB").start();

    }
}

//两个同步方法，两个对象，这时候锁是不同的对象。互不干扰。
class Person4{
    public synchronized void game() {
        System.out.println("game");
    }
    public synchronized void study(){
        System.out.println("study");
    }
}
