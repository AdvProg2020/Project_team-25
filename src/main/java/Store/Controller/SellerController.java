package Store.Controller;

import Store.Model.*;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;

public class SellerController {

    private static final String INVALID_VALUE_ERROR = "The value you entered for this field is invalid!";

    public static class InvalidValueException extends Exception {
        InvalidValueException(String message) {
            super(message);
        }
    }

    public static void editPersonalInfo(Seller seller, String field, String value) throws InvalidValueException {
        if (field.equalsIgnoreCase("first name")) {
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

    public static String editProduct(Seller seller, Product product, Product newProduct) {
        for (Product product1: seller.getProducts())
            if (!product1.equals(product) && product1.equals(newProduct))
                return "Your new product is in current seller's products!";
        Manager.addRequest(new Request(product, true, newProduct));
        newProduct.setProductID(product.getProductID());
        return "Ok";
    }

    public static void addProduct(Seller seller, Product product) {
        Manager.addRequest(new Request(product, false, null));
    }

    public static void removeProduct(Seller seller, Product product) {
        seller.removeProduct(product);
    }

    public static String editOff(Seller seller, Offer offer, Offer newOffer) {
        for (Offer offer1: seller.getOffers())
            if (!offer1.equals(offer) && offer1.equals(newOffer))
                return "Your new offer has at least one product which is in off!";
        Manager.addRequest(new Request(seller, offer, true, newOffer));
        newOffer.setOffID(offer.getOffID());
        return "Ok";
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
        if (field.equalsIgnoreCase("first name") || field.equalsIgnoreCase("family name")) {
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

    public static String addFilterToProduct(Seller seller, String idString, String filter) {
        if (!StringUtils.isNumeric(idString) || !Product.hasProductWithID(Integer.parseInt(idString))) {
            return "Invalid ID!";
        }
        Product product = Product.getProductByID(Integer.parseInt(idString));
        if (product.getSeller() != seller) {
            return "You do not own this product!";
        }
        if (product.hasFilter(filter)) {
            return "This product already has this filter!";
        }
        product.addFilter(filter);
        return "Ok";
    }

    public static String removeFilterFromProduct(Seller seller, String idString, String filter) {
        if (!StringUtils.isNumeric(idString) || !Product.hasProductWithID(Integer.parseInt(idString))) {
            return "Invalid ID!";
        }
        Product product = Product.getProductByID(Integer.parseInt(idString));
        if (product.getSeller() != seller) {
            return "You do not own this product!";
        }
        if (!product.hasFilter(filter)) {
            return "This product does not have this filter!";
        }
        product.deleteFilter(filter);
        return "Ok";
    }

    public static String addFilterToOffer(Seller seller, String idString, String filter) {
        if (!StringUtils.isNumeric(idString) || !Offer.hasOfferByID(Integer.parseInt(idString)) || Offer.getOfferByID(Integer.parseInt(idString)) == null) {
            return "Invalid ID!";
        }
        Offer offer = Offer.getOfferByID(Integer.parseInt(idString));
        if ((Seller) offer.getUser() != seller) {
            return "You do not own this offer!";
        }
        if (offer.hasFilter(filter)) {
            return "This offer already has this filter!";
        }
        offer.addFilter(filter);
        return "Ok";
    }

    public static String removeFilterFromOffer(Seller seller, String idString, String filter) {
        if (!StringUtils.isNumeric(idString) || !Offer.hasOfferByID(Integer.parseInt(idString)) || Offer.getOfferByID(Integer.parseInt(idString)) == null) {
            return "Invalid ID!";
        }
        Offer offer = Offer.getOfferByID(Integer.parseInt(idString));
        if ((Seller) offer.getUser() != seller) {
            return "You do not own this offer!";
        }
        if (!offer.hasFilter(filter)) {
            return "This offer does not have this filter!";
        }
        offer.removeFilter(filter);
        return "Ok";
    }

    public static String addAds(Seller seller, int id) {
        if (Product.getProductByID(id) == null)
            return "This product doesn't exist!";
        else if (!seller.getProducts().contains(Product.getProductByID(id)))
            return "This seller doesn't have this product!";
        else{
            Manager.addRequest(new Request(seller, Product.getProductByID(id)));
            return "Ok";
        }
    }
}
