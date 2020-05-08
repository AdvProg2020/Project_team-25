package Store.View;

import Store.InputManager;
import Store.Model.*;
import Store.ResourceHandler;

import java.util.regex.Matcher;

public class MainMenu {

    public static User currentUser;
    public static Customer guest = new Customer("guest", "guest", "guest",
            "guest@approject.com", "00000000000", "guest", 0.0);

    public static void init() {
        // Set current user
        String input;
        Matcher matcher;
        System.out.println("\nMain menu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("exit")) {
            if (input.equalsIgnoreCase("user page")) {
                if (currentUser == null) {
                    System.out.println("You must login before going into your user page!");
                }
                else if (currentUser instanceof Customer) {
                    CustomerMenu.init();
                }
                else if (currentUser instanceof Seller) {
                    SellerMenu.init();
                }
                else if (currentUser instanceof Manager) {
                    ManagerMenu.init();
                }
                System.out.println("\nMain menu\n");
            }
            else if (input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
                System.out.println("\nMain menu\n");
            }
            else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
                System.out.println("\nMain menu\n");
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nMain menu\n");
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
        exitAll();
    }

    private static void exitAll() {
        System.out.println("Save Current Database?");
        String input = InputManager.getNextLine();
        MainMenu.currentUser = null;
        if (input.equalsIgnoreCase("Y")) {
            ResourceHandler.resetFile();
            ResourceHandler.writeAll();
        }
        System.exit(0);
    }

    private static void printHelp() {
        System.out.println("List of main commands: ");
        System.out.println("user page");
        System.out.println("products");
        System.out.println("offs");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("exit");
        System.out.println("*******");
    }

    private static void handleLogin() {
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

    private static void handleLogout() {
        if (MainMenu.currentUser == MainMenu.guest) {
            System.out.println("You haven't signed in!");
        } else {
            MainMenu.currentUser = MainMenu.guest;
        }
    }
}
