package main.java.Store.View;

import main.java.Store.Model.Customer;
import main.java.Store.Model.Manager;
import main.java.Store.Model.Seller;
import main.java.Store.Model.User;

public class MainMenu {

    public static User currentUser;

    public static void init() {
        // Set current user
        int a = 0;
        while (true) {
            if (a == 1) {
                ProductsMenu.init();
            }
            else if (a == 2) {
                SignUpAndLoginMenu.init();
            }
            else if (a == 3) {
                OffersMenu.init();
            }
            else if (a == 4) {
                if (currentUser instanceof Customer) {
                    CustomerMenu.init();
                }
                else if (currentUser instanceof Manager) {
                    ManagerMenu.init();
                }
                else if (currentUser instanceof Seller) {
                    SellerMenu.init();
                }
            }
        }
    }

    private static void printHelp() {

    }
}
