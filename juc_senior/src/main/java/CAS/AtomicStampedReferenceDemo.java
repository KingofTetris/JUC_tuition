package CAS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
class Book{
    String name;
    int price;
}
public class AtomicStampedReferenceDemo {

    public static void main(String[] args) {

    }

    private static void ABAStampedSigletonThread() {
        Book javaBook = new Book("javaBook",22);
        //初始邮戳，java,1
        AtomicStampedReference<Book> atomicStampedReference = new AtomicStampedReference<>(javaBook,1);
        System.out.println(atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());
        Book mysql = new Book("mysql",132);
        //修改成mysql,2
        boolean b = atomicStampedReference.compareAndSet(javaBook, mysql, 1, 2);
        System.out.println(b + "\t" + atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());
        //最后修改成java 3
        boolean b1 = atomicStampedReference.compareAndSet(mysql, javaBook, 2, 3);
        System.out.println(b1 + "\t" + atomicStampedReference.getReference() + "\t" + atomicStampedReference.getStamp());
        //虽然结果是javaBook 但是邮戳已经变了。不是2了。
    }
}
