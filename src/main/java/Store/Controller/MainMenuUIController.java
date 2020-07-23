package Store.Controller;

import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Random;

public class MainMenuUIController {
    public static User currentUser;
    public static Customer guest = new Customer("guest", "guest", "guest",
            "guest@approject.com", "00000000000", "guest");
    public static SimpleStringProperty currentUserUsername = new SimpleStringProperty("Not Logged In");
    public static SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    public static SimpleStringProperty loginLogoutButtonText = new SimpleStringProperty("Login");

    public static Product staticAdUpper;
    public static Product staticAdLower;
    public static Product[] slideshowAd = new Product[5];

    public static Product getStaticAdLower() {
        return staticAdLower;
    }

    public static void setStaticAdUpper(Product staticAdUpper) {
        MainMenuUIController.staticAdUpper = staticAdUpper;
    }

    public static void setStaticAdLower(Product staticAdLower) {
        MainMenuUIController.staticAdLower = staticAdLower;
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

    public static void resetAdds() {
        staticAdLower = null;
        staticAdUpper = null;
        for (int adIndex = 0; adIndex < 5; adIndex++) {
            slideshowAd[adIndex] = null;
        }
    }

    public static void setupNewAd(Product product) {
        if (staticAdUpper == null) {
            staticAdUpper = product;
            return;
        }
        else if (staticAdLower == null) {
            staticAdLower = product;
            return;
        }
        else {
            for (int slideIndex = 0; slideIndex < 5; slideIndex++) {
                if (slideshowAd[slideIndex] == null) {
                    slideshowAd[slideIndex] = product;
                    return;
                }
            }
        }

        Random random = new Random();
        int pool = random.nextInt();
        if (pool % 3 == 0) {
            staticAdUpper = product;
        }
        else if (pool % 3 == 1) {
            staticAdLower = product;
        }
        else {
            pool = random.nextInt();
            slideshowAd[pool % 5] = product;
        }
    }
}
