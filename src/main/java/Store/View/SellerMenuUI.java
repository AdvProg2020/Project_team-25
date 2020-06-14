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
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
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

    public AnchorPane anchorPane;
    public GridPane gridPane;

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
    public TextField productNameInOffer;
    public TextField productBrandInOffer;
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

    ArrayList<Product> offerProducts;
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
            if (!productNameInOffer.getText().isEmpty() && !productBrandInOffer.getText().isEmpty())
            {

            }
            else
                throwError("Incomplete Fields");
        });
        minesProductOffer.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!productNameInOffer.getText().isEmpty() && !productBrandInOffer.getText().isEmpty())
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
            if (!productNameInOffer.getText().isEmpty() && !productBrandInOffer.getText().isEmpty())
            {

            }
            else
                throwError("Incomplete Fields");
        });
    }

    private ArrayList<Integer> productsAddedToOffer()
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

        Product product = new Product(CheckingStatus.CREATION, parent, name, seller, brandString, price, availabilityBool, description);
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
        if (seller.getOffers().size() > 6)
            configureGridPane(seller.getOffers().size());
        showOffersInGridPane(seller.getOffers());
    }


    @FXML
    private void setupInitialBuyers()
    {
     //create table shows buyers
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
    }

    @FXML
    private void setupInitialManageProducts()
    {
        if (seller.getProducts().size() > 4)
            configureGridPane(seller.getProducts().size());
        showProductsInGridPane(seller.getProducts());
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
        if (seller.getSellLog().size() > 2)
            configureGridPane(seller.getSellLog().size());
        for (int i = 0; i < gridPane.getRowCount(); i++)
            showFactorInGridPane(i, seller.getSellLog().get(i));
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

    private void sliceButton(Button button, String string)
    {

    }

    private void showProductsInGridPane(ArrayList<Product> products)
    {
        for (int i = 0; i < (products.size() + 1) / 2; i++)
            for (int j = 0; j < 2; j++)
                showEachProductInGridPane(i, j, products.get(2 * i + j));
    }

    private void showOffersInGridPane(ArrayList<Offer> offers)
    {
        int i = 0;
        for (Offer offer: offers)
            showEachOfferInGridPane(offers.get(i), i++);
    }

    private void showEachOfferInGridPane(Offer offer, int i)
    {
        Button addFilter = new Button("Add Filter");
        Button removeFilter = new Button("Remove Filter");
        Button edit = new Button("Edit");
        Label offerNameDate = new Label();
        offerNameDate.setText(String.valueOf(offer.getStartingTime()) + "\n" + String.valueOf(offer.getEndingTime()));
        Label products = new Label();
        products.setText(String.valueOf(offer.getProducts()));
        Label offPercent = new Label();
        products.setText(offer.getOffPercent() + "%");
        addFilter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedOffer = offer;
                openStage("addFilterOffer");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        removeFilter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedOffer = offer;
                openStage("removeFilterOffer");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedOffer = offer;
                openStage("editOffer");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        gridPane.add(offerNameDate, 1, i);
        gridPane.add(products, 3, i);
        gridPane.add(offPercent, 2, i);
        gridPane.add(addFilter, 0, i);
        gridPane.add(removeFilter, 0, i);
        gridPane.add(edit, 0, i);
        configurePositionsInOfferGridPane(addFilter, edit, removeFilter, offerNameDate, offPercent, products);
    }

    private void showEachProductInGridPane(int i, int j, Product product)
    {
        Button addFilter = new Button("Add Filter");
        Button removeFilter = new Button("Remove Filter");
        Button edit = new Button("Edit");
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
        addFilter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedProduct = product;
                openStage("addFilterProduct");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        removeFilter.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            try {
                selectedProduct = product;
                openStage("removeFilterProduct");
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
        gridPane.add(productView, j, i);
        gridPane.add(info, j, i);
        gridPane.add(addFilter, j, i);
        gridPane.add(removeFilter, j, i);
        gridPane.add(edit, j, i);
        configurePositionsInManageProductGridPane(productView, info, addFilter, edit, removeFilter);
    }

    private void showFactorInGridPane(int i, SellLogItem sellLog)
    {
        Label dateText = new Label();
        Label customerText = new Label();
        Label productsText = new Label();
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
        productsText.setText(String.valueOf(sellLog.getProducts()));
        gridPane.add(dateText, 0, i);
        gridPane.add(incomeText, 0, i);
        gridPane.add(offValueText, 0, i);
        gridPane.add(customerText, 0, i);
        gridPane.add(sendStatusText, 0, i);
        gridPane.add(productsText, 0, i);
        configurePositionsInFactorGridPane(dateText, customerText, productsText, incomeText, offValueText, sendStatusText);
    }

    private void configureGridPane(int size)
    {
        RowConstraints newRow;
        if (menuState.equalsIgnoreCase("SellerMenuPersonal")) {
            for (int i = 0; i < (size - 2); i++) {
                anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 100);
                gridPane.setPrefHeight(gridPane.getPrefHeight() + 100);
                newRow = new RowConstraints();
                newRow.setMinHeight(100);
                newRow.setMaxHeight(100);
                gridPane.getRowConstraints().add(newRow);
            }
        }
        else if (menuState.equalsIgnoreCase("SellerMenuManageProducts")) {
            for (int i = 0; i < (size - 1) / 2; i++) {
                anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 250);
                gridPane.setPrefHeight(gridPane.getPrefHeight() + 250);
                newRow = new RowConstraints();
                newRow.setMinHeight(250);
                newRow.setMaxHeight(250);
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
