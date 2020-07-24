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
            if (value.matches("^[a-zA-Z0-9]\\w{3,14}$")) {
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

    public static String purchase(Customer customer, String input, boolean bank, String address) throws Exception {
        System.out.println("infinity");
        System.out.println(input);
        if (input.isEmpty()) {
            try{
                System.out.println("Step 3");
                if (customer.canBuy(bank)) {
                    System.out.println("Step 4");
                    customer.buy(bank, address);
                    System.out.println(customer);
                    System.out.println("Step 5");
                    return customer.getNewFactor();
                }
            }catch (Exception e){
                throw e;
            }
            throw (new Exception("You don't have enough money!"));
        } else {
            OffCode offCode = Manager.getOffCodeByCode(input);
            if (offCode != null && offCode.canBeUsedInDate(new Date()) && offCode.isUserIncluded(customer)) {
                try {
                    if (customer.canBuy(offCode, bank)) {
                        customer.buy(offCode, bank, address);
                        return customer.getNewFactor();
                    }
                }catch (Exception e){
                    throw e;
                }
                throw (new Exception("You don't have enough money!"));
            }
            throw (new Exception("Your offcode is invalid"));
        }
    }

}