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
            return "There isn't any product with this name!";
        }
        customer.addToCart(product);
        return "Product added to cart successfully.";
    }

    public static String decreaseProduct(Customer customer, Product product) {
        if (product == null) {
            return "There isn't any product with this name!";
        }
        if (customer.isInCart(product)) {
            customer.removeFromCart(product);
            return "Successfully deleted.";
        }
        return "You haven't selected this product!";
    }

    public static String rateProduct(Customer customer, Product product, double rating) {
        if (product == null) {
            return "There isn't any product with this name!";
        }
        if (!customer.hasBoughtProduct(product)) {
            return "You haven't bought this product!";
        }
        product.rate(customer, rating);
        return "Rating submitted.";
    }

    public static String purchase(Customer customer, String input) {
        if (input.equalsIgnoreCase("null")) {
            if (customer.canBuy()) {
                customer.buy();
                return "bBought successfully. " + customer.getBuyLog().get(customer.getBuyLog().size() - 1);
            }
            return "You don't have enough money!";
        } else {
            OffCode offCode = Manager.getOffCodeByCode(input);
            if (offCode != null && offCode.canBeUsedInDate(new Date()) && offCode.isUserIncluded(customer)) {
                if (customer.canBuy(offCode)) {
                    customer.buy();
                    return "Bought successfully. " + customer.getBuyLog().get(customer.getBuyLog().size() - 1);
                }
                return "You don't have enough money!";
            }
            return "The offCode is invalid!";
        }
    }

}
