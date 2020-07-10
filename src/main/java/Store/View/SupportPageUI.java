package Store.View;

import Store.Controller.CustomerController;
import Store.Controller.MainMenuUIController;
import Store.Controller.ProductController;
import Store.Main;
import Store.Model.*;
import Store.View.AdditionalUtils.PannableCanvas;
import Store.View.AdditionalUtils.SceneGestures;
import Store.View.AdditionalUtils.VideoPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SupportPageUI {

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;

    public Label loggedInStatusText;
    public Button signUpButton;
    public Button loginLogoutButton;

    public SupportPageUI() {

    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/SupportPage.fxml"));
        return root;
    }

    public static void showSupportPage() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
    }

    public void setupBindings() {
        mainMenuButton.setOnAction((e) -> {
            try {
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        productsButton.setOnAction((e) -> ProductsMenuUI.showProductsMenu());
        offersButton.setOnAction((e) -> OffersMenuUI.showOffersMenu());
        userPageButton.setOnAction((e) -> UserPageHandlerUI.handleEvent());
    }
}
