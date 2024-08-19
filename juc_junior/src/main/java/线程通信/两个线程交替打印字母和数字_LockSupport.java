package 线程通信;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
/**
 * @author by KingOfTetris
 * @date 2023/9/6
 */
//LockSupport两个重要方法,park和unpark.
    //park阻塞自己，unpark等待别人发通行证给自己
public class 两个线程交替打印字母和数字_LockSupport {
    static Thread t1 = null, t2 = null;
    public static void main(String[] args) throws Exception{
        t1 = new Thread(()->{
            for(char p = 'A'; p <= 'Z'; p++) {
                System.out.print(p);
                LockSupport.unpark(t2); //因为要在t1里面用t2，就只能全部先创建好写在为什么的static静态变量里面。
                LockSupport.park();
            }
        },"t1");
        t2 = new Thread(()->{
            for(int i = 1;i<=26;i++){
                LockSupport.park();
                System.out.print(i);
                LockSupport.unpark(t1);
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
