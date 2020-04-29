package Store.View;

import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Model.Manager;
import Store.Model.User;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class SignUpAndLoginMenu {

    private static final String CREATE_ACCOUNT_REGEX = "^create account (customer|seller|manager) ([^\\s]+)$";
    private static final String LOGIN_REGEX = "^login ([^\\s]+)$";

    public static void init() {
        String input;
        Matcher matcher;
        System.out.println("\nSign up and login menu\n");
        while(!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            System.out.println(input);
            if((matcher = InputManager.getMatcher(input, CREATE_ACCOUNT_REGEX)).find()) {
                handleCreateAccount(matcher.group(1), matcher.group(2));
            }
            else if((matcher = InputManager.getMatcher(input, LOGIN_REGEX)).find()) {
                if (handleLogin(matcher.group(1))) {
                    System.out.println("Login successful");
                    break;
                }
            }
            else if(input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else if(input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
            }
            else if(input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
            }
            else {
                System.out.println("invalid command");
            }
        }
    }

    private static void handleCreateAccount(String type, String username) {
        if (User.getUserByUsername(username) != null) {
            System.out.println("A user with this username already exists");
            return;
        }
        if (type.equalsIgnoreCase("manager") && Manager.hasManager) {
            System.out.println("You can only create the first manager via this method");
            return;
        }
        ArrayList<String> attributes = getPersonal(username);
        if (type.equalsIgnoreCase("seller")) {
            double money = getMoney();
            String companyName, companyDescription;
            System.out.println("Company Name: ");
            companyName = InputManager.getNextLine();
            System.out.println("Company Description: ");
            companyDescription = InputManager.getNextLine();

            SignUpAndLoginController.createSeller(attributes, money, companyName, companyDescription);
        }
        else if (type.equalsIgnoreCase("customer")) {
            SignUpAndLoginController.createCustomer(attributes, getMoney());
        }
        else {
            SignUpAndLoginController.createManager(attributes);
        }
    }

    private static double getMoney() {
        System.out.println("Initial Money: ");
        String input;
        while (!(input = InputManager.getNextLine()).matches("^\\d+(.\\d+)?$")) {
            System.out.println("Invalid floating-point number format");
        }
        return Double.parseDouble(input);
    }

    private static ArrayList<String> getPersonal(String username) {
        String firstName, lastName, email, phoneNumber, password;
        System.out.println("Password: ");
        password = InputManager.getNextLine();
        System.out.println("First Name: ");
        firstName = InputManager.getNextLine();
        System.out.println("Last Name: ");
        lastName = InputManager.getNextLine();
        System.out.println("Email: ");
        while (!(email = InputManager.getNextLine()).matches("[^\\s]+@[^\\s]+(\\.)com")) {
            System.out.println("Invalid format");
        }
        System.out.println("Phone Number: ");
        while (!(phoneNumber = InputManager.getNextLine()).matches("^[0-9]+$")) {
            System.out.println("Invalid format");
        }
        ArrayList<String> res = new ArrayList<String>();
        res.add(username);
        res.add(firstName);
        res.add(lastName);
        res.add(email);
        res.add(phoneNumber);
        res.add(password);
        return res;
    }

    private static boolean handleLogin(String username) {
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("No user with this username exists");
            return false;
        }

        System.out.println("Please enter your password: ");
        String password = InputManager.getNextLine();
        if (user.validatePassword(username)) {
            MainMenu.currentUser = user;
            return true;
        }
        else {
            System.out.println("Invalid password");
            return false;
        }
    }

    private static void printHelp() {
        System.out.println("create account [type] [username]");
        System.out.println("login [username]");
        System.out.println("back");
    }
}
