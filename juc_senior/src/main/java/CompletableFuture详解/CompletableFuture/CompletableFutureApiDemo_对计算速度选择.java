package CompletableFuture详解.CompletableFuture;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 */

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class CompletableFutureApiDemo_对计算速度选择 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture<String> playA = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("A come in");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "playA";
        }, threadPool);


        CompletableFuture<String> playB = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("B come in");
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "playB";
        }, threadPool);

        ///A.applyToEither(B,f) 谁运行短 谁就是赢家.
        CompletableFuture<String> result = playA.applyToEither(playB, f -> {
            return f + " is winner";
        });

        /**
         * A come in
         * B come in
         * main-----------winner:playA is winner
         */
        System.out.println(Thread.currentThread().getName() + "-----------winner:" + result.join());
        threadPool.shutdown();
    }
}
