package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ManagerController;
import Store.Controller.ProductController;
import Store.Controller.ProductsController;
import Store.Main;
import Store.Model.*;
import Store.Model.Enums.VerifyStatus;
import javafx.beans.binding.BooleanBinding;
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
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

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
    public ImageView imageProfile;
    public TextField usernameField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField passwordField;
    public TextField phoneNumberField;
    public TextField emailField;
    public Button editPersonalInfoButton;
    public Button changeImageButton;

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
        reset();
        handlePersonalInfo();
        showRequests();
        showCategories();
        showProducts();
        showUsers();
        showOffCodes();
    }

    private void showOffCodes() {
        offCodesList.getChildren().clear();
        for (OffCode offCode : ManagerController.sortOffCodes(currentOffCodesSort, Manager.getOffCodes())) {
            Label userLabel = new Label(String.format("Code: %s, OffPercent: %s, MaximumOff: %s$, UsageCounts: %s, StartingTime: %s, EndingTime: %s", offCode.getCode(), offCode.getOffPercentage(), offCode.getMaximumOff(), offCode.getUsageCount(), new Date(offCode.getStartingTime().getTime()).toLocalDate().toString(), new Date(offCode.getEndingTime().getTime()).toLocalDate().toString()));
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
        nameTextField.setPromptText(offCode.getCode());
        maximumOffField.setPromptText(offCode.getMaximumOff() + "$");
        offValueField.setPromptText(offCode.getOffPercentage() + "");
        usageOffField.setPromptText(offCode.getUsageCount() + "");
        startingDatePicker.setValue(new Date(offCode.getStartingTime().getTime()).toLocalDate());
        endingDatePicker.setValue(new Date(offCode.getEndingTime().getTime()).toLocalDate());
        nameTextField.setDisable(true);
        startingDatePicker.setDisable(true);
        endingDatePicker.setDisable(true);
        usageOffField.setDisable(true);

        deleteButton.setOnMouseClicked(event -> {
            Main.errorPopUp(ManagerController.removeOffCode((Manager) MainMenuUIController.currentUser, offCode.getCode()), "confirm", (Stage) deleteButton.getScene().getWindow());
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
                    ManagerController.editOffCode((Manager) MainMenuUIController.currentUser, offCode, "maximumOff", maximumOffValue + "");
                }
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            try {
                if (!offValueField.getText().isEmpty()) {
                    offPercent = Double.parseDouble(offValueField.getText());
                    ManagerController.editOffCode((Manager) MainMenuUIController.currentUser, offCode, "offPercentage", offPercent + "");
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
        addNewOffCodeContainer.setPrefSize(300, 300);

        okButton.setOnMouseClicked(event -> {
            double maximumOffValue;
            double offPercent;
            int usageCount;
            if (Manager.getOffCodeByCode(nameTextField.getText()) != null) {
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
                ManagerController.createOffCode((Manager) MainMenuUIController.currentUser, nameTextField.getText(), offPercent, maximumOffValue, usageCount, Date.valueOf(startingDatePicker.getValue()), Date.valueOf(endingDatePicker.getValue()));
            } catch (Exception exception) {
                Main.errorPopUp("Invalid Format!", "error", (Stage) okButton.getScene().getWindow());
                return;
            }
            Main.errorPopUp("OffCode added.", "confirm", (Stage) okButton.getScene().getWindow());
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
                okButton
        );
        Main.setupOtherStage(new Scene(addNewOffCodeContainer), "Add OffCode");
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
            for (String filter : Product.getAllFilters(category.getName())) {
                Button button = new Button(filter);
                button.setId("filter");
                hBox.getChildren().addAll(button);
            }
            categoriesList.getChildren().addAll(categoryLabel, region, hBox, new Separator());
        }
    }

    private void editCategory(Category category) {
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
            Main.errorPopUp(ManagerController.removeCategory((Manager) MainMenuUIController.currentUser, category), "confirm", (Stage) deleteButton.getScene().getWindow());
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
            showCategories();
        });

        okButton.setOnMouseClicked(event -> {
            if (Manager.categoryByName(categoryNameTextField.getText()) != null) {
                Main.errorPopUp("Process failed: the category exists.", "error", (Stage) okButton.getScene().getWindow());
            } else if (!categoryNameTextField.getText().isEmpty()) {
                ManagerController.editCategory((Manager) MainMenuUIController.currentUser, category, "change name", categoryNameTextField.getText());
                Main.errorPopUp("Category's name changed.", "confirm", (Stage) okButton.getScene().getWindow());
            }
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            showCategories();
        });

        for (String filter : Product.getAllFilters("null")) {
            ToggleButton filterButton = new ToggleButton(filter);
            filterButton.setId("categoryButton");
            if (category.isInFilter(filter)) {
                filterButton.setSelected(true);
            }
            filterButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    ManagerController.editCategory((Manager) MainMenuUIController.currentUser, category, "add filter", filter);
                } else {
                    ManagerController.editCategory((Manager) MainMenuUIController.currentUser, category, "remove filter", filter);
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
            if (Manager.categoryByName(categoryNameTextField.getText()) != null) {
                Main.errorPopUp("Process failed: the category exists.", "error", (Stage) button.getScene().getWindow());
            } else if (!parentCategory[0].isEmpty() && !categoryNameTextField.getText().isEmpty()) {
                ManagerController.addCategory((Manager) MainMenuUIController.currentUser, categoryNameTextField.getText(), parentCategory[0]);
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

        for (Category category : Manager.getAllCategories()) {
            ToggleButton categoryName = new ToggleButton(category.getName());
            categoryName.setToggleGroup(toggleGroup);
            categoryName.setId("categoryButton");
            categoryName.setOnMouseClicked(event -> {
                parentCategory[0] = category.getName();
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
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        imageProfile.setPreserveRatio(false);
        imageProfile.setFitWidth(200);
        imageProfile.setFitHeight(200);

    }

    public void setupBindings() {
        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        addCategoryButton.setOnMouseClicked(event -> addCategory());
        addManagerButton.setOnMouseClicked(event -> {
            SignUpManagerMenuUI.showSignUpMenu();
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

    private void reset() {
        currentProductsSort = "";
        currentUsersSort = "";
        currentOffCodesSort = "";
        sortProductsChoiceBox.setItems(FXCollections.observableArrayList(availableSortsProducts));
        sortUsersChoiceBox.setItems(FXCollections.observableArrayList(availableSortsUsers));
        sortOffCodesChoiceBox.setItems(FXCollections.observableArrayList(availableSortsOffCodes));
    }

    private void handlePersonalInfo() {
        Manager manager = (Manager) MainMenuUIController.currentUser;
        usernameField.setText(manager.getUsername());
        firstNameField.setText(manager.getName());
        lastNameField.setText(manager.getFamilyName());
        passwordField.setText(manager.getPassword());
        phoneNumberField.setText(manager.getPhoneNumber());
        emailField.setText(manager.getEmail());
        usernameField.setPromptText(manager.getUsername());
        firstNameField.setPromptText(manager.getName());
        lastNameField.setPromptText(manager.getFamilyName());
        passwordField.setPromptText(manager.getPassword());
        phoneNumberField.setPromptText(manager.getPhoneNumber());
        emailField.setPromptText(manager.getEmail());

        firstNameField.setId("infoField");
        lastNameField.setId("infoField");
        emailField.setId("infoField");
        passwordField.setId("infoField");
        phoneNumberField.setId("infoField");
        usernameField.setId("infoField");

        editPersonalInfoButton.setOnMouseClicked(event -> {
            if (firstNameField.getText().equals(manager.getName()) && lastNameField.getText().equals(manager.getFamilyName()) && passwordField.getText().equals(manager.getPassword()) && phoneNumberField.getText().equals(manager.getPhoneNumber()) && emailField.getText().equals(manager.getEmail())) {
                Main.errorPopUp("You Should Change Some Fields.", "error", (Stage) usernameField.getScene().getWindow());
                return;
            }
            ManagerController.editPersonalInfo(manager, "first name", firstNameField.getText());
            ManagerController.editPersonalInfo(manager, "family name", lastNameField.getText());
            String changePasswordMessage = ManagerController.editPersonalInfo(manager, "password", passwordField.getText());
            String changePhoneMessage = ManagerController.editPersonalInfo(manager, "phone number", phoneNumberField.getText());
            String changeEmailMessage = ManagerController.editPersonalInfo(manager, "email", emailField.getText());

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
                    manager.setProfilePicturePath(fileChooser.showOpenDialog(new Stage()).getPath());
                }
                catch (Exception exception) {
                    // do nothing
                }
                handlePersonalInfo();
            }
        });

        String path;
        if (manager.getProfilePicturePath().isEmpty()) {
            path = "src/main/resources/Images/images.jpg";
        }
        else {
            path = manager.getProfilePicturePath();
        }

        File file = new File(path);
        imageProfile.setImage(new Image(file.toURI().toString()));

        final Circle clip = new Circle(100, 100, 100);
        imageProfile.setClip(clip);
        clip.setStroke(Color.ORANGE);
    }
}
