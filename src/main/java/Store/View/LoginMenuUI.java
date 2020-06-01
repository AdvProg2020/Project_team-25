package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Main;
import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Main;
import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LoginMenuUI {

    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public Label errorMessage;


    public LoginMenuUI() {

    }

    public static void handleEvent() {
        if (MainMenuUIController.currentUser == MainMenuUIController.guest) {
            showLoginMenu();
        }
        else {
            MainMenuUIController.setCurrentUser(MainMenuUIController.guest);
        }
    }

    public static void showLoginMenu() {
        try {
            Main.setupOtherStage(new Scene(getContent()), "Login");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(LoginMenuUI.class.getClassLoader().getResource("FXML/Login.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        setupBindings();
    }

    public void setupBindings() {
        loginButton.setOnAction((e) -> handleLoginValidation());
    }

    private boolean checkEmptyFields() {
        boolean result = true;
        if (usernameField.getText().isEmpty()) {
            setError(usernameField, true);
            result = false;
        }
        if (passwordField.getText().isEmpty()) {
            setError(passwordField, true);
            result = false;
        }
        if (!result) {
            throwError("Please fill out all the needed fields!");
        }
        return result;
    }

    private void handleLoginValidation() {
        if (!checkEmptyFields()) {
            return;
        }
        resetAllErrors();
        String username = usernameField.getText();
        if (User.getUserByUsername(username) == null) {
            throwError("There is no user with this username!");
            setError(usernameField, true);
            return;
        }
        User user = User.getUserByUsername(username);
        if (!user.validatePassword(passwordField.getText())) {
            throwError("Invalid password!");
            setError(passwordField, true);
            return;
        }
        MainMenuUIController.setCurrentUser(user);
        moveShoppingCart();
        ((Stage) loginButton.getScene().getWindow()).close();
    }

    private void resetAllFields() {
        resetAllErrors();
        usernameField.setText("");
        passwordField.setText("");
    }

    private void resetAllErrors() {
        errorMessage.setVisible(false);
        setError(usernameField, false);
        setError(passwordField, false);
    }

    private void throwError(String message) {
        errorMessage.setVisible(true);
        errorMessage.setText(message);
    }

    private void setError(TextField field, boolean isError) {
        ObservableList<String> styleClass = field.getStyleClass();
        if (isError) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        }
        else {
            styleClass.removeAll(Collections.singleton("error"));
        }
    }

    private static boolean handleLogin(String username) {
        User user;
        if ((user = User.getUserByUsername(username)) == null) {
            System.out.println("No user with this username exists!");
            return false;
        }
        System.out.println("Please enter your password: ");
        String password = InputManager.getNextLine();
        String message = SignUpAndLoginController.handleLogin(username, password);
        System.out.println(message);
        if (message.equals("Login successful.")) {
            MainMenu.currentUser = user;
            return true;
        }
        return false;
    }

    public static void logoutWrapper() {
        if (MainMenu.currentUser == MainMenu.guest) {
            System.out.println("You haven't signed in!");
        } else {
            MainMenu.currentUser = MainMenu.guest;
            MainMenu.init();
        }
    }

//    public static void loginWrapper() {
//        if (MainMenu.currentUser == MainMenu.guest) {
//            SignUpCustomerAndSellerMenuUI.init();
//            if (MainMenu.currentUser != MainMenu.guest) {
//                moveShoppingCart();
//            }
//        } else {
//            System.out.println("You have signed in!");
//        }
//    }
//
    private static void moveShoppingCart() {
        if (MainMenuUIController.currentUser instanceof Customer) {
            for (Product product : MainMenuUIController.guest.getCart()) {
                ((Customer) MainMenuUIController.currentUser).addToCart(product);
            }
        }
        MainMenuUIController.guest.getCart().clear();
    }
}
