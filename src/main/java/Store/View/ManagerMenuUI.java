package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ManagerController;
import Store.Controller.ProductController;
import Store.Controller.ProductsController;
import Store.Main;
import Store.Model.*;
import Store.Model.Enums.VerifyStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

public class ManagerMenuUI {
    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;
    public VBox requestsList;
    public VBox categoriesList;
    public Button addCategoryButton;
    public ComboBox sortProductsChoiceBox;
    public VBox productsList;
    public Button addManagerButton;
    public VBox usersList;
    public ComboBox sortUsersChoiceBox;
    public VBox offCodesList;
    public ComboBox sortOffCodesChoiceBox;
    public Button addOffCodeButton;

    private static ArrayList<String> availableSortsProducts = new ArrayList<String>(Arrays.asList("rating", "price", "visit", "lexicographical"));
    private static ArrayList<String> availableSortsOffCodes = new ArrayList<String>(Arrays.asList("time of starting", "time of ending", "code", "off percentage", "maximum off", "usage count"));
    private static ArrayList<String> availableSortsUsers = new ArrayList<String>(Arrays.asList("name", "family name", "phone number", "username", "email"));
    private static String currentProductsSort = "";
    private static String currentUsersSort = "";
    private static String currentOffCodesSort = "";

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(ManagerMenuUI.class.getClassLoader().getResource("FXML/ManagerMenu.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
        showRequests();
        showCategories();
        showProducts();
        showUsers();
        showOffCodes();
    }

    private void showOffCodes() {
        offCodesList.getChildren().clear();
        for (OffCode offCode : ManagerController.sortOffCodes(currentOffCodesSort, Manager.getOffCodes())) {
            Calendar startingTime = Calendar.getInstance();
            startingTime.setTime(offCode.getStartingTime());
            Calendar endingTime = Calendar.getInstance();
            endingTime.setTime(offCode.getStartingTime());
            Label userLabel = new Label(String.format("Code: %s, OffPercent: %s, MaximumOff: %s$, StartingTime: %s/%s/%s, EndingTime: %s/%s/%s", offCode.getCode(), offCode.getOffPercentage(), offCode.getMaximumOff(), startingTime.get(Calendar.MONTH), startingTime.get(Calendar.DAY_OF_MONTH), startingTime.get(Calendar.YEAR), endingTime.get(Calendar.MONTH), endingTime.get(Calendar.DAY_OF_MONTH), endingTime.get(Calendar.YEAR)));
            userLabel.setOnMouseClicked(event -> {
                editOffCode(offCode);
            });
            userLabel.setId("request");
            userLabel.setPrefWidth(offCodesList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(20);
            offCodesList.getChildren().addAll(userLabel, region, new Separator());
        }
    }

    private void editOffCode(OffCode offCode) {

    }

    private void showUsers() {
        usersList.getChildren().clear();
        for (User user : ManagerController.sortUsers(currentUsersSort, User.getAllUsers())) {
            Label userLabel = new Label(String.format("Username: %s, User's Name: %s %s, User's PhoneNumber: %s, Users's Email: %s", user.getUsername(), user.getName(), user.getFamilyName(), user.getPhoneNumber(), user.getEmail()));
            if (user instanceof Manager) {
                userLabel.setDisable(true);
            }
            userLabel.setOnMouseClicked(event -> {
                deleteUser(user);
            });
            userLabel.setId("request");
            userLabel.setPrefWidth(usersList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(20);
            usersList.getChildren().addAll(userLabel, region, new Separator());
        }

    }

    private void deleteUser(User user) {
        HBox hBox = new HBox();
        hBox.setPrefSize(200, 50);
        hBox.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        hBox.setAlignment(Pos.CENTER);
        Button deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");
        deleteButton.setPrefSize(100, 50);
        deleteButton.setOnMouseClicked(event -> {
            ManagerController.deleteUserByName((Manager) MainMenuUIController.currentUser, user.getUsername());
            deleteButton.setDisable(true);
            showUsers();
        });
        hBox.getChildren().add(deleteButton);
        Main.setupOtherStage(new Scene(hBox), "Delete User");
    }

    private void showProducts() {
        productsList.getChildren().clear();
        for (Product allProduct : ProductsController.sort(currentProductsSort, Product.getAllProducts())) {
            Label productLabel = new Label(String.format("Product's Name: %s, Product's Price: %s, Product's Rating: %s, Product's Brand: %s", allProduct.getName(), allProduct.getPrice(), allProduct.getAverageRating(), allProduct.getBrand()));
            productLabel.setOnMouseClicked(event -> {
                deleteProduct(allProduct);
            });
            productLabel.setId("request");
            productLabel.setPrefWidth(productsList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(20);
            productsList.getChildren().addAll(productLabel, region, new Separator());
        }
    }

    private void deleteProduct(Product product) {
        HBox hBox = new HBox();
        hBox.setPrefSize(200, 50);
        hBox.getStylesheets().add("CSS/manager_menu_stylesheet.css");

        hBox.setAlignment(Pos.CENTER);
        Button deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");
        deleteButton.setPrefSize(100, 50);
        deleteButton.setOnMouseClicked(event -> {
            ManagerController.removeProducts((Manager) MainMenuUIController.currentUser, product);
            deleteButton.setDisable(true);
            showProducts();
        });
        hBox.getChildren().add(deleteButton);
        Main.setupOtherStage(new Scene(hBox), "Delete Product");
    }

    private void showCategories() {
        categoriesList.getChildren().clear();
        for (Category category : Manager.getAllCategories()) {
            Label categoryLabel = new Label(String.format("\t\tCategory ID: %s,\t\tCategory Directory: %s", category.getId(), category.getFullName()));
            categoryLabel.setOnMouseClicked(event -> {
                editCategory(category);
            });
            categoryLabel.setId("request");
            categoryLabel.setPrefWidth(categoriesList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(10);
            HBox hBox = new HBox();
            for (String filter : new HashSet<>(category.getFilters())) {
                Button button = new Button(filter);
                button.setId("filter");
                hBox.getChildren().addAll(button);
            }
            categoriesList.getChildren().addAll(categoryLabel, region, hBox, new Separator());
        }
    }

    private void editCategory(Category category) {

    }


    private void showRequests() {
        requestsList.getChildren().clear();
        for (Request pendingRequest : Manager.getPendingRequests()) {
            Label request = new Label(String.format("\t\tRequest ID: %s,\t\tRequestType: %s,\t\tStatus: %s", pendingRequest.getId(), pendingRequest.getRequestType(), pendingRequest.getStatus(), pendingRequest.getSeller()));
            request.setOnMouseClicked(event -> handleRequest(pendingRequest));
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
                ManagerController.handleRequest((Manager) MainMenuUIController.currentUser, true, request);
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
                ManagerController.handleRequest((Manager) MainMenuUIController.currentUser, false, request);
                rejectButton.setDisable(true);
                showRequests();
            });
            rejectButton.setId("rejectButton");
            hBox.getChildren().addAll(acceptButton, region, rejectButton);
        }
        Main.setupOtherStage(new Scene(hBox), "Handle Requests");
    }


    private void initialSetup() {
        sortProductsChoiceBox.setItems(FXCollections.observableArrayList(availableSortsProducts));
        sortUsersChoiceBox.setItems(FXCollections.observableArrayList(availableSortsUsers));
        sortOffCodesChoiceBox.setItems(FXCollections.observableArrayList(availableSortsOffCodes));
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);


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

        sortProductsChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentProductsSort = newValue.toString();
            showProducts();
        });

        sortUsersChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentUsersSort = newValue.toString();
            showUsers();
        });

        sortOffCodesChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentOffCodesSort = newValue.toString();
            showOffCodes();
        });

    }

    public static void showManagerMenu()
    {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
