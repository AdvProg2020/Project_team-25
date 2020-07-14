package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.Networking.Client.Controller.ClientSignUpAndLoginController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainMenuUI {

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;

    public ImageView upperStaticAd;
    public ImageView lowerStaticAd;
    public ImageView slideshowSlide1;
    public ImageView slideshowSlide2;
    public ImageView slideshowSlide3;
    public ImageView slideshowSlide4;
    public ImageView slideshowSlide5;

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/MainMenu.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(ClientMainMenuController.currentUserUsername);
        signUpButton.disableProperty().bind(ClientMainMenuController.isLoggedIn);
        loginLogoutButton.textProperty().bind(ClientMainMenuController.loginLogoutButtonText);
        setupAds();

        Platform.runLater(() -> {
            while (!ClientMainMenuController.hasManager()) {
                SignUpManagerMenuUI.showSignUpMenu();
            }
        });
    }

    private void setupAds() {
        if (MainMenuUIController.getStaticAdUpper() != null) {
            String path;
            if (MainMenuUIController.getStaticAdUpper().getImagePath().isEmpty()) {
                path = "src/main/resources/Images/Your_Ad_Here.png";
            }
            else {
                path = MainMenuUIController.getStaticAdUpper().getImagePath();
            }

            File file = new File(path);
            upperStaticAd.setImage(new Image(file.toURI().toString()));
        }
        if (MainMenuUIController.getStaticAdLower() != null) {
            String path;
            if (MainMenuUIController.getStaticAdLower().getImagePath().isEmpty()) {
                path = "src/main/resources/Images/Your_Ad_Here.png";
            }
            else {
                path = MainMenuUIController.getStaticAdLower().getImagePath();
            }

            File file = new File(path);
            lowerStaticAd.setImage(new Image(file.toURI().toString()));
        }

        SequentialTransition slideshow = new SequentialTransition();
        int counter = 0;
        for (ImageView slide : new ImageView[] {slideshowSlide1, slideshowSlide2, slideshowSlide3, slideshowSlide4, slideshowSlide5}) {
            if (MainMenuUIController.getSlideshowAd()[counter] != null) {
                slide.setImage(new Image(this.getClass().getResource("/Images/"
                        + MainMenuUIController.getSlideshowAd()[counter].getImagePath()).toExternalForm()));
            }
            counter++;
            SequentialTransition sequentialTransition = new SequentialTransition();

            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), slide);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            PauseTransition stayOn = new PauseTransition(Duration.millis(5000));

            FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), slide);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            sequentialTransition.getChildren().addAll(fadeIn, stayOn, fadeOut);
            slideshow.getChildren().add(sequentialTransition);
        }
        slideshow.setCycleCount(Animation.INDEFINITE);
        slideshow.play();
    }

    public void setupBindings() {
        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        productsButton.setOnAction((e) -> ProductsMenuUI.showProductsMenu());
        offersButton.setOnAction((e) -> OffersMenuUI.showOffersMenu());
        userPageButton.setOnAction(e -> {
            Map<String, Object> userInfo = ClientSignUpAndLoginController.getUserInfo(ClientHandler.username);
            System.out.println(userInfo.get("type"));
            if (userInfo.get("type").equals("Customer"))
                CustomerMenuUI.showCustomerMenu();
            else if (userInfo.get("type").equals("Seller"))
                SellerMenuUI.showSellerMenu();
            else if (userInfo.get("type").equals("Manager"))
                ManagerMenuUI.showManagerMenu();
        });
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());
    }

}
