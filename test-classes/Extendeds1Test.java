import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;

public class Extendeds1Test {

    @BeforeEach
    void setup() {
        Extendeds1.products.clear();
        Extendeds1.users.clear();
        Extendeds1.coupons.clear();
    }

    @Test
    void testAddCoupon() {
        Extendeds1.coupons.put("TEST10", 10.0);
        assertTrue(Extendeds1.coupons.containsKey("TEST10"));
        assertEquals(10.0, Extendeds1.coupons.get("TEST10"));
    }

    @Test
    void testSaveAndLoadProductsToFile() throws IOException {
        Product p = new Product("Test", "Cat", 10.0, "C1", 5);
        Extendeds1.products.add(p);
        String filename = "test_products.txt";
        Extendeds1.saveProductsToFile(filename);

        Extendeds1.products.clear();
        Extendeds1.loadProductsFromFile(filename);

        assertEquals(1, Extendeds1.products.size());
        Product loaded = Extendeds1.products.get(0);
        assertEquals("Test", loaded.getName());
        assertEquals("Cat", loaded.getCategory());
        assertEquals(10.0, loaded.getPrice());
        assertEquals("C1", loaded.getCode());
        assertEquals(5, loaded.getStock());

        new File(filename).delete();
    }

    @Test
    void testSaveAndLoadUsersToFile() throws IOException {
        User u = new User("testuser", "pw", Role.USER);
        u.addBalance(100.0);
        Extendeds1.users.add(u);
        String filename = "test_users.txt";
        Extendeds1.saveUsersToFile(filename);

        Extendeds1.users.clear();
        Extendeds1.loadUsersFromFile(filename);

        assertEquals(1, Extendeds1.users.size());
        User loaded = Extendeds1.users.get(0);
        assertEquals("testuser", loaded.getUsername());
        assertEquals("pw", loaded.getPassword());
        assertEquals(Role.USER, loaded.getRole());
        assertEquals(100.0, loaded.getBalance());

        new File(filename).delete();
    }

    @Test
    void testSearchAndFilterByName() {
        Product p1 = new Product("Apple", "Fruit", 1.0, "A1", 10);
        Product p2 = new Product("Banana", "Fruit", 2.0, "B1", 20);
        Extendeds1.products.add(p1);
        Extendeds1.products.add(p2);

        // Simulate search by name "apple"
        List<Product> found = new ArrayList<>();
        String n = "apple";
        for (Product p : Extendeds1.products)
            if (p.getName().toLowerCase().contains(n)) found.add(p);

        assertEquals(1, found.size());
        assertEquals("Apple", found.get(0).getName());
    }
}