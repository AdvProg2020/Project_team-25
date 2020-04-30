package Store.Controller;

import Store.Model.*;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;

public class SellerController {

    private static final String INVALID_VALUE_ERROR = "The value you entered for this field is invalid";

    public static class InvalidValueException extends Exception {
        InvalidValueException(String message) {
            super(message);
        }
    }

    public static void editPersonalInfo(Seller seller, String field, String value) throws InvalidValueException {
        if (field.equalsIgnoreCase("name")) {
            if (!StringUtils.isAlpha(value)) {
                throw new InvalidValueException(INVALID_VALUE_ERROR);
            }
            seller.setName(value);
        }
        if (field.equalsIgnoreCase("family name")) {
            seller.setFamilyName(value);
        }
        else if (field.equalsIgnoreCase("email")) {
            if (!isValidEmail(value)) {
                throw new InvalidValueException(INVALID_VALUE_ERROR);
            }
            seller.setEmail(value);
        }
        if (field.equalsIgnoreCase("phone number")) {
            if (!value.matches("^[0-9]+$")) {
                throw new InvalidValueException(INVALID_VALUE_ERROR);
            }
            seller.setPhoneNumber(value);
        }
        else if (field.equalsIgnoreCase("password")) {
            if (value.matches("^[a-zA-Z]\\w{3,14}$")) {
                seller.setPassword(value);
            }
            else {
                throw new InvalidValueException(INVALID_VALUE_ERROR);
            }
        }
        else if (field.equalsIgnoreCase("company name")) {
            seller.setCompanyName(value);
        }
        if (field.equalsIgnoreCase("company description")) {
            seller.setCompanyDescription(value);
        }
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static void editProduct(Seller seller, Product product, Product newProduct) {
        Manager.addRequest(new Request(product, true, newProduct));
    }

    public static void addProduct(Seller seller, Product product) {
        Manager.addRequest(new Request(product, false, null));
    }

    public static void removeProduct(Seller seller, Product product) {
        Product.deleteProduct(product);
    }

    public static void editOff(Seller seller, Offer offer, Offer newOffer) {
        Manager.addRequest(new Request(seller, offer, true, newOffer));
    }

    public static void addOff(Seller seller, Offer offer) {
        Manager.addRequest(new Request(seller, offer, false, null));
    }

    public static ArrayList<String> makeBuyersUnique(ArrayList<String> buyers) {
        ArrayList<String> result = new ArrayList<String>();
        for (String buyer : buyers) {
            if (!result.contains(buyer)) {
                result.add(buyer);
            }
        }
        return result;
    }

    public static boolean isValidField(String field) {
        if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("family name")) {
            return true;
        }
        else if (field.equalsIgnoreCase("email") || field.equalsIgnoreCase("phone number")) {
            return true;
        }
        else if (field.equalsIgnoreCase("password")) {
            return true;
        }
        else if (field.equalsIgnoreCase("company name") || field.equalsIgnoreCase("company description")) {
            return true;
        }
        return false;
    }

    public static boolean isValidID(Seller seller, int id) {
        for (Product product : seller.getProducts()) {
            if (product.getProductID() == id) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Offer> getOffersOfThisSeller(Seller seller) {
        ArrayList<Offer> result = new ArrayList<Offer>();
        for (Offer offer : Offer.getAllOffers()) {
            if (offer.getUser() == seller) {
                result.add(offer);
            }
        }
        return result;
    }

    public static boolean isProductFromThisSeller(Seller seller, Product product) {
        return seller == product.getSeller();
    }
}
