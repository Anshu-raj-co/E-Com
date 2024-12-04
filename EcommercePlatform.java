package oop_assignment;

import java.util.*;

class Product {
    private int id;
    private String name;
    private String description;
    private double price; 
    private int stock;

    private static final double USD_TO_INR_CONVERSION_RATE = 75.0; 

    
    public Product(int id, String name, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price * USD_TO_INR_CONVERSION_RATE; 
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int amount) {
        this.stock -= amount;
    }

    public void displayInfo() {
        System.out.println("Product ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.printf("Price: ₹%.2f\n", getPrice()); 
        System.out.println("Stock: " + stock);
    }
}


class User {
    private int id;
    private String username;
    private String password;
    private String email;

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}


class Cart {
    private Map<Product, Integer> products = new HashMap<>();

    public boolean addProduct(Product product, int quantity) {
        if (product.getStock() >= quantity) {  
            products.put(product, products.getOrDefault(product, 0) + quantity);
            product.reduceStock(quantity);  
            return true;
        } else {
            System.out.println("Requested quantity for " + product.getName() + " is not available.");
            return false;
        }
    }

    public void displayCartItems() {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            product.displayInfo();
            System.out.println("Quantity: " + quantity);
        }
    }

    public double calculateTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.getPrice() * quantity;
        }
        return total;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }
}


class Order {
    private int orderId;
    private int userId;
    private Map<Product, Integer> products;
    private double totalAmount;
    private String status;

    public Order(int orderId, int userId, Map<Product, Integer> products, double totalAmount, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.products = products;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public void displayOrderDetails() {
        System.out.println("Order ID: " + orderId);
        System.out.println("User ID: " + userId);
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            product.displayInfo();
            System.out.println("Quantity: " + quantity);
        }
        System.out.printf("Total Amount: ₹%.2f\n", totalAmount);
        System.out.println("Status: " + status);
    }
}

public class EcommercePlatform {
    private static List<Product> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
       
        addSampleProducts();

        // Register a New User
        System.out.println("=== User Registration ===");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        User newUser = new User(1, username, password, email);
        users.add(newUser);
        System.out.println("User registered successfully!");

        // User Login
        System.out.println("\n=== User Login ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        boolean isLoggedIn = newUser.login(loginUsername, loginPassword);
        if (!isLoggedIn) {
            System.out.println("Login failed.");
            return;
        }
        System.out.println("Login successful!");

        // Display Products by Section
        System.out.println("\n=== Available Products by Section ===");
        displayProductsBySection("Computer Accessories");
        displayProductsBySection("Laptops");
        displayProductsBySection("Smartphones");

        // Add Products to Cart
        Cart cart = new Cart();
        System.out.println("\n=== Add Products to Cart ===");
        for (Product product : products) {
            product.displayInfo();
            System.out.print("Enter quantity to add to cart (or 0 to skip): ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); 
            if (quantity > 0) {
                boolean added = cart.addProduct(product, quantity);
                if (!added) {
                    System.out.println("Unable to add " + quantity + " units of " + product.getName() + " due to insufficient stock.");
                }
            }
        }

        // Cart Items and Total
        System.out.println("\n=== Cart Items ===");
        cart.displayCartItems();
        System.out.printf("Total: ₹%.2f\n", cart.calculateTotal());

        // Place Order
        System.out.println("\n=== Place Order ===");
        Order order = new Order(1, newUser.getId(), cart.getProducts(), cart.calculateTotal(), "Placed");
        order.displayOrderDetails();

        // Payment
        System.out.println("\n=== Payment ===");
        if (processPayment()) {
            System.out.println("Payment successful! Order placed.");
        } else {
            System.out.println("Payment failed. Please try again.");
        }

        scanner.close();
    }

    
    public static void addSampleProducts() {
        
        products.add(new Product(1, "Keyboard", "Mechanical Gaming Keyboard", 49.99, 100));
        products.add(new Product(2, "Mouse", "Wireless Mouse", 29.99, 150));
        products.add(new Product(3, "Monitor", "27-inch 4K Monitor", 299.99, 75));
        products.add(new Product(4, "Graphics Card", "NVIDIA RTX 4090", 699.99, 20));

        
        products.add(new Product(5, "Lenovo Laptop", "Lenovo Legion 5i pro", 599.99, 50));
        products.add(new Product(6, "Apple MacBook", "MacBook Pro M3", 999.99, 30));
        products.add(new Product(7, "HP Laptop", "HP Omen", 649.99, 40));
        products.add(new Product(8, "Dell Laptop", "Dell Alienware", 899.99, 25));

        
        products.add(new Product(9, "Apple iPhone", "iPhone 16 Pro", 799.99, 100));
        products.add(new Product(10, "Google Pixel", "Google Pixel 9 Pro", 699.99, 70));
        products.add(new Product(11, "Samsung Galaxy", "Samsung Galaxy S24 Ultra", 749.99, 80));
    }

    // Display products by section
    public static void displayProductsBySection(String section) {
        System.out.println("\n--- " + section + " ---");
        for (Product product : products) {
            if (section.equals("Computer Accessories") && product.getName().matches("Keyboard|Mouse|Monitor|Graphics Card")) {
                product.displayInfo();
            } else if (section.equals("Laptops") && product.getName().matches("Lenovo Laptop|Apple MacBook|HP Laptop|Dell Laptop")) {
                product.displayInfo();
            } else if (section.equals("Smartphones") && product.getName().matches("Apple iPhone|Google Pixel|Samsung Galaxy")) {
                product.displayInfo();
            }
            System.out.println(); 
        }
    }

    // Process payment
    public static boolean processPayment() {
        System.out.println("Choose payment method: 1. Debit Card 2. Credit Card");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter PIN: ");
        int pin = scanner.nextInt();
        scanner.nextLine(); 

        
        return pin == 1234;
    }
}
