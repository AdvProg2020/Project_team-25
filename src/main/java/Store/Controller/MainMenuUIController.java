package Store.Controller;

import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class MainMenuUIController {
    public static User currentUser;
    public static Customer guest = new Customer("guest", "guest", "guest",
            "guest@approject.com", "00000000000", "guest", 0.0);
    public static SimpleStringProperty currentUserUsername = new SimpleStringProperty("Not Logged In");
    public static SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    public static SimpleStringProperty loginLogoutButtonText = new SimpleStringProperty("Login");

    public static Product staticAdUpper;
    public static Product staticAdLower;
    public static Product[] slideshowAd = new Product[5];

    public static Product getStaticAdLower() {
        return staticAdLower;
    }

    public static Product getStaticAdUpper() {
        return staticAdUpper;
    }

    public static Product[] getSlideshowAd() {
        return slideshowAd;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        isLoggedIn.setValue(currentUser != guest);
        loginLogoutButtonText.setValue((isLoggedIn.getValue() ? "Logout" : "Login"));
        currentUserUsername.setValue((isLoggedIn.getValue() ? currentUser.getUsername() : "Not Logged In"));
    }
}
