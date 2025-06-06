import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExtendedTest {

    @Test
    void testProductCreation() {
        Product p = new Product("Test", "Category", 1000.0, "CODE1", 10);
        assertEquals("Test", p.getName());
        assertEquals("Category", p.getCategory());
        assertEquals(1000.0, p.getPrice());
        assertEquals("CODE1", p.getCode());
        assertEquals(10, p.getStock());
    }

    @Test
    void testAddStock() {
        Product p = new Product("Test", "Category", 1000.0, "CODE1", 10);
        p.addStock(5);
        assertEquals(15, p.getStock());
    }

    @Test
    void testReduceStock() {
        Product p = new Product("Test", "Category", 1000.0, "CODE1", 10);
        p.reduceStock(3);
        assertEquals(7, p.getStock());
    }

    @Test
    void testUserPasswordCheck() {
        User u = new User("user1", "pass123", Role.USER);
        assertTrue(u.checkPassword("pass123"));
        assertFalse(u.checkPassword("wrongpass"));
    }

    @Test
    void testUserBalance() {
        User u = new User("user1", "pass123", Role.USER);
        u.addBalance(10000);
        assertEquals(10000, u.getBalance());
        assertTrue(u.deductBalance(5000));
        assertEquals(5000, u.getBalance());
        assertFalse(u.deductBalance(6000)); // insufficient
        assertEquals(5000, u.getBalance());
    }

    @Test
    void testOrderAddProductAndTotal() {
        Product p1 = new Product("Test1", "Cat", 1000.0, "C1", 10);
        Product p2 = new Product("Test2", "Cat", 2000.0, "C2", 5);
        Order o = new Order();
        o.addProduct(p1, 2); // 2*1000 = 2000
        o.addProduct(p2, 1); // 1*2000 = 2000
        assertEquals(2, o.getProducts().size());
        assertEquals(2, o.getQuantities().size());
        assertEquals(4000.0, o.getTotal());
    }
}