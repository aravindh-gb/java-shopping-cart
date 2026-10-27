import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Customer {

    String[] cat = {"Dairy", "Snacks", "Beverages"};
    String[] dairy = {"Milk", "Paneer", "Curd"};
    String[] snacks = {"Chips", "Biscuits", "Samosa"};
    String[] drinks = {"Coffee", "Lemon Soda", "Badam Milk"};
    int[] dp = {28, 90, 35};
    int[] sp = {20, 15, 12};
    int[] bp = {25, 30, 45};
    int tot = 0;
    int[][] quantity = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    static final String BILL_FILE = "bills.txt";

    void getMenu() {
        System.out.println("Categories:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ". " + cat[i] + ":");
            for (int j = 0; j < 3; j++) {
                System.out.println("   " + (j + 1) + ". " + getName(i, j) + ": Rs." + getPrice(i, j));
            }
        }
    }

    void getDairy() {
        System.out.println("Dairy & Price:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ". " + dairy[i] + ": Rs." + dp[i]);
        }
    }

    String getDairy(int i) {
        return dairy[i - 1];
    }

    void getSnacks() {
        System.out.println("Snacks & Price:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ". " + snacks[i] + ": Rs." + sp[i]);
        }
    }

    String getSnack(int i) {
        return snacks[i - 1];
    }

    void getDrinks() {
        System.out.println("Beverages & Price:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ". " + drinks[i] + ": Rs." + bp[i]);
        }
    }

    String getDrink(int i) {
        return drinks[i - 1];
    }

    // Helper: item name by category index (0-2) and item index (0-2)
    String getName(int i, int j) {
        if (i == 0) return dairy[j];
        else if (i == 1) return snacks[j];
        else return drinks[j];
    }

    // Helper: item price by category index (0-2) and item index (0-2)
    int getPrice(int i, int j) {
        if (i == 0) return dp[j];
        else if (i == 1) return sp[j];
        else return bp[j];
    }

    // Returns true only if the item number is 1-3 and the quantity is positive
    boolean isValid(int item, int qty) {
        if (item < 1 || item > 3) {
            System.out.println("Invalid item number. Choose 1-3.");
            return false;
        }
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return false;
        }
        return true;
    }

    void setQuantity(int i, int j, int k) {
        quantity[i - 1][j - 1] += k;
    }

    void getTotal() {
        tot = 0; // reset so the total isn't added twice when viewed again
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tot += quantity[i][j] * getPrice(i, j);
            }
        }
        System.out.println("Total is Rs." + tot);
    }

    boolean isCartEmpty() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (quantity[i][j] > 0) return false;
            }
        }
        return true;
    }

    // FILE HANDLING: write the current bill to bills.txt (append mode)
    void saveBill() {
        if (isCartEmpty()) {
            System.out.println("Cart is empty. Nothing to save.");
            return;
        }
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        int billTotal = 0;
        try (PrintWriter pw = new PrintWriter(new FileWriter(BILL_FILE, true))) {
            pw.println("========================================");
            pw.println("BILL - " + time);
            pw.println("----------------------------------------");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (quantity[i][j] > 0) {
                        int amount = quantity[i][j] * getPrice(i, j);
                        billTotal += amount;
                        pw.println(getName(i, j) + " x " + quantity[i][j]
                                + " @ Rs." + getPrice(i, j) + " = Rs." + amount);
                    }
                }
            }
            pw.println("----------------------------------------");
            pw.println("TOTAL: Rs." + billTotal);
            pw.println("========================================");
            System.out.println("Bill saved to " + BILL_FILE);
        } catch (IOException e) {
            System.out.println("Error saving bill: " + e.getMessage());
            return;
        }
        // Clear the cart after billing
        quantity = new int[3][3];
        tot = 0;
    }

    // FILE HANDLING: read and print all saved bills from bills.txt
    void viewBills() {
        try (BufferedReader br = new BufferedReader(new FileReader(BILL_FILE))) {
            String line;
            System.out.println("Saved Bills:");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("No saved bills found.");
        }
    }
}

class Main {
    public static void main(String[] args) {
        Customer c1 = new Customer();
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the Store:");
        while (true) {
            System.out.println("1. View Categories");
            System.out.println("2. Add items");
            System.out.println("3. View total");
            System.out.println("4. Save bill to file");
            System.out.println("5. View saved bills");
            System.out.println("6. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    c1.getMenu();
                    break;
                case 2:
                    System.out.println("Select category:");
                    int catChoice = sc.nextInt();
                    switch (catChoice) {
                        case 1:
                            c1.getDairy();
                            System.out.println("Select dairy item to add (1-3):");
                            int dairyChoice = sc.nextInt();
                            System.out.println("Enter quantity:");
                            int quantity1 = sc.nextInt();
                            if (c1.isValid(dairyChoice, quantity1)) {
                                c1.setQuantity(1, dairyChoice, quantity1);
                                System.out.println(quantity1 + " " + c1.getDairy(dairyChoice) + " added to cart.");
                            }
                            break;
                        case 2:
                            c1.getSnacks();
                            System.out.println("Select snack to add (1-3):");
                            int snackChoice = sc.nextInt();
                            System.out.println("Enter quantity:");
                            int quantity2 = sc.nextInt();
                            if (c1.isValid(snackChoice, quantity2)) {
                                c1.setQuantity(2, snackChoice, quantity2);
                                System.out.println(quantity2 + " " + c1.getSnack(snackChoice) + " added to cart.");
                            }
                            break;
                        case 3:
                            c1.getDrinks();
                            System.out.println("Select beverage to add (1-3):");
                            int drinkChoice = sc.nextInt();
                            System.out.println("Enter quantity:");
                            int quantity3 = sc.nextInt();
                            if (c1.isValid(drinkChoice, quantity3)) {
                                c1.setQuantity(3, drinkChoice, quantity3);
                                System.out.println(quantity3 + " " + c1.getDrink(drinkChoice) + " added to cart.");
                            }
                            break;
                        default:
                            System.out.println("Invalid category choice.");
                    }
                    break;
                case 3:
                    c1.getTotal();
                    break;
                case 4:
                    c1.saveBill();
                    break;
                case 5:
                    c1.viewBills();
                    break;
                case 6:
                    System.out.println("Thank you for shopping!");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}