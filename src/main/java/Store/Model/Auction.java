package Store.Model;

import Store.Model.Enums.CheckingStatus;
import Store.Model.Log.BuyLogItem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Auction implements Serializable {
    int auctionId;
    private Seller seller;
    private Product product;
    private Customer currentBuyer;
    private double basePrice;
    private double highestPrice = 0.0;
    private LocalDateTime endingTime;
    private Queue<String> messages = new LinkedList<>();
    private int messagePort;

    private static ArrayList<Auction> allAuctions = new ArrayList<>();
    private static ArrayList<Product> allAuctionsProducts = new ArrayList<>();


    private static int idCounter = 0;

    public Auction(Seller seller, Product product, LocalDateTime endingTime) {
        this.auctionId = idCounter++;
        this.seller = seller;
        this.product = product;
        this.endingTime = endingTime;
        this.basePrice = product.getPrice();
        this.highestPrice = basePrice;
        messages.add("Start");
        allAuctions.add(this);
        allAuctionsProducts.add(product);
    }

    public static void setAllAuctions(ArrayList<Auction> allAuctions) {
        Auction.allAuctions = allAuctions;
    }

    public static void setAllAuctionsProducts(ArrayList<Product> allAuctionsProducts) {
        Auction.allAuctionsProducts = allAuctionsProducts;
    }

    public static void setIdCounter(int idCounter) {
        Auction.idCounter = idCounter;
    }

    public static double getMoneyInAuctions(Customer customer) {
        double sum = 0;
        for (Auction auction: allAuctions){
            if (auction.getCurrentBuyer() != null && auction.getCurrentBuyer().equals(customer))
                sum += auction.getHighestPrice();
        }
        return sum;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setMessagePort(int messagePort) {
        this.messagePort = messagePort;
    }

    public Queue<String> getMessages()
    {
        return messages;
    }

    public int getMessegePort() {
        return messagePort;
    }

    synchronized public static ArrayList<Product> getAllAuctionsProducts() {
        return allAuctionsProducts;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static Auction getAuctionOfProduct(Product product) {
        for (Auction auction: allAuctions)
        {
            if (auction.getProduct().equals(product) && auction.getSeller().equals(product.getSeller()))
                return auction;
        }
        return null;
    }

    public Customer getCurrentBuyer() {
        return currentBuyer;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public User getSeller() {
        return seller;
    }

    public Product getProduct() {
        return product;
    }

    public static Auction getAuctionByID(int id) {
        for (Auction auction : allAuctions) {
            if (auction.getAuctionID() == id) {
                return auction;
            }
        }
        return null;
    }

    public int getAuctionID() {
        return auctionId;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    public LocalDateTime getEndingTime() {
        return this.endingTime;
    }

    public static ArrayList<Auction> getAllAuctions() {
        return allAuctions;
    }

    public void finish(){
        if (currentBuyer != null) {
            currentBuyer.setMoney(currentBuyer.getMoney() - highestPrice);
            seller.setMoney(seller.getMoney() + highestPrice - Manager.getKarmozd());
            ArrayList<Product> arrayList = new ArrayList<>();
            arrayList.add(product);
            currentBuyer.getBuyLog().add(new BuyLogItem(new Date(), arrayList, 0, seller.getUsername(), false, "Address"));
            seller.handleLogs(0, arrayList, new Date(), currentBuyer, highestPrice);
        }
        allAuctions.remove(this);
        boolean flag = false;
        for (Auction auction : allAuctions)
            if (auction.getProduct().equals(product))
                flag = true;
            if (!flag)
                allAuctionsProducts.remove(product);
    }

    @Override
    public String toString() {
        return "{" +
                "auctionID=" + auctionId +
                ", endingTime=" + endingTime +
                ", Product=" + this.product +
                '}';
    }

    @Override
    public boolean equals(Object object)
    {
        Auction auction = (Auction) object;
        if(seller.equals(auction.getSeller()) && product.equals(auction.getProduct()))
            return true;
        return false;
    }

    public void changeBet(Customer customer, double newPrice) {
        this.currentBuyer = customer;
        this.highestPrice = newPrice;
    }
//
//    public void add(String message) {
//        messages.add(message);
//    }
}