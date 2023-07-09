package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo6 {
    public static void main(String[] args) throws InterruptedException {

        Person6 person1 = new Person6();
        Person6 person2 = new Person6();



        new Thread(()-> person2.study(),"BB").start();

        new Thread(()-> {
            person1.game();
        },"AA").start();


    }
}

//两个静态同步方法，两个对象
//只要被被static修饰，锁的对象就是Class模板对象，这个锁全局唯一。
//只和你的调用顺序有关。
class Person6{
    public static synchronized void game() {
        System.out.println("game");
    }
    public static synchronized void study(){
        System.out.println("study");
    }
}
