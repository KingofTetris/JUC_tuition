package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo7 {
    public static void main(String[] args) throws InterruptedException {

        Person7 person = new Person7();

        new Thread(()-> person.study(),"BB").start();
        new Thread(()-> {
            person.game();
        },"AA").start();


    }
}

//一个静态同步，一个普通。一个对象
//这是不同的锁，互不影响。
class Person7{
    public static synchronized void game() {
        System.out.println("game");
    }
    public void study(){
        System.out.println("study");
    }
}
