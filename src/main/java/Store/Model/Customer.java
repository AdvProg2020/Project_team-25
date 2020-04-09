package Store.Model;

import java.util.ArrayList;

public class Customer extends User {

    private ArrayList<BuyLogItem> buyLog = new ArrayList<BuyLogItem>();
    private ArrayList<Product> cart = new ArrayList<Product>();

    Customer(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        super(username, name, familyName, email, phoneNumber, password, money);
        allUsers.add(this);
    }

    public void buy() {

    }

    public void addToCart(Product product) {

    }
}
