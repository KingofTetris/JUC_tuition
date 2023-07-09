package CompletableFuture详解.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author by KingOfTetris
 * @date 2023/6/30
 *
 *
 * TODO 可以套到你的项目里面成为重要吹逼资本。效率提高N-1倍。
 *
 * 切记：功能--->性能（完成--->完美）
 * 电商网站比价需求分析：
 * 1. 需求说明：
 *   a. 同一款产品，同时搜索出同款产品在各大电商平台的售价
 *   b. 同一款产品，同时搜索出本产品在同一个电商平台下，各个入驻卖家售价是多少
 * 2. 输出返回：
 *   a. 出来结果希望是同款产品的在不同地方的价格清单列表，返回一个List<String>
 * 例如：《Mysql》 in jd price is 88.05  《Mysql》 in taobao price is 90.43
 * 3. 解决方案，对比同一个产品在各个平台上的价格，要求获得一个清单列表
 *   a. step by step，按部就班，查完淘宝查京东，查完京东查天猫....
 *   b. all in，万箭齐发，一口气多线程异步任务同时查询
 *
 * 这里就是CompletableFuture的实战用法，比价！
 * 一个一个查，和异步一起查，效率差了n-1倍
 *  * 这种异步查询的方法大大节省了时间消耗，可以融入简历项目中，和面试官有所探讨
 */
public class CompletableFutureMallDemo {

    static List<NetMall> list = Arrays.asList(new NetMall("jd"),
            new NetMall("taobao"),
            new NetMall("dangdang"),
            new NetMall("pdd"),
            new NetMall("sn"),
            new NetMall("tmall"));
    /**
     * 流式调用
     * step by step
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPrice(List<NetMall> list, String productName) {
        //《Mysql》 in jd price is 88.05
        return list
                .stream()
                .map(netMall ->
                        //String.format(String,Obj..obj) 保留2位小数
                        //两个占位符%s,%.2f
                        String.format("《" + productName + "》" + "in %s price is %.2f",
                                netMall.getNetMallName(),
                                netMall.calcPrice(productName)))
                .collect(Collectors.toList());
    }

    /**
     *
     * List<NetMall>---->List<CompletableFuture<String>>----->List<String>
     * all in
     * 把list里面的内容映射给CompletableFuture()
     * @param list
     * @param productName
     * @return
     */
    public static List<String> getPriceByCompletableFuture(List<NetMall> list, String productName) {
        return list.stream().//第一次映射
                map(netMall -> CompletableFuture.supplyAsync(() ->
                        String.format("《" + productName + "》" + "in %s price is %.2f",
                                netMall.getNetMallName(),
                                netMall.calcPrice(productName)))) //Stream<CompletableFuture<String>>
                .collect(Collectors.toList()) //List<CompletableFuture<String>>
                .stream()//第二次映射 Stream<String>
                .map(s -> s.join()).collect(Collectors.toList()); //List<String>
    }

    public static void main(String[] args) {
        /**
         * 采用step by setp方式查询
         * 《masql》in jd price is 110.11
         * 《masql》in taobao price is 109.32
         * 《masql》in dangdang price is 109.24
         * ------costTime: 3094 毫秒
         */
        long StartTime = System.currentTimeMillis();
        List<String> list1 = getPrice(list, "mysql");
        //从普通list中一个一个去查
        for (String element : list1) {
            System.out.println(element);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("------costTime: " + (endTime - StartTime) + " 毫秒");
        /**
         * 采用 all in三个异步线程方式查询
         * 《mysql》in jd price is 109.71
         * 《mysql》in taobao price is 110.69
         * 《mysql》in dangdang price is 109.28
         * ------costTime1009 毫秒
         */
        long StartTime2 = System.currentTimeMillis();
        List<String> list2 = getPriceByCompletableFuture(list, "mysql");
        for (String element : list2) {
            System.out.println(element);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("------costTime" + (endTime2 - StartTime2) + " 毫秒");
    }
}

@Data
class NetMall {
    private String netMallName;

    public double calcPrice(String productName) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0);
//        return String.format("%.2f", ThreadLocalRandom.current().nextDouble() * 2 + productName.charAt(0));
    }

    public NetMall(String netMallName) {
        this.netMallName = netMallName;
    }

    public NetMall(){}
}
