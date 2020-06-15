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

    public Label balance;
    public Button personalInfoButton;
    public Button myOffersButton;
    public Button manageProductsButton;
    public Button offersButton;
    public Button productsButton;
    public Button mainMenuButton;
    public Button logoutButton;
    public Button viewSalesButton;
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
    public TextField companyNameTextField;
    public TextArea companyInformationTextField;
    public TextField initialMoneyTextField;
    public Label errorText;

    static String phoneNumber, firstName, lastName, email, companyName, companyInformation;
    ArrayList<String> filters = new ArrayList<>();
    ArrayList<Product> offerProducts = new ArrayList<>();
    static Offer selectedOffer;
    static Product selectedProduct;
    String filter = new String("");

    public PasswordField passEdit;
    public TextField offCodeTextField;
    public TextArea addressTextArea;
    public TextField oldPass;
    public PasswordField newPass;
    public PasswordField confirmationNewPass;

    public TextField startDateOffer;
    public TextField endDateOffer;
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
    public TextField productFilters;
    public ImageView plusFilterProduct;
    public ImageView minesFilterProduct;
    String availablityString;

    public Button doneAddOffer;
    public Button doneEditOffer;
    public Button doneAddProduct;
    public Button doneEditProduct;
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
        else
            setupInitial();
    }



    @FXML
    private void setupInitialEditOffer()
    {
        offerProducts = selectedOffer.getProducts();
        startDateOffer.setText(String.valueOf(selectedOffer.getStartingTime()));
        endDateOffer.setText(String.valueOf(selectedOffer.getEndingTime()));
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
                    SellerUIController.removeFilterFromOffer(seller, selectedOffer, filterTextField.getText());
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

            }
            else
                throwError("Incomplete Fields");
        });
        minesProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productIDInOffer.getText().isEmpty())
            {

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
                if
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
        imagePath.setText(selectedProduct.getImagePath());
        videoPath.setText(selectedProduct.getVideoPath());
        category.setText(selectedProduct.getCategory().getFullName());
        productName.setText(selectedProduct.getName());
        brand.setText(selectedProduct.getBrand());
        price.setText(selectedProduct.getPrice() + "");
        attributes.setText(selectedProduct.getAttributes());
        description.setText(selectedProduct.getDescription());
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
                    SellerUIController.removeFilterFromProduct(seller, selectedProduct, filterTextField.getText());
                }catch (Exception exception)
                {
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
    }

   /* public void doneAddOffer()
    {
        try {
            Offer offer = createOffer(seller);
            for (Product product: selectedOffer.getProducts())
                offer.addProduct(product);
            ArrayList<String> filters = selectedOffer.getFilters();
            for (String filter: filters)
                offer.addFilter(filter);
            SellerUIController.editOff(seller, selectedOffer, offer);
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
        }catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }
    */
    public void doneAddProduct()
    {
        try {
            SellerUIController.addProduct(seller, createProduct(seller));
        }catch (Exception exception)
        {
            throwError(exception.getMessage());
        }
    }

    public void doneEditProduct()
    {
        try {
            SellerUIController.editProduct(seller, selectedProduct, createProduct(seller));
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

    private void getOfferProducts(Seller seller, Offer offer, Offer changeOffer) {
        String input;

    }

    private Offer createOffer(Seller seller) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");

        String input = startDateOffer.getText();
        Date startingTime, endingTime;
        try {
            startingTime = dateFormat.parse(input);
        }
        catch (ParseException exception) {
            throw new Exception("Invalid date format!");
        }

        input = endDateOffer.getText();
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
        for (Offer offer: seller.getOffers())
        showEachOfferInHBox(offer);
    }


    @FXML
    private void setupInitialBuyers()
    {
         for (String string: seller.getBuyers())
             showEachBuyerInHBox(string);
    }

    @FXML
    private void setupInitial()
    {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        logoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
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
        for (Product product: seller.getProducts())
            showEachProductInHBox(product);
    }

    @FXML
    private void setupInitialPersonalMenu()
    {
        //profile = new ImageView(new Image(seller.getProfilePicturePath()));
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
            if (seller.validatePassword(oldPass.getText()) && newPass.getText().equals(confirmationNewPass.getText()))
                SellerUIController.editPersonalInfo(seller, "password", newPass.getText());
        } catch (Exception exception) {
            throwError(exception.getMessage());
        }
        finally {
            ((Stage)oldPass.getScene().getWindow()).close();
        }
    }

    private void showEachOfferInHBox(Offer offer)
    {
        HBox hBox = new HBox();
        Button edit = new Button("Edit");
        edit.setPrefWidth(78);
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedOffer = offer;
                openStage("editOffer");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();

        hBox.getChildren().addAll(vBox1, vBox2, vBox3, vBox4, vBox5, vBox6, edit);
        hBox.setMinHeight(100);     hBox.setMaxHeight(100);

        Label startingDate = new Label("Starting Date");
        Label endingDate = new Label("Ending Date");
        Label offPercent = new Label("Off Percent");
        Label offerStatus = new Label("Offer Status");
        Label filters = new Label("Filters");
        Label products = new Label("Products");

        Label startingDate2 = new Label();
        Label endingDate2 = new Label();
        Label offPercent2 = new Label();
        Label offerStatus2 = new Label();
        TextArea filters2 = new TextArea();
        TextArea products2 = new TextArea();

        startingDate2.setText(offer.getStartingTime() + "");
        endingDate2.setText(offer.getEndingTime() + "");
        offPercent2.setText(offer.getOffPercent() + "");
        offerStatus2.setText(offer.getOfferStatus() + "");
        for (String filter: offer.getFilters())
            filters2.setText(filters2.getText() + "\n-" + filter);
        for (Product product: offer.getProducts())
            products2.setText(products2.getText() + "\n-" + product.getName() + "-----" + product.getBrand());
        vBox1.getChildren().addAll(startingDate, startingDate2);
        vBox2.getChildren().addAll(endingDate, endingDate2);
        vBox3.getChildren().addAll(offPercent, offPercent2);
        vBox4.getChildren().addAll(offerStatus, offerStatus2);
        vBox5.getChildren().addAll(filters, filters2);
        vBox6.getChildren().addAll(products, products2);

        vBox1.setAlignment(Pos.TOP_CENTER);
        vBox1.setSpacing(20);
        vBox2.setAlignment(Pos.TOP_CENTER);
        vBox2.setSpacing(20);
        vBox3.setAlignment(Pos.TOP_CENTER);
        vBox3.setSpacing(20);
        vBox4.setAlignment(Pos.TOP_CENTER);
        vBox4.setSpacing(20);
        vBox5.setAlignment(Pos.TOP_CENTER);
        vBox6.setAlignment(Pos.TOP_CENTER);

        vBox5.setMinWidth(157);     vBox5.setMaxWidth(157);
        vBox6.setMaxWidth(172);     vBox6.setMinWidth(172);
        filters2.setEditable(false);
        products2.setEditable(false);
        HBox.setMargin(edit, new Insets(40, 0, 0, 0));
        myOffersVBox.getChildren().add(hBox);
    }

    private void showEachProductInHBox(Product product)
    {
        HBox hBox = new HBox();
        Button edit = new Button("Edit");
        Image productImage = new Image(product.getImagePath());
        ImageView productView = new ImageView(productImage);
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedProduct = product;
                openStage("editProduct");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        productView.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
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

        productView.setFitHeight(138);      productView.setFitWidth(182);
        HBox hBox5 = new HBox();
        hBox5.getChildren().addAll(edit);
        VBox vBox3 = new VBox();
        vBox3.setAlignment(Pos.TOP_CENTER);
        vBox3.getChildren().addAll(productView, hBox5);
        hBox.getChildren().addAll(vBox3);
        manageProductsVBox.getChildren().addAll(hBox);
    }

    private void showEachFactorInHBox(SellLogItem sellLog)
    {
        HBox hBox = new HBox();
        hBox.setMaxHeight(100);     hBox.setMinHeight(100);
        Label dateText = new Label();
        Label customerText = new Label();
        TextArea productsText = new TextArea();
        Label incomeText = new Label();
        Label offValueText = new Label();
        Label sendStatusText = new Label();
        dateText.setText(String.valueOf(sellLog.getDate()));
        incomeText.setText(String.valueOf(sellLog.getIncomeValue()));
        offValueText.setText(String.valueOf(sellLog.getOffValue()));
        if (sellLog.isSendStatus())
            sendStatusText.setText("Arrived");
        else
            sendStatusText.setText("Not Arrived");
        customerText.setText(sellLog.getCustomerName());
        for(Product product: sellLog.getProducts())
            productsText.setText(productsText.getText() + "\n-" + product.getName() + "-----" + product.getBrand());
        VBox vBox1 = new VBox(), vBox2 = new VBox(), vBox3 = new VBox(), vBox4 = new VBox(), vBox5 = new VBox(), vBox6 = new VBox();
        vBox1.getChildren().add(dateText);
        vBox2.getChildren().add(customerText);
        vBox3.getChildren().add(incomeText);
        vBox4.getChildren().add(offValueText);
        vBox5.getChildren().add(sendStatusText);
        vBox6.getChildren().add(productsText);
        vBox6.setMaxWidth(172);     vBox6.setMinWidth(172);
        productsText.setEditable(false);
        hBox.getChildren().addAll(vBox1, vBox2, vBox3, vBox4, vBox5, vBox6);
    }

    private void showEachBuyerInHBox(String string)
    {
        Label label = new Label(string);
        viewBuyersVBox.getChildren().add(label);
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/SellerMenu.fxml"));
        return root;
    }

    public void offersButtonClicked()
    {
        menuState = "SellerMenu";
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
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/EditEnterPass.fxml"));
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
            Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/ChangePassword.fxml"));
            Main.setupOtherStage(new Scene(root), "Change password");
        }
    }

    private void throwError(String error)
    {
        errorText.setText(error);
    }

    public static void showSellerMenu() {
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
