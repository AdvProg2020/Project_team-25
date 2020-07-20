package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ManagerController;
import Store.Controller.ProductsController;
import Store.Main;
import Store.Model.*;
import Store.Model.Enums.VerifyStatus;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientManagerController;
import Store.Networking.FileTransportClient;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class OperatorMenuUI {
    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;
    public ImageView imageProfile;
    public TextField usernameField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField passwordField;
    public TextField phoneNumberField;
    public TextField emailField;
    public Button editPersonalInfoButton;
    public Button changeImageButton;

    public static Parent getContent() throws IOException {
        System.out.println(OperatorMenuUI.class.getClassLoader().getResource("FXML/OperatorMenu.fxml"));
        Parent root = FXMLLoader.load(OperatorMenuUI.class.getClassLoader().getResource("FXML/OperatorMenu.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
        handlePersonalInfo();
    }

    private void initialSetup() {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        imageProfile.setPreserveRatio(false);
        imageProfile.setFitWidth(200);
        imageProfile.setFitHeight(200);

    }

    public void setupBindings() {
        offersButton.setOnAction((e) -> OffersMenuUI.showOffersMenu());
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());
        loginLogoutButton.setOnAction((e) -> {
            LoginMenuUI.handleEvent();
            try {
                Parent root = MainMenuUI.getContent();
                Main.setPrimaryStageScene(new Scene(root));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        mainMenuButton.setOnAction((e) -> {
            try {
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        productsButton.setOnAction((e) -> {
            ProductsMenuUI.showProductsMenu();
        });

    }

    public static void showOperatorMenu() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void handlePersonalInfo() {
        Map<String, Object> operator = ClientManagerController.getUserInfo(ClientHandler.username);
        usernameField.setText((String) operator.get("username"));
        usernameField.setDisable(true);
        firstNameField.setText((String) operator.get("name"));
        lastNameField.setText((String) operator.get("familyName"));
        passwordField.setText((String) operator.get("password"));
        phoneNumberField.setText((String) operator.get("phoneNumber"));
        emailField.setText((String) operator.get("email"));
        usernameField.setPromptText((String) operator.get("username"));
        firstNameField.setPromptText((String) operator.get("name"));
        lastNameField.setPromptText((String) operator.get("familyName"));
        passwordField.setPromptText((String) operator.get("password"));
        phoneNumberField.setPromptText((String) operator.get("phoneNumber"));
        emailField.setPromptText((String) operator.get("email"));

        firstNameField.setId("infoField");
        lastNameField.setId("infoField");
        emailField.setId("infoField");
        passwordField.setId("infoField");
        phoneNumberField.setId("infoField");
        usernameField.setId("infoField");

        editPersonalInfoButton.setOnMouseClicked(event -> {
            if (firstNameField.getText().equals(operator.get("name")) && lastNameField.getText().equals(operator.get("familyName")) && passwordField.getText().equals(operator.get("password")) && phoneNumberField.getText().equals(operator.get("phoneNumber")) && emailField.getText().equals(operator.get("email"))) {
                Main.errorPopUp("You Should Change Some Fields.", "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            ClientManagerController.editPersonalInfo((String) operator.get("username"), "first name", firstNameField.getText());
            ClientManagerController.editPersonalInfo((String) operator.get("username"), "family name", lastNameField.getText());

            String changePasswordMessage = ClientManagerController.editPersonalInfo((String) operator.get("username"), "password", passwordField.getText());
            String changePhoneMessage = ClientManagerController.editPersonalInfo((String) operator.get("username"), "phone number", phoneNumberField.getText());
            String changeEmailMessage = ClientManagerController.editPersonalInfo((String) operator.get("username"), "email", emailField.getText());

            if (changeEmailMessage.equals("Invalid email format!")) {
                Main.errorPopUp(changeEmailMessage, "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            if (changePhoneMessage.equals("Phone number format is incorrect!")) {
                Main.errorPopUp(changePhoneMessage, "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            if (changePasswordMessage.equals("Invalid password format!")) {
                Main.errorPopUp(changePasswordMessage, "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            Main.errorPopUp("Info has changed successfully.", "confirm", (Stage) usernameField.getScene().getWindow());
            handlePersonalInfo();
        });


        changeImageButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileChooser fileChooser = new FileChooser();
                try {
                    String path = fileChooser.showOpenDialog(new Stage()).getPath();
                    File file = new File(path);
                    File copyFile = new File("src/main/resources/Images/" + ClientHandler.username + ".jpg");
                    Files.copy(file.toPath(), copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    FileTransportClient.sendFile(ClientHandler.username, ClientHandler.token, "I", ClientHandler.username + ".jpg");
                }
                catch (Exception exception) {
                    // do nothing
                }
                handlePersonalInfo();
            }
        });

        FileTransportClient.receiveFile(ClientHandler.username, ClientHandler.token, "I", ClientHandler.username + ".jpg");
        File file = new File("src/main/resources/Images/" + ClientHandler.username + ".jpg");
        imageProfile.setImage(new Image(file.toURI().toString()));

        final Circle clip = new Circle(100, 100, 100);
        imageProfile.setClip(clip);
        clip.setStroke(Color.ORANGE);
    }
}
