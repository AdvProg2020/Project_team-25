package Store.View;

import Store.InputManager;
import Store.Main;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.Networking.Client.Controller.ClientSignUpAndLoginController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.*;

import Store.InputManager;
import Store.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

    private static Media logoutSoundEffect;
    private static MediaPlayer soundEffectPlayer;


    public LoginMenuUI() {

    }

    public static void handleEvent() {
        if (!ClientHandler.hasLoggedIn) {
            showLoginMenu();
        } else {
            ClientMainMenuController.shutdownP2P();
            ClientMainMenuController.setCurrentUser("");
            logoutSoundEffect = new Media(LoginMenuUI.class.getResource("/Audio/Bubble-Pop-Sound-Effect.wav").toExternalForm());
            soundEffectPlayer = new MediaPlayer(logoutSoundEffect);
            soundEffectPlayer.setVolume(0.5);
            soundEffectPlayer.play();
        }
    }

    public static void showLoginMenu() {
        try {
            Main.setupOtherStage(new Scene(getContent()), "Login");
        } catch (IOException e) {
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
        if (!ClientSignUpAndLoginController.isUsernameWithThisName(username)) {
            throwError("There is no user with this username!");
            setError(usernameField, true);
            return;
        }
        Map<String, Object> userInfo = ClientSignUpAndLoginController.getUserInfo(username);
        if (!userInfo.get("password").equals(passwordField.getText())) {
            throwError("Invalid password!");
            setError(passwordField, true);
            return;
        }
        ClientMainMenuController.setCurrentUser(username);
//        MainMenuUIController.setCurrentUser(User.getUserByUsername(username)); //ATTENTION
//        moveShoppingCart();
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
        } else {
            styleClass.removeAll(Collections.singleton("error"));
        }
    }

//    private static boolean handleLogin(String username) {
//        User user;
//        if ((user = User.getUserByUsername(username)) == null) {
//            System.out.println("No user with this username exists!");
//            return false;
//        }
//        System.out.println("Please enter your password: ");
//        String password = InputManager.getNextLine();
//        String message = SignUpAndLoginController.handleLogin(username, password);
//        System.out.println(message);
//        if (message.equals("Login successful.")) {
//            MainMenu.currentUser = user;
//            return true;
//        }
//        return false;
//    }

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
//    private static void moveShoppingCart() {
//        ClientSignUpAndLoginController.moveShoppingCart(ClientHandler.username);
//    }
}
