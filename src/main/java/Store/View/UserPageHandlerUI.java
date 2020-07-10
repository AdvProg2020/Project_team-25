package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.Operator;
import Store.Model.Seller;

public class UserPageHandlerUI {

    public static void handleEvent() {
        System.out.println(MainMenuUIController.currentUser.toString());
        System.out.println(MainMenuUIController.currentUser.getType());

        if (MainMenuUIController.currentUser instanceof Customer)
            CustomerMenuUI.showCustomerMenu();
        else if (MainMenuUIController.currentUser instanceof Seller)
            SellerMenuUI.showSellerMenu();
        else if (MainMenuUIController.currentUser instanceof Operator) {
            System.out.println("SHOULD SHOW");
            OperatorMenuUI.showOperatorMenu();
        }
        else if (MainMenuUIController.currentUser instanceof Manager)
            ManagerMenuUI.showManagerMenu();
    }
}
