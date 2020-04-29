package Store;

import Store.Model.Manager;
import Store.View.MainMenu;
import Store.View.ManagerMenu;
import Store.View.SellerMenu;
import Store.View.SignUpAndLoginMenu;

public class Main {

    public static void main(String[] args) {
//        ResourceHandler.resetFile();
//        ResourceHandler.writeAll();

        ResourceHandler.readAll();
        System.out.println(Manager.hasManager);
        SignUpAndLoginMenu.init();
        ManagerMenu.init();
        //SellerMenu.init();

        System.out.println("Save Current Database?");
        String input = InputManager.getNextLine();
        MainMenu.currentUser = null;
        if (input.equalsIgnoreCase("Y")) {
            ResourceHandler.resetFile();
            ResourceHandler.writeAll();
        }
    }
}
