package Store;

import Store.Model.Manager;
import Store.Model.Seller;
import Store.View.*;

public class Main {

    public static void main(String[] args) {

//        ResourceHandler.resetFile();
//        ResourceHandler.writeAll();

        ResourceHandler.readAll();
        System.out.println(Manager.hasManager);
        SignUpAndLoginMenu.init();
        ProductsMenu.init();
        if (MainMenu.currentUser instanceof Manager) {
            ManagerMenu.init();
        }
        else if (MainMenu.currentUser instanceof Seller) {
            SellerMenu.init();
        }
        else {
            CustomerMenu.init();
        }

        System.out.println("Save Current Database?");
        String input = InputManager.getNextLine();
        MainMenu.currentUser = null;
        if (input.equalsIgnoreCase("Y")) {
            ResourceHandler.resetFile();
            ResourceHandler.writeAll();
        }
    }
}
