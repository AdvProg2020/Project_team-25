package Store.Controller;

import Store.Model.*;
import Store.View.MainMenu;
import Store.View.SignUpAndLoginMenu;

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
        Customer.addCustomer(new Customer(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3),
                attributes.get(4), attributes.get(5), money));
    }

    public static String handleLogin(String username, String password) {
        User user = User.getUserByUsername(username);

        if (user.validatePassword(password)) {
            MainMenu.currentUser = user;
            return "Login successful.";
        } else {
            return "Invalid password!";
        }
    }

    public static String handleCreateAccount(String type, String username) {
        if (User.getUserByUsername(username) != null) {
            return "A user with this username already exists!";
        }
        if (type.equalsIgnoreCase("manager") && Manager.hasManager) {
            return "You can only create the first manager via this method!";
        }
        ArrayList<String> attributes = SignUpAndLoginMenu.getPersonal(username);
        if (type.equalsIgnoreCase("seller")) {
            double money = SignUpAndLoginMenu.getMoney();
            ArrayList<String> companyDescription = SignUpAndLoginMenu.getCompanyDescription();

            SignUpAndLoginController.createSeller(attributes, money, companyDescription.get(0), companyDescription.get(1));
        } else if (type.equalsIgnoreCase("customer")) {
            SignUpAndLoginController.createCustomer(attributes, SignUpAndLoginMenu.getMoney());
        } else {
            SignUpAndLoginController.createManager(attributes);
        }
        return "Register successfully.";
    }

}
