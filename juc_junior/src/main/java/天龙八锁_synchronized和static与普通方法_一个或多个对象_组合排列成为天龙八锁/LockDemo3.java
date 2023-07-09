package 天龙八锁_synchronized和static与普通方法_一个或多个对象_组合排列成为天龙八锁;

/**
 * @author by KingOfTetris
 * @date 2023/6/28
 */
public class LockDemo3 {
    public static void main(String[] args) throws InterruptedException {

        Person3 person = new Person3();
        new Thread(()-> person.study(),"BB").start();

        new Thread(()-> {
            try {
                System.out.println("AA");
                person.game();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"AA").start();

    }
}
//一个同步方法，一个普通方法，这个时候普通方法先执行，不受锁影响。
//但是要注意，BB得放前面，因为AA没人跟他竞争，他相当于也是普通方法。
class Person3{
    public synchronized void game() throws InterruptedException {
        System.out.println("game");
    }
    public void study(){
        System.out.println("study");
    }
}
