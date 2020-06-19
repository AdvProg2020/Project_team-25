package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Main;
import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.Seller;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;

public class MainMenuUI {

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
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
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        setupAds();
    }

    private void setupAds() {
        if (MainMenuUIController.getStaticAdUpper() != null) {
            upperStaticAd.setImage(new Image(this.getClass().getResource("/Images/"
                    + MainMenuUIController.getStaticAdUpper().getImagePath()).toExternalForm()));
        }
        if (MainMenuUIController.getStaticAdLower() != null) {
            lowerStaticAd.setImage(new Image(this.getClass().getResource("/Images/"
                    + MainMenuUIController.getStaticAdLower().getImagePath()).toExternalForm()));
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
            if (MainMenuUIController.currentUser instanceof Customer) {
                CustomerMenuUI.showCustomerMenu();
            }
            else if (MainMenuUIController.currentUser instanceof Seller)
                try {
                    Main.setPrimaryStageScene(new Scene(CustomerMenuUI.getContent(), 1200, 600));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            else if (MainMenuUIController.currentUser instanceof Manager)
                try {
                    Main.setPrimaryStageScene(new Scene(CustomerMenuUI.getContent(), 1200, 600));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
        });
    }

}
