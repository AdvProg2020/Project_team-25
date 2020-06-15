package Store.View;

import Store.Controller.CustomerController;
import Store.Controller.CustomerUIController;
import Store.Controller.MainMenuUIController;
import Store.InputManager;
import Store.Main;
import Store.Model.Customer;
import Store.Model.Log.BuyLogItem;
import Store.Model.OffCode;
import Store.Model.Product;
import com.jfoenix.animation.alert.CenterTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CustomerMenuUI implements Initializable {

    private static Customer customer;
    private static String menuState = "CustomerMenu";

    public VBox myProductVBox, cartVBox, discountVBox;

    public Label balance;
    public Button personalInfoButton;
    public Button myDiscountButton;
    public Button myProductsButton;
    public Button cartButton;
    public Button offersButton;
    public Button productsButton;
    public Button mainMenuButton;
    public Button logoutButton;
    public Button ordersButton;
    public Label loggedInStatusText;

    public ImageView profile;
    public Button submitEditButton;
    public Button changePassButton;
    public TextField usernameTextField;
    public PasswordField passwordField;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
    public TextField initialMoneyTextField;
    public Label errorText;


    public Button purchaseButton;
    public Button decreaseCartButton;
    public Button increaseCartButton;
    public Label totalPriceCart;

    int[] currentRatings;
    Label[] averageRatingLabels;
    static String phoneNumber, firstName, lastName, email;

    public PasswordField passEdit;
    public TextField offCodeTextField;
    public TextArea addressTextArea;
    public TextField oldPass;
    public PasswordField newPass;
    public PasswordField confirmationNewPass;
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customer = (Customer) MainMenuUIController.currentUser;
        if (menuState.equalsIgnoreCase("changePass"))
        {

        }
        else if (menuState.equalsIgnoreCase("extraInfoPurchase"))
        {

        }
        else if (menuState.equalsIgnoreCase("enterPass"))
        {

        }
        else
        {
            setupInitial();
        }
    }

    @FXML
    private void setupInitial()
    {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        logoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        balance.textProperty().bind(new SimpleStringProperty(String.valueOf(customer.getMoney())));
        setupInitialMyDiscount();
        setupInitialCart();
        setupInitialMyProducts();
        setupInitialPersonalMenu();
    }

    private void setupInitialMyProducts()
    {
        ArrayList<Product> showProducts = new ArrayList<>();
        for (BuyLogItem buyLogItem: customer.getBuyLog()) {
            for (Product product : buyLogItem.getProducts())
                showProducts.add(product);
        }
        int i = 0;
        for (Product product: showProducts)
            showEachProductInHBox(product, i++);
        averageRatingLabels = new Label[showProducts.size()];
        currentRatings = new int[showProducts.size()];
        for (int currentRating: currentRatings)
            currentRating = 0;
    }

    private void setupInitialMyDiscount()
    {
        for (OffCode offCode: customer.getOffCodes().keySet())
            showEachDiscountInHBox(offCode, customer.getOffCodes().get(offCode));
    }
    @FXML
    private void setupInitialCart()
    {
        for (Product product: customer.getCart())
            showEachProductInHBoxCart(product);
        totalPriceCart.textProperty().bind(new SimpleStringProperty(String.valueOf(customer.getTotalCartPrice())));
    }

    @FXML
    private void setupInitialPersonalMenu()
    {
        //profile = new ImageView(new Image(customer.getProfilePicturePath()));
        emailTextField.setText(customer.getEmail());
        firstNameTextField.setText(customer.getName());
        lastNameTextField.setText(customer.getFamilyName());
        phoneNumberTextField.setText(customer.getPhoneNumber());
        usernameTextField.setText(customer.getUsername());
        usernameTextField.setEditable(false);
    }

    public void changePassAction()
    {
        try {
            openStage("changePass");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void finalChangePassButtonClicked()
    {
        try {
            if (customer.validatePassword(oldPass.getText()) && newPass.getText().equals(confirmationNewPass.getText()))
                CustomerUIController.editPersonalInfo(customer, "password", newPass.getText());
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
        finally {
            ((Stage)oldPass.getScene().getWindow()).close();
        }
    }

    private void showEachProductInHBox(Product product, int i)
    {
        HBox hBox = new HBox();
        String starPath = "resources\\Icons\\StarSelected.png";
        Image productImage = new Image(product.getImagePath());
        ImageView productView = new ImageView(productImage);
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        Button ratingStar1 = new Button("");
        ratingStar1.getStyleClass().add("rateButton");
        ImageView activeStar = new ImageView(new Image(starPath));
        ratingStar1.getChildrenUnmodifiable().add(activeStar);
        Button ratingStar2 = new Button("");
        ratingStar2.getStyleClass().add("rateButton");
        ImageView activeStar1 = new ImageView(new Image(starPath));
        ratingStar2.getChildrenUnmodifiable().add(activeStar1);
        Button ratingStar3 = new Button("");
        ratingStar3.getStyleClass().add("rateButton");
        ImageView activeStar2 = new ImageView(new Image(starPath));
        ratingStar3.getChildrenUnmodifiable().add(activeStar2);
        Button ratingStar4 = new Button("");
        ratingStar4.getStyleClass().add("rateButton");
        ImageView activeStar3 = new ImageView(new Image(starPath));
        ratingStar4.getChildrenUnmodifiable().add(activeStar3);
        Button ratingStar5 = new Button("");
        ratingStar5.getStyleClass().add("rateButton");
        ImageView activeStar4 = new ImageView(new Image(starPath));
        ratingStar5.getChildrenUnmodifiable().add(activeStar4);
        Button rateProductButton = new Button("Rate");
        Button[] starButtons = new Button[] {ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            int finalButtonIndex = buttonIndex + 1;
            starButtons[buttonIndex].setOnAction((e) -> handleRatingChange(finalButtonIndex, activeStar, activeStar1, activeStar2, activeStar3, activeStar4, i));
        }
        rateProductButton.setOnAction((e) -> handleRating(product, currentRatings[i], ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4, i));

        handleCanRate(product, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);

        hBox.setMinHeight(165);
        VBox vBox = new VBox(), vBox1 = new VBox(), vBox2 = new VBox();
        vBox.setMinHeight(165);         vBox.setMaxHeight(165);
        vBox.setMinWidth(752);          vBox.setMaxWidth(752);
        hBox.getChildren().addAll(vBox, productView);
        vBox1.setMinHeight(89);         vBox1.setMaxHeight(89);
        vBox1.setMinWidth(752);         vBox1.setMaxWidth(752);
        vBox2.setMinHeight(89);         vBox2.setMaxHeight(89);
        vBox2.setMinWidth(752);         vBox2.setMaxWidth(752);
        vBox.getChildren().addAll(vBox1, vBox2);
        HBox hBox1 = new HBox(), hBox2 = new HBox(), hBox3 = new HBox(), hBox4 = new HBox();
        Label label = new Label(), label1 = new Label("Name"), label2 = new Label("Seller"), label3 = new Label("Average Rating"), label4 = new Label("Category"), label5 = new Label("Brand"), label6 = new Label("Price"), label7 = new Label("Filters"), label8 = new Label("Description");
        hBox1.getChildren().addAll(label1, label2, label3, label4, label5);
        hBox1.setSpacing(70);
        Separator separator = new Separator(), separator1 = new Separator(), separator2 = new Separator(), separator3 = new Separator(), separator4 = new Separator(), separator5 = new Separator(), separator6 = new Separator();
        separator.setMinWidth(134);     separator.setMaxWidth(134);     separator.setMaxHeight(7);         separator.setVisible(false);
        hBox3.getChildren().addAll(label6, label7, separator, label8);
        Label label9 = new Label(), label10 = new Label(), label11 = new Label(), label12 = new Label(), label13 = new Label(), label14 = new Label(), label15 = new Label(), label16 = new Label();
        label9.setMaxWidth(84);        label9.setMinWidth(84);        label9.setMaxHeight(40);      label9.setMinHeight(40);
        label10.setMaxWidth(96);        label10.setMinWidth(96);        label10.setMaxHeight(40);      label10.setMinHeight(40);
        label11.setMaxWidth(87);        label11.setMinWidth(87);        label11.setMaxHeight(40);      label11.setMinHeight(40);
        label12.setMaxWidth(101);        label12.setMinWidth(101);        label12.setMaxHeight(40);      label12.setMinHeight(40);
        label13.setMaxWidth(101);        label13.setMinWidth(101);        label13.setMaxHeight(40);      label13.setMinHeight(40);
        label14.setMaxWidth(85);        label14.setMinWidth(85);        label14.setMaxHeight(40);      label14.setMinHeight(40);
        label15.setMaxWidth(309);        label15.setMinWidth(309);        label15.setMaxHeight(40);      label15.setMinHeight(40);
        label16.setMaxWidth(286);        label16.setMinWidth(286);        label16.setMaxHeight(40);      label16.setMinHeight(40);
        label9.setText(product.getName());      label10.setText(product.getSeller().getUsername());     label11.setText(product.getAverageRating() + "");
        label12.setText(product.getCategory() + "");        label13.setText(product.getBrand());        label14.setText(product.getPrice() + "");
        label16.setText(product.getDescription());
        for (String filter: product.getFilters())
            label15.setText(filter + "- ");
        separator1.setMinWidth(50);     separator1.setMaxWidth(50);     separator1.setMaxHeight(7);         separator1.setVisible(false);
        separator2.setMinWidth(56);     separator2.setMaxWidth(56);     separator2.setMaxHeight(7);         separator2.setVisible(false);
        separator3.setMinWidth(121);     separator3.setMaxWidth(121);     separator3.setMaxHeight(7);         separator3.setVisible(false);
        separator4.setMinWidth(57);     separator4.setMaxWidth(57);     separator4.setMaxHeight(7);         separator4.setVisible(false);
        separator5.setMinWidth(42);     separator5.setMaxWidth(42);     separator5.setMaxHeight(7);         separator5.setVisible(false);
        separator6.setMinWidth(31);     separator6.setMaxWidth(31);     separator6.setMaxHeight(7);         separator6.setVisible(false);
        hBox2.getChildren().addAll(label9, separator1, label10, separator2, label11, separator3, label12, separator4, label13);
        hBox4.getChildren().addAll(label14, separator5, label15, separator6, label16);
        vBox1.getChildren().addAll(hBox1, hBox2);
        vBox2.getChildren().addAll(hBox3, hBox4);

        productView.setFitHeight(138);      productView.setFitWidth(182);
        HBox hBox5 = new HBox();
        hBox5.getChildren().addAll(ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5);
        VBox vBox3 = new VBox();
        vBox3.getChildren().addAll(productView, hBox5, rateProductButton);
        hBox.getChildren().addAll(vBox3);
        myProductVBox.getChildren().addAll(hBox);
    }

    private void handleCanRate(Product product, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4) {
        if (!(MainMenuUIController.currentUser instanceof Customer)) {
            setRatingDisable(true, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        }
        else if (product.hasBeenRatedBefore((Customer) MainMenuUIController.currentUser)) {
            setRatingDisable(true, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        }
        else if (!((Customer) MainMenuUIController.currentUser).hasBoughtProduct(product)) {
            setRatingDisable(true, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        }
        else {
            setRatingDisable(false, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        }
    }

    private void handleRatingChange(int index, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4, int i) {
        currentRatings[i] = index;
        ImageView[] starImages = new ImageView[] {activeStar, activeStar1, activeStar2, activeStar3, activeStar4};
        resetStars(activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        for (int buttonIndex = 0; buttonIndex < index; buttonIndex++) {
            starImages[buttonIndex].setVisible(true);
        }
    }

    private void handleRating(Product product, int currentRating, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4, int i) {
        CustomerController.rateProduct((Customer) MainMenuUIController.currentUser, product, currentRating);
        resetStars(activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        handleCanRate(product, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        averageRatingLabels[i].setText("" + product.getAverageRating());
    }


    private void resetStars(ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4) {
        ImageView[] starImages = new ImageView[] {activeStar, activeStar1, activeStar2, activeStar3, activeStar4};
        for (ImageView imageView : starImages) {
            imageView.setVisible(false);
        }
    }

    private void setRatingDisable(boolean disable, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4) {
        resetStars(activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        Button[] starButtons = new Button[] {ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            starButtons[buttonIndex].setDisable(disable);
        }
        rateProductButton.setDisable(disable);
    }

    private void showEachProductInHBoxCart(Product product)
    {
        HBox hBox = new HBox();
        Image productImage = new Image(product.getImagePath());
        ImageView productView = new ImageView(productImage);
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        Image plus = new Image("resources\\Icons\\Plus.png");
        Image mines = new Image("resources\\Icons\\Negative.png");
        ImageView plusView = new ImageView(plus);
        ImageView minesView = new ImageView(mines);
        plusView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                CustomerUIController.increaseProduct(customer, product);
                CustomerMenuUI.showCustomerMenu();
            }
            catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
        });
        minesView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                CustomerUIController.decreaseProduct(customer, product);
                CustomerMenuUI.showCustomerMenu();
            }
            catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
        });
        hBox.setMinHeight(165);
        VBox vBox = new VBox(), vBox1 = new VBox(), vBox2 = new VBox();
        vBox.setMinHeight(165);         vBox.setMaxHeight(165);
        vBox.setMinWidth(752);          vBox.setMaxWidth(752);
        hBox.getChildren().addAll(vBox, productView);
        vBox1.setMinHeight(89);         vBox1.setMaxHeight(89);
        vBox1.setMinWidth(752);         vBox1.setMaxWidth(752);
        vBox2.setMinHeight(89);         vBox2.setMaxHeight(89);
        vBox2.setMinWidth(752);         vBox2.setMaxWidth(752);
        vBox.getChildren().addAll(vBox1, vBox2);
        HBox hBox1 = new HBox(), hBox2 = new HBox(), hBox3 = new HBox(), hBox4 = new HBox();
        Label label = new Label(), label1 = new Label("Name"), label2 = new Label("Seller"), label3 = new Label("Average Rating"), label4 = new Label("Category"), label5 = new Label("Brand"), label6 = new Label("Price"), label7 = new Label("Filters"), label8 = new Label("Description");
        hBox1.getChildren().addAll(label1, label2, label3, label4, label5);
        hBox1.setSpacing(70);
        Separator separator = new Separator(), separator1 = new Separator(), separator2 = new Separator(), separator3 = new Separator(), separator4 = new Separator(), separator5 = new Separator(), separator6 = new Separator();
        separator.setMinWidth(134);     separator.setMaxWidth(134);     separator.setMaxHeight(7);         separator.setVisible(false);
        hBox3.getChildren().addAll(label6, label7, separator, label8);
        Label label9 = new Label(), label10 = new Label(), label11 = new Label(), label12 = new Label(), label13 = new Label(), label14 = new Label(), label15 = new Label(), label16 = new Label();
        label9.setMaxWidth(84);        label9.setMinWidth(84);        label9.setMaxHeight(40);      label9.setMinHeight(40);
        label10.setMaxWidth(96);        label10.setMinWidth(96);        label10.setMaxHeight(40);      label10.setMinHeight(40);
        label11.setMaxWidth(87);        label11.setMinWidth(87);        label11.setMaxHeight(40);      label11.setMinHeight(40);
        label12.setMaxWidth(101);        label12.setMinWidth(101);        label12.setMaxHeight(40);      label12.setMinHeight(40);
        label13.setMaxWidth(101);        label13.setMinWidth(101);        label13.setMaxHeight(40);      label13.setMinHeight(40);
        label14.setMaxWidth(85);        label14.setMinWidth(85);        label14.setMaxHeight(40);      label14.setMinHeight(40);
        label15.setMaxWidth(309);        label15.setMinWidth(309);        label15.setMaxHeight(40);      label15.setMinHeight(40);
        label16.setMaxWidth(286);        label16.setMinWidth(286);        label16.setMaxHeight(40);      label16.setMinHeight(40);
        label9.setText(product.getName());      label10.setText(product.getSeller().getUsername());     label11.setText(product.getAverageRating() + "");
        label12.setText(product.getCategory() + "");        label13.setText(product.getBrand());        label14.setText(product.getPrice() + "");
        label16.setText(product.getDescription());
        for (String filter: product.getFilters())
            label15.setText(filter + "- ");
        separator1.setMinWidth(50);     separator1.setMaxWidth(50);     separator1.setMaxHeight(7);         separator1.setVisible(false);
        separator2.setMinWidth(56);     separator2.setMaxWidth(56);     separator2.setMaxHeight(7);         separator2.setVisible(false);
        separator3.setMinWidth(121);     separator3.setMaxWidth(121);     separator3.setMaxHeight(7);         separator3.setVisible(false);
        separator4.setMinWidth(57);     separator4.setMaxWidth(57);     separator4.setMaxHeight(7);         separator4.setVisible(false);
        separator5.setMinWidth(42);     separator5.setMaxWidth(42);     separator5.setMaxHeight(7);         separator5.setVisible(false);
        separator6.setMinWidth(31);     separator6.setMaxWidth(31);     separator6.setMaxHeight(7);         separator6.setVisible(false);
        hBox2.getChildren().addAll(label9, separator1, label10, separator2, label11, separator3, label12, separator4, label13);
        hBox4.getChildren().addAll(label14, separator5, label15, separator6, label16);
        vBox1.getChildren().addAll(hBox1, hBox2);
        vBox2.getChildren().addAll(hBox3, hBox4);

        HBox hBox5 = new HBox();
        hBox5.getChildren().addAll(minesView, plusView);
        hBox5.setSpacing(20);
        hBox5.setAlignment(Pos.TOP_CENTER);
        VBox vBox3 = new VBox();
        vBox3.getChildren().addAll(hBox5, productView);
        hBox.getChildren().addAll(vBox3);
        cartVBox.getChildren().addAll(hBox);
    }

    private void showEachDiscountInHBox(OffCode offCode, int quantity)
    {
        HBox hBox = new HBox();
        Label offCodeText = new Label("Code");
        Label maximumOff = new Label("Maximum Off");
        Label offPercent = new Label("Off Percent");
        Label endingTime = new Label("Ending Date");
        Label numberCanBeUsed = new Label("Remaining Number");
        Label startingTime = new Label("Starting Date");
        Label offCodeText2 = new Label();
        Label maximumOff2 = new Label();
        Label offPercent2 = new Label();
        Label endingTime2 = new Label();
        Label numberCanBeUsed2 = new Label();
        Label startingTime2 = new Label();
        offCodeText2.setText(offCode.getCode());
        maximumOff2.setText(String.valueOf(offCode.getMaximumOff()));
        numberCanBeUsed2.setText(String.valueOf(quantity));
        offPercent2.setText(String.valueOf(offCode.getOffPercentage()));
        endingTime2.setText(String.valueOf(offCode.getEndingTime()));
        startingTime2.setText(String.valueOf(offCode.getStartingTime()));
        VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();
        vBox1.getChildren().addAll(offCodeText, offCodeText2);
        vBox2.getChildren().addAll(offPercent, offPercent2);
        vBox3.getChildren().addAll(maximumOff, maximumOff2);
        vBox4.getChildren().addAll(startingTime, startingTime2);
        vBox5.getChildren().addAll(endingTime,endingTime2);
        vBox6.getChildren().addAll(numberCanBeUsed, numberCanBeUsed2);
        hBox.setMinHeight(100);     hBox.setMaxHeight(100);
        hBox.setMinWidth(940);      hBox.setMaxWidth(940);
        hBox.setSpacing(15);
        HBox.setMargin(vBox1, new Insets(0, 0, 0, 10));            vBox1.setSpacing(30);
        HBox.setMargin(vBox2, new Insets(0, 0, 0, 10));            vBox2.setSpacing(30);
        HBox.setMargin(vBox3, new Insets(0, 0, 0, 10));            vBox3.setSpacing(30);
        HBox.setMargin(vBox4, new Insets(0, 0, 0, 10));            vBox4.setSpacing(30);
        HBox.setMargin(vBox5, new Insets(0, 0, 0, 10));            vBox5.setSpacing(30);
        HBox.setMargin(vBox6, new Insets(0, 0, 0, 10));            vBox6.setSpacing(30);
        vBox1.setAlignment(Pos.TOP_CENTER);         vBox4.setAlignment(Pos.TOP_CENTER);         vBox5.setAlignment(Pos.TOP_CENTER);
        vBox2.setAlignment(Pos.TOP_CENTER);         vBox3.setAlignment(Pos.TOP_CENTER);         vBox6.setAlignment(Pos.TOP_CENTER);

        vBox1.setMaxWidth(97);      vBox1.setMinWidth(97);

        hBox.getChildren().addAll(vBox1, vBox2, vBox3, vBox4, vBox5, vBox6);

        discountVBox.getChildren().addAll(hBox);
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/CustomerMenu2.fxml"));
        return root;
    }

    public void offersButtonClicked()
    {
        menuState = "CustomerMenu";
    }

    public void productsButtonClicked() throws IOException {
        menuState = "CustomerMenu";
        Main.setPrimaryStageScene(new Scene(ProductsMenuUI.getContent()));
    }

    public void mainMenuButtonClicked() throws IOException {
        menuState = "CustomerMenu";
        Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
    }

    public void cartButtonClicked() throws IOException {
        changeScene("CustomerMenuCart");
    }

    public void personalButtonClicked() throws IOException {
        changeScene("CustomerMenuPersonal");
    }

    public void myProductsButtonClicked() throws IOException {
        changeScene("CustomerMenuMyProducts");
    }

    public void purchaseButtonClicked() throws IOException {
        if (customer == MainMenuUIController.guest) {
            throwError("You must login");
        }
        if (customer.getCart().isEmpty()) {
            throwError("Your cart is empty!");
        }
        openStage("extraInfoPurchase");
    }

    public void finalBuyButton() throws IOException {
        try {
            if (addressTextArea.getText() != null) {
                String factor = CustomerController.purchase(customer, offCodeTextField.getText());
                showFactor(factor);
                menuState = "CustomerMenu";
                showCustomerMenu();
            }
            else
                throw (new Exception("Address field is empty!"));
        }
        catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
        finally {
            ((Stage)addressTextArea.getScene().getWindow()).close();
            changeScene("CustomerMenuPersonal");
        }
    }

    private void showFactor(String factor)
    {

    }

    public void submitEditButtonClicked() throws IOException {
        phoneNumber = phoneNumberTextField.getText();
        email = emailTextField.getText();
        firstName = firstNameTextField.getText();
        lastName = lastNameTextField.getText();
        openStage("enterPass");
    }

    public void editPasswordSubmit()
    {
        if (customer.validatePassword(passEdit.getText()))
        {
            try {
                CustomerUIController.editPersonalInfo(customer, "phone number", phoneNumber);
                CustomerUIController.editPersonalInfo(customer, "email", email);
                CustomerUIController.editPersonalInfo(customer, "family name", lastName);
                CustomerUIController.editPersonalInfo(customer, "first name", firstName);
            }catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
            finally {
                ((Stage)passEdit.getScene().getWindow()).close();
            }
        }
        else
            throwError("The entered password is invalid");
    }

    private void openStage(String lock) throws IOException {
        menuState = lock;
        if (lock.equalsIgnoreCase("enterPass"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditEnterPass.fxml"));
            Main.setupOtherStage(new Scene(root), "Check for apply edit");
        }
        else if (lock.equalsIgnoreCase("extraInfoPurchase"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/CustomerMenuPurchaseExtraInfo.fxml"));
            Main.setupOtherStage(new Scene(root), "Final informations to purchase");
        }
        else if (lock.equalsIgnoreCase("changePass"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/ChangePassword.fxml"));
            Main.setupOtherStage(new Scene(root), "Change password");
        }
    }

    private void throwError(String error)
    {
        errorText.setText(error);
    }

    public static void showCustomerMenu() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void changeScene(String newState) throws IOException {
        menuState = newState;
        Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/" + newState + ".fxml"));
        Main.setPrimaryStageScene(new Scene(root));
    }
}
