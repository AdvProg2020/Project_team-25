package Store.Controller;

import Store.Model.*;
import Store.Networking.BankAPI;
import Store.Networking.MainServer;
import Store.View.MainMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignUpAndLoginController {

    public static void createManager(ArrayList<String> attributes) throws Exception {
        if (Manager.hasManager) {
            ((Manager) User.getUserByUsername(MainMenuUIController.currentUserUsername.getValue())).addNewManager(new Manager(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5)));
        }else{
            new Manager(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5));
            int bankAccount = 0;
            String input = "";
            try {
                input = String.valueOf(MainServer.sendAndReceiveToBankAPICreateAccount());
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (Pattern.matches("\\d+", input))
            {
                bankAccount = Integer.parseInt(input);
                Manager.setBankAccount(bankAccount);
            }
            else{
                throw new Exception(input);
            }
        }
    }

    public static void createSeller(ArrayList<String> attributes, String companyName, String companyDescription) {
        Seller seller = new Seller(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4),
                attributes.get(5), companyName, companyDescription);
        Manager.addRequest(new Request(seller));
    }

    public static void createCustomer(ArrayList<String> attributes) throws Exception {
        Customer customer = new Customer(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5));
        System.out.println(attributes);
        int bankAccount = 0;
        String input = "";
        try {
            input = String.valueOf(MainServer.sendAndReceiveToBankAPICreateAccount());
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (Pattern.matches("\\d+", input))
        {
            bankAccount = Integer.parseInt(input);
            Customer.addCustomer(customer, bankAccount);
        }
        else{
            throw new Exception(input);
        }
    }

    /*public static String handleLogin(String username, String password) {
        User user = User.getUserByUsername(username);

        if (user.validatePassword(password)) {
            MainMenu.currentUser = user;
            return "Login successful.";
        } else {
            return "Invalid password!";
        }
    }*/

    /*public static String handleCreateAccount(String type, String username) {
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
    }*/

    public static void handleCreateAccount(String type, ArrayList<String> attributes) throws Exception {
        if (type.equalsIgnoreCase("seller")) {
            String companyName = attributes.get(6);
            String companyDescription = attributes.get(7);
            SignUpAndLoginController.createSeller(new ArrayList<>(attributes.subList(0, 6)), companyName, companyDescription);
        }
        else if (type.equalsIgnoreCase("customer")) {
            SignUpAndLoginController.createCustomer(new ArrayList<>(attributes.subList(0, 6)));
        }
        else {
            System.err.println("STATE: " + Manager.hasManager);
            SignUpAndLoginController.createManager(attributes);
        }
    }

}