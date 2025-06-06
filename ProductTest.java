import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

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
}