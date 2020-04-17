package main.java.Store.Model;

import main.java.Store.Model.Log.BuyLogItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends User {

    private OffCode enableOffCode = null;
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

    public OffCode getEnableOffCode() {
        return enableOffCode;
    }

    public void setEnableOffCode(OffCode enableOffCode) {
        this.enableOffCode = enableOffCode;
    }

    public boolean buy() {
        if(money >= getTotalCartPriceWithDiscount()) {
            money -= getTotalCartPriceWithDiscount();
            cart.clear();
            enableOffCode.setUsageCount(enableOffCode.getUsageCount + 1);
            enableOffCode.removeUser(this);
            enableOffCode = null;
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

    private double getTotalCartPriceWithDiscount() {
        double totalDiscount = getTotalCartPrice() * (enableOffCode.getOffPercentage()) / 100.00;
        if( totalDiscount > enableOffCode.getMaximumOff() )
            totalDiscount = enableOffCode.getMaximumOff();
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
