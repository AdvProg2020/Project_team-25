package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ManagerController;
import Store.Controller.ProductsController;
import Store.Main;
import Store.Model.*;
import Store.Model.Enums.VerifyStatus;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class OperatorMenuUI {
    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
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
        Operator operator = (Operator) MainMenuUIController.currentUser;
        usernameField.setText(operator.getUsername());
        usernameField.setDisable(true);
        firstNameField.setText(operator.getName());
        lastNameField.setText(operator.getFamilyName());
        passwordField.setText(operator.getPassword());
        phoneNumberField.setText(operator.getPhoneNumber());
        emailField.setText(operator.getEmail());
        usernameField.setPromptText(operator.getUsername());
        firstNameField.setPromptText(operator.getName());
        lastNameField.setPromptText(operator.getFamilyName());
        passwordField.setPromptText(operator.getPassword());
        phoneNumberField.setPromptText(operator.getPhoneNumber());
        emailField.setPromptText(operator.getEmail());

        firstNameField.setId("infoField");
        lastNameField.setId("infoField");
        emailField.setId("infoField");
        passwordField.setId("infoField");
        phoneNumberField.setId("infoField");
        usernameField.setId("infoField");

        editPersonalInfoButton.setOnMouseClicked(event -> {
            if (firstNameField.getText().equals(operator.getName()) && lastNameField.getText().equals(operator.getFamilyName()) && passwordField.getText().equals(operator.getPassword()) && phoneNumberField.getText().equals(operator.getPhoneNumber()) && emailField.getText().equals(operator.getEmail())) {
                Main.errorPopUp("You Should Change Some Fields.", "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            ManagerController.editPersonalInfo(operator, "first name", firstNameField.getText());
            ManagerController.editPersonalInfo(operator, "family name", lastNameField.getText());
            String changePasswordMessage = ManagerController.editPersonalInfo(operator, "password", passwordField.getText());
            String changePhoneMessage = ManagerController.editPersonalInfo(operator, "phone number", phoneNumberField.getText());
            String changeEmailMessage = ManagerController.editPersonalInfo(operator, "email", emailField.getText());

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
                    operator.setProfilePicturePath(fileChooser.showOpenDialog(new Stage()).getPath());
                }
                catch (Exception exception) {
                    // do nothing
                }
                handlePersonalInfo();
            }
        });

        String path;
        if (operator.getProfilePicturePath().isEmpty()) {
            path = "src/main/resources/Images/images.jpg";
        }
        else {
            path = operator.getProfilePicturePath();
        }

        File file = new File(path);
        imageProfile.setImage(new Image(file.toURI().toString()));

        final Circle clip = new Circle(100, 100, 100);
        imageProfile.setClip(clip);
        clip.setStroke(Color.ORANGE);
    }
}
