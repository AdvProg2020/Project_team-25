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

    public AnchorPane anchorPane;
    public GridPane gridPane;

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
        if (menuState.equals("CustomerMenuMyProducts"))
        {
            setupInitial();
            //calculate size of my products
            int sizeOfMyProducts = 0;
            ArrayList<Product> showProducts = new ArrayList<>();
            for (BuyLogItem buyLogItem: customer.getBuyLog()) {
                sizeOfMyProducts += buyLogItem.getProducts().size();
                for (Product product : buyLogItem.getProducts())
                    showProducts.add(product);
            }
            if (sizeOfMyProducts > 4)
                configureGridPane(sizeOfMyProducts);
            showProductsInGridPane(showProducts);

            averageRatingLabels = new Label[sizeOfMyProducts];
            currentRatings = new int[sizeOfMyProducts];
            for (int currentRating: currentRatings)
                currentRating = 0;

        }
        else if (menuState.equals("CustomerMenuCart"))
        {
            setupInitial();
            //calculate size of my products
            int sizeOfCart = customer.getCart().size();
            if (sizeOfCart > 4)
                configureGridPane(sizeOfCart);
            showProductsInGridPaneCart(customer.getCart());
            setupInitialCart();
        }
        else if (menuState.equals("CustomerMenuPersonal"))
        {
            setupInitial();
            int sizeOfMyDiscounts = customer.getOffCodes().size();
            if (sizeOfMyDiscounts > 2)
                configureGridPane(sizeOfMyDiscounts);
            showDiscountsInGridPane(customer.getOffCodes());
            setupInitialPersonalMenu();
        }
        else if (menuState.equalsIgnoreCase("customerMenu"))
        {
            setupInitial();
        }
        else if (menuState.equalsIgnoreCase("changePass"))
        {

        }
        else if (menuState.equalsIgnoreCase("extraInfoPurchase"))
        {

        }
        else if (menuState.equalsIgnoreCase("enterPass"))
        {

        }
    }

    @FXML
    private void setupInitial()
    {
        sliceButton(myDiscountButton, "My Discount");
        sliceButton(personalInfoButton, "Personal Information");
        sliceButton(myProductsButton, "My Products");
        sliceButton(cartButton, "Cart");
        loggedInStatusText.setText(customer.getUsername());
        balance.textProperty().bind(new SimpleStringProperty(String.valueOf(customer.getMoney())));
    }

    @FXML
    private void setupInitialCart()
    {
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

    private void sliceButton(Button button, String string)
    {

    }

    private void showProductsInGridPaneCart(ArrayList<Product> products)
    {
        for (int i = 0; i < (products.size() + 1) / 2; i++)
            for (int j = 0; j < 2; j++)
                showEachProductInGridPaneCart(i, j, products.get(2 * i + j));
    }

    private void showProductsInGridPane(ArrayList<Product> products)
    {
        for (int i = 0; i < (products.size() + 1) / 2; i++)
            for (int j = 0; j < 2; j++)
                showEachProductInGridPane(i, j, products.get(2 * i + j));
    }

    private void showDiscountsInGridPane(HashMap<OffCode, Integer> offCodes)
    {
        int i = 0;
        for (OffCode offCode: offCodes.keySet())
            showEachDiscountInGridPane(i++, offCode, offCodes.get(offCode));
    }

    private void showEachProductInGridPane(int i, int j, Product product)
    {
        String starPath = "resources\\Icons\\StarSelected.png";
        Label info = new Label();
        info.setText(product.getName() + "-------" + product.getCategory() + "-------" + product.getBrand() + "-------" + product.getAverageRating());
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
            starButtons[buttonIndex].setOnAction((e) -> handleRatingChange(finalButtonIndex, activeStar, activeStar1, activeStar2, activeStar3, activeStar4, i, j));
        }
        rateProductButton.setOnAction((e) -> handleRating(product, currentRatings[2 * i + j], ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4, i, j));

        handleCanRate(product, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);

        gridPane.add(productView, j, i);
        gridPane.add(info, j, i);
        gridPane.add(ratingStar1, j, i);
        gridPane.add(ratingStar2, j, i);
        gridPane.add(ratingStar3, j, i);
        gridPane.add(ratingStar4, j, i);
        gridPane.add(ratingStar5, j, i);
        gridPane.add(rateProductButton, j, i);
        configurePositionsInGridPane(productView, info, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton);
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

    private void handleRatingChange(int index, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4, int i, int j) {
        currentRatings[2 * i + j] = index;
        ImageView[] starImages = new ImageView[] {activeStar, activeStar1, activeStar2, activeStar3, activeStar4};
        resetStars(activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        for (int buttonIndex = 0; buttonIndex < index; buttonIndex++) {
            starImages[buttonIndex].setVisible(true);
        }
    }

    private void handleRating(Product product, int currentRating, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4, int i, int j) {
        CustomerController.rateProduct((Customer) MainMenuUIController.currentUser, product, currentRating);
        resetStars(activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        handleCanRate(product, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        averageRatingLabels[2 * i + j].setText("" + product.getAverageRating());
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

    private void showEachProductInGridPaneCart(int i, int j, Product product)
    {
        Image plus = new Image("resources\\Icons\\Plus.png");
        Image mines = new Image("resources\\Icons\\Negative.png");
        ImageView plusView = new ImageView(plus);
        ImageView minesView = new ImageView(mines);
        Label info = new Label();
        info.setText(product.getName() + "-------" + product.getCategory() + "-------" + product.getBrand() + "--------" + product.getAverageRating());
        Image productImage = new Image(product.getImagePath());
        ImageView productView = new ImageView(productImage);
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        gridPane.add(productView, j, i);
        gridPane.add(info, j, i);
        gridPane.add(plusView, j, i);
        gridPane.add(minesView, j, i);
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
        configurePositionsInCartGridPane(productView, info, plusView, minesView);
    }

    private void showEachDiscountInGridPane(int i, OffCode offCode, int quantity)
    {
        Label offCodeText = new Label();
        Label maximumOff = new Label();
        Label offPercent = new Label();
        Label endingTime = new Label();
        Label numberCanBeUsed = new Label();
        offCodeText.setText(offCode.getCode());
        maximumOff.setText(String.valueOf(offCode.getMaximumOff()));
        numberCanBeUsed.setText(String.valueOf(quantity));
        offPercent.setText(String.valueOf(offCode.getOffPercentage()));
        endingTime.setText(String.valueOf(offCode.getEndingTime()));
        gridPane.add(offCodeText, 0, i);
        gridPane.add(maximumOff, 0, i);
        gridPane.add(offPercent, 0, i);
        gridPane.add(endingTime, 0, i);
        gridPane.add(numberCanBeUsed, 0, i);
        configurePositionsInDiscountGridPane(offCodeText, offPercent, maximumOff, endingTime, numberCanBeUsed);
    }


    private void configureGridPane(int size)
    {
        RowConstraints newRow;
        if (!menuState.equals("CustomerMenuMyDiscounts")) {
            for (int i = 0; i < (size - 1) / 2; i++) {
                anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 250);
                gridPane.setPrefHeight(gridPane.getPrefHeight() + 250);
                newRow = new RowConstraints();
                newRow.setMinHeight(250);
                newRow.setMaxHeight(250);
                gridPane.getRowConstraints().add(newRow);
            }
        }
        else{
            for (int i = 0; i < (size - 2); i++) {
                anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 100);
                gridPane.setPrefHeight(gridPane.getPrefHeight() + 100);
                newRow = new RowConstraints();
                newRow.setMinHeight(100);
                newRow.setMaxHeight(100);
                gridPane.getRowConstraints().add(newRow);
            }
        }
        //putPaneInGridPane();
    }

    private void putPaneInGridPane()
    {
        Pane pane;
        for (int i = 0; i < gridPane.getRowCount(); i++)
            for (int j = 0; j < gridPane.getColumnCount(); j++)
            {
                pane = new Pane();
                pane.setMinWidth(462);      pane.setMaxWidth(464);
                pane.setMinHeight(250);     pane.setMaxHeight(250);
                gridPane.add(pane, j, i);
            }
    }

    public void configurePositionsInGridPane(ImageView productImage, Label info, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton)
    {
        productImage.setFitHeight(184);
        productImage.setFitWidth(200);
        GridPane.setValignment(productImage, VPos.CENTER);
        GridPane.setHalignment(productImage, HPos.RIGHT);
        GridPane.setMargin(productImage, new Insets(-50, 10, 0, 0));
        info.setPrefWidth(211);      info.setPrefHeight(172);
        GridPane.setMargin(info, new Insets(10, 0, 0, 10));
        GridPane.setHalignment(info, HPos.LEFT);
        GridPane.setValignment(info, VPos.TOP);
        Button[] ratings = new Button[]{ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        int count = 2;
        for (Button rating: ratings)
        {
            rating.setPrefWidth(26);
            rating.setPrefHeight(26);
            GridPane.setValignment(rating, VPos.BOTTOM);
            GridPane.setHalignment(rating, HPos.CENTER);
            GridPane.setMargin(rating, new Insets(0, (count--) * 100, 12, 0));
        }
        rateProductButton.setPrefWidth(49);         rateProductButton.setPrefHeight(25);
        GridPane.setValignment(rateProductButton, VPos.BOTTOM);
        GridPane.setHalignment(rateProductButton, HPos.RIGHT);
        GridPane.setMargin(rateProductButton, new Insets(0, 20, 12, 0));
    }

    public void configurePositionsInCartGridPane(ImageView productImage, Label info, ImageView plus, ImageView mines)
    {
        productImage.setFitWidth(220);          productImage.setFitHeight(219);
        info.setPrefHeight(225);            info.setPrefWidth(211);
        plus.setFitHeight(20);          plus.setFitWidth(20);
        mines.setFitHeight(20);            mines.setFitWidth(20);
        GridPane.setHalignment(productImage, HPos.RIGHT);
        GridPane.setHalignment(info, HPos.LEFT);
        GridPane.setHalignment(plus, HPos.RIGHT);
        GridPane.setHalignment(mines, HPos.CENTER);
        GridPane.setValignment(productImage, VPos.CENTER);
        GridPane.setValignment(info, VPos.TOP);
        GridPane.setValignment(plus, VPos.TOP);
        GridPane.setValignment(mines, VPos.TOP);
        GridPane.setMargin(productImage, new Insets(0, 10, 0, 0));
        GridPane.setMargin(info, new Insets(10, 0, 0, 0));
        GridPane.setMargin(plus, new Insets(10, 5, 0, 0));
        GridPane.setMargin(mines, new Insets(10, 0, 0, 0));
    }


    public void configurePositionsInDiscountGridPane(Label offCodeText, Label offPercent, Label maximumOff, Label endingTime, Label numberRemain)
    {
        Label[] lables = new Label[5];
        lables[0].setText("Code: ");
        lables[1].setText("Percent: ");
        lables[2].setText("Maximum Off: ");
        lables[3].setText("End Time: ");
        lables[4].setText("Remaining Number: ");
        for (int i = 0; i < 5; i++) {
            gridPane.add(lables[i], GridPane.getColumnIndex(offCodeText), GridPane.getRowIndex(offCodeText));
            GridPane.setMargin(lables[i], new Insets(0, 0, 50, 200 * i + 10));
        }
        lables = new Label[]{offCodeText, offPercent, maximumOff, endingTime, numberRemain};
        for (int i = 0; i < 5; i++)
            GridPane.setMargin(lables[i], new Insets(30, 0, 0, 200 * i + 40));

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

    public void logoutButtonClicked(){

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
