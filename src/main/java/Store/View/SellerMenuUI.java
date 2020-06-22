package Store.View;

import Store.Controller.*;
import Store.InputManager;
import Store.Main;
import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import Store.Model.Log.BuyLogItem;
import Store.Model.Log.SellLogItem;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SellerMenuUI implements Initializable {

    private static Seller seller;
    private static String menuState = "SellerMenu";

    public VBox manageProductsVBox, viewSalesVBox, myOffersVBox, viewBuyersVBox;

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
    public Button logoutButton;
    public Label loggedInStatusText;
    public ComboBox myOffersSortBy;
    public ComboBox manageProductsSortBy;

    public ImageView profile;
    public Button submitEditButton;
    public Button changePassButton;
    public TextField usernameTextField;
    public PasswordField passwordField;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
    public TextField companyNameTextField;
    public TextArea companyInformationTextField;
    public Label errorText;

    static String phoneNumber, firstName, lastName, email, companyName, companyInformation;
    ArrayList<String> filters = new ArrayList<>();
    ArrayList<Product> offerProducts = new ArrayList<>();
    static Offer selectedOffer;
    static Product selectedProduct;
    String filter = new String("");

    public PasswordField passEdit;
    public TextField oldPass;
    public PasswordField newPass;
    public PasswordField confirmationNewPass;

    public TextField startDayOffer;
    public TextField startMonthOffer;
    public TextField startYearOffer;
    public TextField endDayOffer;
    public TextField endMonthOffer;
    public TextField endYearOffer;
    public TextField offPercentOffer;
    public TextField productIDInOffer;
    public ImageView plusFilterOffer;
    public ImageView minesFilterOffer;
    public ImageView plusProductOffer;
    public ImageView minesProductOffer;

    public TextField filterTextField;

    public TextField imagePath;
    public TextField videoPath;
    public TextField category;
    public TextField productName;
    public TextField brand;
    public TextField price;
    public ComboBox availablity;
    public TextField attributes;
    public TextField description;
    public ImageView plusFilterProduct;
    public ImageView minesFilterProduct;
    String availablityString;

    static int buyerscount = 0;

    String currentSortOffer = new String("time of starting");
    String currentSortProduct = new String("visited");
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seller = (Seller) MainMenuUIController.currentUser;
        if (menuState.equalsIgnoreCase("changePass"))
        {

        }
        else if (menuState.equalsIgnoreCase("addProduct"))
        {
            setupInitialAddProduct();
        }
        else if(menuState.equalsIgnoreCase("addOffer"))
        {
            setupInitialAddOffer();
        }
        else if (menuState.equalsIgnoreCase("enterPass"))
        {

        }
        else if (menuState.equalsIgnoreCase("editOffer"))
        {
            setupInitialEditOffer();
        }
        else if (menuState.equalsIgnoreCase("editProduct"))
        {
            setupInitialEditProduct();
        }
        else {
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

    private void resetAll()
    {
        for (int i = 1; i < manageProductsVBox.getChildren().size();)
            manageProductsVBox.getChildren().remove(i);
        for (int i = 1; i < myOffersVBox.getChildren().size();)
            myOffersVBox.getChildren().remove(i);
        for (int i = 1; i < viewSalesVBox.getChildren().size();)
            viewSalesVBox.getChildren().remove(i);
    }

    @FXML
    private void setupInitialEditOffer()
    {
        offerProducts = selectedOffer.getProducts();
        startDayOffer.setText(selectedOffer.getStartingTime().getDay() + "");
        startMonthOffer.setText(selectedOffer.getStartingTime().getMonth() + "");
        startYearOffer.setText(selectedOffer.getStartingTime().getYear() + "");
        endDayOffer.setText(selectedOffer.getEndingTime().getDay() + "");
        endMonthOffer.setText(selectedOffer.getEndingTime().getMonth() + "");
        endYearOffer.setText(selectedOffer.getEndingTime().getYear() + "");
        offPercentOffer.setText(String.valueOf(selectedOffer.getOffPercent()));
        plusFilterOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty())
            {
                try {
                    SellerUIController.addFilterToOffer(seller, selectedOffer, filterTextField.getText());
                }catch (Exception exception)
                {
                    throwError(exception.getMessage());
                }
            }
            else
                throwError("Filter Field is Empty");
        });
        minesFilterOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty())
            {
                try {
                    SellerUIController.removeFilterFromOffer(seller, selectedOffer.getOffID() + "", filterTextField.getText());
                }catch (Exception exception)
                {
                    throwError(exception.getMessage());
                }
            }
            else
                throwError("Filter Field is Empty");
        });
        plusProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty())
            {
                if (!StringUtils.isNumeric(productIDInOffer.getText()) || !SellerUIController.isProductFromThisSeller(seller, Product.getProductByID(Integer.parseInt(productIDInOffer.getText())))) {
                    throwError("Please enter a valid ID");
                }
                Product product = Product.getProductByID(Integer.parseInt(productIDInOffer.getText()));
                if (selectedOffer.containsProduct(product)) {
                    throwError("Already added this product");
                }
                else if ((selectedOffer == null || !selectedOffer.getProducts().contains(product)) && Offer.getOfferOfProduct(product) != null) {
                    throwError("This product is already in another offer!");
                }
                else {
                    selectedOffer.addProduct(product);
                    throwError("Product added to list");
                }
            }
            else
                throwError("Incomplete Fields");
        });
        minesProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty())
            {
                if (!StringUtils.isNumeric(productIDInOffer.getText()) || !SellerUIController.isProductFromThisSeller(seller, Product.getProductByID(Integer.parseInt(productIDInOffer.getText())))) {
                    throwError("Please enter a valid ID");
                }
                Product product = Product.getProductByID(Integer.parseInt(productIDInOffer.getText()));
                if (!selectedOffer.containsProduct(product)) {
                    throwError("Doesn't exist in offer");
                }
                else {
                    selectedOffer.removeProduct(product);
                    throwError("Product removed from list");
                }
            }
            else
                throwError("Incomplete Fields");
        });
    }

    @FXML
    private void setupInitialAddOffer()
    {
        plusFilterOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty())
            {
                try {
                    SellerUIController.addFilterToOffer(seller, selectedOffer, filterTextField.getText());
                }catch (Exception exception)
                {
                    throwError(exception.getMessage());
                }
            }
            else
                throwError("Filter Field is Empty");
        });
        plusProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty())
            {
                if (!StringUtils.isNumeric(productIDInOffer.getText()) || !SellerController.isProductFromThisSeller(seller, Product.getProductByID(Integer.parseInt(productIDInOffer.getText())))) {
                    throwError("Please enter a valid ID");
                }
                Product product = Product.getProductByID(Integer.parseInt(productIDInOffer.getText()));
                if (selectedOffer.containsProduct(product)) {
                    throwError("Already added this product");
                }
                else if ((selectedOffer == null || !selectedOffer.getProducts().contains(product)) && Offer.getOfferOfProduct(product) != null) {
                    throwError("This product is already in another offer!");
                }
                else {
                    selectedOffer.addProduct(product);
                    throwError("Product added to list");
                }
            }
            else
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
    private void setupInitialEditProduct()
    {
        /*imagePath.setText(selectedProduct.getImagePath());
        videoPath.setText(selectedProduct.getVideoPath());
        */
        if (selectedProduct.getCategory() != null)
            category.setText(selectedProduct.getCategory().getId() + "");
        else
            category.setText("");
        productName.setText(selectedProduct.getName());
        brand.setText(selectedProduct.getBrand());
        price.setText(selectedProduct.getPrice() + "");
        attributes.setText(selectedProduct.getAttributes());
        description.setText(selectedProduct.getDescription());
        if (selectedProduct.getAvailablity())
            availablityString = "available";
        else
            availablityString = "inavailable";
        availablity.setItems(FXCollections.observableArrayList("Available", "Inavailable"));
        availablity.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                availablityString = newValue.toString();
            }
        });
        plusFilterProduct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty())
            {
                try {
                    SellerUIController.addFilterToProduct(seller, selectedProduct, filterTextField.getText());
                }catch (Exception exception)
                {
                    throwError(exception.getMessage());
                }
            }
            else
                throwError("Filter Field is Empty");
        });
        minesFilterProduct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty())
            {
                try {
                    SellerUIController.removeFilterFromProduct(seller, selectedProduct.getProductID() + "", filterTextField.getText());
                }catch (Exception exception) {
                    throwError(exception.getMessage());
                }
            }
            else
                throwError("Filter Field is Empty");
        });
    }

    @FXML
    private void setupInitialAddProduct()
    {
        availablityString = "available";
        plusFilterProduct.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!filterTextField.getText().isEmpty())
            {
                try {
                    SellerUIController.addFilterToProduct(seller, selectedProduct, filterTextField.getText());
                }catch (Exception exception) {
                    throwError(exception.getMessage());
                }
            }
            else
                throwError("Filter Field is Empty");
        });
    }

    public void doneAddOffer()
    {
        try {
            Offer offer = createOffer(seller);
            for (Product product: selectedOffer.getProducts())
                offer.addProduct(product);
            ArrayList<String> filters = selectedOffer.getFilters();
            for (String filter: filters)
                offer.addFilter(filter);
            SellerUIController.addOff(seller, offer);
            ((Stage) filterTextField.getScene().getWindow()).close();
            menuState = "offer";
            showSellerMenu();
        }catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }

    public void doneEditOffer()
    {
        try {
            Offer offer = createOffer(seller);
            for (Product product: selectedOffer.getProducts())
                offer.addProduct(product);
            ArrayList<String> filters = selectedOffer.getFilters();
            for (String filter: filters)
                offer.addFilter(filter);
            SellerUIController.editOff(seller, selectedOffer, offer);
            ((Stage) filterTextField.getScene().getWindow()).close();
            menuState = "offer";
            showSellerMenu();
        }catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }
    public void doneAddProduct()
    {
        try {
            SellerUIController.addProduct(seller, createProduct(seller));
            ((Stage) filterTextField.getScene().getWindow()).close();
            menuState = "product";
            showSellerMenu();
        }catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }

    public void doneEditProduct()
    {
        try {
            SellerUIController.editProduct(seller, selectedProduct, createProduct(seller));
            ((Stage) filterTextField.getScene().getWindow()).close();
            menuState = "product";
            showSellerMenu();
        }catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }

    private Product createProduct(Seller seller) throws Exception {
        String attribute = category.getText();
        if (!StringUtils.isNumeric(attribute) || !Category.hasCategoryWithId(Integer.parseInt(attribute))) {
            throw new Exception("Invalid category ID!");
        }
        Category parent = Category.getCategoryById(Integer.parseInt(attribute));
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
        ArrayList<String> filters = selectedProduct.getFilters();
        String descriptionString = description.getText();
        Product product = new Product(CheckingStatus.CREATION, parent, name, seller, brandString, price, availabilityBool, descriptionString);
        for (String filterToAdd : filters) {
            product.addFilter(filterToAdd);
        }
        return product;
    }

    private Offer createOffer(Seller seller) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");

        String input = startDayOffer.getText() + "-" + startMonthOffer.getText() + "-" + (Integer.parseInt(startYearOffer.getText()) + 1900);
        Date startingTime, endingTime;
        try {
            startingTime = dateFormat.parse(input);
        }
        catch (ParseException exception) {
            throw new Exception("Invalid date format!");
        }

        input = endDayOffer.getText() + "-" + endMonthOffer.getText() + "-" + (Integer.parseInt(endYearOffer.getText()) + 1900);
        try {
            endingTime = dateFormat.parse(input);
        }
        catch (ParseException exception) {
            throw new Exception("Invalid date format!");
        }

        input = offPercentOffer.getText();
        if (!input.matches("\\d+(\\.\\d+)?")) {
            throw new Exception("Invalid percentage!");
        }
        double offPercentage = Double.parseDouble(input);

        Offer offer = new Offer(seller, CheckingStatus.CREATION, offPercentage);
        offer.setStartingTime(startingTime);
        offer.setEndingTime(endingTime);

        return offer;
    }

    @FXML
    private void setupInitialMyOffers()
    {
        for (Offer offer: SellerUIController.getOffersOfThisSeller(seller))
            showEachOfferInHBox(offer);
        myOffersSortBy.setItems(FXCollections.observableArrayList("time of starting", "time of ending"));
        myOffersSortBy.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentSortOffer = newValue.toString();
            myOffersVBox.getChildren().clear();
            for (Offer offer : OffersController.sort(currentSortOffer, seller.getOffers())) {
                showEachOfferInHBox(offer);
            }
        });
    }


    @FXML
    private void setupInitialBuyers()
    {
        ArrayList<String> buyers = new ArrayList<>();
        String string2;
        for (String string: seller.getBuyers())
            buyers.add(string);
        SellerUIController.makeBuyersUnique(buyers);
        for (int i = 1; i < seller.getBuyers().size(); i+=2)
            showEachBuyerInHBox((Customer)Customer.getUserByUsername(seller.getBuyers().get(i-1)), (Customer)Customer.getUserByUsername(seller.getBuyers().get(i)));
        if (seller.getBuyers().size() % 2 == 1)
            showEachBuyerInHBox((Customer)Customer.getUserByUsername(seller.getBuyers().get(seller.getBuyers().size() - 1)), null);
    }

    @FXML
    private void setupInitial()
    {
        product.setOnSelectionChanged(e ->  setupInitialPersonalMenu());
        offer.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        viewBuyers.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        viewSales.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        personal.setOnSelectionChanged(e -> setupInitialPersonalMenu());
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        logoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                MainMenuUIController.setCurrentUser(MainMenuUIController.guest);
                mainMenuButtonClicked();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        balance.textProperty().bind(new SimpleStringProperty(String.valueOf(seller.getMoney())));

        setupInitialPersonalMenu();
        setupInitialBuyers();
        setupInitialManageProducts();
        setupInitialMyOffers();
        setupInitialSalesHistory();
    }

    @FXML
    private void setupInitialManageProducts()
    {
        for (Product product: seller.getProducts()) {
            System.out.println(product);
            showEachProductInHBox(product);
        }
        manageProductsSortBy.setItems(FXCollections.observableArrayList("rating", "price", "visit", "lexicographical"));
        manageProductsSortBy.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentSortProduct = newValue.toString();
            manageProductsVBox.getChildren().clear();
            for (Product product : ProductsController.sort(currentSortProduct, seller.getProducts())) {
                showEachProductInHBox(product);
            }
        });
    }

    @FXML
    private void setupInitialPersonalMenu()
    {
        try{
            profile.setImage(new Image(seller.getProfilePicturePath()));
        }catch (Exception exception) {

        }
        emailTextField.setText(seller.getEmail());
        firstNameTextField.setText(seller.getName());
        lastNameTextField.setText(seller.getFamilyName());
        phoneNumberTextField.setText(seller.getPhoneNumber());
        usernameTextField.setText(seller.getUsername());
        usernameTextField.setEditable(false);
        companyNameTextField.setText(seller.getCompanyName());
        companyInformationTextField.setText(seller.getCompanyDescription());
    }
    @FXML
    private void setupInitialSalesHistory()
    {
        for (SellLogItem sellLogItem: seller.getSellLog())
            showEachFactorInHBox(sellLogItem);
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
            if (seller.validatePassword(oldPass.getText()) && newPass.getText().equals(confirmationNewPass.getText())) {
                SellerUIController.editPersonalInfo(seller, "password", newPass.getText());
                ((Stage) oldPass.getScene().getWindow()).close();
                menuState = "personal";
                showSellerMenu();
            }
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
    }

    private void showEachOfferInHBox(Offer offer)
    {
        HBox hBox = new HBox();
        Button edit = new Button("Edit");
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                offerProducts.clear();
                selectedOffer = offer;
                for (Product product: offer.getProducts())
                    offerProducts.add(product);
                openStage("editOffer");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        ArrayList<VBox> vBoxes = new ArrayList<>();
        for (int j = 0; j < 7; j++) {
            vBoxes.add(new VBox());
            vBoxes.get(j).setAlignment(Pos.CENTER);
            vBoxes.get(j).setPrefWidth(100);
        }
        hBox.setSpacing(40);
        hBox.setMaxHeight(70);      hBox.setMinHeight(70);
        vBoxes.get(0).setPrefWidth(62);
        Label id = new Label(), offPercent = new Label(), startDate = new Label(), endDate = new Label();
        id.setText(offer.getOffID() + "");
        offPercent.setText(offer.getOffPercent() + "");
        startDate.setText(offer.getStartingTime().getYear() + "/" + offer.getStartingTime().getMonth() + "/" + offer.getStartingTime().getDay());
        endDate.setText(offer.getEndingTime().getYear() + "/" + offer.getEndingTime().getMonth() + "/" + offer.getEndingTime().getDay());
        TextArea filters = new TextArea(), products = new TextArea();
        filters.setEditable(false);         products.setEditable(false);
        for (String filter: offer.getFilters())
            filters.setText(filters.getText() + "-" + filter + "\n");
        for (Product product: offer.getProducts())
            products.setText(products.getText() + "-" + product.getName() + "------" + product.getBrand() + "\n");
        filters.setMaxWidth(100);       products.setMaxWidth(100);
        vBoxes.get(0).getChildren().addAll(id);
        vBoxes.get(1).getChildren().add(offPercent);
        vBoxes.get(2).getChildren().add(startDate);
        vBoxes.get(3).getChildren().add(endDate);
        vBoxes.get(4).getChildren().add(filters);
        vBoxes.get(5).getChildren().add(products);
        vBoxes.get(6).getChildren().add(edit);
        for (int j = 0; j < 7; j++)
            hBox.getChildren().add(vBoxes.get(j));
        myOffersVBox.getChildren().add(hBox);
    }

    private void showEachProductInHBox(Product product)
    {
        HBox hBox = new HBox();
        /*hBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {

            }
        });*/
        Button edit = new Button("Edit");
        Button remove = new Button("Remove");
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedProduct = product;
                openStage("editProduct");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        remove.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            SellerUIController.removeProduct(seller, product);
            menuState = "product";
        });
        /*productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });*/
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
        if (product.getCategory() != null)
            category.setText(product.getCategory() + "");
        else
            category.setText("");
        brand.setText(product.getBrand());
        average.setText(product.getAverageRating() + "");
        productStatus.setText(product.getProductStatus() + "");
        price.setText(product.getPrice() + "");
        TextArea filters = new TextArea(), discription = new TextArea();
        filters.setEditable(false);     discription.setEditable(false);
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
        vBoxes.get(10).getChildren().addAll(edit, remove);
        vBoxes.get(10).setPrefWidth(95);
        filters.setMaxWidth(99);
        discription.setMaxWidth(99);
        for (int j = 0; j < 11; j++)
            hBox.getChildren().add(vBoxes.get(j));
        manageProductsVBox.getChildren().addAll(hBox);
    }

    private void showEachFactorInHBox(SellLogItem sellLog)
    {
        HBox hBox = new HBox();
        hBox.setMaxHeight(100);     hBox.setMinHeight(100);
        hBox.setSpacing(20);
        Label dateText = new Label();
        Label customerText = new Label();
        TextArea productsText = new TextArea();
        Label incomeText = new Label();
        Label offValueText = new Label();
        Label sendStatusText = new Label();
        dateText.setText((sellLog.getDate().getYear() + 1900) + "/" + sellLog.getDate().getMonth() + "/" + sellLog.getDate().getDay());
        incomeText.setText(String.valueOf(sellLog.getIncomeValue()));
        offValueText.setText(String.valueOf(sellLog.getOffValue()));
        if (sellLog.isSendStatus())
            sendStatusText.setText("Arrived");
        else
            sendStatusText.setText("Not Arrived");
        customerText.setText(sellLog.getCustomerName());
        for(Product product: sellLog.getProducts())
            productsText.setText(productsText.getText() + "\n-" + product.getName() + "-----" + product.getBrand());
        productsText.setEditable(false);
        VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();
        vBox1.getChildren().add(dateText);
        vBox2.getChildren().add(customerText);
        vBox3.getChildren().add(incomeText);
        vBox4.getChildren().add(offValueText);
        vBox5.getChildren().add(sendStatusText);
        vBox6.getChildren().add(productsText);
        vBox1.setPrefWidth(100);    vBox1.setAlignment(Pos.CENTER);
        vBox2.setPrefWidth(100);    vBox2.setAlignment(Pos.CENTER);
        vBox3.setPrefWidth(100);    vBox3.setAlignment(Pos.CENTER);
        vBox4.setPrefWidth(100);    vBox4.setAlignment(Pos.CENTER);
        vBox5.setPrefWidth(100);    vBox5.setAlignment(Pos.CENTER);
        vBox6.setPrefWidth(240);    vBox6.setAlignment(Pos.CENTER);
        productsText.setEditable(false);
        hBox.getChildren().addAll(vBox3, vBox4, vBox2, vBox5, vBox1, vBox6);
        viewSalesVBox.getChildren().add(hBox);
    }

    private void showEachBuyerInHBox(Customer customer1, Customer customer2)
    {
        HBox hBox = new HBox();
        hBox.setMinHeight(131);     hBox.setMaxHeight(131);
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        hBox1.setPrefWidth(475);        hBox2.setPrefWidth(475);
        hBox1.setSpacing(20);           hBox2.setSpacing(20);
        Label username = new Label(), email = new Label(), phoneNumber = new Label();
        ImageView imageView = new ImageView();
        hBox1.setAlignment(Pos.CENTER_LEFT);        hBox2.setAlignment(Pos.CENTER_LEFT);
        imageView.setFitWidth(133);     imageView.setFitHeight(131);
        username.setText(customer1.getUsername());
        email.setText(customer1.getEmail());
        phoneNumber.setText(customer1.getPhoneNumber());
        //imageView.setImage();
        HBox.setMargin(username, new Insets(0, 0, 0, 10));
        hBox1.getChildren().addAll(username, email, phoneNumber, imageView);
        if (customer2 != null)
        {
            username.setText(customer2.getUsername());
            email.setText(customer2.getEmail());
            phoneNumber.setText(customer2.getPhoneNumber());
            //imageView.setImage();
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

    private void showFactor(String factor)
    {

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

    public void editPasswordSubmit()
    {
        if (seller.validatePassword(passEdit.getText()))
        {
            try {
                SellerUIController.editPersonalInfo(seller, "phone number", phoneNumber);
                SellerUIController.editPersonalInfo(seller, "email", email);
                SellerUIController.editPersonalInfo(seller, "family name", lastName);
                SellerUIController.editPersonalInfo(seller, "first name", firstName);
                SellerUIController.editPersonalInfo(seller, "company name", companyName);
                SellerUIController.editPersonalInfo(seller, "company description", companyInformation);
                ((Stage)passEdit.getScene().getWindow()).close();
                menuState = "personal";
                showSellerMenu();
            }catch (Exception exception)
            {
                throwError(exception.getMessage());
            }
        }
        else
            throwError("The entered password is invalid");
    }

    public void addProductButtonClicked() throws IOException {
        selectedProduct = new Product();
        openStage("addProduct");
    }

    public void addOfferButtonClicked() throws IOException {
        selectedOffer = new Offer();
        openStage("addOffer");
    }

    private void openStage(String lock) throws IOException {
        menuState = lock;
        if (lock.equalsIgnoreCase("enterPass"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditEnterPassSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Check for apply edit");
        }
        else if (lock.equalsIgnoreCase("editOffer"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditOffer.fxml"));
            Main.setupOtherStage(new Scene(root), "Edit Offer");
        }
        else if (lock.equalsIgnoreCase("editProduct"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditProduct.fxml"));
            Main.setupOtherStage(new Scene(root), "Edit Product");
        }
        else if (lock.equalsIgnoreCase("addProduct"))
        {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/AddProduct.fxml"));
            Main.setupOtherStage(new Scene(root), "Add New Product");
        }
        else if (lock.equalsIgnoreCase("addOffer"))
        {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/AddOffer.fxml"));
            Main.setupOtherStage(new Scene(root), "Add New Offer");
        }
        else if (lock.equalsIgnoreCase("changePass"))
        {
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/ChangePasswordSeller.fxml"));
            Main.setupOtherStage(new Scene(root), "Change password");
        }
    }

    private void throwError(String error)
    {
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

}
