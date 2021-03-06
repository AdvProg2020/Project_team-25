package Store.View;

import Store.Controller.CustomerController;
import Store.Controller.ManagerController;
import Store.InputManager;
import Store.Model.Customer;
import Store.Model.Log.BuyLogItem;
import Store.Model.OffCode;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class CustomerMenu {

    private static Customer customer;
    private static final String EDIT_PERSONAL_INFO = "^edit (.+)$";
    private static final String SHOW_PRODUCT = "^view (\\d+)$";
    private static final String INCREASE_PRODUCT = "^increase (\\d+)$";
    private static final String DECREASE_PRODUCT = "^decrease (\\d+)$";
    private static final String SHOW_ORDER = "^show order (\\d+)$";
    private static final String RATE_PRODUCT = "^rate (\\d+) ([1-5])$";

    public static void init() {
        customer = (Customer) MainMenu.currentUser;
        String input;

        System.out.println("\nCustomer menu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("view personal info")) {
                viewPersonalInfo();
                System.out.println("\nCustomer menu\n");
            } else if (input.equalsIgnoreCase("view cart")) {
                viewCart();
                System.out.println("\nCustomer menu\n");
            } else if (input.equalsIgnoreCase("purchase")) {
                purchaseWrapper();
            } else if (input.equalsIgnoreCase("view orders")) {
                viewOrders();
                System.out.println("\nCustomer menu\n");
            } else if (input.equalsIgnoreCase("view balance")) {
                viewBalance();
            } else if (input.equalsIgnoreCase("view discount codes")) {
                viewDiscountCodes();
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
                System.out.println("\nCustomer menu\n");
            } else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
                System.out.println("\nCustomer menu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                SignUpAndLoginMenu.logoutWrapper();
            } else {
                System.out.println("Invalid command!");
            }
        }
       /* if (MainMenu.currentUser == null) {
            MainMenu.init();
        }*/

    }

    private static void viewPersonalInfo() {
        String input;
        Matcher matcher;
        System.out.println("\nCustomer menu -> View Personal Info\n");
        System.out.println(customer);//
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, EDIT_PERSONAL_INFO)).find()) {
                editPersonalInfoWrapper(matcher.group(1));
            } else if (input.equalsIgnoreCase("logout")) {
                SignUpAndLoginMenu.logoutWrapper();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void editPersonalInfoWrapper(String field) {
        if (field.equalsIgnoreCase("username")) {
            System.out.println("This field cannot be edited!");
        }
        else if (!ManagerController.isValidField(field)) {
            System.out.println("This is not a valid field!");
        }
        else {
            String value;
            if (field.equalsIgnoreCase("password")) {
                System.out.println("Please enter your password: ");
                String passwordGuess = InputManager.getNextLine();
                if (customer.validatePassword(passwordGuess)) {
                    System.out.println("Please enter your new password: ");
                    value = InputManager.getNextLine();
                }
                else {
                    System.out.println("The password you entered is incorrect!");
                    return;
                }
            }
            else {
                System.out.println("Value: ");
                value = InputManager.getNextLine();
            }
            System.out.println(ManagerController.editPersonalInfo(customer, field, value));
        }
    }

    private static void viewCart() {
        String input;
        Matcher matcher;
        System.out.println("\nCustomer menu -> View Cart\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("show products")) {
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
            } else if (input.equalsIgnoreCase("logout")) {
                SignUpAndLoginMenu.logoutWrapper();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void showProducts() {
        for (Product product : customer.getCart()) {
            System.out.println(product);
            System.out.println("*******");
        }
    }

    private static void showProduct(String attribute) {
        if (Product.getProductByID(Integer.parseInt(attribute)) == null) {
            System.out.println("There isn't any product with this ID!");
        }
        else
            System.out.println(Product.getProductByID(Integer.parseInt(attribute)));
    }

    private static void increaseProductWrapper(String attribute) {
        System.out.println(CustomerController.increaseProduct(customer, Product.getProductByID(Integer.parseInt(attribute))));
    }

    private static void decreaseProductWrapper(String attribute) {
        System.out.println(CustomerController.decreaseProduct(customer, Product.getProductByID(Integer.parseInt(attribute))));
    }

    private static void showTotalCartPrice() {
        System.out.println("The total cart price Is: " + customer.getTotalCartPrice());
    }

    private static void purchaseWrapper() {
        if (customer == MainMenu.guest) {
            System.out.println("You must login first to finalize your purchase!");
            return;
        }
        if (customer.getCart().isEmpty()) {
            System.out.println("You haven't selected any products");
            return;
        }
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
        System.out.println("\nCustomer menu -> View Orders\n");
        for (BuyLogItem buyLogItem : customer.getBuyLog()) {
            System.out.println(buyLogItem);
        }
        System.out.println("_________________________");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_ORDER)).find()) {
                showOrder(Integer.parseInt(matcher.group(1)));
            } else if ((matcher = InputManager.getMatcher(input, RATE_PRODUCT)).find()) {
                rateProductWrapper(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void showOrder(int id) {
        if (Product.getProductByID(id) == null || !customer.hasBoughtProduct(Product.getProductByID(id))) {
            System.out.println("You haven't bought this product!");
        }
        else
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
        System.out.println("*******\n");
        System.out.println("List of main commands: ");
        System.out.println("view personal info");
        System.out.println("view cart");
        System.out.println("purchase");
        System.out.println("view orders");
        System.out.println("view balance");
        System.out.println("view discount codes");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the view personal info submenu: ");
        System.out.println("edit [filed]");
        System.out.println("back");

        System.out.println("\nList of commands in the view cart submenu: ");
        System.out.println("show products");
        System.out.println("view [ProductID]");
        System.out.println("increase [ProductID]");
        System.out.println("decrease [ProductID]");
        System.out.println("show total price");
        System.out.println("purchase");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the view orders submenu: ");
        System.out.println("show order [orderID]");
        System.out.println("rate [Product ID] [1-5]");
        System.out.println("back");
        System.out.println("*******");

    }
}
