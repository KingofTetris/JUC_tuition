package ThreadLocal详解;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author by KingOfTetris
 * @date 2023/7/4
 * 在继续ThreadLocalMap为什么用虚引用前，我们先演示一下什么是强软弱虚 四大引用
 */

class MyObject{
    //这个方法本质就像C++中的析构函数，他的产生就是为了迎合C++程序员
    //也就是一个对象的死前遗言(被GC前)就可以在这里面写，但用不好，就有可能导致对象又复活
    //现在这个方法已经被丢弃了。
    @Override
    protected void finalize() throws Throwable {
        System.out.println("------------invoke finalized method!!!!!!!!");
    }
}
public class ReferenceDemo {
    public static void main(String[] args) {
        //强引用 强引用 带编制就是硬，只要你不手动把他设置为null，他就会不会被回收。
//        StrongReference();
        //软引用 说白了就是强引用的青春版，内存够用的时候 浪费一点就浪费一点，不够用了才回收。
        /**
         * 拓展一下软引用的场景，如果某个APP需要读取大量的本地图片，比如微信聊天记录里面的图片。
         * 1.如果每次加载图片都要去硬盘读，那么就非常慢了
         * 2.如果一次性全部加载到内存，就有可能OOM
         *
         * 这个时候软引用就很有用了
         *
         *  思路是这样的：用一个HashMap来保存 图片的路径 和 相应图片的软引用 ，分别作为KV
         *  当内存不足的时候，JVM就会自懂回收一些缓存图片对象占用的空间，有效避免OOM
         *
         *  Map<String,SoftReference<BitMap>> imageCache = new HashMap<>();
         *
         */
//        SoftRefenrence();
        //弱引用 就比软引用还垃圾了，内存不管够不够用，只要一GC，就把他干掉。
//        WeakReference();
        //虚引用 这哥们比较抽象，也不是JUC这个课程重点，先看代码 听不懂的话，去看宋红康JVM
        /**
         * 与其他引用类型不同，虚引用的作用并不是用于获取对象的引用，而是用于跟踪对象被垃圾回收的状态。
         * 仅仅是提高了一种对象被finalize以后，做某些事情的通知机制。
         * 听不懂的话，去看宋红康JVM
         */
        PhantomReference();
    }

    private static void PhantomReference() {
        MyObject myObject = new MyObject();
        ReferenceQueue<MyObject> queue = new ReferenceQueue<>();
        //把myObject改成虚引用
        PhantomReference<MyObject> phantomReference = new PhantomReference<>(myObject,queue);

        List<byte[]> list = new ArrayList<>();

        new Thread(()->{
            while (true){
                list.add(new byte[1 * 1024 * 1024]);
                try{ TimeUnit.MILLISECONDS.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                System.out.println(phantomReference.get() + "\t 1MB add ok");
            }
        },"t1").start();

        new Thread(()->{
            while (true){
                Reference<? extends MyObject> mObject = queue.poll();
                if (mObject != null){
                    System.out.println(mObject);
                    System.out.println("有虚引用对象被回收进入了引用队列");
                    break;
                }
            }
        },"t2").start();
    }

    private static void WeakReference() {
        WeakReference<MyObject> weakReference = new WeakReference<>(new MyObject());
        System.out.println("---------gc before内存够用:" + weakReference.get());
        System.gc();
        System.out.println("---------gc after内存够用:" + weakReference.get());
    }

    private static void SoftRefenrence() {
        //Java里面直接使用new SoftReference实现
        SoftReference<MyObject> myObjectSoftReference = new SoftReference<>(new MyObject());
        System.out.println("----------softReference:" + myObjectSoftReference.get());
        System.gc();
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        System.out.println("---------gc after内存够用:" + myObjectSoftReference.get());

        try {
            byte[] bytes = new byte[20 * 1024 * 1024];//20MB
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //手动设置虚拟机参数，-Xms10m -Xmx10m
            // 让这个程序的最大运行内存只有10M，那么你创建20MB的内存不够用OOM，软引用的对象就会被回收。
            System.out.println("gc after内存不够" + myObjectSoftReference.get());
        }
    }

    private static void StrongReference() {
        /**
         * myObject就是一个强引用，他引用了一个MyObject对象
         * 1.强引用可以阻止被引用的对象被垃圾回收器回收。只要强引用存在，垃圾回收器就不会回收被引用的对象。
         * 2.强引用可以直接访问到对象的所有方法和字段，因为它们之间的关系是直接的
         */

        /**
         * 当一个对象没有任何强引用指向它时，它就变成了不可达对象，
         * 会被垃圾回收器标记并在合适的时机进行回收。强引用所引用的对象只有在以下情况下才会被回收：
         *
         * 强引用被显式地设置为null，解除了引用关系。
         * 当前对象的作用域结束，超出作用域范围后，不再有任何强引用指向它。
         * 系统内存不足，垃圾回收器被触发，回收无法再访问的对象。
         */
        MyObject myObject = new MyObject();//这样new出来的对象就是强引用，只要强引用存在，垃圾回收器就不会回收被引用的对象。
        System.out.println("gc before \t " + myObject);
        myObject = null;//手动解除引用关系
        System.gc();//手动GC，给GC线程发出通知，什么时候GC，也是GC这个守护线程自己决定。
        try{ TimeUnit.SECONDS.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
        System.out.println("gc after \t" + myObject);
    }
}
