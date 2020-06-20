package Store.Controller;

import Store.InputManager;
import Store.Model.*;

import java.util.Date;

public class CustomerUIController {

    public static void editPersonalInfo(User user, String field, String value) throws Exception {
        if (field.equalsIgnoreCase("email")) {
            if (isValidEmail(value)) {
                user.setEmail(value);
                return;
            }
            else {
                throw (new Exception("Email field has not filled correctly"));
            }
        } else if (field.equalsIgnoreCase("phone number")) {
            if (InputManager.getMatcher(value, "^[0-9]+$").find()) {
                user.setPhoneNumber(value);
                return;
            } else {
                throw (new Exception("Phone number field has not filled correctly"));
            }
        } else if (field.equalsIgnoreCase("family name")) {
            user.setFamilyName(value);
            return;
        } else if (field.equalsIgnoreCase("first name")) {
            user.setName(value);
            return;
        } else if (field.equalsIgnoreCase("password")) {
            if (value.matches("^[a-zA-Z]\\w{3,14}$")) {
                user.setPassword(value);
                return;
            }
            else {
                throw (new Exception("Password field has not filled correctly"));
            }
        }
        throw (new Exception("Something wrong happened!"));
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static void increaseProduct(Customer customer, Product product) throws Exception {
        if (product.getAvailablity()) {
            customer.addToCart(product);
        }
        else
            throw (new Exception("The product is not available"));
    }

    public static void decreaseProduct(Customer customer, Product product) throws Exception {
        if (customer.isInCart(product)) {
            customer.removeFromCart(product);
        }
        else
            throw (new Exception("You haven't selected this product!"));
    }

    public static void rateProduct(Customer customer, Product product, double rating) throws Exception {
        if (product.hasBeenRatedBefore(customer)) {
            throw (new Exception("You have already rated this product!"));
        }
        if (!customer.hasBoughtProduct(product)) {
            throw (new Exception("You haven't bought this product!"));
        }
        product.rate(customer, rating);
    }

    public static String purchase(Customer customer, String input) throws Exception {
        if (input.equalsIgnoreCase("null")) {
            if (customer.canBuy()) {
                customer.buy();
                return customer.getNewFactor();
            }
            throw (new Exception("You don't have enough money!"));
        } else {
            OffCode offCode = Manager.getOffCodeByCode(input);
            if (offCode != null && offCode.canBeUsedInDate(new Date()) && offCode.isUserIncluded(customer)) {
                if (customer.canBuy(offCode)) {
                    customer.buy(offCode);
                    return customer.getNewFactor();
                }
                throw (new Exception("You don't have enough money!"));
            }
            throw (new Exception("Your offcode is invalid"));
        }
    }

}
