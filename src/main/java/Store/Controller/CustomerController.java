package Store.Controller;

import Store.InputManager;
import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.OffCode;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CustomerController {

    public static String increaseProduct(Customer customer, Product product) {
        if (product == null) {
            return "there isn't any product with this name";
        }
        customer.addToCart(product);
        return "added product to cart successfully";
    }

    public static String decreaseProduct(Customer customer, Product product) {
        if (product == null) {
            return "there isn't any product with this name";
        }
        if (customer.isInCart(product)) {
            customer.removeFromCart(product);
            return "successfully deleted";
        }
        return "you haven't selected this product";
    }

    public static String rateProduct(Customer customer, Product product, double rating) {
        if (product == null) {
            return "there isn't any product with this name";
        }
        if (!customer.hasBoughtProduct(product)) {
            return "you hasn't buy this product";
        }
        product.rate(customer, rating);
        return "yor rating was submitted";
    }

    public static String purchase(Customer customer) {
        getReceiverInfo(); // for what??
        System.out.println("Print code of your offCode (if you haven't offCode Print Null): ");
        String input = InputManager.getNextLine();
        if (!input.equalsIgnoreCase("null")) {
            System.out.println("*******");
            if (customer.canBuy()) {
                customer.buy();
                return "bought successfully. " + customer.getBuyLog().get(customer.getBuyLog().size() - 1);
            }
            return "you hadn't enough money";
        } else {
            OffCode offCode = Manager.getOffCodeByCode(input);
            if (offCode != null && offCode.canBeUsedInDate(new Date())) {
                System.out.println("*******");
                if (customer.canBuy(offCode)) {
                    customer.buy();
                    return "bought successfully. " + customer.getBuyLog().get(customer.getBuyLog().size() - 1);
                }
                return "you hadn't enough money";
            }
            return "the offCode is invalid";
        }
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

}
