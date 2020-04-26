package Store.Model;

import Store.Model.Enums.RequestType;
import Store.Model.Log.SellLogItem;

import java.util.ArrayList;
import java.util.Date;

public class Seller extends User {

    private double money;
    private String companyName;
    private String companyDescription;
    private ArrayList<SellLogItem> sellLog = new ArrayList<SellLogItem>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();

    Seller(String username, String name, String familyName, String email, String phoneNumber, String password, double money, String companyName, String companyDescription) {
        super(username, name, familyName, email, phoneNumber, password);
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.money = money;
        this.type = "Seller";
    }

    public Seller(User user, String password, double money, String companyName, String companyDescription)
    {
        super(user.getUsername(), user.getName(), user.getFamilyName(), user.getEmail(), user.getPhoneNumber(), password);
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.money = money;
        this.type = "Seller";
    }

    public void requestAddProduct(Product product) {
        new Request(product, false, null);
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

    public ArrayList<String> getBuyers()
    {
        ArrayList<String> buyers = null;
        for(SellLogItem sellLogItem: sellLog)
             buyers.add(sellLogItem.getCustomerName());
        return buyers;
    }
    public void requestChangeProduct(Product product, Product newProduct) {
        new Request(product, true, newProduct);
    }

    public void requestRegisterSeller() {
        new Request(this);
    }

    public void handleLogs(double offValue, ArrayList<Product> sellProducts, Date date, Customer customer, double income) {
        sellLog.add(new SellLogItem(sellLog.size() + 1, date, sellProducts, income, offValue, customer.getName(), false));
    }

    public void removeProduct(int id) {
        Product removeProduct = null;
        for (Product product : products)
            if (product.getProductID() == id) {
                removeProduct = product;
                break;
            }
        Product.deleteProduct(removeProduct);
        products.remove(removeProduct);
        Offer.removeProductFromOffer(removeProduct);
    }

    public void removeProducts(ArrayList<Product> productsToRemove) {
        for(Product product: productsToRemove) {
            this.removeProduct(product);
        }
    }

    public void removeProduct(Product productToRemove)
    {
        Product.deleteProduct(productToRemove);
        products.remove(productToRemove);
        Offer.removeProductFromOffer(productToRemove);
    }
    //showing categories handled in controller

    public void requestAddOffer(Offer offer) {
        new Request(this, offer, false, null);
    }

    public void requestChangeOffer(Offer offer, Offer newOffer) {
        new Request(this, offer, true, newOffer);
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public double getMoney() {
        return money;
    }

    public static void doRequest(Request request) {
        Seller seller = request.getSeller();
        ArrayList<Product> products = seller.getProducts();
        ArrayList<Offer> offers = seller.getOffers();
        if (request.getRequestType() == RequestType.ADD_NEW_OFFER) {
            Offer.addOfferToAllOffers(request.getOffer());
            offers.add(request.getOffer());
        } else if (request.getRequestType() == RequestType.ADD_NEW_PRODUCT) {
            Product.addProduct(request.getProduct());
            products.add(request.getProduct());
        } else if (request.getRequestType() == RequestType.CHANGE_OFFER) {
            Offer.deleteOfferFromAllOffers(request.getOffer());
            Offer.addOfferToAllOffers(request.getOffer());
            offers.remove(request.getOffer());
            offers.add(request.getNewOffer());
        } else if (request.getRequestType() == RequestType.CHANGE_PRODUCT) {
            Product.deleteProduct(request.getProduct());
            Product.addProduct(request.getNewProduct());
            products.remove(request.getProduct());
            products.add(request.getNewProduct());
        } else if (request.getRequestType() == RequestType.REGISTER_SELLER) {
            allUsers.add(request.getSeller());
        }
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public void delete() {
        while (products.size() > 0) {
            Product product = products.get(0);
            products.remove(0);
            Product.deleteProduct(product);
        }
        while (offers.size() > 0) {
            Offer offer = offers.get(0);
            Offer.deleteOfferFromAllOffers(offer);
            offers.remove(0);
        }
        allUsers.remove(this);
    }

    @Override
    public String toString()
    {
        String output = null;
        output += "Username: " + username;
        output += "\nFirst Name: " + name;
        output += "\nFamily Name: " + familyName;
        output += "\nCompany Name: " + companyName;
        if(companyDescription != null)
            output += "\nCompany Discriptions: " + familyName;
        output += "\nEmail: " + email;
        output += "\nPhone Number: " + phoneNumber;
        output += "\nSell Log:\n";
        output += "\nProducts:\n";
        for(Product product: products)
            output += product;
        for(SellLogItem sellLogItem: sellLog)
            output += sellLogItem;
        output += "\nOffers:\n";
        for(Offer offer: offers)
            output += offer;
        return output;
    }

    @Override
    public boolean equals(Object object) {
        Seller seller = (Seller) object;
        if(super.equals(seller))
            if(companyName.equals(seller.getCompanyName()) && companyDescription.equals(seller.getCompanyDescription()) && offers.equals(seller.getOffers()) && sellLog.equals(seller.getSellLog()) && products.equals(seller.getProducts()))
                return true;
        return false;
    }
}
