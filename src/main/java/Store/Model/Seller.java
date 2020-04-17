package main.java.Store.Model;

import main.java.Store.Model.Enums.RequestType;
import main.java.Store.Model.Log.SellLogItem;
import java.util.ArrayList;

public class Seller extends User {

    private double money;
    private String companyName;
    private String companyDescription;
    private ArrayList<SellLogItem> sellLog = new ArrayList<SellLogItem>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName) {
        super(username, name, familyName, email, phoneNumber, password);
        companyDescription = "";
        this.money = money;
    }

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName, String companyDescription) {
        super(username, name, familyName, email, phoneNumber, password);
        this.companyDescription = companyDescription;
        this.money = money;
    }

    public void requestAddProduct(Product product) {
        new Request(product, false, null, null);
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public ArrayList<SellLogItem> getSellLog() {
        return sellLog;
    }

    public void requestChangeProduct(Product product, Product newProduct) {
        new Request(product , true, product, newProduct);
    }

    public void requestRegisterSeller()
    {
        new Request(this);
    }

    public void removeProduct(int id)
    {
        Product removeProduct = null;
        for(Product product: products)
            if(product.getProductID() == id)
            {
                removeProduct = product;
                break;
            }
        products.remove(removeProduct);
    }

    //showing categories handled in controller

    public void requestAddOffer(Offer offer) {
        new Request(this, offer, false, null, null);
    }

    public void requestChangeOffer(Offer offer, Offer newOffer) {
        new Request(this, offer, true, offer, newOffer);
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMoney() {
        return money;
    }

    public static void doRequest(Request request)
    {
        Seller seller = request.getSeller();
        ArrayList<Product> products = seller.getProducts();
        ArrayList<Offer> offers = seller.getOffers();
        if(request.getRequestType() == RequestType.ADD_NEW_OFFER)
        {
            offers.add(request.getOffer());
        }
        else if(request.getRequestType() == RequestType.ADD_NEW_PRODUCT)
        {
            products.add(request.getProduct());
        }
        else if(request.getRequestType() == RequestType.CHANGE_OFFER)
        {
            offers.remove(request.getOffer);
            offers.add(request.getNewOffer);
        }
        else if(request.getRequestType() == RequestType.CHANGE_PRODUCT)
        {
            products.remove(request.getProduct);
            products.add(request.getNewProduct);
        }
        else if(request.getRequestType() == RequestType.REGISTER_SELLER)
        {
            allUsers.add(request.getSeller());
        }
    }

    private ArrayList<Offer> getOffers()
    {
        return offers;
    }
    private ArrayList<Product> getProducts()
    {
        return products;
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
