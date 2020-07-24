package Store.View;


import Store.Controller.MainMenuUIController;
import Store.Controller.ManagerController;
import Store.Main;

import Store.Model.Enums.VerifyStatus;

import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.Networking.Client.Controller.ClientManagerController;
import Store.Networking.Client.Controller.ClientProductController;
import Store.Networking.Client.Controller.ClientProductsController;
import Store.Networking.FileTransportClient;
import Store.Networking.HashMapGenerator;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ManagerMenuUI {
    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;
    public Button auctionPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;
    public VBox requestsList;
    public VBox categoriesList;
    public VBox viewBuysVBox;
    public Button addCategoryButton;
    public ComboBox sortProductsChoiceBox;
    public VBox productsList;
    public Button addManagerButton;
    public Button addOperatorButton;
    public VBox usersList;
    public ComboBox sortUsersChoiceBox;
    public VBox offCodesList;
    public ComboBox sortOffCodesChoiceBox;
    public Button addOffCodeButton;
    public ImageView imageProfile;
    public TextField usernameField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField passwordField;
    public TextField phoneNumberField;
    public TextField emailField;
    public Button editPersonalInfoButton;
    public Button changeImageButton;

    private static List<String> availableSortsProducts = new ArrayList<String>(Arrays.asList("rating", "price", "visit", "lexicographical"));
    private static List<String> availableSortsOffCodes = new ArrayList<String>(Arrays.asList("time of starting", "time of ending", "code", "off percentage", "maximum off", "usage count"));
    private static List<String> availableSortsUsers = new ArrayList<String>(Arrays.asList("name", "family name", "phone number", "username", "email"));
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
        reset();
        handlePersonalInfo();
        showLogs();
        showRequests();
        showCategories();
        showProducts();
        showUsers();
        showOffCodes();
    }

    private void showLogs() {
        List<Map<String, Object>> buyLogs = ClientManagerController.getBuyLogs();
        for (Map buyLog: buyLogs){
            showLog(buyLog);
        }
    }

    private void showLog(Map buyLog) {
        HBox hBox = new HBox();
        hBox.setMaxHeight(100);     hBox.setMinHeight(100);
        hBox.setSpacing(20);
        Label dateText = new Label();
        TextArea addressText = new TextArea();
        Button send = new Button("Send");
        /*TextArea productsText = new TextArea();
        Label incomeText = new Label();
        Label offValueText = new Label();
        Label sendStatusText = new Label();
        */
        dateText.setText(buyLog.get("date") + "");
        addressText.setText(buyLog.get("address") + "");
        addressText.setWrapText(true);
        //send.disableProperty().bind(ClientManagerController.isReceived(buyLog));
        /*incomeText.setText(String.valueOf(sellLog.getIncomeValue()));
        offValueText.setText(String.valueOf(sellLog.getOffValue()));
        if (sellLog.isSendStatus())
            sendStatusText.setText("Arrived");
        else
            sendStatusText.setText("Not Arrived");
        */
        /*for(Product product: sellLog.getProducts())
            productsText.setText(productsText.getText() + "\n-" + product.getName() + "-----" + product.getBrand());
        productsText.setEditable(false);
        */VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();
        vBox1.getChildren().add(dateText);
        vBox4.getChildren().add(addressText);
        vBox5.getChildren().add(send);
        Button details = new Button("View Details");
        send.setOnAction(e -> {
            ClientManagerController.sendProduct(buyLog);
            showLogs();
        });
        details.setOnAction(e -> {
            showDetails(buyLog);
        });
        vBox3.getChildren().add(details);
        /*vBox3.getChildren().add(incomeText);
        vBox4.getChildren().add(offValueText);
        vBox5.getChildren().add(sendStatusText);
        vBox6.getChildren().add(productsText);
        */vBox1.setPrefWidth(200);    vBox1.setAlignment(Pos.CENTER);
        vBox3.setPrefWidth(100);    vBox3.setAlignment(Pos.CENTER);
        vBox4.setPrefWidth(267);    vBox4.setAlignment(Pos.CENTER);
        vBox5.setPrefWidth(100);    vBox5.setAlignment(Pos.CENTER);
        /*vBox4.setPrefWidth(100);    vBox4.setAlignment(Pos.CENTER);
        vBox5.setPrefWidth(100);    vBox5.setAlignment(Pos.CENTER);
        vBox6.setPrefWidth(240);    vBox6.setAlignment(Pos.CENTER);
        productsText.setEditable(false);
        hBox.getChildren().addAll(vBox3, vBox4, vBox2, vBox5, vBox1, vBox6);
        */
        hBox.getChildren().addAll(vBox1, vBox3, vBox4, vBox5);
        viewBuysVBox.getChildren().add(hBox);
    }

    private void showDetails(Map buyLog) {
        VBox vBox = new VBox();
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        Label[] titles = new Label[6];
        for (int i = 0; i < 6; i++)
            titles[i] = new Label();
        titles[0].setText("ID");
        titles[0].setPrefWidth(43);
        titles[1].setText("Date");
        titles[1].setPrefWidth(121);
        titles[2].setText("Seller");
        titles[2].setPrefWidth(98);
        titles[3].setText("Is Sent");
        titles[3].setPrefWidth(81);
        titles[4].setText("OffValue");
        titles[4].setPrefWidth(66);
        titles[5].setText("Products");
        titles[5].setPrefWidth(123);
        hBox1.getChildren().addAll(titles[0], titles[1], titles[2], titles[3], titles[4], titles[5]);
        hBox1.setSpacing(10);
        for (int i = 0; i < 6; i++) {
            titles[i].setTextFill(Color.valueOf("ebe9e9"));
        }
        Label[] labels = new Label[6];
        for (int i = 0; i < 6; i++)
            labels[i] = new Label();
        labels[0].setText(buyLog.get("id") + "");
        labels[0].setPrefWidth(43);
        labels[1].setText(buyLog.get("date") + "");
        labels[1].setPrefWidth(121);
        labels[2].setText((String) buyLog.get("sellerName"));
        labels[2].setPrefWidth(98);
        labels[3].setText(buyLog.get("isReceived") + "");
        labels[3].setPrefWidth(81);
        labels[4].setText(buyLog.get("offValue") + "");
        labels[4].setPrefWidth(66);
        labels[5].setText(buyLog.get("products") + "");
        labels[5].setPrefWidth(123);
        labels[5].setWrapText(true);
        hBox2.getChildren().addAll(labels[0], labels[1], labels[2], labels[3], labels[4], labels[5]);
        for (int i = 0; i < 6; i++) {
            labels[i].setTextFill(Color.valueOf("ebe9e9"));
        }
        vBox.setSpacing(20);
        vBox.getChildren().addAll(hBox1, hBox2);
        vBox.setPrefWidth(670);
        vBox.setPrefHeight(250);
        Main.setupOtherStage(new Scene(vBox), "Log");
    }

    private void showOffCodes() {
        offCodesList.getChildren().clear();
        for (Map<String, Object> offCode : ClientManagerController.getSortedOffCodes(currentOffCodesSort)) {
            Label userLabel = new Label(String.format("Code: %s, OffPercent: %s, MaximumOff: %s$, UsageCounts: %s, StartingTime: %s, EndingTime: %s", offCode.get("code"), offCode.get("offPercentage"), offCode.get("maximumOff"), offCode.get("usageCount"), offCode.get("startingTime"), offCode.get("startingTime")));
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

    private void editOffCode(Map<String, Object> offCode) {
        VBox addNewOffCodeContainer = new VBox();
        TextField nameTextField = new TextField();
        TextField maximumOffField = new TextField();
        TextField offValueField = new TextField();
        TextField usageOffField = new TextField();
        DatePicker startingDatePicker = new DatePicker();
        DatePicker endingDatePicker = new DatePicker();
        Button okButton = new Button("Ok");
        Button deleteButton = new Button("Delete");

        addNewOffCodeContainer.setAlignment(Pos.TOP_CENTER);
        addNewOffCodeContainer.setPrefSize(300, 300);
        nameTextField.setPromptText((String) offCode.get("code"));
        maximumOffField.setPromptText(offCode.get("maximumOff") + "$");
        offValueField.setPromptText(offCode.get("offPercentage") + "");
        usageOffField.setPromptText(offCode.get("usageCount") + "");
        startingDatePicker.setValue(LocalDate.parse((String) offCode.get("startingTime")));
        endingDatePicker.setValue(LocalDate.parse((String) offCode.get("endingTime")));
        nameTextField.setDisable(true);
        startingDatePicker.setDisable(true);
        endingDatePicker.setDisable(true);
        usageOffField.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Main.errorPopUp(ClientManagerController.removeOffCode(ClientHandler.username, (String) offCode.get("code")), "confirm", (Stage) deleteButton.getScene().getWindow());
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
            showOffCodes();
        });

        okButton.setOnMouseClicked(event -> {
            double maximumOffValue;
            double offPercent;

            try {
                if (!maximumOffField.getText().isEmpty()) {
                    maximumOffValue = Double.parseDouble(maximumOffField.getText());
                    ClientManagerController.editOffCode(ClientHandler.username, (String) offCode.get("code"), "maximumOff", maximumOffValue + "");
                }
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            try {
                if (!offValueField.getText().isEmpty()) {
                    offPercent = Double.parseDouble(offValueField.getText());
                    ClientManagerController.editOffCode(ClientHandler.username, (String) offCode.get("code"), "offPercentage", offPercent + "");
                }
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }

            Main.errorPopUp("OffCode Edited Successfully.", "confirm", (Stage) okButton.getScene().getWindow());
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            showOffCodes();
        });

        addNewOffCodeContainer.getChildren().addAll(new Label("Enter Code: "), nameTextField, new Separator(),
                new Label("Enter Max Off: "), maximumOffField, new Separator(),
                new Label("Enter Off Value: "), offValueField, new Separator(),
                new Label("Enter Usage Counts: "), usageOffField, new Separator(),
                new Label("Select Starting Date: "), startingDatePicker, new Separator(),
                new Label("Select Ending Date: "), endingDatePicker, new Separator(),
                okButton, deleteButton
        );
        Main.setupOtherStage(new Scene(addNewOffCodeContainer), "Edit OffCode");
    }

    private void addOffCode() {
        List<String> assignedCustomers = new ArrayList<>();

        VBox addNewOffCodeContainer = new VBox();
        TextField nameTextField = new TextField();
        TextField maximumOffField = new TextField();
        TextField offValueField = new TextField();
        TextField usageOffField = new TextField();
        DatePicker startingDatePicker = new DatePicker();
        DatePicker endingDatePicker = new DatePicker();
        Button okButton = new Button("Ok");
        startingDatePicker.setEditable(false);
        endingDatePicker.setEditable(false);

        addNewOffCodeContainer.setAlignment(Pos.TOP_CENTER);
        addNewOffCodeContainer.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        addNewOffCodeContainer.setPrefSize(300, 500);

        okButton.setOnMouseClicked(event -> {
            double maximumOffValue;
            double offPercent;
            int usageCount;
            if (ClientManagerController.isOffCodeWithThisCode(nameTextField.getText())) {
                Main.errorPopUp("Code is already created!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            if (nameTextField.getText().isEmpty()) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            try {
                maximumOffValue = Double.parseDouble(maximumOffField.getText());
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            try {
                offPercent = Double.parseDouble(offValueField.getText());
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            try {
                usageCount = Integer.parseInt(usageOffField.getText());
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            if (endingDatePicker.getValue().isBefore(startingDatePicker.getValue())) {
                Main.errorPopUp("Invalid Time Selection!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            try {
                ClientManagerController.createOffCode(ClientHandler.username, nameTextField.getText(), offPercent, maximumOffValue, usageCount, Date.valueOf(startingDatePicker.getValue()), Date.valueOf(endingDatePicker.getValue()));
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            Main.errorPopUp("OffCode added.", "confirm", (Stage) okButton.getScene().getWindow());
            for (String assignedCustomer : assignedCustomers) {
                ClientManagerController.assignOffCodeToUser(nameTextField.getText(), assignedCustomer);
            }
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            showOffCodes();
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(300, 300);
        VBox allCustomers = new VBox();
        allCustomers.setPrefWidth(scrollPane.getPrefWidth());

        scrollPane.setContent(allCustomers);
        allCustomers.setAlignment(Pos.TOP_CENTER);


        for (Map<String, Object> user : ClientManagerController.getAllUsers("")) {
            if (user.get("type").equals("Customer")) {
                ToggleButton toggleButton = new ToggleButton((String) user.get("name"));
                toggleButton.setId("customerButton");
                toggleButton.setAlignment(Pos.TOP_CENTER);

                toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        assignedCustomers.add((String) user.get("name"));
                    } else {
                        assignedCustomers.remove((String)user.get("name"));
                    }
                });
                Region region = new Region();
                region.setPrefHeight(5);
                allCustomers.getChildren().addAll(toggleButton, region);
            }
        }

        addNewOffCodeContainer.getChildren().addAll(new Label("Enter Code: "), nameTextField, new Separator(),
                new Label("Enter Max Off: "), maximumOffField, new Separator(),
                new Label("Enter Off Value: "), offValueField, new Separator(),
                new Label("Enter Usage Counts: "), usageOffField, new Separator(),
                new Label("Select Starting Date: "), startingDatePicker, new Separator(),
                new Label("Select Ending Date: "), endingDatePicker, new Separator(),
                new Label("Select Customers: "), scrollPane, new Separator(),
                okButton
        );
        Main.setupOtherStage(new Scene(addNewOffCodeContainer), "Add OffCode");
    }

    private void showUsers() {
        usersList.getChildren().clear();
        for (Map<String, Object> user : ClientManagerController.getAllUsers(currentUsersSort)) {
            Label userLabel = new Label(String.format("Username: %s, User's Name: %s %s, User's PhoneNumber: %s, Users's Email: %s", user.get("username"), user.get("name"), user.get("familyName"), user.get("phoneNumber"), user.get("email")));
            if (user.get("type").equals("Manager") || user.get("type").equals("Operator")) {
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

    private void deleteUser(Map<String, Object> user) {
        HBox hBox = new HBox();
        hBox.setPrefSize(200, 50);
        hBox.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        hBox.setAlignment(Pos.CENTER);
        Button deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");
        deleteButton.setPrefSize(100, 50);
        deleteButton.setOnMouseClicked(event -> {
            ClientManagerController.deleteUserByName(ClientHandler.username, (String)user.get("username"));
            deleteButton.setDisable(true);
            showUsers();
        });
        hBox.getChildren().add(deleteButton);
        Main.setupOtherStage(new Scene(hBox), "Delete User");
    }

    private void showProducts() {
        productsList.getChildren().clear();
        for (Map<String, Object> allProduct : ClientProductsController.getToBeShownProducts(new ArrayList<>(), "null", -1, -1, "null", "null", "null", "null", "(.*)", currentProductsSort)) {
            Label productLabel = new Label(String.format("Product's Name: %s, Product's Price: %s, Product's Rating: %s, Product's Brand: %s", allProduct.get("name"), allProduct.get("price"), allProduct.get("averageRating"), allProduct.get("brand")));
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

    private void deleteProduct(Map<String, Object> product) {
        HBox hBox = new HBox();
        hBox.setPrefSize(200, 50);
        hBox.getStylesheets().add("CSS/manager_menu_stylesheet.css");

        hBox.setAlignment(Pos.CENTER);
        Button deleteButton = new Button("Delete");
        deleteButton.setId("deleteButton");
        deleteButton.setPrefSize(100, 50);
        deleteButton.setOnMouseClicked(event -> {
            ClientManagerController.removeProduct(ClientHandler.username, (String) product.get("id"));
            deleteButton.setDisable(true);
            showProducts();
        });
        hBox.getChildren().add(deleteButton);
        Main.setupOtherStage(new Scene(hBox), "Delete Product");
    }

    private void showCategories() {
        categoriesList.getChildren().clear();
        for (Map<String, Object> category : ClientProductsController.getAllCategories()) {
            Label categoryLabel = new Label(String.format("\t\tCategory ID: %s,\t\tCategory Directory: %s", category.get("id"), category.get("fullName")));
            categoryLabel.setOnMouseClicked(event -> {
                editCategory(category);
            });
            categoryLabel.setId("request");
            categoryLabel.setPrefWidth(categoriesList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(10);
            HBox hBox = new HBox();
            for (String filter : ClientProductsController.getAllFilters((String) category.get("name"))) {
                Button button = new Button(filter);
                button.setId("filter");
                hBox.getChildren().addAll(button);
            }
            categoriesList.getChildren().addAll(categoryLabel, region, hBox, new Separator());
        }
    }

    private void editCategory(Map<String, Object> category) {
        VBox addNewCategoryContainer = new VBox();
        VBox vBox = new VBox();
        TextField categoryNameTextField = new TextField();
        Button okButton = new Button("Ok");
        Button deleteButton = new Button("Delete");

        addNewCategoryContainer.setAlignment(Pos.TOP_CENTER);
        categoryNameTextField.setPromptText("Name");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        scrollPane.setPrefSize(400, 300);

        vBox.setPrefSize(400, 300);
        vBox.setAlignment(Pos.TOP_CENTER);

        deleteButton.setOnMouseClicked(event -> {
            Main.errorPopUp(ClientManagerController.removeCategory(ClientHandler.username, (String)category.get("name")), "confirm", (Stage) deleteButton.getScene().getWindow());
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
            showCategories();
        });

        okButton.setOnMouseClicked(event -> {
            if (ClientManagerController.isCategoryWithThisName(categoryNameTextField.getText())) {
                Main.errorPopUp("Process failed: the category exists.", "error", (Stage) okButton.getScene().getWindow());
            } else if (!categoryNameTextField.getText().isEmpty()) {
                ClientManagerController.editCategory(ClientHandler.username, (String)category.get("name"), "change name", categoryNameTextField.getText());
                Main.errorPopUp("Category's name changed.", "confirm", (Stage) okButton.getScene().getWindow());
            }
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            showCategories();
        });

        for (String filter : ClientProductsController.getAllFilters("null")) {
            ToggleButton filterButton = new ToggleButton(filter);
            filterButton.setId("categoryButton");
            if (ClientManagerController.isInFilter((String)category.get("name"), filter)) {
                filterButton.setSelected(true);
            }
            filterButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    ClientManagerController.editCategory(ClientHandler.username, (String) category.get("name"), "add filter", filter);
                } else {
                    ClientManagerController.editCategory(ClientHandler.username, (String) category.get("name"), "remove filter", filter);
                }
                showCategories();
            });
            Region region = new Region();
            region.setPrefHeight(10);
            vBox.getChildren().addAll(filterButton, new Separator(), region);
        }


        scrollPane.setContent(vBox);
        addNewCategoryContainer.getChildren().addAll(new Label("Enter Name: "), categoryNameTextField, new Label("Select Special Filters For This Category(Automatic Addition):"), scrollPane, okButton, deleteButton);
        Main.setupOtherStage(new Scene(addNewCategoryContainer), "Edit Category");
    }

    private void addCategory() {
        VBox addNewCategoryContainer = new VBox();
        VBox vBox = new VBox();
        TextField categoryNameTextField = new TextField();
        ToggleGroup toggleGroup = new ToggleGroup();
        Button button = new Button("Ok");
        final String[] parentCategory = new String[1];
        parentCategory[0] = "";

        addNewCategoryContainer.setAlignment(Pos.TOP_CENTER);
        categoryNameTextField.setPromptText("Name");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        scrollPane.setPrefSize(400, 300);

        vBox.setPrefSize(400, 300);
        vBox.setAlignment(Pos.TOP_CENTER);

        ToggleButton base = new ToggleButton("No Category");
        base.setToggleGroup(toggleGroup);
        base.setId("categoryButton");
        button.setOnMouseClicked(event -> {
            if (ClientManagerController.isCategoryWithThisName(categoryNameTextField.getText())) {
                Main.errorPopUp("Process failed: the category exists.", "error", (Stage) button.getScene().getWindow());
            } else if (!parentCategory[0].isEmpty() && !categoryNameTextField.getText().isEmpty()) {
                ClientManagerController.addCategory(ClientHandler.username, categoryNameTextField.getText(), parentCategory[0]);
                Main.errorPopUp("Category added.", "confirm", (Stage) button.getScene().getWindow());
            } else {
                Main.errorPopUp("Process failed: fields should be filled.", "error", (Stage) button.getScene().getWindow());
            }
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();
            showCategories();
        });
        base.setOnMouseClicked(event -> {
            parentCategory[0] = "null";
        });
        vBox.getChildren().addAll(base, new Separator());

        for (Map<String, Object> category : ClientProductsController.getAllCategories()) {
            ToggleButton categoryName = new ToggleButton((String) category.get("name"));
            categoryName.setToggleGroup(toggleGroup);
            categoryName.setId("categoryButton");
            categoryName.setOnMouseClicked(event -> {
                parentCategory[0] = (String) category.get("name");
            });
            Region region = new Region();
            region.setPrefHeight(10);
            vBox.getChildren().addAll(categoryName, new Separator(), region);
        }
        scrollPane.setContent(vBox);
        addNewCategoryContainer.getChildren().addAll(new Label("Enter Name: "), categoryNameTextField, new Label("Select Parent Category"), scrollPane, button);
        Main.setupOtherStage(new Scene(addNewCategoryContainer), "Create Category");
    }


    private void showRequests() {
        requestsList.getChildren().clear();
        for (Map<String, Object> pendingRequest : ClientManagerController.getPendingRequests()) {
            Label request = new Label(String.format("\t\tRequest ID: %s,\t\tRequestType: %s,\t\tStatus: %s", pendingRequest.get("id"), pendingRequest.get("requestType"), pendingRequest.get("status"), pendingRequest.get("sellerName")));
            request.setOnMouseClicked(event -> handleRequest(pendingRequest));
            request.setId("request");
            request.setPrefWidth(requestsList.getPrefWidth());
            Region region = new Region();
            region.setPrefHeight(10);
            requestsList.getChildren().addAll(request, region, new Separator());
        }
    }

    private void handleRequest(Map<String, Object> request) {
        HBox hBox = new HBox();
        hBox.setPrefSize(200, 50);
        hBox.getStylesheets().add("CSS/manager_menu_stylesheet.css");
        if ((request.get("status")).equals("ACCEPTED") || request.get("status").equals("REJECTED")) {
            hBox.setAlignment(Pos.CENTER);
            Button deleteButton = new Button("Delete");
            deleteButton.setId("deleteButton");
            deleteButton.setPrefSize(100, 50);
            deleteButton.setOnMouseClicked(event -> {
                ClientManagerController.removeRequest((String)request.get("id"));
                deleteButton.setDisable(true);
                showRequests();
            });
            hBox.getChildren().add(deleteButton);
        }

        if (request.get("status").equals("WAITING")) {
            Button acceptButton = new Button("Accept");
            Button rejectButton = new Button("Reject");
            acceptButton.setOnMouseClicked(event -> {
                ClientManagerController.handleRequest(ClientHandler.username, true, (String)request.get("id"));
                acceptButton.setDisable(true);
                rejectButton.setDisable(true);
                showRequests();
                showProducts();
                showCategories();
                showUsers();
                showOffCodes();
                showLogs();
            });
            acceptButton.setId("acceptButton");
            acceptButton.setPrefSize(100, 50);
            Region region = new Region();
            region.setPrefWidth(10);
            rejectButton.setPrefSize(100, 50);
            rejectButton.setOnMouseClicked(event -> {
                ClientManagerController.handleRequest(ClientHandler.username, false, (String)request.get("id"));
                rejectButton.setDisable(true);
                acceptButton.setDisable(true);
                showRequests();
            });
            rejectButton.setId("rejectButton");
            hBox.getChildren().addAll(acceptButton, region, rejectButton);
        }
        Main.setupOtherStage(new Scene(hBox), "Handle Requests");
    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(ClientMainMenuController.currentUserUsername);
        signUpButton.disableProperty().bind(ClientMainMenuController.isLoggedIn);
        loginLogoutButton.textProperty().bind(ClientMainMenuController.loginLogoutButtonText);
        imageProfile.setPreserveRatio(false);
        imageProfile.setFitWidth(200);
        imageProfile.setFitHeight(200);

    }

    public void setupBindings() {
        offersButton.setOnAction((e) -> OffersMenuUI.showOffersMenu());
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());
        auctionPageButton.setOnAction(e -> AuctionsMenuUI.showAuctionsMenu());
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
        addCategoryButton.setOnMouseClicked(event -> addCategory());
        addManagerButton.setOnMouseClicked(event -> {
            SignUpManagerMenuUI.showSignUpMenu();
            showUsers();
        });
        addOperatorButton.setOnAction((e) -> {
            System.out.println("ADDING AN OPERATOR");
            SignUpOperatorMenuUI.showSignUpMenu();
            showUsers();
        });
        addOffCodeButton.setOnMouseClicked(event -> addOffCode());
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

    public static void showManagerMenu() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void reset() {
        currentProductsSort = "";
        currentUsersSort = "";
        currentOffCodesSort = "";
        sortProductsChoiceBox.setItems(FXCollections.observableArrayList(availableSortsProducts));
        sortUsersChoiceBox.setItems(FXCollections.observableArrayList(availableSortsUsers));
        sortOffCodesChoiceBox.setItems(FXCollections.observableArrayList(availableSortsOffCodes));
    }

    private void handlePersonalInfo() {
        Map<String, Object> manager = ClientManagerController.getUserInfo(ClientHandler.username);
        usernameField.setText((String) manager.get("username"));
        usernameField.setDisable(true);
        firstNameField.setText((String) manager.get("name"));
        lastNameField.setText((String) manager.get("familyName"));
        passwordField.setText((String) manager.get("password"));
        phoneNumberField.setText((String) manager.get("phoneNumber"));
        emailField.setText((String) manager.get("email"));
        usernameField.setPromptText((String) manager.get("username"));
        firstNameField.setPromptText((String) manager.get("name"));
        lastNameField.setPromptText((String) manager.get("familyName"));
        passwordField.setPromptText((String) manager.get("password"));
        phoneNumberField.setPromptText((String) manager.get("phoneNumber"));
        emailField.setPromptText((String) manager.get("email"));

        firstNameField.setId("infoField");
        lastNameField.setId("infoField");
        emailField.setId("infoField");
        passwordField.setId("infoField");
        phoneNumberField.setId("infoField");
        usernameField.setId("infoField");

        editPersonalInfoButton.setOnMouseClicked(event -> {
            if (firstNameField.getText().equals(manager.get("name")) && lastNameField.getText().equals(manager.get("familyName")) && passwordField.getText().equals(manager.get("password")) && phoneNumberField.getText().equals(manager.get("phoneNumber")) && emailField.getText().equals(manager.get("email"))) {
                Main.errorPopUp("You Should Change Some Fields.", "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            ClientManagerController.editPersonalInfo((String) manager.get("username"), "first name", firstNameField.getText());
            ClientManagerController.editPersonalInfo((String) manager.get("username"), "family name", lastNameField.getText());

            String changePasswordMessage = ClientManagerController.editPersonalInfo((String) manager.get("username"), "password", passwordField.getText());
            String changePhoneMessage = ClientManagerController.editPersonalInfo((String) manager.get("username"), "phone number", phoneNumberField.getText());
            String changeEmailMessage = ClientManagerController.editPersonalInfo((String) manager.get("username"), "email", emailField.getText());

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
//
        FileTransportClient.receiveFile(ClientHandler.username, ClientHandler.token, "I", ClientHandler.username + ".jpg");
        File file = new File("src/main/resources/Images/" + ClientHandler.username + ".jpg");
        imageProfile.setImage(new Image(file.toURI().toString()));
//
//        final Circle clip = new Circle(100, 100, 100);
//        imageProfile.setClip(clip);
//        clip.setStroke(Color.ORANGE);
    }
}