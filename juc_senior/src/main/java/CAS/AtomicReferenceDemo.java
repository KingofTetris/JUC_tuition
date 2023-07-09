package CAS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author by KingOfTetris
 * @date 2023/7/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    String username;
    int age;
}
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        AtomicReference<User> atomicReference = new AtomicReference<>();
        User z3 = new User("z3",22);
        User l4 = new User("l4",26);

        atomicReference.set(z3);

        System.out.println(atomicReference.compareAndSet(z3,l4) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(z3,l4) + "\t" + atomicReference.get().toString());
    }
}
