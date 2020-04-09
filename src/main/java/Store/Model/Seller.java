package Store.Model;

import java.util.ArrayList;

public class Seller extends User {

    private String companyName;
    private ArrayList<SellLogItem> sellLog = new ArrayList<SellLogItem>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName) {
        super(username, name, familyName, email, phoneNumber, password, money);
    }

    public void addProduct(Product product) {

    }

    public void requestChangeProduct(Product product, Product newProduct) {

    }

    public void requestDeleteProduct(Product product) {

    }

    public void requestAddOffer(Offer offer) {

    }

    public void requestChangeOffer(Offer offer, Offer newOffer) {

    }
}
