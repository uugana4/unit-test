import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Extendeds1 {
    static Scanner sc = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    static List<Product> products = new ArrayList<>();
    static Map<String, Double> coupons = new HashMap<>();

    // --- Бараа хадгалах/унших ---
    static void saveProductsToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Product p : products) {
                pw.printf("%d,%s,%s,%.2f,%s,%d\n",
                    p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getCode(), p.getStock());
            }
        } catch (Exception e) {
            System.out.println("Бараа хадгалах үед алдаа гарлаа: " + e.getMessage());
        }
    }
    static void loadProductsFromFile(String filename) {
        products.clear();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String[] arr = fileScanner.nextLine().split(",");
                if (arr.length == 6) {
                    Product p = new Product(
                        arr[1],
                        arr[2],
                        Double.parseDouble(arr[3]),
                        arr[4],
                        Integer.parseInt(arr[5])
                    );
                    products.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Бараа унших үед алдаа гарлаа: " + e.getMessage());
        }
    }

    // --- Хэрэглэгч хадгалах/унших ---
    static void saveUsersToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (User u : users) {
                pw.printf("%s,%s,%s,%.2f\n",
                    u.getUsername(), u.getPassword(), u.getRole(), u.getBalance());
            }
        } catch (Exception e) {
            System.out.println("Хэрэглэгч хадгалах үед алдаа гарлаа: " + e.getMessage());
        }
    }
    static void loadUsersFromFile(String filename) {
        users.clear();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String[] arr = fileScanner.nextLine().split(",");
                if (arr.length == 4) {
                    User u = new User(
                        arr[0],
                        arr[1],
                        arr[2].equals("ADMIN") ? Role.ADMIN : Role.USER
                    );
                    u.addBalance(Double.parseDouble(arr[3]));
                    users.add(u);
                }
            }
        } catch (Exception e) {
            System.out.println("Хэрэглэгч унших үед алдаа гарлаа: " + e.getMessage());
        }
    }

    // --- Захиалга хадгалах/унших (товч хувилбар) ---
    static void saveOrdersToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (User u : users) {
                for (Order o : u.getOrders()) {
                    pw.printf("%s,%d,%.2f,%s,%s,%s\n",
                        u.getUsername(),
                        o.getOrderId(),
                        o.getTotal(),
                        o.getStatus(),
                        o.getPaymentId() == null ? "" : o.getPaymentId(),
                        o.getDate().toString()
                    );
                    for (int i = 0; i < o.getProducts().size(); i++) {
                        Product p = o.getProducts().get(i);
                        int qty = o.getQuantities().get(i);
                        pw.printf("ITEM,%d,%d\n", p.getId(), qty);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Захиалга хадгалах үед алдаа гарлаа: " + e.getMessage());
        }
    }
    static void loadOrdersFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            User currentUser = null;
            Order currentOrder = null;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] arr = line.split(",");
                if (arr.length >= 6 && !arr[0].equals("ITEM")) {
                    String username = arr[0];
                    int orderId = Integer.parseInt(arr[1]);
                    double total = Double.parseDouble(arr[2]);
                    OrderStatus status = OrderStatus.valueOf(arr[3]);
                    String paymentId = arr[4].isEmpty() ? null : arr[4];
                    LocalDateTime date = LocalDateTime.parse(arr[5]);
                    for (User u : users) {
                        if (u.getUsername().equals(username)) {
                            currentUser = u;
                            currentOrder = new Order(orderId, total, status, paymentId, date);
                            u.addOrder(currentOrder);
                            break;
                        }
                    }
                } else if (arr.length == 3 && arr[0].equals("ITEM") && currentOrder != null) {
                    int productId = Integer.parseInt(arr[1]);
                    int qty = Integer.parseInt(arr[2]);
                    for (Product p : products) {
                        if (p.getId() == productId) {
                            currentOrder.addProduct(p, qty);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Захиалга унших үед алдаа гарлаа: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        loadProductsFromFile("products.txt");
        loadUsersFromFile("users.txt");
        loadOrdersFromFile("orders.txt");

        coupons.put("SALE10", 10.0);
        coupons.put("VIP20", 20.0);
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", Role.ADMIN));
            users.add(new User("user", "user123", Role.USER));
        }

        while (true) {
            System.out.println("\n--- Онлайн дэлгүүр ---");
            System.out.println("Нэвтрэх хэсэг");
            System.out.println("\n1. Нэвтрэх\n2. Бүртгүүлэх\n0. Гарах");
            System.out.print("Сонголт: ");
            String c = sc.nextLine();
            if (c.equals("1")) {
                User u = login();
                if (u != null) {
                    if (u.getRole() == Role.ADMIN) adminMenu(u);
                    else userMenu(u);
                }
            } else if (c.equals("2")) {
                signUp();
            } else if (c.equals("0")) {
                saveProductsToFile("products.txt");
                saveUsersToFile("users.txt");
                saveOrdersToFile("orders.txt");
                System.out.println("Системээс гарлаа.");
                break;
            }
        }
    }

    static User login() {
        System.out.print("Нэр: "); String n = sc.nextLine();
        System.out.print("Нууц үг: "); String p = sc.nextLine();
        for (User u : users)
            if (u.getUsername().equals(n) && u.getPassword().equals(p)) return u;
        System.out.println("Нэвтрэх нэр эсвэл нууц үг буруу!");
        return null;
    }

    static void signUp() {
        System.out.print("Нэр: "); String n = sc.nextLine();
        for (User u : users) if (u.getUsername().equals(n)) {
            System.out.println("Ийм нэртэй хэрэглэгч байна!"); return;
        }
        System.out.print("Нууц үг: "); String p = sc.nextLine();
        System.out.print("Админ эрхтэй бол admin гэж бич: "); String r = sc.nextLine();
        users.add(new User(n, p, r.equals("admin") ? Role.ADMIN : Role.USER));
        System.out.println("Бүртгэл амжилттай.");
    }

    static void adminMenu(User admin) {
        while (true) {
            System.out.println("\n[Админ цэс]");
            System.out.println("1. Бараа нэмэх/нэгтгэх");
            System.out.println("2. Бараа засах");
            System.out.println("3. Бараа устгах");
            System.out.println("4. Барааны жагсаалт");
            System.out.println("5. Купон нэмэх");
            System.out.println("6. Борлуулалтын тайлан");
            System.out.println("7. Хайлт ба шүүлт");
            System.out.println("0. Гарах");
            System.out.print("Сонголт: ");
            String c = sc.nextLine();
            switch (c) {
                case "1": addOrMergeProduct(); break;
                case "2": editProduct(); break;
                case "3": deleteProduct(); break;
                case "4": printProducts(); break;
                case "5": addCoupon(); break;
                case "6": salesReport(); break;
                case "7": searchAndFilter(); break;
                case "0": return;
            }
        }
    }

    static void userMenu(User user) {
        while (true) {
            System.out.println("\n[Хэрэглэгч цэс]");
            System.out.println("1. Бараа харах");
            System.out.println("2. Захиалга хийх");
            System.out.println("3. Дансанд мөнгө нэмэх");
            System.out.println("4. Миний захиалгууд");
            System.out.println("5. Хайлт ба шүүлт");
            System.out.println("0. Гарах");
            System.out.print("Сонголт: ");
            String c = sc.nextLine();
            switch (c) {
                case "1": printProducts(); break;
                case "2": makeOrder(user); break;
                case "3": addBalance(user); break;
                case "4": printUserOrders(user); break;
                case "5": searchAndFilter(); break;
                case "0": return;
            }
        }
    }

    static void addOrMergeProduct() {
        System.out.print("Барааны нэр: ");
        String name = sc.nextLine();
        System.out.print("Ангилал: ");
        String category = sc.nextLine();
        System.out.print("Код: ");
        String code = sc.nextLine();
        Product found = null;
        for (Product p : products)
            if (p.getName().equalsIgnoreCase(name) && p.getCategory().equalsIgnoreCase(category) && p.getCode().equalsIgnoreCase(code)) found = p;
        if (found != null) {
            System.out.print("Ижил бараа байна. Үлдэгдэл нэмэх тоо: ");
            int addStock = Integer.parseInt(sc.nextLine());
            found.addStock(addStock);
            System.out.println("Үлдэгдэл амжилттай нэмэгдлээ.");
        } else {
            System.out.print("Үнэ: ");
            double price = Double.parseDouble(sc.nextLine());
            System.out.print("Үлдэгдэл: ");
            int stock = Integer.parseInt(sc.nextLine());
            products.add(new Product(name, category, price, code, stock));
            System.out.println("Бараа амжилттай нэмэгдлээ.");
        }
    }

    static void editProduct() {
        printProducts();
        System.out.print("Засах барааны ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Product p = null;
        for (Product pr : products) if (pr.getId() == id) p = pr;
        if (p == null) { System.out.println("Бараа олдсонгүй."); return; }
        System.out.print("Шинэ нэр (" + p.getName() + "): ");
        String n = sc.nextLine();
        if (!n.isEmpty()) p.setName(n);
        System.out.print("Шинэ ангилал (" + p.getCategory() + "): ");
        String cat = sc.nextLine();
        if (!cat.isEmpty()) p.setCategory(cat);
        System.out.print("Шинэ үнэ (" + p.getPrice() + "): ");
        String prc = sc.nextLine();
        if (!prc.isEmpty()) p.setPrice(Double.parseDouble(prc));
        System.out.println("Засвар амжилттай.");
    }

    static void deleteProduct() {
        printProducts();
        System.out.print("Устгах барааны ID: ");
        int id = Integer.parseInt(sc.nextLine());
        Product toRemove = null;
        for (Product p : products) if (p.getId() == id) toRemove = p;
        if (toRemove != null) {
            products.remove(toRemove);
            System.out.println("Бараа устгагдлаа.");
        } else System.out.println("Бараа олдсонгүй.");
    }

    static void printProducts() {
        System.out.println("\n--- Барааны жагсаалт ---");
        for (Product p : products)
            System.out.printf("ID:%d | %s | Ангилал: %s | Үнэ: %.2f₮ | Код: %s | Үлдэгдэл: %d\n",
                p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getCode(), p.getStock());
    }

    static void addCoupon() {
        System.out.print("Купон код: ");
        String code = sc.nextLine().trim();
        if (coupons.containsKey(code)) {
            System.out.println("Код бүртгэлтэй байна!");
            return;
        }
        System.out.print("Хөнгөлөлтийн хувь: ");
        double percent = Double.parseDouble(sc.nextLine());
        coupons.put(code, percent);
        System.out.println("Купон нэмэгдлээ.");
    }

    static void salesReport() {
        System.out.println("\n--- Борлуулалтын тайлан ---");
        System.out.println("1. Өдрийн\n2. 7 хоногийн\n3. Сарын\n4. Интервал\n0. Буцах");
        String c = sc.nextLine();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = null, to = now;
        if (c.equals("1")) from = now.toLocalDate().atStartOfDay();
        else if (c.equals("2")) from = now.minusDays(7);
        else if (c.equals("3")) from = now.minusMonths(1);
        else if (c.equals("4")) {
            System.out.print("Эхлэх огноо (yyyy-MM-dd): ");
            from = LocalDate.parse(sc.nextLine()).atStartOfDay();
            System.out.print("Дуусах огноо (yyyy-MM-dd): ");
            to = LocalDate.parse(sc.nextLine()).atTime(23,59,59);
        } else return;
        double total = 0;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (User u : users) {
            for (Order o : u.getOrders()) {
                if (o.getStatus() == OrderStatus.PAID && !o.getDate().isBefore(from) && !o.getDate().isAfter(to)) {
                    System.out.printf("OrderID:%d | Хэрэглэгч: %s | Дүн: %.2f₮ | Огноо: %s\n",
                        o.getOrderId(), u.getUsername(), o.getTotal(), o.getDate().format(fmt));
                    total += o.getTotal();
                }
            }
        }
        System.out.printf("НИЙТ БОРЛУУЛАЛТ: %.2f₮\n", total);
    }

    static void searchAndFilter() {
        System.out.println("1. Нэрээр хайх\n2. Ангиллаар\n3. Код\n4. Үнийн интервал\n0. Буцах");
        String c = sc.nextLine();
        List<Product> found = new ArrayList<>();
        if (c.equals("1")) {
            System.out.print("Нэр: ");
            String n = sc.nextLine().toLowerCase();
            for (Product p : products)
                if (p.getName().toLowerCase().contains(n)) found.add(p);
        } else if (c.equals("2")) {
            System.out.print("Ангилал: ");
            String cat = sc.nextLine().toLowerCase();
            for (Product p : products)
                if (p.getCategory().toLowerCase().contains(cat)) found.add(p);
        } else if (c.equals("3")) {
            System.out.print("Код: ");
            String code = sc.nextLine().toLowerCase();
            for (Product p : products)
                if (p.getCode().toLowerCase().contains(code)) found.add(p);
        } else if (c.equals("4")) {
            System.out.print("Доод үнэ: ");
            double min = Double.parseDouble(sc.nextLine());
            System.out.print("Дээд үнэ: ");
            double max = Double.parseDouble(sc.nextLine());
            for (Product p : products)
                if (p.getPrice() >= min && p.getPrice() <= max) found.add(p);
        } else return;
        if (found.isEmpty()) System.out.println("Илэрц олдсонгүй.");
        else for (Product p : found)
            System.out.printf("ID:%d | %s | Ангилал: %s | Үнэ: %.2f₮ | Код: %s | Үлдэгдэл: %d\n",
                p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getCode(), p.getStock());
    }

    static void makeOrder(User user) {
        Order order = new Order();
        while (true) {
            printProducts();
            System.out.print("Захиалах барааны ID (0 бол дуусгах): ");
            int id = Integer.parseInt(sc.nextLine());
            if (id == 0) break;
            Product p = null;
            for (Product pr : products) if (pr.getId() == id) p = pr;
            if (p == null) { System.out.println("Бараа олдсонгүй."); continue; }
            System.out.print("Тоо ширхэг: ");
            int qty = Integer.parseInt(sc.nextLine());
            if (qty > p.getStock()) { System.out.println("Үлдэгдэл хүрэлцэхгүй!"); continue; }
            order.addProduct(p, qty);
            p.reduceStock(qty);
        }
        if (order.getTotal() == 0) { System.out.println("Захиалга хоосон."); return; }
        System.out.print("Купон код оруулах уу? (байхгүй бол ENTER): ");
        String coupon = sc.nextLine().trim();
        double discount = 0;
        if (!coupon.isEmpty() && coupons.containsKey(coupon)) {
            discount = coupons.get(coupon);
            System.out.println("Урамшуулал: " + discount + "%");
        }
        double pay = order.getTotal() * (1 - discount / 100);
        System.out.printf("Төлөх дүн: %.2f₮\n", pay);
        if (user.getBalance() < pay) {
            System.out.println("Үлдэгдэл хүрэлцэхгүй. Захиалга цуцлагдлаа.");
            order.setStatus(OrderStatus.CANCELLED);
        } else {
            user.deductBalance(pay);
            order.setStatus(OrderStatus.PAID);
            order.setPaymentId("PAY" + order.getOrderId());
            System.out.println("Төлбөр амжилттай! Захиалгын ID: " + order.getOrderId() + ", Төлөв: " + order.getStatus());
        }
        user.addOrder(order);
    }

    static void addBalance(User user) {
        System.out.print("Нэмэх мөнгө: ");
        double amt = Double.parseDouble(sc.nextLine());
        user.addBalance(amt);
        System.out.printf("Шинэ үлдэгдэл: %.2f₮\n", user.getBalance());
    }

    static void printUserOrders(User user) {
        System.out.println("\n--- Миний захиалгууд ---");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Order o : user.getOrders()) {
            System.out.printf("OrderID:%d | Төлөв: %s | Дүн: %.2f₮ | Огноо: %s\n",
                o.getOrderId(), o.getStatus(), o.getTotal(), o.getDate().format(fmt));
            for (int i = 0; i < o.getProducts().size(); i++) {
                Product p = o.getProducts().get(i);
                int qty = o.getQuantities().get(i);
                System.out.printf("  - %s x%d\n", p.getName(), qty);
            }
        }
    }
}