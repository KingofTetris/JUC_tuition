package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo5 {
    public static void main(String[] args) throws InterruptedException {

        Person5 person = new Person5();

        new Thread(()-> {
            person.game();
        },"AA").start();
        new Thread(()-> person.study(),"BB").start();

    }
}

//两个静态同步方法，一个对象
//只要被被static修饰，锁的对象就是Class模板对象，这个锁全局唯一。
//只和你的调用顺序有关。
class Person5{
    //注意static。锁的是class模板
    public static synchronized void game() {
        System.out.println("game");
    }
    public static synchronized void study(){
        System.out.println("study");
    }
}
