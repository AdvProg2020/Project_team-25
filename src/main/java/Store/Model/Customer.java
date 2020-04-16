package main.java.Store.Model;

import main.java.Store.Model.Log.BuyLogItem;

import java.util.ArrayList;

public class Customer extends User {

    private double money;
    private ArrayList<BuyLogItem> buyLog = new ArrayList<BuyLogItem>();
    private ArrayList<Product> cart = new ArrayList<Product>(); // It's better to have a cart entry

    Customer(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        super(username, name, familyName, email, phoneNumber, password);
        this.money = money;
        allUsers.add(this);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void buy() {

    }

    public void addToCart(Product product) {

    }

    public int getTotalCartPrice() {
        return 0;
    }

    @Override
    public void delete()
    {
        allUsers.remove(this);
    }
}
