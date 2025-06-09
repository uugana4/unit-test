import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class DummyOrder extends Order {
    // Minimal stub for testing addOrder/getOrders
}

public class UserTest {

    @Test
    void testUserConstructorAndGetters() {
        User user = new User("alice", "pass123", Role.USER);
        assertEquals("alice", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertEquals(0.0, user.getBalance());
    }

    @Test
    void testAddBalance() {
        User user = new User("bob", "pw", Role.ADMIN);
        user.addBalance(100.0);
        assertEquals(100.0, user.getBalance());
    }

    @Test
    void testDeductBalanceSuccess() {
        User user = new User("bob", "pw", Role.ADMIN);
        user.addBalance(50.0);
        assertTrue(user.deductBalance(30.0));
        assertEquals(20.0, user.getBalance());
    }

    @Test
    void testDeductBalanceFail() {
        User user = new User("bob", "pw", Role.ADMIN);
        user.addBalance(10.0);
        assertFalse(user.deductBalance(20.0));
        assertEquals(10.0, user.getBalance());
    }

    @Test
    void testCheckPassword() {
        User user = new User("eve", "secret", Role.USER);
        assertTrue(user.checkPassword("secret"));
        assertFalse(user.checkPassword("wrong"));
    }

    @Test
    void testAddOrderAndGetOrders() {
        User user = new User("dan", "pw", Role.USER);
        DummyOrder order = new DummyOrder();
        user.addOrder(order);
        List<Order> orders = user.getOrders();
        assertEquals(1, orders.size());
        assertSame(order, orders.get(0));
    }
}