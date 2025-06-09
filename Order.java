import java.time.LocalDateTime;
import java.util.*;

public class Order {
    private static int nextOrderId = 1;
    private int orderId;
    private List<Product> products = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private double total;
    private OrderStatus status;
    private String paymentId;
    private LocalDateTime date;

    public Order() {
        this.orderId = nextOrderId++;
        this.status = OrderStatus.PENDING;
        this.date = LocalDateTime.now();
    }
    public void addProduct(Product p, int qty) {
        products.add(p);
        quantities.add(qty);
        total += p.getPrice() * qty;
    }
    public int getOrderId() { return orderId; }
    public double getTotal() { return total; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus s) { status = s; }
    public void setPaymentId(String pid) { paymentId = pid; }
    public String getPaymentId() { return paymentId; }
    public LocalDateTime getDate() { return date; }
    public List<Product> getProducts() { return products; }
    public List<Integer> getQuantities() { return quantities; }
}