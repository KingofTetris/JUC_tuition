package votile关键字原理;

import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

//演示四大屏障
class VolatileTest{
    int i = 0;
    //volatile
    volatile boolean flag = false;
    public void write(){
        i = 2;//普通写
        flag = true;//volatile写
    }
    public void read(){
        if(flag){ //volatile读
            System.out.println("----i=" + i);//普通读
        }
    }
}
public class VolatileFourMemoryBarrier {
    public static void main(String[] args) {
        VolatileTest volatileTest = new VolatileTest();
        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                volatileTest.write();
            }).start();
        }
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        for (int i = 0; i < 1000; i++) {
            new Thread(()->{
                volatileTest.read();
            }).start();
        }
    }
}
