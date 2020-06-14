package Store.View;

import Store.Controller.CustomerController;
import Store.Controller.MainMenuUIController;
import Store.Controller.ProductController;
import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Main;
import Store.Model.*;
import Store.View.AdditionalUtils.NodeGestures;
import Store.View.AdditionalUtils.PannableCanvas;
import Store.View.AdditionalUtils.SceneGestures;
import Store.View.AdditionalUtils.VideoPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.codehaus.plexus.util.StringUtils;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;

public class ProductMenuUI {

    private static Product productToShow = null;
    private static final double SCALE_DELTA = 1.1;

    private static String sellerName = "";

    public VBox imageAndVideoVBox;
    public VideoPlayer videoPlayer;

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;

    public Label loggedInStatusText;
    public Button signUpButton;
    public Button loginLogoutButton;

    public Label nameLabel;
    public Label brandLabel;
    public Label descriptionLabel;
    public Label priceLabel;
    public Label categoryLabel;
    public Label averageRatingLabel;
    public Label dateOfOfferLabel;
    public Label productStatusLabel;
    public Label filtersLabel;

    public Button ratingStar1;
    public ImageView activeStar;
    public Button ratingStar2;
    public ImageView activeStar1;
    public Button ratingStar3;
    public ImageView activeStar2;
    public Button ratingStar4;
    public ImageView activeStar3;
    public Button ratingStar5;
    public ImageView activeStar4;
    int currentRating = 0;
    public Button rateProductButton;

    public TextField commentTitleTextField;
    public TextArea commentContentTextArea;
    public Button submitCommentButton;
    public Label errorMessageLabel;

    public Button addToCartButton;

    public HBox sellersHBox;
    public ImageView productImageView;
    public Label offPercentageLabel;

    public VBox commentSectionVBox;

    public TextField compareProductIDTextField;
    public Button compareButton;

    private ArrayList<Button> sellerButtons = new ArrayList<Button>();


    public ProductMenuUI() {

    }

    public static Parent getContent(Product product) throws IOException {
        productToShow = product;
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/ProductMenu.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
        addToCartButton.setDisable(!productToShow.getAvailablity());

//        Media media = new Media(ProductMenuUI.class.getResource("/Undertale_Enemy_Approaching_Yellow_Trimmed.wav").toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();

        setupImageAndVideo();
        setupLabels();
        setupSellerOptions();
        handleCanRate();
        setupCommentsSection();
    }

    private void setupOffPercentageLabel() {
        if (Offer.getOfferOfProduct(productToShow) != null) {
            offPercentageLabel.setText(Offer.getOfferOfProduct(productToShow).getOffPercent() + "% OFF");
            offPercentageLabel.setVisible(true);
        }
        else {
            offPercentageLabel.setText("No Offer");
            offPercentageLabel.setVisible(false);
        }
    }

    private void setupImageAndVideo() {
        if (!productToShow.getImagePath().isEmpty()) {
            productImageView.setImage(new Image(ProductMenuUI.class.getResource("/Images/" + productToShow.getImagePath()).toExternalForm()));
        }
        PannableCanvas canvas = new PannableCanvas();

        setupOffPercentageLabel();
        imageAndVideoVBox.getChildren().clear();

        StackPane innerGroup = new StackPane(productImageView, offPercentageLabel);
        innerGroup.setAlignment(Pos.TOP_LEFT);
        canvas.getChildren().add(innerGroup);
        canvas.setPrefWidth(Region.USE_COMPUTED_SIZE);
        canvas.setPrefHeight(Region.USE_COMPUTED_SIZE);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.setOnScroll(sceneGestures.getOnScrollEventHandler());
        canvas.setOnMousePressed(sceneGestures.getOnMousePressedEventHandler());
        canvas.setOnMouseDragged(sceneGestures.getOnMouseDraggedEventHandler());

        final Group group = new Group(canvas);

        imageAndVideoVBox.getChildren().add(new AnchorPane(group));
        handleImageAndVideoGrayscale();

        if (!productToShow.getVideoPath().isEmpty()) {
            videoPlayer = new VideoPlayer(ProductMenuUI.class.getResource("/Videos/" + productToShow.getVideoPath()).toExternalForm());
            imageAndVideoVBox.getChildren().add(videoPlayer);
        }
    }

    private void handleImageAndVideoGrayscale() {
        if (!productToShow.getAvailablity()) {
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-1);
            imageAndVideoVBox.setEffect(monochrome);
            offPercentageLabel.setText("SOLD OUT");
            offPercentageLabel.setVisible(true);
        }
        else {
            imageAndVideoVBox.setEffect(null);
        }
    }

    private void setupLabels() {
        nameLabel.setText(productToShow.getName() + "   (ID: " + productToShow.getProductID() + ")");
        brandLabel.setText(productToShow.getBrand());
        descriptionLabel.setText(productToShow.getDescription());
        priceLabel.setText("" + productToShow.getPrice());
        if (productToShow.getCategory() != null) {
            categoryLabel.setText(productToShow.getCategory().getFullName());
        }
        else {
            categoryLabel.setText("None");
        }

        averageRatingLabel.setText("" + productToShow.getAverageRating());

        if (productToShow.getStartingDate() != null) {
            dateOfOfferLabel.setText(productToShow.getStartingDate().toString());
        }
        else {
            dateOfOfferLabel.setText("None");
        }

        handleAvailabilityLabel();

        ArrayList<String> filters = productToShow.getFilters();
        String filtersString = "";
        for (String filter : filters) {
            filtersString = filtersString.concat(filter + "   ");
        }
        filtersLabel.setText(filtersString);
    }

    private void handleAvailabilityLabel() {
        if (productToShow.getAvailablity()) {
            productStatusLabel.setText("Available");
            ObservableList<String> styleClass = productStatusLabel.getStyleClass();
            if (styleClass.contains("unavailable")) {
                styleClass.remove("unavailable");
            }
            styleClass.add("available");
        }
        else {
            productStatusLabel.setText("Unavailable");
            ObservableList<String> styleClass = productStatusLabel.getStyleClass();
            if (styleClass.contains("available")) {
                styleClass.remove("available");
            }
            styleClass.add("unavailable");
        }
    }

    private void setupSellerOptions() {
        ArrayList<Seller> allSellersOfProduct = ProductController.getAllSellersOfProduct(productToShow);
        System.out.println(productToShow.getName());
        for (Seller seller : allSellersOfProduct) {
            System.out.println("SELLER: " + seller.getUsername());
            Button currentButton = new Button(seller.getUsername());
            if (seller == productToShow.getSeller()) {
                currentButton.setDisable(true);
            }
            currentButton.setOnAction((e) -> changeProductSeller(seller.getUsername()));
            currentButton.getStylesheets().add(ProductMenuUI.class.getResource("/CSS/product_menu_stylesheet.css").toExternalForm());
            currentButton.getStyleClass().add("sellerButton");
            sellerButtons.add(currentButton);

            sellersHBox.getChildren().add(currentButton);
        }
    }

    private void changeProductSeller(String username) {
        Product newProduct = ProductController.getProductWithDifferentSeller(productToShow, username);
        for (Button button : sellerButtons) {
            button.setDisable(false);
            if (button.getText().equalsIgnoreCase(newProduct.getSeller().getUsername())) {
                button.setDisable(true);
            }
        }
        productToShow = newProduct;
        setupImageAndVideo();
        handleAvailabilityLabel();
        setupCommentsSection();
        addToCartButton.setDisable(!productToShow.getAvailablity());
    }

    public void setupBindings() {
        mainMenuButton.setOnAction((e) -> {
            try {
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        productsButton.setOnAction((e) -> ProductsMenuUI.showProductsMenu());

        Button[] starButtons = new Button[] {ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            int finalButtonIndex = buttonIndex + 1;
            starButtons[buttonIndex].setOnAction((e) -> handleRatingChange(finalButtonIndex));
        }
        rateProductButton.setOnAction((e) -> handleRating());

        submitCommentButton.setOnAction((e) -> handleSubmitComment());
        addToCartButton.setOnAction((e) -> addToCart(productToShow));
        compareButton.setOnAction((e) -> handleCompare());
    }

    private void setRatingDisable(boolean disable) {
        resetStars();
        Button[] starButtons = new Button[] {ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            starButtons[buttonIndex].setDisable(disable);
        }
        rateProductButton.setDisable(disable);
    }

    private void handleCanRate() {
        if (!(MainMenuUIController.currentUser instanceof Customer)) {
            setRatingDisable(true);
        }
        else if (productToShow.hasBeenRatedBefore((Customer) MainMenuUIController.currentUser)) {
            setRatingDisable(true);
        }
        else if (!((Customer) MainMenuUIController.currentUser).hasBoughtProduct(productToShow)) {
            setRatingDisable(true);
        }
        else {
            setRatingDisable(false);
        }
    }

    private void resetStars() {
        ImageView[] starImages = new ImageView[] {activeStar, activeStar1, activeStar2, activeStar3, activeStar4};
        for (ImageView imageView : starImages) {
            imageView.setVisible(false);
        }
    }

    private void handleRatingChange(int index) {
        currentRating = index;
        ImageView[] starImages = new ImageView[] {activeStar, activeStar1, activeStar2, activeStar3, activeStar4};

        resetStars();
        for (int buttonIndex = 0; buttonIndex < index; buttonIndex++) {
            starImages[buttonIndex].setVisible(true);
        }
    }

    private void handleRating() {
        CustomerController.rateProduct((Customer) MainMenuUIController.currentUser, productToShow, currentRating);
        resetStars();
        handleCanRate();
        averageRatingLabel.setText("" + productToShow.getAverageRating());
    }

    private void handleSubmitComment() {
        resetAllErrors();
        if (!checkEmptyFields()) {
            if (commentTitleTextField.getText().isEmpty()) {
                setError(commentTitleTextField, true);
            }
            throwError("Please fill out both fields");
            return;
        }
        String title = commentTitleTextField.getText();
        String content = commentContentTextArea.getText();
        System.out.println(title);
        System.out.println(content);
        resetAllFields();
        ProductController.addComment(productToShow, MainMenuUIController.currentUser, title, content);
        throwError("Comment submitted");
        setupCommentsSection();
    }

    private boolean checkEmptyFields() {
        return !(commentContentTextArea.getText().isEmpty() || commentTitleTextField.getText().isEmpty());
    }

    private void resetAllFields() {
        resetAllErrors();
        commentTitleTextField.setText("");
        commentContentTextArea.setText("");
    }

    private void resetAllErrors() {
        setError(commentTitleTextField, false);
    }

    private void throwError(String message) {
        errorMessageLabel.setVisible(true);
        errorMessageLabel.setText(message);
    }

    private void setError(TextField field, boolean isError) {
        ObservableList<String> styleClass = field.getStyleClass();
        if (isError) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        }
        else {
            styleClass.removeAll(Collections.singleton("error"));
        }
    }

    private void addToCart(Product product) {
        if (MainMenuUIController.currentUser instanceof Customer) {
            ((Customer) MainMenuUIController.currentUser).addToCart(product);
        }
        else {
            throwError("Only customer type accounts can\nadd products to their cart");
        }
    }

    private void setupCommentsSection() {
        commentSectionVBox.getChildren().clear();
        for (Comment comment : productToShow.getComments()) {
            commentSectionVBox.getChildren().add(createCommentBox(comment));
        }
    }

    private VBox createCommentBox(Comment comment) {
        Label title = new Label();
        title.setText(comment.getCommentTitle());
        title.setStyle("-fx-text-fill: black;" +
                "-fx-font-size: 16;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");

        Label userCredentials = new Label();
        userCredentials.setText(comment.getCommentingUser().getUsername() + " a.k.a " + comment.getCommentingUser().getName());
        userCredentials.setStyle("-fx-text-fill: #00579e;" +
                "-fx-font-size: 16;");

        Label buyStatus = new Label();
        if (comment.getHasBought()) {
            buyStatus.setText("--Has bought this product");
            buyStatus.setStyle("-fx-text-fill: green;" +
                    "-fx-font-size: 16;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");
        }
        else {
            buyStatus.setText("--Has not bought this product");
            buyStatus.setStyle("-fx-text-fill: red;" +
                    "-fx-font-size: 16;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");
        }

        Label content = new Label();
        content.setText(comment.getCommentText());
        content.setStyle("-fx-text-fill: black;" +
                "-fx-font-size: 16;");

        HBox middleHBox = new HBox(userCredentials, buyStatus);
        middleHBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        middleHBox.setSpacing(20);

        VBox result = new VBox(title, middleHBox, content);
        result.setAlignment(Pos.TOP_LEFT);
        result.setPrefHeight(Region.USE_COMPUTED_SIZE);
        return result;
    }

    private void handleCompare() {
        String otherID = compareProductIDTextField.getText();
        if (otherID.isEmpty() || !otherID.matches("^\\d+$")) {
            setError(compareProductIDTextField, true);
            return;
        }
        if (Product.getProductByID(Integer.parseInt(otherID)) == null) {
            setError(compareProductIDTextField, true);
            return;
        }

        setError(compareProductIDTextField, false);
        compareProductIDTextField.setText("");
        CompareProducts.showLoginMenu(productToShow, Product.getProductByID(Integer.parseInt(otherID)));
    }
}
