package Store.View;

import Store.Controller.SellerController;
import Store.Controller.SellerUIController;
import Store.Main;
import Store.Model.Enums.CheckingStatus;
import Store.Model.Log.BuyLogItem;
import Store.Model.Log.SellLogItem;
import Store.Networking.BankAPI;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientSellerController;

import Store.Model.Seller;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.*;
import Store.View.DateTimePicker.DateTimePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class SellerMenuUI implements Initializable {

    private static Map<String, Object> seller;
    private static String menuState = "SellerMenu";

    public VBox manageProductsVBox, viewSalesVBox, myOffersVBox, viewBuyersVBox;

    public GridPane gridPane;

    public TabPane tabPane;
    public Tab product;
    public Tab offer;
    public Tab personal;
    public Tab viewSales;
    public Tab viewBuyers;

    public Label balance;
    public Button offersButton;
    public Button productsButton;
    public Button mainMenuButton;
    public Button auctionPageButton;
    public Button logoutButton;
    public Label loggedInStatusText;
    public ComboBox myOffersSortBy;
    public ComboBox manageProductsSortBy;

    public ImageView profile;
    public Button submitEditButton;
    public Button changePassButton;
    public Button chargeWalletButton;
    public Button dechargeWalletButton;
    public TextField usernameTextField;
    public PasswordField passwordField;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
    public TextField companyNameTextField;
    public TextArea companyInformationTextField;
    public Label errorText;

    public TextField moneyBank;
    public TextField bankPass;

    static String phoneNumber, firstName, lastName, email, companyName, companyInformation;
    ArrayList<String> filters = new ArrayList<>();
    List<String> offerProducts = new ArrayList<>();
    static Map<String, Object> selectedOffer;
    static Map<String, Object> selectedProduct;
    String filter = new String("");

    public PasswordField passEdit;
    public TextField oldPass;
    public PasswordField newPass;
    public PasswordField confirmationNewPass;

    public DateTimePicker endDateAuction = new DateTimePicker();

    public DatePicker startDateOffer;
    public DatePicker endDateOffer;
    public TextField offPercentOffer;
    public TextField productIDInOffer;
    public ImageView plusProductOffer;
    public ImageView minesProductOffer;

    public TextField filterTextField;

    public TextField imagePath;
    public TextField videoPath;
    public ComboBox category;
    public TextField productName;
    public TextField brand;
    public TextField price;
    public ComboBox availablity;
    public TextField attributes;
    public TextField description;
    public ImageView plusFilterProduct;
    public ImageView minesFilterProduct;
    String availablityString;
    String categoryString;

    public HBox auctionDateHBox;

    public TextField requestedProductId;

    public Button supportPageButton;

    ArrayList<String> productsOfOfferHandler;
    static Map<String, Object> showedSellLog;
    String currentSortOffer = new String("time of starting");
    String currentSortProduct = new String("visited");

    String filePath = "";

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seller = ClientManagerController.getUserInfo(ClientHandler.username);
        if (menuState.equalsIgnoreCase("changePass")) {

        } else if (menuState.equalsIgnoreCase("sellLog")) {
        }
        else if (menuState.equalsIgnoreCase("chargeWallet")) {
        }
        else if (menuState.equalsIgnoreCase("dechargeWallet")){
        }
        else if (menuState.equalsIgnoreCase("sellLog")) {
            showSellLog();
            menuState = "SellerMenu";
        } else if (menuState.equalsIgnoreCase("imagePath")) {

        } else if (menuState.equalsIgnoreCase("adsRequest")) {
        }
        else if (menuState.equalsIgnoreCase("endDate")){
            endDateAuction = new DateTimePicker();
            auctionDateHBox.getChildren().add(endDateAuction);
        }
        else if (menuState.equalsIgnoreCase("adsRequest")){

        } else if (menuState.equalsIgnoreCase("addProduct")) {
            setupInitialAddProduct();
        } else if (menuState.equalsIgnoreCase("addOffer")) {
            setupInitialAddOffer();
        } else if (menuState.equalsIgnoreCase("enterPass")) {

        } else if (menuState.equalsIgnoreCase("editOffer")) {
            setupInitialEditOffer();
        } else if (menuState.equalsIgnoreCase("editProduct")) {
            setupInitialEditProduct();
        } else {
            if (menuState.equalsIgnoreCase("product") || menuState.equalsIgnoreCase("offer") || menuState.equalsIgnoreCase("personal"))
                resetAll();
            setupInitial();
            if (menuState.equalsIgnoreCase("product"))
                tabPane.getSelectionModel().select(product);
            else if (menuState.equalsIgnoreCase("offer"))
                tabPane.getSelectionModel().select(offer);
            else if (menuState.equalsIgnoreCase("personal"))
                tabPane.getSelectionModel().select(personal);
        }
    }

    private void showSellLog() {
        Label[] labels = new Label[7];
        for (int i = 0; i < 7; i++)
            labels[i] = new Label();
        labels[0].setText(showedSellLog.get("id") + "");
        labels[1].setText(showedSellLog.get("date") + "");
        labels[2].setText((String) showedSellLog.get("customerName"));
        labels[3].setText(showedSellLog.get("incomeValue") + "");
        labels[4].setText(showedSellLog.get("SendStatus") + "");
        labels[5].setText(showedSellLog.get("offValue") + "");
        labels[6].setText(showedSellLog.get("products") + "");
        labels[6].setWrapText(true);
        gridPane.addRow(1, labels[0], labels[1], labels[2], labels[3], labels[4], labels[5], labels[6]);
        for (int i = 0; i < 7; i++) {
            GridPane.setHalignment(labels[i], HPos.CENTER);
            GridPane.setValignment(labels[i], VPos.CENTER);
            labels[i].setTextFill(Color.valueOf("ebe9e9"));
        }
    }

    private void resetAll() {
        for (int i = 1; i < manageProductsVBox.getChildren().size(); )
            manageProductsVBox.getChildren().remove(i);
        for (int i = 1; i < myOffersVBox.getChildren().size(); )
            myOffersVBox.getChildren().remove(i);
        for (int i = 1; i < viewSalesVBox.getChildren().size(); )
            viewSalesVBox.getChildren().remove(i);
    }

    @FXML
    private void setupInitialEditOffer() {
        productsOfOfferHandler = new ArrayList<>();
        productsOfOfferHandler.addAll((List<String>) selectedOffer.get("products"));
        startDateOffer.setValue(LocalDate.now());
        endDateOffer.setValue(LocalDate.now());
        offerProducts = (List<String>) selectedOffer.get("products");
        offPercentOffer.setText(String.valueOf(selectedOffer.get("offPercent")));
        plusProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty()) {
                if ((!StringUtils.isNumeric(productIDInOffer.getText()) || !ClientSellerController.isProductWithThisID(productIDInOffer.getText())) || !ClientSellerController.isProductFromThisSeller(productIDInOffer.getText())) {
                    throwError("Please enter a valid ID");
                    return;
                }
                if (productsOfOfferHandler.contains(productIDInOffer.getText())) {
                    throwError("Already added this product");
                    return;
                } else if ((productsOfOfferHandler == null || !productsOfOfferHandler.contains(productIDInOffer.getText())) && ClientSellerController.isProductInOffer(productIDInOffer.getText())) {
                    throwError("This product is already in another offer!");
                    return;
                } else {
                    System.out.println("hello");
                    productsOfOfferHandler.add(productIDInOffer.getText());
                    throwError("Product added to list");
                }
            } else
                throwError("Incomplete Fields");
        });
        minesProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty()) {
                if (!StringUtils.isNumeric(productIDInOffer.getText()) || !ClientSellerController.isProductFromThisSeller(productIDInOffer.getText())) {
                    throwError("Please enter a valid ID");
                    return;
                }
                if (!productsOfOfferHandler.contains(productIDInOffer.getText())) {
                    throwError("Doesn't exist in offer");
                    return;
                } else {
                    productsOfOfferHandler.remove(productIDInOffer.getText());
                    throwError("Product removed from list");
                }
            } else
                throwError("Incomplete Fields");
        });
    }

    @FXML
    private void setupInitialAddOffer() {
        selectedOffer.put("products", new ArrayList<>());
        startDateOffer.setValue(LocalDate.now());
        endDateOffer.setValue(LocalDate.now());
        plusProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty()) {
                if ((!StringUtils.isNumeric(productIDInOffer.getText()) || !(ClientSellerController.isProductWithThisID(productIDInOffer.getText())) || !ClientSellerController.isProductFromThisSeller(productIDInOffer.getText()))) {
                    throwError("Please enter a valid ID");
                    return;
                }
                if ((selectedOffer == null || !((List<String>) selectedOffer.get("products")).contains(productIDInOffer.getText()) && ClientSellerController.isProductInOffer(productIDInOffer.getText()))) {
                    throwError("This product is already in another offer!");
                    return;
                } else if (((List<String>) selectedOffer.get("products")).contains(productIDInOffer.getText())) {
                    throwError("Already added this product");
                    return;
                } else {
                    ((List<String>) selectedOffer.get("products")).add(productIDInOffer.getText());
                    throwError("Product added to list");
                    return;
                }
            } else
                throwError("Incomplete Fields");
        });
    }

    /*   private ArrayList<Integer> productsAddedToOffer()
       {
           ArrayList<Integer> returnArray = new ArrayList<>();
           for (Product product: selectedOffer.getProducts()){
               if (!returnArray.contains(product.getProductID()))
                   returnArray.add(product.getProductID());
           }
           return returnArray;
       }

       private ArrayList<Integer> productsNotAddedToOffer()
       {
           ArrayList<Integer> returnArray = new ArrayList<>();
           for (Product product: seller.getProducts()){
               if (!Offer.hasOfferByID(product.getProductID()) && !returnArray.contains(product.getProductID()))
                   returnArray.add(product.getProductID());
           }
           return returnArray;
       }
   */
    @FXML
    private void setupInitialEditProduct() {
        /*imagePath.setText(selectedProduct.getImagePath());
        videoPath.setText(selectedProduct.getVideoPath());
        */
        if (ClientSellerController.isProductInCategory((String) selectedProduct.get("id")))
            categoryString = (String) ((Map<String, Object>) selectedProduct.get("category")).get("name");
        else
            categoryString = "";
        ArrayList<String> categories = new ArrayList<>();
        categories.add("");
        for (Map<String, Object> category : ClientProductsController.getAllCategories())
            categories.add((String) category.get("name"));

        category.setItems(FXCollections.observableArrayList(categories));
        category.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                categoryString = newValue.toString();
            }
        });
        category.getSelectionModel().select(categoryString);
        productName.setText((String) selectedProduct.get("name"));
        brand.setText((String) selectedProduct.get("brand"));
        price.setText(selectedProduct.get("price") + "");
        attributes.setText((String) selectedProduct.get("attributes"));
        description.setText((String) selectedProduct.get("description"));
        if ((Boolean) selectedProduct.get("availability"))
            availablityString = "available";
        else
            availablityString = "unavailable";
        availablity.setItems(FXCollections.observableArrayList("Available", "Inavailable"));
        availablity.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                availablityString = newValue.toString();
            }
        });
        plusFilterProduct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty()) {
                try {
                    ((List<String>) selectedProduct.get("filters")).add(filterTextField.getText());
                } catch (Exception exception) {
                    throwError(exception.getMessage());
                }
            } else
                throwError("Filter Field is Empty");
        });
        minesFilterProduct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty()) {
                try {
                    ((List<String>) selectedProduct.get("filters")).remove(filterTextField.getText());
                } catch (Exception exception) {
                    throwError(exception.getMessage());
                }
            } else
                throwError("Filter Field is Empty");
        });
    }

    @FXML
    private void setupInitialAddProduct() {
//        if (ClientSellerController.isProductInCategory((String) selectedProduct.get("id")))
//            categoryString = (String) ((Map<String, Object>) selectedProduct.get("category")).get("name");
//        else
        categoryString = "";
        ArrayList<String> categories = new ArrayList<>();
        selectedProduct.put("filters", new ArrayList<>());
        categories.add("");
        for (Map<String, Object> category : ClientProductsController.getAllCategories())
            categories.add((String) category.get("name"));

        category.setItems(FXCollections.observableArrayList(categories));
        category.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                categoryString = newValue.toString();
            }
        });
        availablityString = "available";
        plusFilterProduct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty()) {
                try {
                    ((List<String>) selectedProduct.get("filters")).add(filterTextField.getText());
                } catch (Exception exception) {
                    throwError(exception.getMessage());
                }
            } else
                throwError("Filter Field is Empty");
        });
    }

    public void doneAddOffer() {
        try {
            Map<String, Object> offer = createOffer();
            System.out.println();
            offer.put("products", selectedOffer.get("products"));
            ClientSellerController.addOfferFromSeller(offer);
            ((Stage) offPercentOffer.getScene().getWindow()).close();
            menuState = "offer";
            showSellerMenu();
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    public void doneEditOffer() {
        try {
            Map<String, Object> offer = createOffer();
            offer.put("products", productsOfOfferHandler);
            ClientSellerController.editOffer((String) selectedOffer.get("id"), offer);
            ((Stage) offPercentOffer.getScene().getWindow()).close();
            menuState = "offer";
            showSellerMenu();
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    public void doneAddProduct() {
        try {
            ClientSellerController.addProductFromSeller(createProduct());
            ((Stage) filterTextField.getScene().getWindow()).close();
            menuState = "product";
            showSellerMenu();
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    public void enterDate(){
        LocalDateTime localDateTime = endDateAuction.dateTimeProperty().getValue();
        try {
            ClientSellerController.addAuction(selectedProduct, localDateTime);
            ((Stage)(endDateAuction.getScene().getWindow())).close();
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    public void doneEditProduct() {
        try {
            Map<String, Object> product1 = createProduct();
//            product1.setImagePath(selectedProduct.getImagePath());
//            product1.setVideoPath(selectedProduct.getVideoPath());
            ClientSellerController.editProduct((String) selectedProduct.get("id"), product1);
            ((Stage) filterTextField.getScene().getWindow()).close();
            menuState = "product";
            showSellerMenu();
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    private Map<String, Object> createProduct() throws Exception {
        String attribute = categoryString;
        String name = productName.getText();
        String brandString = brand.getText();
        attribute = price.getText();
        if (!attribute.matches("^\\d+(\\.\\d+)?$")) {
            throw new Exception("invalid price input");
        }
        double price = Double.parseDouble(attribute);
        boolean availabilityBool = false;
        if (availablityString.equalsIgnoreCase("available"))
            availabilityBool = true;
        List<String> filters = (List<String>) selectedProduct.get("filters");
        String descriptionString = description.getText();
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("category", categoryString);
        product.put("brand", brandString);
        product.put("price", price);
        product.put("availability", availabilityBool);
        product.put("description", descriptionString);
        product.put("filters", filters);
        product.put("filePath", filePath);
        System.out.println("FILE PATH! : " + filePath);

        return product;
    }

    private Map<String, Object> createOffer() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");

        String input = startDateOffer.getValue().getDayOfMonth() + "-" + startDateOffer.getValue().getMonth().getValue() + "-" + startDateOffer.getValue().getYear();
        Date startingTime, endingTime;
        try {
            startingTime = dateFormat.parse(input);
        } catch (ParseException exception) {
            throw new Exception("Invalid date format!");
        }

        input = endDateOffer.getValue().getDayOfMonth() + "-" + endDateOffer.getValue().getMonth().getValue() + "-" + endDateOffer.getValue().getYear();
        try {
            endingTime = dateFormat.parse(input);
        } catch (ParseException exception) {
            throw new Exception("Invalid date format!");
        }

        input = offPercentOffer.getText();
        if (!input.matches("\\d+(\\.\\d+)?")) {
            throw new Exception("Invalid percentage!");
        }
        double offPercentage = Double.parseDouble(input);

        Map<String, Object> offer = new HashMap<>();
        offer.put("offPercentage", offPercentage);
        offer.put("startingTime", startingTime.getTime() + "");
        offer.put("endingTime", endingTime.getTime() + "");

        return offer;
    }

    @FXML
    private void setupInitialMyOffers() {
        for (Map<String, Object> offer : ClientSellerController.getOfferOfThisSeller())
            showEachOfferInHBox(offer);
        myOffersSortBy.setItems(FXCollections.observableArrayList("time of starting", "time of ending"));
        myOffersSortBy.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentSortOffer = newValue.toString();
            myOffersVBox.getChildren().clear();
            for (Map<String, Object> offer : ClientSellerController.sortOffersOfSeller(currentSortOffer)) {
                showEachOfferInHBox(offer);
            }
        });
    }


    @FXML
    private void setupInitialBuyers() {
        List<Map<String, Object>> buyers = (List<Map<String, Object>>) seller.get("buyers");
        for (int i = 1; i < buyers.size(); i += 2)
            showEachBuyerInHBox(buyers.get(i - 1), buyers.get(i));
        if (buyers.size() % 2 == 1)
            showEachBuyerInHBox(buyers.get(buyers.size() - 1), null);
    }

    @FXML
    private void setupInitial() {
        auctionPageButton.setOnAction(e -> AuctionsMenuUI.showAuctionsMenu());
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());
        product.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        offer.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        viewBuyers.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        viewSales.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        personal.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        loggedInStatusText.textProperty().bind(ClientMainMenuController.currentUserUsername);
        logoutButton.textProperty().bind(ClientMainMenuController.loginLogoutButtonText);
        logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            LoginMenuUI.handleEvent();
            try {
                mainMenuButtonClicked();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        balance.textProperty().bind(new SimpleStringProperty(String.valueOf(seller.get("money"))));

        setupInitialPersonalMenu();
        setupInitialBuyers();
        setupInitialManageProducts();
        setupInitialMyOffers();
        setupInitialSalesHistory();
    }

    @FXML
    private void setupInitialManageProducts() {
        for (Map<String, Object> product : (List<Map<String, Object>>) seller.get("products")) {
            showEachProductInHBox(product);
        }
        manageProductsSortBy.setItems(FXCollections.observableArrayList("rating", "price", "visit", "lexicographical"));
        manageProductsSortBy.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentSortProduct = newValue.toString();
            manageProductsVBox.getChildren().clear();
            for (Map<String, Object> product : ClientSellerController.sortProductsOfSeller(currentSortProduct)) {
                showEachProductInHBox(product);
            }
        });
    }

    @FXML
    private void setupInitialPersonalMenu() {
        String path = "src/main/resources/Icons/unknown.png";
//        if (seller.getProfilePicturePath().isEmpty()) {
//            path = "src/main/resources/Icons/unknown.png";
//        }
//        else {
//            path = seller.getProfilePicturePath();
//        }

        File file = new File(path);
        profile.setImage(new Image(file.toURI().toString()));

        emailTextField.setText((String) seller.get("email"));
        firstNameTextField.setText((String) seller.get("name"));
        lastNameTextField.setText((String) seller.get("familyName"));
        phoneNumberTextField.setText((String) seller.get("phoneNumber"));
        usernameTextField.setText((String) seller.get("username"));
        usernameTextField.setEditable(false);
        companyNameTextField.setText((String) seller.get("companyName"));
        companyInformationTextField.setText((String) seller.get("companyDescription"));
        chargeWalletButton.setOnAction(e -> {
            try {
                openStage("chargeWallet");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        dechargeWalletButton.setOnAction(e -> {
            try {
                openStage("dechargeWallet");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    @FXML
    private void setupInitialSalesHistory() {
        for (Map<String, Object> sellLogItem : (List<Map<String, Object>>) seller.get("log"))
            showEachFactorInHBox(sellLogItem);
    }

    public void changePassAction() {
        try {
            openStage("changePass");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void finalChangePassButtonClicked() {
        try {
            if (ClientSellerController.validatePassword(oldPass.getText()) && newPass.getText().equals(confirmationNewPass.getText())) {
                ClientSellerController.editPersonalInfo("password", newPass.getText());
                ((Stage) oldPass.getScene().getWindow()).close();
                menuState = "personal";
                showSellerMenu();
            }
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    private void showEachOfferInHBox(Map<String, Object> offer) {
        HBox hBox = new HBox();
        Button edit = new Button("Edit");
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                offerProducts.clear();
                selectedOffer = offer;
                offerProducts.addAll((List<String>) offer.get("products"));
                openStage("editOffer");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 6; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        hBox.setSpacing(40);
        hBox.setMaxHeight(70);
        hBox.setMinHeight(70);
        vBoxes.get(0).setPrefWidth(62);
        Label id = new Label(), offPercent = new Label(), startDate = new Label(), endDate = new Label();
        id.setText(offer.get("id") + "");
        offPercent.setText(offer.get("offPercent") + "");
        startDate.setText(offer.get("startingTime") + "");
        endDate.setText(offer.get("endingTime") + "");
        TextArea products = new TextArea();
        products.setEditable(false);
        for (String product : (List<String>) offer.get("products"))
//            products.setText(products.getText() + "-" + product.getName() + "------" + product.getBrand() + "\n");
            products.setText(products.getText() + "-" + product);
        products.setMaxWidth(100);
        vBoxes.get(0).getChildren().addAll(id);
        vBoxes.get(1).getChildren().add(offPercent);
        vBoxes.get(2).getChildren().add(startDate);
        vBoxes.get(3).getChildren().add(endDate);
        vBoxes.get(4).getChildren().add(products);
        vBoxes.get(5).getChildren().add(edit);
        for (int j = 0; j < 6; j++)
            hBox.getChildren().add(vBoxes.get(j));
        myOffersVBox.getChildren().add(hBox);
    }

    private void showEachProductInHBox(Map<String, Object> product) {
        HBox hBox = new HBox();
        /*hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {

            }
        });*/
        Button edit = new Button("Edit");
        Button remove = new Button("Remove");
        Button auction = new Button("Auction");
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedProduct = product;
                openStage("editProduct");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        remove.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            ClientSellerController.removeProduct((String) product.get("id"));
            menuState = "product";
            showSellerMenu();
        });
        auction.disableProperty().bind(ClientSellerController.getIsInAuction(product));
        auction.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            try {
                selectedProduct = product;
                openStage("endDate");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        /*productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });*/
        hBox.setMaxHeight(70);
        hBox.setMinHeight(70);
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 11; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        vBoxes.get(0).setPrefWidth(60);
        vBoxes.get(10).setPrefWidth(140);
        Label id = new Label(), name = new Label(), sellerName = new Label(), category = new Label(), brand = new Label(), average = new Label(), price = new Label(), productStatus = new Label();
        id.setText(product.get("id") + "");
        name.setText((String) product.get("name"));
        sellerName.setText((String) product.get("sellerUsername"));
        if (ClientSellerController.isProductInCategory((String) product.get("id")))
            category.setText(product.get("category") + "");
        else
            category.setText("");
        brand.setText((String) product.get("brand"));
        average.setText(product.get("averageRating") + "");
        productStatus.setText(product.get("status") + "");
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
        vBoxes.get(10).getChildren().addAll(edit, remove, auction);
        vBoxes.get(10).setPrefWidth(95);
        filters.setMaxWidth(99);
        discription.setMaxWidth(99);
        for (int j = 0; j < 11; j++)
            hBox.getChildren().add(vBoxes.get(j));
        manageProductsVBox.getChildren().addAll(hBox);
    }

    private void showEachFactorInHBox(Map<String, Object> sellLog) {
        HBox hBox = new HBox();
        hBox.setMaxHeight(100);
        hBox.setMinHeight(100);
        hBox.setSpacing(20);
        Label dateText = new Label();
        Label customerText = new Label();
        /*TextArea productsText = new TextArea();
        Label incomeText = new Label();
        Label offValueText = new Label();
        Label sendStatusText = new Label();
        */
        dateText.setText((String) sellLog.get("date"));
        /*incomeText.setText(String.valueOf(sellLog.getIncomeValue()));
        offValueText.setText(String.valueOf(sellLog.getOffValue()));
        if (sellLog.isSendStatus())
            sendStatusText.setText("Arrived");
        else
            sendStatusText.setText("Not Arrived");
        */
        customerText.setText((String) sellLog.get("customerName"));
        /*for(Product product: sellLog.getProducts())
            productsText.setText(productsText.getText() + "\n-" + product.getName() + "-----" + product.getBrand());
        productsText.setEditable(false);
        */
        VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();
        vBox1.getChildren().add(dateText);
        vBox2.getChildren().add(customerText);
        Button details = new Button("View Details");
        details.setOnAction(e -> {
            showedSellLog = sellLog;
            try {
                openStage("sellLog");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        vBox3.getChildren().add(details);
        /*vBox3.getChildren().add(incomeText);
        vBox4.getChildren().add(offValueText);
        vBox5.getChildren().add(sendStatusText);
        vBox6.getChildren().add(productsText);
        */
        vBox1.setPrefWidth(200);
        vBox1.setAlignment(Pos.CENTER);
        vBox2.setPrefWidth(100);
        vBox2.setAlignment(Pos.CENTER);
        vBox3.setPrefWidth(100);
        vBox3.setAlignment(Pos.CENTER);
        /*vBox4.setPrefWidth(100);    vBox4.setAlignment(Pos.CENTER);
        vBox5.setPrefWidth(100);    vBox5.setAlignment(Pos.CENTER);
        vBox6.setPrefWidth(240);    vBox6.setAlignment(Pos.CENTER);
        productsText.setEditable(false);
        hBox.getChildren().addAll(vBox3, vBox4, vBox2, vBox5, vBox1, vBox6);
        */
        hBox.getChildren().addAll(vBox1, vBox2, vBox3);
        viewSalesVBox.getChildren().add(hBox);
    }

    private void showEachBuyerInHBox(Map<String, Object> customer1, Map<String, Object> customer2) {
        HBox hBox = new HBox();
        hBox.setMinHeight(131);
        hBox.setMaxHeight(131);
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        hBox1.setPrefWidth(475);
        hBox2.setPrefWidth(475);
        hBox1.setSpacing(20);
        hBox2.setSpacing(20);
        Label username = new Label(), email = new Label(), phoneNumber = new Label();
        ImageView imageView = new ImageView();
        hBox1.setAlignment(Pos.CENTER_LEFT);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        imageView.setFitWidth(133);
        imageView.setFitHeight(131);
        System.out.println(customer1);
        System.out.println(customer2);
        username.setText((String) customer1.get("username"));
        email.setText((String) customer1.get("email"));
        phoneNumber.setText((String) customer1.get("phoneNumber"));
        File file = null;
//        if (customer1.getProfilePicturePath().equals("")) {
//            file = new File("src/main/resources/Icons/unknown.png");
//        } else {
//            file = new File(customer1.getProfilePicturePath());
//        }
        file = new File("src/main/resources/Icons/unknown.png");
        imageView.setImage(new Image(file.toURI().toString()));
        HBox.setMargin(username, new Insets(0, 0, 0, 10));
        hBox1.getChildren().addAll(username, email, phoneNumber, imageView);
        if (customer2 != null) {
            username = new Label();
            email = new Label();
            phoneNumber = new Label();
            imageView = new ImageView();
            imageView.setFitWidth(133);
            imageView.setFitHeight(131);
            username.setText((String) customer2.get("username"));
            email.setText((String) customer2.get("email"));
            phoneNumber.setText((String) customer2.get("phoneNumber"));
//            if (customer2.getProfilePicturePath().equals("")) {
//                file = new File("src/main/resources/Icons/unknown.png");
//            } else {
//                file = new File(customer2.getProfilePicturePath());
//            }
            file = new File("src/main/resources/Icons/unknown.png");
            imageView.setImage(new Image(file.toURI().toString()));
            hBox2.getChildren().addAll(username, email, phoneNumber, imageView);
        }
        hBox.getChildren().addAll(hBox1, hBox2);
        viewBuyersVBox.getChildren().add(hBox);
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/SellerMenu.fxml"));
        return root;
    }

    public void offersButtonClicked() throws IOException {
        menuState = "SellerMenu";
        Main.setPrimaryStageScene(new Scene(OffersMenuUI.getContent()));
    }

    public void productsButtonClicked() throws IOException {
        menuState = "SellerMenu";
        Main.setPrimaryStageScene(new Scene(ProductsMenuUI.getContent()));
    }

    public void mainMenuButtonClicked() throws IOException {
        menuState = "SellerMenu";
        Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
    }

    private void showFactor(String factor) {

    }

    public void submitEditButtonClicked() throws IOException {
        phoneNumber = phoneNumberTextField.getText();
        email = emailTextField.getText();
        firstName = firstNameTextField.getText();
        lastName = lastNameTextField.getText();
        companyInformation = companyInformationTextField.getText();
        companyName = companyNameTextField.getText();
        openStage("enterPass");
    }

    public void changePic() throws IOException {
        FileChooser fileChooser = new FileChooser();
        try {
//            seller.setProfilePicturePath(fileChooser.showOpenDialog(new Stage()).getPath());
        } catch (Exception exception) {
            // do nothing
        }
        menuState = "personal";
        showSellerMenu();
    }

//    public void doneImageButton()
//    {
//        if (imagePath.getText().isEmpty())
//            throwError("Path field is empty!");
//        else{
//            seller.setProfilePicturePath(imagePath.getText());
//            ((Stage)imagePath.getScene().getWindow()).close();
//        }
//    }

    public void imagePathClicked() {
        FileChooser fileChooser = new FileChooser();
        try {
//            selectedProduct.setImagePath(fileChooser.showOpenDialog(new Stage()).getPath());
        } catch (Exception exception) {
            // do nothing
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
            hashMap.put("username", (String)seller.get("username"));
            hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
            if (hashMap.get("content").equals("error")){
                throwError((String)hashMap.get("type"));
            }
            else{
                menuState = "personal";
                ((Stage)moneyBank.getScene().getWindow()).close();
                showSellerMenu();
            }
        }
    }

    public void dechargeWallet()
    {
        if (!Pattern.matches("(\\d+)(\\.(\\d+))?", moneyBank.getText()))
            throwError("Money filed should be filled correctly!");
        else{
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", "dechargeWallet");
            hashMap.put("money", Double.parseDouble(moneyBank.getText()));
            hashMap.put("username", (String)seller.get("username"));
            hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
            if (hashMap.get("content").equals("error")){
                throwError((String)hashMap.get("type"));
            }
            else{
                menuState = "personal";
                ((Stage)moneyBank.getScene().getWindow()).close();
                showSellerMenu();
            }
        }
    }
    public void videoPathClicked() {
        FileChooser fileChooser = new FileChooser();
        try {
//            selectedProduct.setVideoPath(fileChooser.showOpenDialog(new Stage()).getPath());
        } catch (Exception exception) {
            // do nothing
        }
    }

    public void doneAdsButton() {
        try {
            if (requestedProductId.getText().isEmpty())
                throwError("ID is empty!");
            else if (!Pattern.matches("\\d+", requestedProductId.getText()))
                throwError("Invalid Format");
            else {
                ClientSellerController.addAds(Integer.parseInt(requestedProductId.getText()));
                ((Stage) requestedProductId.getScene().getWindow()).close();
            }
        } catch (Exception e) {
            throwError(e.getMessage());
        }
    }

    public void adsButtonClicked() throws IOException {
        openStage("adsRequest");
    }

    public void editPasswordSubmit() {
        if (ClientSellerController.validatePassword(passEdit.getText())) {
            try {
                ClientSellerController.editPersonalInfo("phone number", phoneNumber);
                ClientSellerController.editPersonalInfo("email", email);
                ClientSellerController.editPersonalInfo("family name", lastName);
                ClientSellerController.editPersonalInfo("first name", firstName);
                ClientSellerController.editPersonalInfo("company name", companyName);
                ClientSellerController.editPersonalInfo("company description", companyInformation);
                ((Stage) passEdit.getScene().getWindow()).close();
                menuState = "personal";
                showSellerMenu();
            } catch (Exception exception) {
                throwError(exception.getMessage());
            }
        } else
            throwError("The entered password is invalid");
    }

    public void addProductButtonClicked() throws IOException {
        selectedProduct = new HashMap<>();
        openStage("addProduct");
    }

    public void addOfferButtonClicked() throws IOException {
        selectedOffer = new HashMap<>();
        openStage("addOffer");
    }

    private void openStage(String lock) throws IOException {
        menuState = lock;
        if (lock.equalsIgnoreCase("enterPass")) {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditEnterPassSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Check for apply edit");
        } else if (lock.equalsIgnoreCase("editOffer")) {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditOffer.fxml"));
            Main.setupOtherStage(new Scene(root), "Edit Offer");
        } else if (lock.equalsIgnoreCase("editProduct")) {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditProduct.fxml"));
            Main.setupOtherStage(new Scene(root), "Edit Product");
        } else if (lock.equalsIgnoreCase("addProduct")) {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/AddProduct.fxml"));
            Main.setupOtherStage(new Scene(root), "Add New Product");
        } else if (lock.equalsIgnoreCase("addOffer")) {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/AddOffer.fxml"));
            Main.setupOtherStage(new Scene(root), "Add New Offer");
        } else if (lock.equalsIgnoreCase("changePass")) {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/ChangePasswordSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Change password");
        } else if (lock.equalsIgnoreCase("imagePath")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/ImagePathSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Image Path");
        } else if (lock.equalsIgnoreCase("adsRequest")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/RequestAds.fxml"));
            Main.setupOtherStage(new Scene(root), "Request Advertisement");
        } else if (lock.equalsIgnoreCase("sellLog")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/SellLog.fxml"));
            Main.setupOtherStage(new Scene(root), "Sell Log");
        } else if (lock.equalsIgnoreCase("endDate")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/EndDate.fxml"));
            Main.setupOtherStage(new Scene(root), "Enter Ending Time Of Auction");
        } else if (lock.equalsIgnoreCase("chargeWallet")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/ChargeWalletSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Charge Wallet");
        } else if (lock.equalsIgnoreCase("dechargeWallet")) {
            Parent root = FXMLLoader.load(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/DechargeWalletSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Decharge Wallet");
        }
    }

    private void throwError(String error) {
        errorText.setText(error);
        errorText.setVisible(true);
    }

    public static void showSellerMenu() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void filePathClicked()
    {
        FileChooser fileChooser = new FileChooser();
        try {
            Path path = Paths.get(fileChooser.showOpenDialog(new Stage()).getPath());
            System.out.println("############ " + path.getFileName().toString());
            filePath = path.getFileName().toString();
        }
        catch (Exception exception) {
            // do nothing
        }
    }

}

