package Store.View;

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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerMenuUI implements Initializable {

    private static Customer customer;
    private static String menuState = "CustomerMenu";

    public GridPane gridPane;

    public AnchorPane anchorPane;
    public VBox myProductsVBox, cartVBox, discountVBox;

    public TabPane tabPane;
    public Tab cart;
    public Tab personal;
    public Tab product;
    public Tab discount;

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

    public Label factor = new Label("");

    public Button purchaseButton;
    public Button decreaseCartButton;
    public Button increaseCartButton;
    public Label totalPriceCart;

    int[] currentRatings;
    Label[] averageRatingLabels;
    static String phoneNumber, firstName, lastName, email, factorString;

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
        else if (menuState.equalsIgnoreCase("showFactor"))
        {
            setupInitialFactor();
            menuState = "CustomerMenu";
        }
        else
        {
            if (menuState.equalsIgnoreCase("product") || menuState.equalsIgnoreCase("cart") || menuState.equalsIgnoreCase("personal"))
                resetAll();
            setupInitial();
            if (menuState.equalsIgnoreCase("product"))
                tabPane.getSelectionModel().select(product);
            else if (menuState.equalsIgnoreCase("cart"))
                tabPane.getSelectionModel().select(cart);
            else if (menuState.equalsIgnoreCase("personal"))
                tabPane.getSelectionModel().select(personal);
        }
    }

    private void setupInitialFactor() {
        int i = 1;
        Label id, date, seller, offValue;
        TextArea products;
        Scanner scanner = new Scanner(factorString);
        String eachFactor;
        Pattern pattern = Pattern.compile("offValue=(.+), sellerName=(.+), received=(.+), id=(.+), date=(.+), productList=(.+)}");
        Matcher matcher;
        while (scanner.hasNextLine())
        {
            id = new Label(); date = new Label(); seller = new Label(); offValue = new Label();
            products = new TextArea();
            eachFactor = scanner.nextLine();
            matcher = pattern.matcher(eachFactor);
            matcher.find();
            id.setText(matcher.group(4));       date.setText(matcher.group(5));         seller.setText(matcher.group(2));        offValue.setText(matcher.group(1));
            products.setText(matcher.group(6));         products.setEditable(false);        products.setPrefWidth(0);       products.setPrefHeight(0);
            GridPane.setValignment(id, VPos.CENTER);          GridPane.setHalignment(id, HPos.CENTER);
            GridPane.setValignment(seller, VPos.CENTER);          GridPane.setHalignment(seller, HPos.CENTER);
            GridPane.setValignment(date, VPos.CENTER);          GridPane.setHalignment(date, HPos.CENTER);
            GridPane.setValignment(offValue, VPos.CENTER);          GridPane.setHalignment(offValue, HPos.CENTER);
            GridPane.setValignment(products, VPos.CENTER);          GridPane.setHalignment(products, HPos.CENTER);
            gridPane.addRow(i++, id, date, seller, offValue, products);
        }
    }

    private void resetAll()
    {
        for (int i = 1; i < myProductsVBox.getChildren().size();)
            myProductsVBox.getChildren().remove(i);
        for (int i = 1; i < cartVBox.getChildren().size();)
            cartVBox.getChildren().remove(i);
        for (int i = 0; i < discountVBox.getChildren().size();)
            discountVBox.getChildren().remove(i);
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
        showProducts = makeUnique(showProducts);
        for (Product product: showProducts)
            showEachProductInHBox(product, i++);
        averageRatingLabels = new Label[showProducts.size()];
        currentRatings = new int[showProducts.size()];
        for (int currentRating: currentRatings)
            currentRating = 0;
    }

    private ArrayList<Product> makeUnique(ArrayList<Product> showProducts) {
        ArrayList<Product> returnedArray = new ArrayList<>();
        for (Product product : showProducts){
            if (!returnedArray.contains(product))
                returnedArray.add(product);
        }
        return returnedArray;
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
        totalPriceCart.setText(customer.getTotalCartPrice() + "");
        totalPriceCart.textProperty().bind(new SimpleStringProperty(String.valueOf(customer.getTotalCartPrice())));
    }

    @FXML
    private void setupInitialPersonalMenu()
    {
        try{
            profile.setImage(new Image(customer.getProfilePicturePath()));
        }catch (Exception exception) {

        }
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
            if (customer.validatePassword(oldPass.getText()) && newPass.getText().equals(confirmationNewPass.getText())) {
                CustomerUIController.editPersonalInfo(customer, "password", newPass.getText());
                ((Stage) oldPass.getScene().getWindow()).close();
                menuState = "personal";
                CustomerMenuUI.showCustomerMenu();
            }
            } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    private void showEachProductInHBox(Product product, int i)
    {
        HBox hBox = new HBox();
        hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {

            }
        });
        String starPath = ProductMenuUI.class.getResource("/Icons/StarNotSelected.png").toExternalForm();
        /*Image productImage = new Image(product.getImagePath());
        ImageView productView = new ImageView(productImage);
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });*/
        ImageView activeStar = new ImageView(new Image(starPath));
        activeStar.setFitWidth(10);     activeStar.setFitHeight(10);
        Button ratingStar1 = new Button("", activeStar);
        ratingStar1.getStyleClass().add("rateButton");
        ImageView activeStar1 = new ImageView(new Image(starPath));
        activeStar1.setFitWidth(5);     activeStar1.setFitHeight(5);
        Button ratingStar2 = new Button("", activeStar1);
        ratingStar2.getStyleClass().add("rateButton");
        ImageView activeStar2 = new ImageView(new Image(starPath));
        activeStar2.setFitWidth(5);     activeStar2.setFitHeight(5);
        Button ratingStar3 = new Button("", activeStar2);
        ratingStar3.getStyleClass().add("rateButton");
        ImageView activeStar3 = new ImageView(new Image(starPath));
        activeStar3.setFitWidth(5);     activeStar3.setFitHeight(5);
        Button ratingStar4 = new Button("", activeStar3);
        ratingStar4.getStyleClass().add("rateButton");
        ImageView activeStar4 = new ImageView(new Image(starPath));
        activeStar4.setFitWidth(5);     activeStar4.setFitHeight(5);
        Button ratingStar5 = new Button("", activeStar4);
        ratingStar5.getStyleClass().add("rateButton");
        Button rateProductButton = new Button("Rate");
        Button[] starButtons = new Button[] {ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            int finalButtonIndex = buttonIndex + 1;
            starButtons[buttonIndex].setOnAction((e) -> handleRatingChange(finalButtonIndex, activeStar, activeStar1, activeStar2, activeStar3, activeStar4, i));
            starButtons[buttonIndex].setMaxSize(5, 5);
        }
        rateProductButton.setOnAction((e) -> handleRating(product, currentRatings[i], ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4, i));

        handleCanRate(product, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);

        hBox.setMaxHeight(70);      hBox.setMinHeight(70);
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 11; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        vBoxes.get(0).setPrefWidth(60);
        vBoxes.get(10).setPrefWidth(140);
        Label id = new Label(), name = new Label(), sellerName = new Label(), category = new Label(), brand = new Label(), average = new Label(), price = new Label(), productStatus = new Label();
        id.setText(product.getProductID() + "");
        name.setText(product.getName());
        sellerName.setText(product.getSeller().getUsername());
        category.setText(product.getCategory() + "");
        brand.setText(product.getBrand());
        average.setText(product.getAverageRating() + "");
        productStatus.setText(product.getProductStatus() + "");
        price.setText(product.getPrice() + "");
        TextArea filters = new TextArea(), discription = new TextArea();
        filters.setEditable(false);         discription.setEditable(false);
        discription.setText(product.getDescription());
        for (String filter: product.getFilters())
            filters.setText(filters.getText() + "-" + filter + "\n");
        vBoxes.get(0).getChildren().add(id);
        vBoxes.get(1).getChildren().add(name);
        vBoxes.get(2).getChildren().add(sellerName);
        vBoxes.get(3).getChildren().add(category);
        vBoxes.get(4).getChildren().add(brand);
        vBoxes.get(5).getChildren().add(average);
        vBoxes.get(6).getChildren().add(price);
        vBoxes.get(7).getChildren().add(discription);
        vBoxes.get(8).getChildren().add(productStatus);
        vBoxes.get(9).getChildren().add(filters);
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        vBoxes.get(10).getChildren().addAll(hBox1, hBox2);
        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5);
        hBox2.getChildren().add(rateProductButton);
        filters.setMaxWidth(100);
        discription.setMaxWidth(100);
        for (int j = 0; j < 11; j++)
            hBox.getChildren().add(vBoxes.get(j));
        myProductsVBox.getChildren().addAll(hBox);
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
        try {
            CustomerUIController.rateProduct((Customer) MainMenuUIController.currentUser, product, currentRating);
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
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
        /*Image productImage = new Image(product.getImagePath());
        ImageView productView = new ImageView(productImage);
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });*/
        hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {

            }
        });
        hBox.setMaxHeight(70);      hBox.setMinHeight(70);
        Image plus = new Image(ProductMenuUI.class.getResource("/Icons/Plus.png").toExternalForm());
        Image mines = new Image(ProductMenuUI.class.getResource("/Icons/Negative.png").toExternalForm());
        ImageView plusView = new ImageView(plus);
        ImageView minesView = new ImageView(mines);
        plusView.setFitWidth(20);       plusView.setFitHeight(20);
        minesView.setFitHeight(20);     minesView.setFitWidth(20);
        plusView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                CustomerUIController.increaseProduct(customer, product);
                menuState = "cart";
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
                menuState = "cart";
                CustomerMenuUI.showCustomerMenu();
            }
            catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
        });
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        vBoxes.get(0).setPrefWidth(74);
        Label id = new Label(), name = new Label(), sellerName = new Label(), category = new Label(), brand = new Label(), average = new Label(), price = new Label(), productStatus = new Label();
        id.setText(product.getProductID() + "");
        name.setText(product.getName());
        sellerName.setText(product.getSeller().getUsername());
        category.setText(product.getCategory() + "");
        brand.setText(product.getBrand());
        average.setText(product.getAverageRating() + "");
        productStatus.setText(product.getProductStatus() + "");
        price.setText(product.getPrice() + "");
        TextArea filters = new TextArea(), discription = new TextArea();
        filters.setEditable(false);         discription.setEditable(false);
        discription.setText(product.getDescription());
        for (String filter: product.getFilters())
            filters.setText(filters.getText() + "-" + filter + "\n");
        filters.setMaxWidth(100);   discription.setMaxWidth(100);
        vBoxes.get(0).getChildren().addAll(plusView, id, minesView);
        vBoxes.get(1).getChildren().add(name);
        vBoxes.get(2).getChildren().add(sellerName);
        vBoxes.get(3).getChildren().add(category);
        vBoxes.get(4).getChildren().add(brand);
        vBoxes.get(5).getChildren().add(average);
        vBoxes.get(6).getChildren().add(price);
        vBoxes.get(7).getChildren().add(discription);
        vBoxes.get(8).getChildren().add(productStatus);
        vBoxes.get(9).getChildren().add(filters);
        for (int j = 0; j < 10; j++)
            hBox.getChildren().add(vBoxes.get(j));
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
        numberCanBeUsed2.setText(String.valueOf(offCode.getUsageCount() - quantity));
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

    public void purchaseButtonClicked() throws IOException {
        if (customer == MainMenuUIController.guest) {
            throwError("You must login");
        }
        if (customer.getCart().isEmpty()) {
            throwError("Your cart is empty!");
        }
        openStage("extraInfoPurchase");
    }

    public void finalBuyButton() throws Exception {
        try {
            if (addressTextArea.getText() != null) {
                factorString = CustomerUIController.purchase(customer, offCodeTextField.getText());
                menuState = "CustomerMenu";
                ((Stage)addressTextArea.getScene().getWindow()).close();
                menuState = "cart";
                showCustomerMenu();
                showFactor();
            }
            else
                throwError("Address field is empty!");
        }
        catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }

    private void showFactor() throws IOException {
        openStage("showFactor");
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
                ((Stage)passEdit.getScene().getWindow()).close();
                menuState = "personal";
                showCustomerMenu();
            }catch (Exception exception)
            {
                throwError(exception.getMessage());
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
        else if (lock.equalsIgnoreCase("showFactor"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/ShowFactor.fxml"));
            Main.setupOtherStage(new Scene(root), "Show Factor");
        }
    }

    private void throwError(String error)
    {
        errorText.setText(error);
        errorText.setVisible(true);
    }

    public static void showCustomerMenu() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
