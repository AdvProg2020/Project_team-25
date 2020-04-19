package Model;

import Model.Log.BuyLogItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User {

    private HashMap <OffCode, Integer> offCodes = new HashMap<>();
    private double money;
    private ArrayList<BuyLogItem> buyLog = new ArrayList<BuyLogItem>();
    private HashMap<Product, Integer> cart = new HashMap<>(); // It's better to have a cart entry

    Customer(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        super(username, name, familyName, email, phoneNumber, password);
        this.money = money;
    }

    public static void addCustomer(Customer customer)
    {
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

    public boolean buy(OffCode offCode) {
        if(money >= getTotalCartPriceWithDiscount(offCode)) {
            money -= getTotalCartPriceWithDiscount(offCode);
            cart.clear();
            offCodes.put(offCode, offCodes.get(offCode) + 1);
            if(offCodes.get(offCode) >= offCode.getUsageCount()) {
                removeOffCodeOfUser(offCode);
            }
            //logs
            return true;
        }
        else
            return false;
    }

    public void removeOffCodeOfUser(OffCode offCode)
    {
        offCodes.remove(offCode);
    }

    public boolean buy() {
        if(money >= getTotalCartPrice()) {
            money -= getTotalCartPrice();
            cart.clear();
            //logs
            return true;
        }
        else
            return false;
    }

    public void addToCart(Product product) {
        if(cart.containsKey(product))
        {
            cart.put(product, cart.get(product) + 1);
        }
        else
        {
            cart.put(product, 1);
        }
    }

    public void removeFromCart(Product product) {
        if(cart.containsKey(product)) {
            cart.put(product, cart.get(product) - 1);
            if (cart.get(product) == 0)
                cart.remove(product);
        }
    }

    public double getTotalCartPrice() {
        double totalPrice = 0;
        for(Product product: cart.keySet())
            totalPrice += (cart.get(product) * product.getPrice());
        return totalPrice;
    }

    private double getTotalCartPriceWithDiscount(OffCode offCode) {
        double totalDiscount = getTotalCartPrice() * (offCode.getOffPercentage()) / 100.00;
        if( totalDiscount > offCode.getMaximumOff() )
            totalDiscount = offCode.getMaximumOff();
        return (getTotalCartPrice() - totalDiscount);
    }

    public HashMap<Product, Integer> getCart() {
        return cart;
    }

    @Override
    public void delete()
    {
        allUsers.remove(this);
    }
}
