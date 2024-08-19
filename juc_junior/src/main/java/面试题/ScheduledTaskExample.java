package 面试题;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTaskExample {

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        // 设置任务，每天凌晨2点执行
        scheduler.scheduleAtFixedRate(() -> {
            // 这里写你的读入数据和存储数据代码
            // 比如:
            // readDataFromSource();
            // storeDataToDatabase();
        }, 0, 24 * 60 * 60, TimeUnit.SECONDS);
    }
}
