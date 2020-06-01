package Store.Controller;

import Store.Model.Customer;
import Store.Model.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class MainMenuUIController {
    public static User currentUser;
    public static Customer guest = new Customer("guest", "guest", "guest",
            "guest@approject.com", "00000000000", "guest", 0.0);
    public static SimpleStringProperty currentUserUsername = new SimpleStringProperty("Not Logged In");
    public static SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    public static SimpleStringProperty loginLogoutButtonText = new SimpleStringProperty("Login");

    public static void setCurrentUser(User user) {
        currentUser = user;
        isLoggedIn.setValue(currentUser != guest);
        loginLogoutButtonText.setValue((isLoggedIn.getValue() ? "Logout" : "Login"));
        currentUserUsername.setValue((isLoggedIn.getValue() ? currentUser.getUsername() : "Not Logged In"));
    }
}
