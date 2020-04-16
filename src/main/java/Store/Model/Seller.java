package main.java.Store.Model;

import main.java.Store.Model.Log.SellLogItem;

import java.util.ArrayList;

public class Seller extends User {

    private double money;
    private String companyName;
    private String companyDescription;
    private ArrayList<SellLogItem> sellLog = new ArrayList<SellLogItem>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();
    private ArrayList<Request> requests = new ArrayList<Request>();

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName) {
        super(username, name, familyName, email, phoneNumber, password);
        companyDescription = "";
        this.money = money;
        allUsers.add(this);
    }

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName, String companyDescription) {
        super(username, name, familyName, email, phoneNumber, password);
        this.companyDescription = companyDescription;
        this.money = money;
        allUsers.add(this);
    }

    public void addProduct(Product product) {
        requests.add(new Request(product));
    }

    public void requestChangeProduct(Product product, Product newProduct) {
        requests.add(new Request(product , newProduct));
    }

    public void requestDeleteProduct(Product product) {
        requests.add(new Request(product, null));
    }

    public void requestAddOffer(Offer offer) {
        requests.add(new Request(this, offer));
    }

    public void requestChangeOffer(Offer offer, Offer newOffer) {
        requests.add(new Request(this, offer, newOffer));
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    @Override
    public void delete()
    {
        while(products.size() > 0)
        {
            Product product = products.get(0);
            products.remove(0);
            Product.deleteProduct(product);
        }
        while(offers.size() > 0)
        {
            Offer offer = offers.get(0);
            offers.remove(0);
            offer.deleteOffer(offer);
        }
        allUsers.remove(this);
    }
}
