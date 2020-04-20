package Store.Model;

import Store.Model.Log.BuyLogItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Customer extends User {

    private HashMap<OffCode, Integer> offCodes = new HashMap<OffCode, Integer>();
    private double money;
    private ArrayList<BuyLogItem> buyLog = new ArrayList<BuyLogItem>();
    private ArrayList<Product> cart = new ArrayList<Product>(); // It's better to have a cart entry

    Customer(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        super(username, name, familyName, email, phoneNumber, password);
        this.money = money;
    }

    public static void addCustomer(Customer customer) {
        allUsers.add(customer);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public HashMap getOffCodes() {
        return offCodes;
    }

    public void addOffCode(OffCode offCode) {
        offCodes.put(offCode, 0);
    }

    public boolean canBuy(OffCode offCode) {
        if (money >= getTotalCartPriceWithDiscount(offCode))
            return true;
        return false;
    }

    public boolean canBuy() {
        if (money >= getTotalCartPrice())
            return true;
        return false;
    }

    public void buy(OffCode offCode) {
        money -= getTotalCartPriceWithDiscount(offCode);
        handleLogs(getTotalCartPrice() - getTotalCartPriceWithDiscount(offCode));
        cart.clear();
        offCodes.put(offCode, offCodes.get(offCode) + 1);
        if (offCodes.get(offCode) >= offCode.getUsageCount()) {
            removeOffCodeOfUser(offCode);
        }
    }

    public void removeOffCodeOfUser(OffCode offCode) {
        offCodes.remove(offCode);
    }

    public void buy() {
        money -= getTotalCartPrice();
        handleLogs(0);
        cart.clear();
    }

    private void handleLogs(double discount) {
        double totalPrice = getTotalCartPrice();
        Product product = null;
        Seller seller = null;
        Date date = new Date();
        ArrayList<Product> productsOfOneSeller = new ArrayList<Product>();
        while (cart.size() > 0) {
            seller = null;
            product = null;
            productsOfOneSeller.clear();
            for (int i = 0; i < cart.size(); i++) {
                product = cart.get(i);
                if (seller == null) {
                    seller = product.getSeller();
                    productsOfOneSeller.add(product);
                    cart.remove(product);
                    i--;
                } else {
                    if (product.getSeller().equals(seller)) {
                        productsOfOneSeller.add(product);
                        cart.remove(product);
                        i--;
                    }
                }
            }
            buyLog.add(new BuyLogItem(buyLog.size() + 1, date, productsOfOneSeller, (discount / totalPrice) * priceOfList(productsOfOneSeller), seller.getName(), false));
            seller.handleLogs((discount / totalPrice) * priceOfList(productsOfOneSeller), productsOfOneSeller, date, this, (1 - discount / totalPrice) * priceOfList(productsOfOneSeller));
        }

    }

    private double priceOfList(ArrayList<Product> list) {
        double total = 0;
        for (Product product : list)
            total += product.getPrice();
        return total;
    }

    public void addToCart(Product product) {
        cart.add(product);
    }

    public void removeFromCart(Product product) {
        if (cart.contains(product)) {
            cart.remove(product);
        }
    }

    public double getTotalCartPrice() {
        double totalPrice = 0;
        for (Product product : cart)
            totalPrice += product.getPrice();
        return totalPrice;
    }

    private double getTotalCartPriceWithDiscount(OffCode offCode) {
        double totalDiscount = getTotalCartPrice() * (offCode.getOffPercentage()) / 100.00;
        if (totalDiscount > offCode.getMaximumOff())
            totalDiscount = offCode.getMaximumOff();
        return (getTotalCartPrice() - totalDiscount);
    }

    public boolean hasBoughtProduct(Product product) {
        if (cart.contains(product))
            return true;
        return false;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    @Override
    public void delete() {
        allUsers.remove(this);
    }
}
