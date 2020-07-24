package Store.View;


import Store.Controller.AuctionsController;
import Store.Controller.MainMenuUIController;
import Store.Main;
import Store.Model.Auction;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.*;
import Store.Networking.HashMapGenerator;
import Store.View.AdditionalUtils.PannableCanvas;
import Store.View.AdditionalUtils.SceneGestures;
import Store.View.AdditionalUtils.VideoPlayer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class AuctionMenuUI {

    private static Map<String, Object> auctionToShow = null;
    private static Map<String, Object> productToShow = null;

    private static String sellerName = "";

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;
    public Button auctionPageButton;

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
    public Label currentBuyer;
    public Label highestPrice;
    public Label conditionLabel;

    public TextField auctionPrice;
    public TextArea message;
    public VBox messagesVBox;

    public Button refreshButton;

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

    public Label errorMessageLabel;

    public Button addToCartButton;

    public HBox sellersHBox;
    public ImageView productImageView;

    static boolean isInAuction;
    static ArrayList<String> messages = new ArrayList<>();

    public AuctionMenuUI() {

    }

    public static Parent getContent(Map<String, Object> auction) throws IOException {
        auctionToShow = auction;
        productToShow = (Map)auctionToShow.get("product");
        isInAuction = true;
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/AuctionMenu.fxml"));
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

//        Media media = new Media(ProductMenuUI.class.getResource("/Undertale_Enemy_Approaching_Yellow_Trimmed.wav").toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();

        setupImageAndVideo();
        setupLabels();
        setupSellerOptions();
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
//            File file = new File(path);
//            productImageView.setImage(new Image(file.toURI().toString()));
//
////            productImageView.setImage(new Image(ProductMenuUI.class.getResource("/Images/" + productToShow.getImagePath()).toExternalForm()));
//        }


//        PannableCanvas canvas = new PannableCanvas();
//
//        setupOffPercentageLabel();
//        imageAndVideoVBox.getChildren().clear();
//
//        StackPane innerGroup = new StackPane(productImageView, offPercentageLabel);
//        innerGroup.setAlignment(Pos.TOP_LEFT);
//        canvas.getChildren().add(innerGroup);
//        canvas.setPrefWidth(Region.USE_COMPUTED_SIZE);
//        canvas.setPrefHeight(Region.USE_COMPUTED_SIZE);
//        SceneGestures sceneGestures = new SceneGestures(canvas);
//        canvas.setOnScroll(sceneGestures.getOnScrollEventHandler());
//        canvas.setOnMousePressed(sceneGestures.getOnMousePressedEventHandler());
//        canvas.setOnMouseDragged(sceneGestures.getOnMouseDraggedEventHandler());
//
//        final Group group = new Group(canvas);
//
//        imageAndVideoVBox.getChildren().add(new AnchorPane(group));
//        handleImageAndVideoGrayscale();



//        if (!productToShow.getVideoPath().isEmpty()) {
//            //System.out.println(ProductMenuUI.class.getResource("/Videos/" + productToShow.getVideoPath()).toExternalForm());
//            String path;
//            path = productToShow.getVideoPath();
//            File file = new File(path);
//            videoPlayer = new VideoPlayer(file.toURI().toString());
//            imageAndVideoVBox.getChildren().add(videoPlayer);
//        }
    }

    private void setupLabels() {
        nameLabel.setText(productToShow.get("name") + "   (ID: " + productToShow.get("id") + ")");
        brandLabel.setText((String) productToShow.get("brand"));
        descriptionLabel.setText((String) productToShow.get("description"));
        priceLabel.setText("" + productToShow.get("price"));
        if (productToShow.get("category") != null) {
            categoryLabel.setText((String) ((Map<String, Object>)productToShow.get("category")).get("fullName"));
        }
        else {
            categoryLabel.setText("None");
        }

        averageRatingLabel.setText("" + productToShow.get("averageRating"));

        if (productToShow.get("startingDate") != null) {
            dateOfOfferLabel.setText(productToShow.get("startingDate").toString());
        }
        else {
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
        if (!(Boolean)productToShow.get("availability")) {
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

    }

    public void setupBindings() {

        highestPrice.textProperty().bind(ClientAuctionController.getHighestPrice(auctionToShow));
        currentBuyer.textProperty().bind(ClientAuctionController.getCurrentBuyer(auctionToShow));
        conditionLabel.textProperty().bind(ClientAuctionController.getCondition(auctionToShow));
        refreshButton.setOnAction(e -> {
            try {
                messages = ClientAuctionController.getMessages(auctionToShow);
            } catch (IOException ioException) {
                System.out.println("3");
                throwError(ioException.getMessage());
            }
            showMessages();
        });
        mainMenuButton.setOnAction((e) -> {
            try {
                isInAuction = false;
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        productsButton.setOnAction((e) -> {
            isInAuction = false;
            ProductsMenuUI.showProductsMenu();
        });
        offersButton.setOnAction((e) -> {
            isInAuction = false;
            OffersMenuUI.showOffersMenu();
        });

        userPageButton.setOnAction(e -> {
            isInAuction = false;
            UserPageHandlerUI.handleEvent();
        });
        supportPageButton.setOnAction((e) -> {
            isInAuction = false;
            SupportPageUI.showSupportPage();
        });
        auctionPageButton.setOnAction(e -> {
            isInAuction = false;
            AuctionsMenuUI.showAuctionsMenu();
        });

        Button[] starButtons = new Button[] {ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5};
        for (int buttonIndex = 0; buttonIndex < 5; buttonIndex++) {
            int finalButtonIndex = buttonIndex + 1;
            starButtons[buttonIndex].setOnAction((e) -> handleRatingChange(finalButtonIndex));
        }
    }

    private void showMessages() {
        messagesVBox.getChildren().clear();
        for (String message: messages)
            showMessage(message);
    }

    private void showMessage(String message) {
        HBox hBox = new HBox();
        Label label = new Label(message);
        label.setWrapText(true);
        hBox.getChildren().add(label);
        messagesVBox.getChildren().add(hBox);
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
        if ((Boolean)comment.get("hasBought")) {
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

    public void tryHigherPrice(){
        if (!Pattern.matches("\\d+(\\.(\\d+))?", auctionPrice.getText()))
            throwError("Enter correct money!");
        else{
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", "increaseAuctionPrice");
            hashMap.put("auctionId", auctionToShow.get("id"));
            hashMap.put("newPrice", Double.parseDouble(auctionPrice.getText()));
            hashMap.put("buyer", ClientMainMenuController.currentUserUsername.getValue());
            hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
            if (hashMap.get("content").equals("error")){
                throwError((String)hashMap.get("type"));
            }
        }
    }

    public void sendMessage(){
        if (!message.getText().isEmpty()){
            try {
                ClientAuctionController.sendMessage(message.getText(), auctionToShow);
            } catch (Exception exception) {
                throwError(exception.getMessage());
            }
        }
    }

}
