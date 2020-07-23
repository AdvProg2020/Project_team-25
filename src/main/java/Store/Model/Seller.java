package Store.Model;

import Store.Controller.MainMenuUIController;
import Store.Model.Enums.RequestType;
import Store.Model.Log.SellLogItem;
import Store.Networking.BankAPI;
import Store.Networking.MainServer;
import Store.View.MainMenuUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class Seller extends User {

    private double money = Manager.getMinimumRemaining();
    private String companyName;
    private String companyDescription;
    private ArrayList<SellLogItem> sellLog = new ArrayList<SellLogItem>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Offer> offers = new ArrayList<Offer>();
    private int bankAccount;

    public Seller(String username, String name, String familyName, String email, String phoneNumber, String password, String companyName, String companyDescription) {
        super(username, name, familyName, email, phoneNumber, password);
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.type = "Seller";
    }

    public Seller(User user, String password, String companyName, String companyDescription)
    {
        super(user.getUsername(), user.getName(), user.getFamilyName(), user.getEmail(), user.getPhoneNumber(), password);
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.type = "Seller";
    }

    public void forceAddOffer(Offer offer)
    {
        offers.add(offer);
    }
    public static void addSeller(Seller seller) {
        allUsers.add(seller);
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(int bankAccount) {
        this.bankAccount = bankAccount;
        try {
            MainServer.sendAndReceiveToBankAPIMove(money, bankAccount, Manager.getBankAccount(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addProduct(Product product)
    {
        products.add(product);
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
        ArrayList<String> buyers = new ArrayList<>();
        for(SellLogItem sellLogItem: sellLog)
            buyers.add(sellLogItem.getCustomerName());
        return buyers;
    }

    public void handleLogs(double offValue, ArrayList<Product> sellProducts, Date date, Customer customer, double income) {
        sellLog.add(new SellLogItem(date, sellProducts, income, offValue, customer.getUsername(), false));
    }

    public void removeProduct(Product productToRemove)
    {
        Product.deleteProduct(productToRemove);
        products.remove(productToRemove);
        Offer.removeProductFromOffer(productToRemove);
    }
    // showing categories handled in controller

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
            Offer.calculateAllOffProducts();
        } else if (request.getRequestType() == RequestType.ADD_NEW_PRODUCT) {
            Product.addProduct(request.getProduct());
            products.add(request.getProduct());
        } else if (request.getRequestType() == RequestType.CHANGE_OFFER) {
            Offer.deleteOfferFromAllOffers(request.getOffer());
            offers.remove(request.getOffer());
            Offer.addOfferToAllOffers(request.getNewOffer());
            offers.add(request.getNewOffer());
            Offer.calculateAllOffProducts();
        } else if (request.getRequestType() == RequestType.CHANGE_PRODUCT) {
            request.getProduct().changeProduct(request.getNewProduct());
        } else if (request.getRequestType() == RequestType.REGISTER_SELLER) {
            allUsers.add(request.getSeller());
        } else if (request.getRequestType() == RequestType.ADD_NEW_ADVERTISEMENT){
            MainMenuUIController.setupNewAd(request.getProduct());
//            if (Request.getAdCounter() % 2 == 0)
//                MainMenuUIController.setStaticAdUpper(request.getProduct());
//            else
//                MainMenuUIController.setStaticAdLower(request.getProduct());
        } else if (request.getRequestType() == RequestType.ADD_NEW_AUCTION){
            new Auction(request.getSeller(), request.getProduct(), request.getDate());
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
        String output = "";
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
        if (!(object instanceof Seller)) {
            return false;
        }
        Seller seller = (Seller) object;
        return (super.equals(object));
    }
}