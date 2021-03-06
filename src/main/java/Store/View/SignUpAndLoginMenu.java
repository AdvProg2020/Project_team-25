package Store.View;

import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class SignUpAndLoginMenu {

    private static final String CREATE_ACCOUNT_REGEX = "^create account (customer|seller|manager) ([^\\s]+)$";
    private static final String LOGIN_REGEX = "^login ([^\\s]+)$";

    public static void init() {
        String input;
        Matcher matcher;
        System.out.println("\nSign up and Login menu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, CREATE_ACCOUNT_REGEX)).find()) {
                handleCreateAccount(matcher.group(1), matcher.group(2));
            } else if ((matcher = InputManager.getMatcher(input, LOGIN_REGEX)).find()) {
                if (handleLogin(matcher.group(1))) {
                    break;
                }
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
                System.out.println("\nSign up and Login menu\n");
            } else if (input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
                System.out.println("\nSign up and Login menu\n");
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void handleCreateAccount(String type, String username) {
        System.out.println(SignUpAndLoginController.handleCreateAccount(type, username));
    }

    public static ArrayList<String> getCompanyDescription() {
        String companyName, companyDescription;
        System.out.println("Company Name: ");
        companyName = InputManager.getNextLine();
        System.out.println("Company Description: ");
        companyDescription = InputManager.getNextLine();
        return new ArrayList(Arrays.asList(companyName, companyDescription));
    }

    public static double getMoney() {
        System.out.println("Initial Money: ");
        String input;
        while (!(input = InputManager.getNextLine()).matches("^\\d+(.\\d+)?$")) {
            System.out.println("Invalid floating-point number format!");
        }
        return Double.parseDouble(input);
    }

    public static ArrayList<String> getPersonal(String username) {
        String firstName, lastName, email, phoneNumber, password;
        System.out.println("Password: ");
        password = InputManager.getNextLine();
        System.out.println("First Name: ");
        firstName = InputManager.getNextLine();
        System.out.println("Last Name: ");
        lastName = InputManager.getNextLine();
        System.out.println("Email: ");
        while (!(email = InputManager.getNextLine()).matches("[^\\s]+@[^\\s]+(\\.)com")) {
            System.out.println("Invalid format!");
        }
        System.out.println("Phone Number: ");
        while (!(phoneNumber = InputManager.getNextLine()).matches("^[0-9]+$")) {
            System.out.println("Invalid format!");
        }
        ArrayList<String> res = new ArrayList<String>(Arrays.asList(username, firstName, lastName, email, phoneNumber, password));
        return res;
    }

    private static boolean handleLogin(String username) {
        User user;
        if ((user = User.getUserByUsername(username)) == null) {
            System.out.println("No user with this username exists!");
            return false;
        }
        System.out.println("Please enter your password: ");
        String password = InputManager.getNextLine();
        String message = SignUpAndLoginController.handleLogin(username, password);
        System.out.println(message);
        if (message.equals("Login successful.")) {
            MainMenu.currentUser = user;
            return true;
        }
        return false;
    }

    public static void logoutWrapper() {
        if (MainMenu.currentUser == MainMenu.guest) {
            System.out.println("You haven't signed in!");
        } else {
            MainMenu.currentUser = MainMenu.guest;
            MainMenu.init();
        }
    }

    private static void printHelp() {
        System.out.println("create account [type] [username]");
        System.out.println("login [username]");
        System.out.println("help");
        System.out.println("offs");
        System.out.println("products");
        System.out.println("back");
    }

    public static void loginWrapper() {
        if (MainMenu.currentUser == MainMenu.guest) {
            SignUpAndLoginMenu.init();
            if (MainMenu.currentUser != MainMenu.guest) {
                moveShoppingCart();
            }
        } else {
            System.out.println("You have signed in!");
        }
    }

    private static void moveShoppingCart() {
        if (MainMenu.currentUser instanceof Customer) {
            for (Product product : MainMenu.guest.getCart()) {
                ((Customer) MainMenu.currentUser).addToCart(product);
            }
        }
        MainMenu.guest.getCart().clear();
    }
}
