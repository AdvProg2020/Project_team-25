package Store.Controller;

import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.OffCode;
import Store.Model.Product;

import java.util.Date;

public class CustomerController {

    public static String increaseProduct(Customer customer, Product product) {
        if (product == null) {
            return "There isn't any product with this name!";
        }
        if (product.getAvailablity()) {
            customer.addToCart(product);
            return "Product added to cart successfully.";
        }
        return "This product is not available!";
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
                return "Bought successfully. \n" + customer.getNewFactor();
            }
            return "You don't have enough money!";
        } else {
            OffCode offCode = Manager.getOffCodeByCode(input);
            if (offCode != null && offCode.canBeUsedInDate(new Date()) && offCode.isUserIncluded(customer)) {
                if (customer.canBuy(offCode)) {
                    customer.buy(offCode);
                    return "Bought successfully. \n" + customer.getNewFactor();
                }
                return "You don't have enough money!";
            }
            return "The offCode is invalid!";
        }
    }

}
