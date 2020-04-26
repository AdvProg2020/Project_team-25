package Store.Controller;

import Store.Model.Offer;
import Store.Model.Product;
import Store.Model.Seller;
import org.codehaus.plexus.util.StringUtils;

public class SellerController {

    private static final String INVALID_VALUE_ERROR = "The value you entered for this field is invalid";

    public static void editPersonalInfo(Seller seller, String field, String value) throws Exception {
        if (field.equalsIgnoreCase("name")) {
            if (!StringUtils.isAlpha(value)) {
                throw new Exception(INVALID_VALUE_ERROR);
            }
            seller.setName(value);
        }
        if (field.equalsIgnoreCase("family name")) {
            if (!StringUtils.isAlpha(value)) {
                throw new Exception(INVALID_VALUE_ERROR);
            }
            seller.setFamilyName(value);
        }
        else if (field.equalsIgnoreCase("email")) {
            if (!isValidEmail(value)) {
                throw new Exception(INVALID_VALUE_ERROR);
            }
            seller.setEmail(value);
        }
        if (field.equalsIgnoreCase("phone number")) {
            if (!StringUtils.isNumeric(value)) {
                throw new Exception(INVALID_VALUE_ERROR);
            }
            seller.setPhoneNumber(value);
        }
        else if (field.equalsIgnoreCase("password")) {
            seller.setPassword(value);
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

    }

    public static void addProduct(Seller seller, Product product) {

    }

    public static void removeProduct(Seller seller, Product product) {

    }

    public static void editOff(Seller seller, Offer offer, Offer newOffer) {

    }

    public static void addOff(Seller seller, Offer offer) {

    }
}
