package Store.View;


import Store.Controller.SignUpAndLoginController;
import Store.InputManager;
import Store.Main;

import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.Networking.Client.Controller.ClientProductController;
import Store.Networking.Client.Controller.ClientSignUpAndLoginController;
import Store.Networking.FileTransportClient;
import Store.Networking.P2P.P2PClient;
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

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;

public class ProductMenuUI {

    private static Map<String, Object> productToShow = null;
    private static final double SCALE_DELTA = 1.1;

    private static String sellerName = "";

    public VBox imageAndVideoVBox;
    public VideoPlayer videoPlayer;

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;

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

    public Button downloadButton;

    private ArrayList<Button> sellerButtons = new ArrayList<Button>();


    public ProductMenuUI() {

    }

    public static Parent getContent(Map product) throws IOException {
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
        loggedInStatusText.textProperty().bind(ClientMainMenuController.currentUserUsername);
        signUpButton.disableProperty().bind(ClientMainMenuController.isLoggedIn);
        loginLogoutButton.textProperty().bind(ClientMainMenuController.loginLogoutButtonText);
        addToCartButton.setDisable(!(Boolean) productToShow.get("availability"));

//        Media media = new Media(ProductMenuUI.class.getResource("/Undertale_Enemy_Approaching_Yellow_Trimmed.wav").toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();

        setupImageAndVideo();
        setupLabels();
        setupSellerOptions();
        handleCanRate();
        setupCommentsSection();
        setupDownloadButton();
    }

    private void setupOffPercentageLabel() {
        Map<String, Object> offer = (Map<String, Object>) productToShow.get("offer");
        if (offer != null) {
            offPercentageLabel.setText(offer.get("offPercent") + "% OFF");
            offPercentageLabel.setVisible(true);
        } else {
            offPercentageLabel.setText("No Offer");
            offPercentageLabel.setVisible(false);
        }
    }

    private void setupImageAndVideo() {
//        if (!productToShow.getImagePath().isEmpty()) {
//            String path;
//            if (productToShow.getImagePath().isEmpty()) {
//                path = "src/main/resources/Images/images.jpg";
//            }
//            else {
//                path = productToShow.getImagePath();
//            }
//
        FileTransportClient.receiveFile(ClientHandler.username, ClientHandler.token, "I", ClientHandler.username + ".jpg");
        File file = new File("src/main/resources/Images/" + ClientHandler.username + ".jpg");
        productImageView.setImage(new Image(file.toURI().toString()));

//            productImageView.setImage(new Image(ProductMenuUI.class.getResource("/Images/" + productToShow.getImagePath()).toExternalForm()));


        PannableCanvas canvas = new PannableCanvas();

        setupOffPercentageLabel();
        imageAndVideoVBox.getChildren().

                clear();

        StackPane innerGroup = new StackPane(productImageView, offPercentageLabel);
        innerGroup.setAlignment(Pos.TOP_LEFT);
        canvas.getChildren().

                add(innerGroup);
        canvas.setPrefWidth(Region.USE_COMPUTED_SIZE);
        canvas.setPrefHeight(Region.USE_COMPUTED_SIZE);
        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.setOnScroll(sceneGestures.getOnScrollEventHandler());
        canvas.setOnMousePressed(sceneGestures.getOnMousePressedEventHandler());
        canvas.setOnMouseDragged(sceneGestures.getOnMouseDraggedEventHandler());

        final Group group = new Group(canvas);

        imageAndVideoVBox.getChildren().

                add(new AnchorPane(group));

        handleImageAndVideoGrayscale();

//        if (!productToShow.getVideoPath().isEmpty()) {
//            //System.out.println(ProductMenuUI.class.getResource("/Videos/" + productToShow.getVideoPath()).toExternalForm());
//            String path;
//            path = productToShow.getVideoPath();
//            File file = new File(path);
//            videoPlayer = new VideoPlayer(file.toURI().toString());
//            imageAndVideoVBox.getChildren().add(videoPlayer);
//        }
        FileTransportClient.receiveFile(ClientHandler.username, ClientHandler.token, "V", productToShow.get("id") + ".mp4");
        file = new File("src/main/resources/Videos/" + productToShow.get("id") + ".mp4");
        videoPlayer = new VideoPlayer(file.toURI().toString());
        imageAndVideoVBox.getChildren().add(videoPlayer);
    }

    private void handleImageAndVideoGrayscale() {
        if (!(Boolean) productToShow.get("availability")) {
            ColorAdjust monochrome = new ColorAdjust();
            monochrome.setSaturation(-1);
            imageAndVideoVBox.setEffect(monochrome);
            offPercentageLabel.setText("SOLD OUT");
            offPercentageLabel.setVisible(true);
        } else {
            imageAndVideoVBox.setEffect(null);
        }
    }

    private void setupLabels() {
        nameLabel.setText(productToShow.get("name") + "   (ID: " + productToShow.get("id") + ")");
        brandLabel.setText((String) productToShow.get("brand"));
        descriptionLabel.setText((String) productToShow.get("description"));
        priceLabel.setText("" + productToShow.get("price"));
        if (productToShow.get("category") != null) {
            categoryLabel.setText((String) ((Map<String, Object>) productToShow.get("category")).get("fullName"));
        } else {
            categoryLabel.setText("None");
        }

        averageRatingLabel.setText("" + productToShow.get("averageRating"));

        if (productToShow.get("startingDate") != null) {
            dateOfOfferLabel.setText(productToShow.get("startingDate").toString());
        } else {
            dateOfOfferLabel.setText("None");
        }

        handleAvailabilityLabel();

        ArrayList<String> filters = (ArrayList<String>) productToShow.get("filters");
        String filtersString = "";
        for (String filter : filters) {
            filtersString = filtersString.concat(filter + "   ");
        }
        filtersLabel.setText(filtersString);
    }

    private void handleAvailabilityLabel() {
        if (!(Boolean) productToShow.get("availability")) {
            productStatusLabel.setText("Available");
            ObservableList<String> styleClass = productStatusLabel.getStyleClass();
            if (styleClass.contains("unavailable")) {
                styleClass.remove("unavailable");
            }
            styleClass.add("available");
        } else {
            productStatusLabel.setText("Unavailable");
            ObservableList<String> styleClass = productStatusLabel.getStyleClass();
            if (styleClass.contains("available")) {
                styleClass.remove("available");
            }
            styleClass.add("unavailable");
        }
    }

    private void setupSellerOptions() {
        List<Map<String, Object>> allSellersOfProduct = ClientProductController.getAllSellersOfProduct((String) productToShow.get("id"));
        System.out.println(productToShow.get("name"));
        for (Map<String, Object> seller : allSellersOfProduct) {
            System.out.println("SELLER: " + seller.get("username"));
            Button currentButton = new Button((String) seller.get("username"));
            if (seller.get("username").equals(productToShow.get("sellerName"))) {
                currentButton.setDisable(true);
            }
            currentButton.setOnAction((e) -> changeProductSeller((String) seller.get("username")));
            currentButton.getStylesheets().add(ProductMenuUI.class.getResource("/CSS/product_menu_stylesheet.css").toExternalForm());
            currentButton.getStyleClass().add("sellerButton");
            sellerButtons.add(currentButton);

            sellersHBox.getChildren().add(currentButton);
        }
    }

    private void changeProductSeller(String username) {
        Map<String, Object> newProduct = ClientProductController.getProductWithDifferentSeller((String) productToShow.get("id"), username);
        for (Button button : sellerButtons) {
            button.setDisable(false);
            if (button.getText().equalsIgnoreCase((String) (newProduct.get("sellerName")))) {
                button.setDisable(true);
            }
        }
        productToShow = newProduct;
        setupImageAndVideo();
        handleAvailabilityLabel();
        setupCommentsSection();
        addToCartButton.setDisable(!(Boolean) productToShow.get("availability"));
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
        offersButton.setOnAction((e) -> OffersMenuUI.showOffersMenu());

        userPageButton.setOnAction(e -> UserPageHandlerUI.handleEvent());
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());

        Button[] starButtons = new Button[]{ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
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
        Button[] starButtons = new Button[]{ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            starButtons[buttonIndex].setDisable(disable);
        }
        rateProductButton.setDisable(disable);
    }

    private void handleCanRate() {
        Map<String, Object> customer = ClientSignUpAndLoginController.getUserInfo(ClientHandler.username);
        if (!ClientHandler.hasLoggedIn) {
            setRatingDisable(true);
        } else if (!(customer.get("type").equals("Customer"))) {
            setRatingDisable(true);
        } else if (ClientProductController.hasBeenRated((String) productToShow.get("id"), ClientHandler.username)) {
            setRatingDisable(true);
        } else if (!ClientProductController.hasBoughtProduct((String) productToShow.get("id"), ClientHandler.username)) {
            setRatingDisable(true);
        } else {
            setRatingDisable(false);
        }
    }

    private void resetStars() {
        ImageView[] starImages = new ImageView[]{activeStar, activeStar1, activeStar2, activeStar3, activeStar4};
        for (ImageView imageView : starImages) {
            imageView.setVisible(false);
        }
    }

    private void handleRatingChange(int index) {
        currentRating = index;
        ImageView[] starImages = new ImageView[]{activeStar, activeStar1, activeStar2, activeStar3, activeStar4};

        resetStars();
        for (int buttonIndex = 0; buttonIndex < index; buttonIndex++) {
            starImages[buttonIndex].setVisible(true);
        }
    }

    private void handleRating() {
        ClientProductController.rateProduct(ClientHandler.username, productToShow, currentRating);
        resetStars();
        handleCanRate();
        averageRatingLabel.setText("" + productToShow.get("averageRating"));
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
        if (!ClientHandler.hasLoggedIn) {
            throwError("You Should Log In");
            return;
        }
        String title = commentTitleTextField.getText();
        String content = commentContentTextArea.getText();
        System.out.println(title);
        System.out.println(content);
        resetAllFields();
        ClientProductController.addComment(productToShow, ClientHandler.username, title, content);
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
        } else {
            styleClass.removeAll(Collections.singleton("error"));
        }
    }

    private void addToCart(Map<String, Object> product) {
        Map<String, Object> customer = ClientSignUpAndLoginController.getUserInfo(ClientHandler.username);
        if (customer.get("type").equals("Customer")) {
            ClientProductController.addToCart(ClientHandler.username, (String) product.get("id"));
        } else {
            throwError("Only customer type accounts can\nadd products to their cart");
        }
    }

    private void setupCommentsSection() {
        commentSectionVBox.getChildren().clear();
        for (Map<String, Object> comment : (ArrayList<Map<String, Object>>) productToShow.get("comments")) {
            commentSectionVBox.getChildren().add(createCommentBox(comment));
        }
    }

    private VBox createCommentBox(Map<String, Object> comment) {
        Label title = new Label();
        title.setText((String) comment.get("title"));
        title.setStyle("-fx-text-fill: black;" +
                "-fx-font-size: 16;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");

        Label userCredentials = new Label();
        userCredentials.setText(comment.get("username") + " a.k.a ");
        userCredentials.setStyle("-fx-text-fill: #00579e;" +
                "-fx-font-size: 16;");

        Label buyStatus = new Label();
        if ((Boolean) comment.get("hasBought")) {
            buyStatus.setText("--Has bought this product");
            buyStatus.setStyle("-fx-text-fill: green;" +
                    "-fx-font-size: 16;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");
        } else {
            buyStatus.setText("--Has not bought this product");
            buyStatus.setStyle("-fx-text-fill: red;" +
                    "-fx-font-size: 16;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0.5, 0.0, 0.0);");
        }

        Label content = new Label();
        content.setText((String) comment.get("text"));
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
        Map<String, Object> secondProduct = ClientProductController.getComparedProduct(otherID);
        if (secondProduct == null) {
            setError(compareProductIDTextField, true);
            return;
        }

        setError(compareProductIDTextField, false);
        compareProductIDTextField.setText("");
        CompareProducts.showLoginMenu(productToShow, secondProduct);
    }

    private void setupDownloadButton() {
        Map<String, Object> customer = ClientSignUpAndLoginController.getUserInfo(ClientHandler.username);
        if (!ClientHandler.hasLoggedIn || !(customer.get("type").equals("Customer"))) {
            return;
        }


        String fileName = ((String) productToShow.get("filePath"));
        if (fileName.isEmpty() ||
                !ClientProductController.hasBoughtProduct((String) productToShow.get("id"), ClientHandler.username)) {
            return;
        }

        downloadButton.setVisible(true);
        downloadButton.setOnAction((e) -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean errorFound = false;
                    try {
                        P2PClient.receiveFile(ClientHandler.username, ClientHandler.token,
                                (String) productToShow.get("sellerName"), fileName);
                    } catch (Exception e) {
                        errorFound = true;
                    }
                    boolean finalErrorFound = errorFound;
                    Platform.runLater(() -> {
                        if (finalErrorFound) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Connection Error");
                            alert.setHeaderText("Seller Server Offline");
                            alert.setContentText("It seems the sellers aren't running the server\non their end");

                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Download Complete");
                            alert.setHeaderText("File of " + productToShow.get("name") + " Downloaded");
                            alert.setContentText("You can access this file from the Downloads folder");

                            alert.show();
                        }
                    });
                }
            }).start();
        });
    }
}
