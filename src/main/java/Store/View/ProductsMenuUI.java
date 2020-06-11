package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ProductsController;
import Store.Main;
import Store.Model.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProductsMenuUI {
    private static ArrayList<String> filters = new ArrayList<String>();
    private static ArrayList<String> availableFilters = new ArrayList<String>();
    private static ArrayList<Product> productsToBeShown = new ArrayList<Product>();
    private static String currentSort = "visit";

    private static String categoryFilter = "null";
    private static double priceLowFilter = -1;
    private static double priceHighFilter = -1;
    private static String brandFilter = "null";
    private static String nameFilter = "null";
    private static String sellerUsernameFilter = "null";
    private static String availabilityFilter = "null";

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;
    public ScrollPane productsList;
    public VBox firstColumn;
    public VBox secondColumn;
    public VBox thirdColumn;


    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/ProductsMenu.fxml"));
        return root;
    }

    public static void showProductsMenu() {
        try {
            Main.setApplicationStage(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
        showProducts();
    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
    }

    public void setupBindings() {
        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
//        mainMenuButton.setOnAction((e) -> ProductsMenuUI.showProductsMenu());
    }

    private void showProducts() {
        productsToBeShown = ProductsController.getFilteredList(filters);
        productsToBeShown = ProductsController.handleStaticFiltering(productsToBeShown, categoryFilter, priceLowFilter,
                priceHighFilter, brandFilter, nameFilter, sellerUsernameFilter, availabilityFilter);
        productsToBeShown = ProductsController.sort(currentSort, productsToBeShown);
        firstColumn.getChildren().removeAll();
        secondColumn.getChildren().removeAll();
        thirdColumn.getChildren().removeAll();

        int productsNumber = 0;
        for (Product product : productsToBeShown) {
            switch (productsNumber % 3) {
                case 0:
                    setGraphicsOfProducts(firstColumn, product);
                    break;
                case 1:
                    setGraphicsOfProducts(secondColumn, product);
                    break;
                default:
                    setGraphicsOfProducts(thirdColumn, product);
            }
            productsNumber++;
        }

    }

    private void setGraphicsOfProducts(VBox vBox, Product product) {
        VBox productInfo = new VBox();
        productInfo.setAlignment(Pos.TOP_CENTER);
        productInfo.setId("productInfo");
        Separator separator = new Separator(Orientation.HORIZONTAL);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(80);
        File file = new File("src/main/resources/Images/images.jpg");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(productInfo.getPrefWidth());
        imageView.setImage(new Image(file.toURI().toString()));
        Label productName = new Label(product.getName());
        Label productPrice = new Label(Double.toString(product.getPrice()) + "$");
        Label productRating = new Label(Double.toString(product.getAverageRating()) + "/5");

        gridPane.add(productRating, 1, 0);
        gridPane.add(productPrice, 0, 0);
        productName.setId("productName");
        productPrice.setId("productPrice");
        productRating.setId("productRating");


        productInfo.getChildren().addAll(imageView, productName, gridPane, separator);
        vBox.getChildren().add(productInfo);
    }
}
