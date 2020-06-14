package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ManagerController;
import Store.Main;
import Store.Model.Enums.VerifyStatus;
import Store.Model.Manager;
import Store.Model.Product;
import Store.Model.Request;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ManagerMenuUI {
    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;
    public VBox requestsList;

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(ManagerMenuUI.class.getClassLoader().getResource("FXML/ManagerMenu.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
        showRequests();
    }

    private void showRequests() {
        requestsList.getChildren().clear();
        for (Request pendingRequest : Manager.getPendingRequests()) {
            Label request = new Label(String.format("\t\tRequest ID: %s,\t\tRequestType: %s,\t\tStatus: %s", pendingRequest.getId(), pendingRequest.getRequestType(), pendingRequest.getStatus(), pendingRequest.getSeller()));
            request.setOnMouseClicked(event -> {
                handleRequest(pendingRequest);
            });
            request.setAlignment(Pos.TOP_CENTER);
            request.setId("request");
            request.setPrefWidth(requestsList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(10);
            requestsList.getChildren().addAll(request, region, new Separator());
        }
    }

    private void handleRequest(Request request) {
        HBox hBox = new HBox();
        hBox.setPrefSize(200, 50);
        hBox.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        if (request.getStatus() == VerifyStatus.ACCEPTED || request.getStatus() == VerifyStatus.REJECTED) {
            hBox.setAlignment(Pos.CENTER);
            Button deleteButton = new Button("Delete");
            deleteButton.setId("deleteButton");
            deleteButton.setPrefSize(100, 50);
            deleteButton.setOnMouseClicked(event -> {
                Manager.removeRequest(request);
                deleteButton.setDisable(true);
                showRequests();
            });
            hBox.getChildren().add(deleteButton);
        }

        if (request.getStatus() == VerifyStatus.WAITING) {
            Button acceptButton = new Button("Accept");
            acceptButton.setOnMouseClicked(event -> {
                ManagerController.handleRequest((Manager)MainMenuUIController.currentUser,true, request);
                acceptButton.setDisable(true);
                showRequests();
            });
            acceptButton.setId("acceptButton");
            acceptButton.setPrefSize(100, 50);
            Region region = new Region();
            region.setPrefWidth(10);
            Button rejectButton = new Button("Reject");
            rejectButton.setPrefSize(100, 50);
            rejectButton.setOnMouseClicked(event -> {
                ManagerController.handleRequest((Manager)MainMenuUIController.currentUser,false, request);
                rejectButton.setDisable(true);
                showRequests();
            });
            rejectButton.setId("rejectButton");
            hBox.getChildren().addAll(acceptButton, region, rejectButton);
        }
        Main.setupOtherStage(new Scene(hBox), "Handle Requests");
    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);

//        Media media = new Media(ProductMenuUI.class.getResource("/Undertale_Enemy_Approaching_Yellow_Trimmed.wav").toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();

    }

    public void setupBindings() {
        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
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
}
