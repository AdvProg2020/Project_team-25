package Store.Model;

import Store.Model.Log.SellLogItem;

import java.util.ArrayList;

public class Seller extends User {

    private String companyName;
    private String companyDescription;
    private ArrayList<SellLogItem> sellLog = new ArrayList<SellLogItem>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName) {
        super(username, name, familyName, email, phoneNumber, password, money);
        companyDescription = "";
    }

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName, String companyDescription) {
        super(username, name, familyName, email, phoneNumber, password, money);
        this.companyDescription = companyDescription;
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
