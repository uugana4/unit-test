import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {

    @Test
    void testOrderConstructorAndGetters() {
        Order order = new Order();
        assertNotNull(order.getProducts());
        assertNotNull(order.getQuantities());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(order.getOrderId() > 0);
        assertNotNull(order.getDate());
        assertEquals(0.0, order.getTotal());
        assertNull(order.getPaymentId());
    }

    @Test
    void testAddProduct() {
        Order order = new Order();
        Product p = new Product("Test", "Cat", 10.0, "C1", 5);
        order.addProduct(p, 2);
        List<Product> products = order.getProducts();
        List<Integer> quantities = order.getQuantities();
        assertEquals(1, products.size());
        assertEquals(p, products.get(0));
        assertEquals(2, quantities.get(0));
        assertEquals(20.0, order.getTotal());
    }

    @Test
    void testSetStatusAndPaymentId() {
        Order order = new Order();
        order.setStatus(OrderStatus.PAID);
        order.setPaymentId("PAY123");
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals("PAY123", order.getPaymentId());
    }

    @Test
    void testOrderCustomConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(42, 99.9, OrderStatus.CANCELLED, "PID", now);
        assertEquals(42, order.getOrderId());
        assertEquals(99.9, order.getTotal());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals("PID", order.getPaymentId());
        assertEquals(now, order.getDate());
    }
}