package Store.View;

import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.Seller;
import Store.Model.User;

import java.util.regex.Matcher;

public class SignUpAndLoginMenu {

    private static User temporaryUser;

    public static void init() {
        String input;
        Matcher matcher;
        System.out.println("\nSign up and login menu\n");
        while(!(input = InputManager.getNextLine()).equalsIgnoreCase("back"))
        {
            if((matcher = InputManager.getMatcher(input, "(?i)create account(?-i) (\\w+) (\\w+)")).find())
            {
                if(!matcher.group(3).equalsIgnoreCase("customer") && !matcher.group(3).equalsIgnoreCase("seller") && !matcher.group(3).equalsIgnoreCase("manager"))
                {
                    System.out.println("wrong type");
                }
                else {
                    if (User.isExist(User.getUserByUsername(matcher.group(3)))) {
                        System.out.println("the username exists");
                    } else {
                        createAccountWrapper(matcher.group(3), matcher.group(4), getPassByUser());
                    }
                }
            }
            else if((matcher = InputManager.getMatcher(input, "(?i)login(?-i) (\\w+)")).find())
            {
                if(!User.isExist(User.getUserByUsername(matcher.group(3))))
                {
                    System.out.println("the username doesn't exist");
                }
                else
                {
                    temporaryUser = login(matcher.group(3), getPassByUser());
                    if(temporaryUser == null)
                        System.out.println("wrong password");
                    else {
                        MainMenu.currentUser = temporaryUser;
                        if (MainMenu.currentUser instanceof Customer)
                            CustomerMenu.init();
                        else if (MainMenu.currentUser instanceof Seller)
                            SellerMenu.init();
                        else if (MainMenu.currentUser instanceof Manager)
                            ManagerMenu.init();
                    }
                }
            }
            else if(input.equalsIgnoreCase("help"))
            {
                printHelp();
            }
            else if(input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
            }
            else if(input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if(input.equalsIgnoreCase("login"))
            {
                handleLogin();
            }
            else if(input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
            }
            else
            {
                System.out.println("invalid command");
            }
        }
    }

    private static String getPassByUser()
    {
        System.out.print("Password: ");
        return InputManager.getNextLine();
    }

    private static void createAccountWrapper(String type, String username, String password) {
        System.out.println(SignUpAndLoginController.createAccount(type, username, password));
    }

    private static User login(String username, String password)
    {
        if(User.getUserByUsername(username).validatePassword(password))
            return User.getUserByUsername(username);
        else
            return null;
    }

    private static void printHelp() {
        System.out.println("create account [type] [username]");
        System.out.println("login [username]");
        System.out.println("back");
    }

    private static void handleLogin() {
        if (MainMenu.currentUser == null) {
            SignUpAndLoginMenu.init();
        } else {
            System.out.println("you have signed in");
        }
    }

    private static void handleLogout() {
        if (MainMenu.currentUser == null) {
            System.out.println("you haven't signed in");
        } else {
            MainMenu.currentUser = null;
            MainMenu.init();
        }
    }
}
