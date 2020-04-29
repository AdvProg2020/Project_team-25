package Store.View;

import Store.Controller.CustomerController;
import Store.Controller.ManagerController;
import Store.InputManager;
import Store.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;

public class CustomerMenu {

    private static Customer customer;
    private static final String EDIT_PERSONAL_INFO = "^edit (password|family name|first name|email|phone number) ([^\\s]+)$";
    private static final String SHOW_PRODUCT = "^view (\\d+)$";
    private static final String INCREASE_PRODUCT = "^increase (\\d+)$";
    private static final String DECREASE_PRODUCT = "^decrease (\\d+)$";
    private static final String SHOW_ORDER = "^show order (\\d+)$";
    private static final String RATE_PRODUCT = "^rate (\\d+) ([1-5])$";

    public static void init() {
        customer = (Customer) MainMenu.currentUser;
        String input;

        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("view personal info")) {
                viewPersonalInfo();
            } else if (input.equalsIgnoreCase("view cart")) {
                viewCart();
            } else if (input.equalsIgnoreCase("purchase")) {
                purchaseWrapper();
            } else if (input.equalsIgnoreCase("view orders")) {
                viewOrders();
            } else if (input.equalsIgnoreCase("view balance")) {
                viewBalance();
            } else if (input.equalsIgnoreCase("view discount codes")) {
                viewDiscountCodes();
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
            } else if (input.equalsIgnoreCase("logout")) {
                MainMenu.currentUser = null;
                break;
            } else {
                System.out.println("invalid command");
            }
        }
        if (MainMenu.currentUser == null) {
            MainMenu.init();
        }

    }

    private static void viewPersonalInfo() {
        String input;
        Matcher matcher;
        System.out.println(customer);//
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, EDIT_PERSONAL_INFO)).find()) {
                editPersonalInfoWrapper(matcher.group(1), matcher.group(2));
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void editPersonalInfoWrapper(String field, String value) {
        ManagerController.editPersonalInfo(customer, field, value);
    }

    private static void viewCart() {
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("show product")) {
                showProducts();
            } else if ((matcher = InputManager.getMatcher(input, SHOW_PRODUCT)).find()) {
                showProduct(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, INCREASE_PRODUCT)).find()) {
                increaseProductWrapper(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, DECREASE_PRODUCT)).find()) {
                decreaseProductWrapper(matcher.group(1));
            } else if (input.equalsIgnoreCase("show total price")) {
                showTotalCartPrice();
            } else if (input.equalsIgnoreCase("purchase")) {
                purchaseWrapper();
                break;
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void showProducts() {
        for (Product product : customer.getCart()) {
            System.out.println(product);
            System.out.println("*******");
            ;
        }
    }

    private static void showProduct(String attribute) {
        if (Product.getProductByID(Integer.parseInt(attribute)) == null) {
            System.out.println("there isn't any product with this ID");
        }
        System.out.println(Product.getProductByID(Integer.parseInt(attribute)));
    }

    private static void increaseProductWrapper(String attribute) {
        System.out.println(CustomerController.increaseProduct(customer, Product.getProductByID(Integer.parseInt(attribute))));
    }

    private static void decreaseProductWrapper(String attribute) {
        System.out.println(CustomerController.decreaseProduct(customer, Product.getProductByID(Integer.parseInt(attribute))));
    }

    private static void showTotalCartPrice() {
        System.out.println("The Total CartPrice Is: " + customer.getTotalCartPrice());
    }

    private static void purchaseWrapper() {
        getReceiverInfo(); // for what??
        System.out.println("Print code of your offCode (if you haven't offCode Print Null): ");
        String input = InputManager.getNextLine();
        System.out.println("*******");

        System.out.println(CustomerController.purchase(customer, input));
    }

    public static ArrayList<String> getReceiverInfo() {
        System.out.println("Print Address: ");
        String address = InputManager.getNextLine();

        System.out.println("Print Phone Number: ");
        String phone = InputManager.getNextLine();

        System.out.println("Print Email: ");
        String email = InputManager.getNextLine();

        System.out.println("*******");

        return new ArrayList<>(Arrays.asList(address, phone, email));
    }

    private static void viewOrders() {
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_ORDER)).find()) {
                showOrder(Integer.parseInt(matcher.group(1)));
            } else if ((matcher = InputManager.getMatcher(input, RATE_PRODUCT)).find()) {
                rateProductWrapper(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void showOrder(int id) {
        if (Product.getProductByID(id) == null) {
            System.out.println("you haven't bought this product");
        }
        System.out.println(Product.getProductByID(id));
    }

    private static void rateProductWrapper(int id, double rating) {
        System.out.println(CustomerController.rateProduct(customer, Product.getProductByID(id), rating));
    }

    private static void viewBalance() {
        System.out.println("Your Balance: " + customer.getMoney());
    }

    private static void viewDiscountCodes() {
        for (OffCode offCode : customer.getOffCodes().keySet()) {
            System.out.println(offCode);
            System.out.println("Your Number Of Usage: " + customer.getOffCodes().get(offCode));
            System.out.println("*******");
        }
    }

    private static void printHelp() {
        System.out.println("list of commands: ");
        System.out.println("view personal info");
        System.out.println("view cart");
        System.out.println("purchase");
        System.out.println("view orders");
        System.out.println("view balance");
        System.out.println("view discount code");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the view personal info submenu: ");
        System.out.println("edit [password|family name|first name|email|phone number] [value]");
        System.out.println("back");

        System.out.println("\nlist of commands in the view cart submenu: ");
        System.out.println("show products");
        System.out.println("view [ProductID]");
        System.out.println("increase [ProductID]");
        System.out.println("decrease [ProductID]");
        System.out.println("show total price");
        System.out.println("purchase");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the view orders submenu: ");
        System.out.println("show order [orderID]");
        System.out.println("rate [Product ID] [1-5]");
        System.out.println("back");
        System.out.println("*******");

    }
}
