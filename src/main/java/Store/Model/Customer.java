package Store.Model;

import Store.Model.Log.BuyLogItem;

import java.util.ArrayList;

public class Customer extends User {

    private ArrayList<BuyLogItem> buyLog = new ArrayList<BuyLogItem>();
    private ArrayList<Product> cart = new ArrayList<Product>(); // It's better to have a cart entry

    Customer(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        super(username, name, familyName, email, phoneNumber, password, money);
        allUsers.add(this);
    }

    public void buy() {

    }

    public void addToCart(Product product) {

    }

    public int getTotalCartPrice() {
        return 0;
    }
}
