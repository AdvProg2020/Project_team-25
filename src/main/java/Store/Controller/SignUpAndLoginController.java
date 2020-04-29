package Store.Controller;

import Store.InputManager;
import Store.Model.*;

import java.util.ArrayList;

public class SignUpAndLoginController {

    public static void createManager(ArrayList<String> attributes) {
        new Manager(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5));
    }

    public static void createSeller(ArrayList<String> attributes, double money, String companyName, String companyDescription) {
        Seller seller = new Seller(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4),
                attributes.get(5), money, companyName, companyDescription);
        Manager.addRequest(new Request(seller));
    }

    public static void createCustomer(ArrayList<String> attributes, double money) {
        new Customer(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4),
                attributes.get(5), money);
    }

}
