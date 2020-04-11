package Store.View;

import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.Seller;
import Store.Model.User;

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
