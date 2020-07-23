package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Main;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientCustomerController;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.Networking.Client.Controller.ClientProductController;
import Store.Model.*;
import Store.Model.Log.BuyLogItem;
import Store.Networking.BankAPI;
import Store.Networking.Client.ClientHandler;
import Store.Networking.HashMapGenerator;
import com.jfoenix.animation.alert.CenterTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerMenuUI implements Initializable {

    private static Map<String, Object> customer;
    private static String menuState = "CustomerMenu";

    public GridPane gridPane;

    public AnchorPane anchorPane;
    public VBox myProductsVBox, cartVBox, discountVBox, viewBuysVBox;
    public VBox factorVBox1, factorVBox2, factorVBox3, factorVBox4, factorVBox5;

    public TabPane tabPane;
    public Tab cart;
    public Tab personal;
    public Tab product;
    public Tab discount;
    public Tab buyHistory;

    public Label balance;
    public Button personalInfoButton;
    public Button myDiscountButton;
    public Button myProductsButton;
    public Button cartButton;
    public Button signUpButton;
    public Button offersButton;
    public Button productsButton;
    public Button mainMenuButton;
    public Button auctionPageButton;
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
    public Button chargeWalletButton;
    public Label errorText;

    public Label factor = new Label("");

    public Button purchaseButton;
    public Button decreaseCartButton;
    public Button increaseCartButton;
    public Label totalPriceCart;

    int[] currentRatings;
    Label[] averageRatingLabels;
    static String phoneNumber, firstName, lastName, email, factorString;

    public TextField imagePath;

    public PasswordField passEdit;
    public TextField offCodeTextField;
    public TextArea addressTextArea;
    public RadioButton byWallet;
    public RadioButton byBank;
    public TextField oldPass;
    public PasswordField newPass;
    public PasswordField confirmationNewPass;
    public TextField moneyBank;
    public TextField bankPass;

    public Button supportPageButton;

    static Map<String, Object> showedBuyLog;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        if (menuState.equalsIgnoreCase("changePass")) {

        }
        else if (menuState.equalsIgnoreCase("extraInfoPurchase"))
        {
            byBank.setOnAction(e -> {
                byWallet.setSelected(false);
            });
            byWallet.setOnAction(e -> {
                byBank.setSelected(false);
            });
        }
        else if (menuState.equalsIgnoreCase("chargeWallet"))
        {

        }
        else if (menuState.equalsIgnoreCase("buyLog"))
        {
            showBuyLog();
            menuState = "CustomerMenu";
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
        Label products;
        Scanner scanner = new Scanner(factorString);
        String eachFactor;
        Pattern pattern = Pattern.compile("offValue=(.+), sellerName=(.+), received=(.+), id=(.+), date=(.+), productList=(.+)}");
        Matcher matcher;
        while (scanner.hasNextLine())
        {
            id = new Label(); date = new Label(); seller = new Label(); offValue = new Label();
            products = new Label();
            eachFactor = scanner.nextLine();
            matcher = pattern.matcher(eachFactor);
            matcher.find();
            id.setText(matcher.group(4));       date.setText(matcher.group(5));         seller.setText(matcher.group(2));        offValue.setText(matcher.group(1));
            products.setText(matcher.group(6));
            products.setWrapText(true);
            id.setMinHeight(70);    date.setMinHeight(70);       offValue.setMinHeight(70);       seller.setMinHeight(70);
            products.setMinHeight(70);      products.setMaxHeight(70);
            factorVBox1.getChildren().add(id);
            factorVBox2.getChildren().add(date);
            factorVBox3.getChildren().add(seller);
            factorVBox4.getChildren().add(offValue);
            factorVBox5.getChildren().add(products);
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
        product.setOnSelectionChanged(e ->  setupInitialPersonalMenu());
        cart.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        personal.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        discount.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        buyHistory.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        logoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());
        auctionPageButton.setOnAction(e -> AuctionsMenuUI.showAuctionsMenu());
        logoutButton.setOnAction((e) -> {
            LoginMenuUI.handleEvent();
            if (customer.get("type").equals("Seller") || customer.get("type").equals("Manager")) {
                try {
                    mainMenuButtonClicked();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else
                showCustomerMenu();
        });
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        balance.textProperty().bind(new SimpleStringProperty(String.valueOf(customer.get("money"))));
        setupInitialMyDiscount();
        setupInitialCart();
        setupInitialMyProducts();
        setupInitialPersonalMenu();
        setupInitialBuyHistory();
    }

    private void showBuyLog() {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        Label[] labels = new Label[6];
        for (int i = 0; i < 6; i++)
            labels[i] = new Label();
        labels[0].setText(showedBuyLog.get("id") + "");
        labels[1].setText(showedBuyLog.get("date") + "");
        labels[2].setText((String) showedBuyLog.get("sellerName"));
        labels[3].setText(showedBuyLog.get("isReceived") + "");
        labels[4].setText(showedBuyLog.get("offValue") + "");
        labels[5].setText(showedBuyLog.get("products") + "");
        labels[5].setWrapText(true);
        gridPane.addRow(1, labels[0], labels[1], labels[2], labels[3], labels[4], labels[5]);
        for (int i = 0; i < 6; i++) {
            GridPane.setHalignment(labels[i], HPos.CENTER);
            GridPane.setValignment(labels[i], VPos.CENTER);
            labels[i].setTextFill(Color.valueOf("ebe9e9"));
        }
    }

    private void showEachBuyLog(Map<String, Object> buyLogItem) {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        HBox hBox = new HBox();
        hBox.setMaxHeight(100);     hBox.setMinHeight(100);
        hBox.setSpacing(20);
        Label dateText = new Label();
        Label sellerText = new Label();
        /*TextArea productsText = new TextArea();
        Label incomeText = new Label();
        Label offValueText = new Label();
        Label sendStatusText = new Label();
        */
        dateText.setText(buyLogItem.get("date") + "");
        /*incomeText.setText(String.valueOf(sellLog.getIncomeValue()));
        offValueText.setText(String.valueOf(sellLog.getOffValue()));
        if (sellLog.isSendStatus())
            sendStatusText.setText("Arrived");
        else
            sendStatusText.setText("Not Arrived");
        */
        sellerText.setText((String) buyLogItem.get("sellerName"));
        /*for(Product product: sellLog.getProducts())
            productsText.setText(productsText.getText() + "\n-" + product.getName() + "-----" + product.getBrand());
        productsText.setEditable(false);
        */VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();
        vBox1.getChildren().add(dateText);
        vBox2.getChildren().add(sellerText);
        Button details = new Button("View Details");
        details.setOnAction(e -> {
            showedBuyLog = buyLogItem;
            try {
                openStage("buyLog");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        vBox3.getChildren().add(details);
        /*vBox3.getChildren().add(incomeText);
        vBox4.getChildren().add(offValueText);
        vBox5.getChildren().add(sendStatusText);
        vBox6.getChildren().add(productsText);
        */vBox1.setPrefWidth(200);    vBox1.setAlignment(Pos.CENTER);
        vBox2.setPrefWidth(100);    vBox2.setAlignment(Pos.CENTER);
        vBox3.setPrefWidth(100);    vBox3.setAlignment(Pos.CENTER);
        /*vBox4.setPrefWidth(100);    vBox4.setAlignment(Pos.CENTER);
        vBox5.setPrefWidth(100);    vBox5.setAlignment(Pos.CENTER);
        vBox6.setPrefWidth(240);    vBox6.setAlignment(Pos.CENTER);
        productsText.setEditable(false);
        hBox.getChildren().addAll(vBox3, vBox4, vBox2, vBox5, vBox1, vBox6);
        */
        hBox.getChildren().addAll(vBox1, vBox2, vBox3);
        viewBuysVBox.getChildren().add(hBox);
    }

    private void setupInitialBuyHistory() {
        for (Map<String, Object> buyLogItem : ((List<Map<String, Object>>) customer.get("log")))
            showEachBuyLog(buyLogItem);
    }

    private void setupInitialMyProducts() {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        ArrayList<Map<String, Object>> showProducts = new ArrayList<>();
        for (Map<String, Object> buyLogItem : (List<Map<String, Object>>) customer.get("log")) {
            for (Map<String, Object> product : (List<Map<String, Object>>) buyLogItem.get("products"))
                showProducts.add(product);
        }
        int i = 0;
        showProducts = makeUnique(showProducts);
        for (Map<String, Object> product : showProducts)
            showEachProductInHBox(product, i++);
        averageRatingLabels = new Label[showProducts.size()];
        currentRatings = new int[showProducts.size()];
        for (int currentRating : currentRatings)
            currentRating = 0;
    }

    private ArrayList<Map<String, Object>> makeUnique(ArrayList<Map<String, Object>> showProducts) {
        ArrayList<Map<String, Object>> returnedArray = new ArrayList<>();
        for (Map<String, Object> product : showProducts) {
            if (!returnedArray.contains(product))
                returnedArray.add(product);
        }
        return returnedArray;
    }

    private void setupInitialMyDiscount() {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        for (Map<String, Object> offCode : (List<Map<String, Object>>) customer.get("offCodes")) {
            showEachDiscountInHBox((Map<String, Object>) offCode.get("offCode"), (int) Math.round(((Double) offCode.get("number"))));
        }

    }
    @FXML
    private void setupInitialCart() {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        ArrayList<Map<String, Object>> cartExample = new ArrayList<>();
        for (Map<String, Object> product : (List<Map<String, Object>>) customer.get("cart"))
            if (!cartExample.contains(product))
                cartExample.add(product);
        /*for (int i = 0; i < cartExample.size(); i++) {
            for (int j = 0; j < cartExample.size(); j++)
            {
                if (i != j && cartExample.get(i).getSeller().getUsername().equals(cartExample.get(j).getSeller().getUsername()) && cartExample.get(i).getProductID() == cartExample.get(j).getProductID())
                {
                    cartExample.remove(cartExample.get(j));
                    j--;
                }
            }
        }*/
        System.out.println(cartExample);
        for (Map<String, Object> product : cartExample)
            showEachProductInHBoxCart(product, numberOfrepeat(product));
        totalPriceCart.setText(customer.get("totalCartPrice") + "");
        totalPriceCart.textProperty().bind(new SimpleStringProperty(String.valueOf(customer.get("totalCartPrice"))));
    }

    /*private void makeUnique(ArrayList<Product> cartExample)
    {

    }*/

    private int numberOfrepeat(Map<String, Object> product) {
        int count = 0;
        for (Map<String, Object> product1 : (List<Map<String, Object>>) customer.get("cart")) {
            if (product.get("id").equals(product1.get("id")) && product.get("sellerName").equals(product1.get("sellerName")))
                count++;
        }
        return count;
    }

    @FXML
    private void setupInitialPersonalMenu() {
//        String path;
//        if (customer.getProfilePicturePath().isEmpty()) {
//            path = "src/main/resources/Images/images.jpg";
//        }
//        else {
//            path = customer.getProfilePicturePath();
//        }

//        File file = new File(path);
//        profile.setImage(new Image(file.toURI().toString()));

        emailTextField.setText((String) customer.get("email"));
        firstNameTextField.setText((String) customer.get("name"));
        lastNameTextField.setText((String) customer.get("familyName"));
        phoneNumberTextField.setText((String) customer.get("phoneNumber"));
        usernameTextField.setText((String) customer.get("username"));
        usernameTextField.setEditable(false);
        chargeWalletButton.setOnAction(e -> {
            try {
                openStage("chargeWallet");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
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
            if (ClientCustomerController.validatePassword(oldPass.getText()) && newPass.getText().equals(confirmationNewPass.getText())) {
                ClientCustomerController.editPersonalInfo(customer, "password", newPass.getText());
                ((Stage) oldPass.getScene().getWindow()).close();
                menuState = "personal";
                CustomerMenuUI.showCustomerMenu();
            }
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    private void showEachProductInHBox(Map<String, Object> product, int i) {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        /*hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {

            }
        });
        */
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
        /*ImageView activeStar = new ImageView(new Image(starPath));
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
*/
        hBox.setMaxHeight(70);      hBox.setMinHeight(70);
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 11; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        vBoxes.get(0).setPrefWidth(60);
        vBoxes.get(10).setPrefWidth(100);
        vBoxes.get(10).getStylesheets().add(getClass().getResource("/CSS/product_menu_stylesheet.css").toExternalForm());
        Label id = new Label(), name = new Label(), sellerName = new Label(), category = new Label(), brand = new Label(), average = new Label(), price = new Label(), productStatus = new Label();
        Button viewProduct = new Button("View Product");
        viewProduct.setPrefWidth(100);
//        viewProduct.setOnAction(e -> {
//            try {
//                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//        });
        vBoxes.get(10).getChildren().add(viewProduct);
        id.setText(product.get("id") + "");
        name.setText((String) product.get("name"));
        sellerName.setText((String) product.get("sellerName"));
        try {
            category.setText(((Map<String, Object>) product.get("category")).get("name") + "");
        }
        catch (Exception exception) {
            category.setText("null");
        }
        brand.setText((String) product.get("brand"));
        average.setText(product.get("averageRating") + "");
        productStatus.setText(product.get("productStatus") + "");
        price.setText(product.get("price") + "");
        TextArea filters = new TextArea(), discription = new TextArea();
        filters.setEditable(false);
        discription.setEditable(false);
        discription.setText((String) product.get("description"));
        for (String filter : (List<String>) product.get("filters"))
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
        /*HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        */vBoxes.get(10).getChildren().addAll(/*hBox1, hBox2*/);
        /*hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        *//*hBox1.getChildren().addAll(ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5);
        hBox2.getChildren().add(rateProductButton);
        */filters.setMaxWidth(100);
        discription.setMaxWidth(100);
        for (int j = 0; j < 11; j++)
            hBox.getChildren().add(vBoxes.get(j));
        myProductsVBox.getChildren().addAll(hBox);
    }

    private void handleCanRate(Map<String, Object> product, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4) {
        if (!(customer.get("type").equals("Customer"))) {
            setRatingDisable(true, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        } else if (ClientProductController.hasBeenRated((String) product.get("id"), ClientHandler.username)) {
            setRatingDisable(true, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        } else if (!ClientProductController.hasBoughtProduct((String) product.get("id"), ClientHandler.username)) {
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

    private void handleRating(Map<String, Object> product, int currentRating, Button ratingStar1, Button ratingStar2, Button ratingStar3, Button ratingStar4, Button ratingStar5, Button rateProductButton, ImageView activeStar, ImageView activeStar1, ImageView activeStar2, ImageView activeStar3, ImageView activeStar4, int i) {
        customer = ClientCustomerController.getUserInfo(ClientHandler.username);
        try {
            ClientCustomerController.rateProduct(customer, product, currentRating);
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
        resetStars(activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        handleCanRate(product, ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5, rateProductButton, activeStar, activeStar1, activeStar2, activeStar3, activeStar4);
        averageRatingLabels[i].setText("" + product.get("averageRating"));
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

    private void showEachProductInHBoxCart(Map<String, Object> product, int count) {
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
        /*hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {

            }
        });*/
        hBox.setMaxHeight(70);      hBox.setMinHeight(70);
        Image plus = new Image(ProductMenuUI.class.getResource("/Icons/Plus.png").toExternalForm());
        Image mines = new Image(ProductMenuUI.class.getResource("/Icons/Negative.png").toExternalForm());
        ImageView plusView = new ImageView(plus);
        ImageView minesView = new ImageView(mines);
        Label plusViewLabel = new Label();      Label minesViewLabel = new Label();
        plusView.setFitWidth(20);       plusView.setFitHeight(20);
        minesView.setFitHeight(20);     minesView.setFitWidth(20);
        plusViewLabel.setGraphic(plusView);
        minesViewLabel.setGraphic(minesView);
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 12; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        vBoxes.get(0).setPrefWidth(74);
        Label id = new Label(), name = new Label(), sellerName = new Label(), category = new Label(), brand = new Label(), average = new Label(), price = new Label(), productStatus = new Label(), number = new Label(), priceInNumber = new Label();
        id.setText(product.get("id") + "");
        name.setText((String) product.get("name"));
        sellerName.setText((String) product.get("sellerName"));
        try {
            category.setText(((Map<String, Object>) product.get("category")).get("name") + "");
        } catch (Exception exception) {
            category.setText("null");
        }
        brand.setText((String) product.get("brand"));
        average.setText(product.get("averageRating") + "");
        productStatus.setText(product.get("productStatus") + "");
        price.setText(product.get("price") + "");
        number.setText(count + "");
        if (product.get("offer") == null)
            priceInNumber.setText((count * Double.parseDouble((String) product.get("price"))) + "");
        else if (ClientCustomerController.canOfferBeUsedInDate(new Date(), (String) product.get("id"))) {
            priceInNumber.setText((count * Double.parseDouble((String) product.get("price")) * (100 - Double.parseDouble((String) ((Map<String, Object>) product.get("offer")).get("offPercent"))) / 100.0) + "");
        } else {
            priceInNumber.setText((count * Double.parseDouble((String) product.get("price"))) + "");
        }
        TextArea filters = new TextArea(), discription = new TextArea();
        filters.setEditable(false);
        discription.setEditable(false);
        discription.setText((String) product.get("description"));
        for (String filter : (List<String>) product.get("filters"))
            filters.setText(filters.getText() + "-" + filter + "\n");
        filters.setMaxWidth(100);   discription.setMaxWidth(100);
        vBoxes.get(0).getChildren().addAll(plusViewLabel, id, minesViewLabel);
        vBoxes.get(1).getChildren().add(name);
        vBoxes.get(2).getChildren().add(sellerName);
        vBoxes.get(3).getChildren().add(category);
        vBoxes.get(4).getChildren().add(brand);
        vBoxes.get(5).getChildren().add(average);
        vBoxes.get(6).getChildren().add(price);
        vBoxes.get(7).getChildren().add(discription);
        vBoxes.get(8).getChildren().add(productStatus);
        vBoxes.get(9).getChildren().add(filters);
        vBoxes.get(10).getChildren().add(number);
        vBoxes.get(11).getChildren().add(priceInNumber);
        for (int j = 0; j < 12; j++)
            hBox.getChildren().add(vBoxes.get(j));
        HBox imageHBox = new HBox();
        imageHBox.setAlignment(Pos.CENTER);
        ImageView imageView = new ImageView();
        imageView.setFitHeight(10);
        imageView.setFitWidth(10);
//        File file = null;
//        if (product.getImagePath().equals("")) {
//            file = new File("src/main/resources/Images/images.jpg");
//        } else {
//            file = new File(product.getImagePath());
//        }
        File file = new File("src/main/resources/Images/images.jpg");
        Image image = new Image(file.toURI().toString());
        imageView = new ImageView(image);
        imageHBox.setMaxHeight(40);
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        imageHBox.getChildren().add(imageView);
        cartVBox.getChildren().addAll(hBox, imageHBox);
        plusViewLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                ClientCustomerController.increaseProduct(customer, product);
                menuState = "cart";
                showCustomerMenu();
            }
            catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
        });
        minesViewLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                ClientCustomerController.decreaseProduct(customer, product);
                menuState = "cart";
                showCustomerMenu();
            }
            catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
        });
    }

    private void showEachDiscountInHBox(Map<String, Object> offCode, int quantity) {
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
        offCodeText2.setText((String) offCode.get("code"));
        maximumOff2.setText(String.valueOf(offCode.get("maximumOff")));
        numberCanBeUsed2.setText(String.valueOf(Integer.parseInt((String) offCode.get("usageCount")) - quantity));
        offPercent2.setText(String.valueOf(offCode.get("offPercentage")));
        endingTime2.setText(String.valueOf(offCode.get("endingTime")));
        startingTime2.setText(String.valueOf(offCode.get("startingTime")));
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

    public void offersButtonClicked() throws IOException {
        menuState = "CustomerMenu";
        Main.setPrimaryStageScene(new Scene(OffersMenuUI.getContent()));
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
        if (!ClientHandler.hasLoggedIn) {
            throwError("You must login");
        } else if (((List) customer.get("cart")).isEmpty()) {
            throwError("Your cart is empty!");
        } else
            openStage("extraInfoPurchase");
    }

    public void doneImageButton() {
//        if (imagePath.getText().isEmpty())
//            throwError("Path field is empty!");
//        else
//            customer.setProfilePicturePath(imagePath.getText());
    }

    public void finalBuyButton() throws Exception {
        try {
            if (addressTextArea.getText() != null) {
                if (byBank.isSelected())
                    factorString = ClientCustomerController.purchase(customer, offCodeTextField.getText(), true, addressTextArea.getText());
                else
                    factorString = ClientCustomerController.purchase(customer, offCodeTextField.getText(), false, addressTextArea.getText());
                menuState = "CustomerMenu";
                ((Stage) addressTextArea.getScene().getWindow()).close();
                menuState = "cart";
                showFactor();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showCustomerMenu();
                    }
                });
            } else
                throwError("Address field is empty!");
        }
        catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }

    public void chargeWallet()
    {
        if (!Pattern.matches("(\\d+)(\\.(\\d+))?", moneyBank.getText()))
            throwError("Money filed should be filled correctly!");
        else{
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", "chargeWallet");
            hashMap.put("money", Double.parseDouble(moneyBank.getText()));
            hashMap.put("username", customer.get("username"));
            hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
            if (hashMap.get("content").equals("error")){
                throwError((String)hashMap.get("type"));
            }
            else{
                menuState = "personal";
                ((Stage)moneyBank.getScene().getWindow()).close();
                showCustomerMenu();
            }
        }
    }

    private void showFactor() throws IOException {
        openStage("showFactor");
    }

    public void changePic() throws IOException {
        FileChooser fileChooser = new FileChooser();
//        try {
//            customer.setProfilePicturePath(fileChooser.showOpenDialog(new Stage()).getPath());
//        }
//        catch (Exception exception) {
//            // do nothing
//        }
        menuState = "personal";
        showCustomerMenu();
    }

    public void submitEditButtonClicked() throws IOException {
        phoneNumber = phoneNumberTextField.getText();
        email = emailTextField.getText();
        firstName = firstNameTextField.getText();
        lastName = lastNameTextField.getText();
        openStage("enterPass");
    }

    public void editPasswordSubmit() {
        if (ClientCustomerController.validatePassword(passEdit.getText())) {
            try {
                ClientCustomerController.editPersonalInfo(customer, "phone number", phoneNumber);
                ClientCustomerController.editPersonalInfo(customer, "email", email);
                ClientCustomerController.editPersonalInfo(customer, "family name", lastName);
                ClientCustomerController.editPersonalInfo(customer, "first name", firstName);
                ((Stage) passEdit.getScene().getWindow()).close();
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
        else if (lock.equalsIgnoreCase("imagePath"))
        {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/ImagePathCustomer.fxml"));
            Main.setupOtherStage(new Scene(root), "Image Path");
        }
        else if (lock.equalsIgnoreCase("buyLog"))
        {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/BuyLog.fxml"));
            Main.setupOtherStage(new Scene(root), "Buy Log");
        }
        else if (lock.equalsIgnoreCase("chargeWallet")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/ChargeWallet.fxml"));
            Main.setupOtherStage(new Scene(root), "Charge Wallet");
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