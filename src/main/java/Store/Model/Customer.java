package Store.Model;

import Store.Model.Log.BuyLogItem;
import Store.Networking.BankAPI;
import Store.Networking.MainServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Customer extends User {

    private HashMap<OffCode, Integer> offCodes = new HashMap<OffCode, Integer>();
    private double money = Manager.getMinimumRemaining();
    private ArrayList<BuyLogItem> buyLog = new ArrayList<BuyLogItem>();
    private ArrayList<Product> cart = new ArrayList<Product>();
    private int bankAccount;

    public Customer(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        this.type = "Customer";
    }

    public Customer(User user, String password) {
        super(user.getUsername(), user.getName(), user.getFamilyName(), user.getEmail(), user.getPhoneNumber(), password);
        this.type = "Customer";
    }


    public int getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(int bankAccount) {
        try {
            MainServer.sendAndReceiveToBankAPIMove(money, bankAccount, Manager.getBankAccount(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bankAccount = bankAccount;
    }

    public static void addCustomer(Customer customer, int bankAccount) {
        customer.setBankAccount(bankAccount);
        allUsers.add(customer);
        System.out.println("added");
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public HashMap<OffCode, Integer> getOffCodes() {
        return offCodes;
    }

    public void addOffCode(OffCode offCode) {
        offCodes.put(offCode, 0);
    }

    public boolean canBuy(OffCode offCode, boolean bank) throws Exception {
        double money = this.money;
        if (bank)
        {
            try {
                Object input = MainServer.sendAndReceiveToBankAPIBalance();
                if (input != null)
                    money = Double.parseDouble((String)input);
                else
                    throw new Exception("something is wrong!");
                if (money >= getTotalCartPriceWithDiscount(offCode))
                    return true;
            }catch (Exception e) {
                throw e;
            }
        }else {
            if (money - Manager.getMinimumRemaining() - Auction.getMoneyInAuctions(this) >= getTotalCartPriceWithDiscount(offCode))
                return true;
        }
        return false;
    }

    public boolean canBuy(boolean bank) throws Exception {
        double money = this.money;
        if (bank)
        {
            try {
                Object input = MainServer.sendAndReceiveToBankAPIBalance();
                if (input != null)
                    money = Double.parseDouble((String)input);
                else
                    throw new Exception("something is wrong!");
                if (money >= getTotalCartPrice())
                    return true;
            }catch (Exception e) {
                throw e;
            }
        }else {
            if (money - Manager.getMinimumRemaining() - Auction.getMoneyInAuctions(this) >= getTotalCartPrice())
                return true;
        }
        return false;
    }

    public void buy(OffCode offCode, boolean bank, String address) throws Exception {
        offCodeAfterBuy();
        if (bank){
            String result = "";
            result = (String)MainServer.sendAndReceiveToBankAPIMove(getTotalCartPriceWithDiscount(offCode), bankAccount, Manager.getBankAccount(), "");
            if (!result.equalsIgnoreCase("done"))
                throw new Exception("some problem happens");
        }
        else
            money -= getTotalCartPriceWithDiscount(offCode);
        handleLogs(getTotalCartPrice() - getTotalCartPriceWithDiscount(offCode), address);
        cart.clear();
        offCodes.put(offCode, offCodes.get(offCode) + 1);
        if (offCodes.get(offCode) >= offCode.getUsageCount()) {
            removeOffCodeOfUser(offCode);
        }
    }

    public void removeOffCodeOfUser(OffCode offCode) {
        offCodes.remove(offCode);
        offCode.removeUser(this);
    }

    public void buy(boolean bank, String address) throws Exception {
        offCodeAfterBuy();
        if (bank){
            String result = "";
            result = (String)MainServer.sendAndReceiveToBankAPIMove(getTotalCartPrice(), bankAccount, Manager.getBankAccount(), "");
            if (!result.equalsIgnoreCase("done"))
                throw new Exception("some problem happens");
        }
        else
            money -= getTotalCartPrice();
        handleLogs(0, address);
        cart.clear();
    }

    private void offCodeAfterBuy()
    {
        if(priceOfList(cart) >= 200)
            Manager.assignOffCodeToUser(OffCode.randomOffCode(20), this);
        else if (priceOfList(cart) >= 100)
            Manager.assignOffCodeToUser(OffCode.randomOffCode(10), this);
    }

    public String getNewFactor()
    {
        String output = "";
        for (BuyLogItem buyLogItem: buyLog)
        {
            if(!buyLogItem.isShowed())
                output += buyLogItem + "\n";
        }
        return output;
    }

    private void handleLogs(double discount, String address) {
        double totalPrice = getTotalCartPrice();
        Product product = null;
        Seller seller = null;
        double offerOff = 0;
        Date date = new Date();
        ArrayList<Product> productsOfOneSeller = new ArrayList<Product>();
        while (cart.size() > 0) {
            seller = null;
            product = null;
            for (int i = 0; i < cart.size(); i++) {
                product = cart.get(i);
                if (seller == null) {
                    productsOfOneSeller = new ArrayList<>();
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
            offerOff = calOfferOff(productsOfOneSeller);
            buyLog.add(new BuyLogItem(date, productsOfOneSeller, priceOfList(productsOfOneSeller) - (priceOfList(productsOfOneSeller) - offerOff) * (1.0 - (discount / totalPrice)), seller.getUsername(), false, address));
            seller.handleLogs(offerOff, productsOfOneSeller, date, this, (priceOfList(productsOfOneSeller) - offerOff) * (100.0 - Manager.getKarmozd()) / 100.0);
            // seller.removeProducts(productsOfOneSeller);
            seller.setMoney(seller.getMoney() + (priceOfList(productsOfOneSeller) - offerOff) * (100.0 - Manager.getKarmozd()) / 100.0);
        }
    }

    private double calOfferOff(ArrayList<Product> products)
    {
        double off = 0;
        for(Product product: products)
            if(Offer.getOfferOfProduct(product) != null)
                off += (Offer.getOfferOfProduct(product).getOffPercent()) * product.getPrice() / 100.0;
        return off;
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
        if (isInCart(product)) {
            cart.remove(product);
        }
    }

    public boolean isInCart(Product product) {
        if (cart == null || !cart.contains(product)) {
            return false;
        }
        return true;
    }

    public double getTotalCartPrice() {
        double totalPrice = 0;
        for (Product product : cart) {
            System.out.println(Offer.getOfferOfProduct(product));
            if (Offer.getOfferOfProduct(product) == null)
                totalPrice += product.getPrice();
            else if(Offer.getOfferOfProduct(product).canBeUsedInDate(new Date()))
                totalPrice += product.getPrice() * (100.0 - Offer.getOfferOfProduct(product).getOffPercent()) / 100.0;
            else
                totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    private double getTotalCartPriceWithDiscount(OffCode offCode) {
        double totalDiscount = getTotalCartPrice() * (offCode.getOffPercentage()) / 100.00;
        if (totalDiscount > offCode.getMaximumOff())
            totalDiscount = offCode.getMaximumOff();
        return (getTotalCartPrice() - totalDiscount);
    }

    public boolean hasBoughtProduct(Product product) {
        for (BuyLogItem buyLogItem : buyLog)
            if (buyLogItem.getProducts().contains(product))
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

    @Override
    public String toString() {
        String output = "";
        output += "Username: " + username;
        output += "\nFirst Name: " + name;
        output += "\nFamily Name: " + familyName;
        output += "\nEmail: " + email;
        output += "\nPhone Number: " + phoneNumber;
        output += "\nOffcodes:\n";
        for (OffCode offCode : offCodes.keySet())
            output += offCode;          //navid
        output += "\nBuy Log:\n";
        for (BuyLogItem buyLogItem : buyLog)
            output += buyLogItem;
        return output;
    }

    public ArrayList<BuyLogItem> getBuyLog() {
        return buyLog;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer customer = (Customer) object;
        if (super.equals(customer))
            if (money == customer.getMoney() && cart.equals(customer.getCart()) && offCodes.equals(customer.getOffCodes()) && buyLog.equals(customer.getBuyLog()))
                return true;
        return false;
    }
}
