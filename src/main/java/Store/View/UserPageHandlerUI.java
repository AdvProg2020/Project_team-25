package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.Operator;
import Store.Model.Seller;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientSignUpAndLoginController;

import java.util.Map;

public class UserPageHandlerUI {

    public static void handleEvent() {
        Map<String, Object> userInfo = ClientSignUpAndLoginController.getUserInfo(ClientHandler.username);
        System.out.println(userInfo.get("type"));
        if (userInfo.get("type").equals("Customer")) {
            CustomerMenuUI.showCustomerMenu();
        }
        else if (userInfo.get("type").equals("Seller")) {
            SellerMenuUI.showSellerMenu();
        }
        else if (userInfo.get("type").equals("Manager")) {
            ManagerMenuUI.showManagerMenu();
        }
        else if (userInfo.get("type").equals("Operator")) {
            OperatorMenuUI.showOperatorMenu();
        }
    }
}
