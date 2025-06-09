import java.util.*;

public class User {
    private String username;
    private String password;
    private Role role;
    private double balance = 0;
    private List<Order> orders = new ArrayList<>();

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public double getBalance() { return balance; }
    public void addBalance(double amount) { balance += amount; }
    public boolean deductBalance(double amount) {
        if (balance >= amount) { balance -= amount; return true; }
        return false;
    }
    public boolean checkPassword(String pw) { return password.equals(pw); }
    public void addOrder(Order o) { orders.add(o); }
    public List<Order> getOrders() { return orders; }
}