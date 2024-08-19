package votile关键字原理;

/**
 * 非常简单的饿汉式，直接创建好instance，用get返回。
 *
 * 单例模式的应用场景
 * 配置管理器：在应用程序中，配置信息通常只需要读取一次，并全局使用。单例模式用于确保配置管理器只被实例化一次。
 *
 * 日志记录器：一个系统中通常只需要一个日志记录器来记录所有的日志信息，使用单例模式可以避免日志文件的重复写入。
 *
 * 数据库连接池：数据库连接是一种有限的资源，使用单例模式可以确保数据库连接池的唯一性，并且能够重用连接，减少连接创建和销毁的开销。
 *
 * 线程池：类似于数据库连接池，线程池也是有限的资源，使用单例模式可以避免创建过多的线程，提高应用程序的并发性能。
 *
 * 任务调度器：在需要全局调度和管理的场景下，如定时任务调度器，单例模式提供了一个集中的管理方式。
 *
 * 网站的计数器：一般也是采用单例模式实现，否则难以同步。
 * ————————————————
 *
 *                             版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
 *
 * 原文链接：https://blog.csdn.net/m0_63653444/article/details/140007103
 *
 *
 *
 * 在Spring框架中，单例模式是通过工厂模式实现的。具体而言，Spring通过一个工厂类（通常是BeanFactory或ApplicationContext）来管理所有的对象实例，该工厂类负责创建、初始化和缓存所有的单例对象。Spring框架中的单例模式是一种非常重要的设计模式，下面我们来看一下几个比较重要的应用。
 *
 *   （1）ApplicationContext
 *
 *   ApplicationContext是Spring框架的核心容器，用于管理Bean的生命周期和依赖注入。ApplicationContext在初始化的过程中会创建多个Bean实例，但是ApplicationContext本身是以单例模式实现的。
 *
 *   （2）BeanFactory
 *
 *    BeanFactory是Spring框架中的一个接口，它定义了获取Bean的方法。在Spring中，可以通过ApplicationContext获取Bean，而ApplicationContext实际上是BeanFactory的一个实现类。
 *
 *    几乎所有的ApplicationContext实现都是以单例模式实现的，因为ApplicationContext的初始化代价较高，同时它被用来获取Bean，应保证整个应用中只有一个实例。
 *
 *   （3）BeanPostProcessor
 *
 *    BeanPostProcessor是Spring框架中的一个接口，它定义了在Bean初始化前后进行自定义处理的方法。BeanPostProcessor提供了一种拦截器机制，允许我们在Bean被实例化和初始化的过程中进行额外的处理。
 *
 *    BeanPostProcessor通常是以单例模式实现的。因为在整个容器的生命周期中，BeanPostProcessor的实例很少发生变化，而且它们通常没有状态。
 *
 *   （4）DefaultListableBeanFactory
 *
 *    DefaultListableBeanFactory是Spring框架中的一个类，它是BeanFactory的一个默认实现。DefaultListableBeanFactory管理了一个Bean定义的注册表，并提供了获取Bean的功能。 DefaultListableBeanFactory使用单例模式实现，保证整个应用中只有一个实例。
 *
 *   （5）AbstractAutowireCapableBeanFactory
 *
 *    AbstractAutowireCapableBeanFactory是Spring框架中的一个类，它是BeanFactory的抽象实现，并且提供了自动装配的能力。
 *
 *    在获取Bean的过程中，AbstractAutowireCapableBeanFactory会检查Bean定义的自动装配模式，然后通过反射机制实例化Bean，为其属性进行依赖注入。 AbstractAutowireCapableBeanFactory使用单例模式实现，确保整个应用中只有一个实例。
 *
 *   （6）HandlerMapping
 *
 *    HandlerMapping是Spring框架中的一个接口，用于将请求映射到处理程序并返回处理程序对象。在Web应用中，HandlerMapping在容器启动时会被实例化，并设置到DispatcherServlet中。
 *
 *    HandlerMapping通常以单例模式实现。因为在整个应用的运行过程中，映射关系不会发生改变，只需要一个实例来进行请求映射即可。
 *
 *    以上只是列举了一部分Spring框架中使用单例模式实现的类，由于Spring框架十分庞大，涵盖的功能很多，还有很多其他类也是以单例模式实现的。如果大家感兴趣可以深入研究一下源码。
 * ————————————————
 *
 *                             版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
 *
 * 原文链接：https://blog.csdn.net/m0_73311735/article/details/132379123
 */
public class EHSSingleton {

    //在该类中创建一个该类的对象供外界去使用
    private static EHSSingleton instance= new EHSSingleton();

    // 构造方法 private 化
    private EHSSingleton(){

    }

    // 得到 Singleton 的实例(唯一途径）
    public static EHSSingleton getInstance() {
        return instance;
    }
}
