package Store.View;


import Store.Main;

import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.Networking.Client.Controller.ClientProductsController;
import Store.Networking.Client.Controller.ClientSignUpAndLoginController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ProductsMenuUI {
    private static ArrayList<String> filters;
    private static ArrayList<String> availableFilters;
    private static List<Map<String, Object>> productsToBeShown;
    private static String[] availableSorts = new String[]{"visit", "rating", "price", "lexicographical"};
    private static String currentSort;

    private static String categoryFilter;
    private static double priceLowFilter;
    private static double priceHighFilter;
    private static String brandFilter;
    private static String nameFilter;
    private static String sellerUsernameFilter;
    private static String availabilityFilter;
    private static String searchQuery;
    private static int pageNumber;
    private static int totalPages;

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;
    public Button signUpButton;
    public Button loginLogoutButton;
    public Label loggedInStatusText;
    public ScrollPane productsList;
    public VBox firstColumn;
    public VBox secondColumn;
    public VBox thirdColumn;
    public Button searchButton;
    public TextField searchString;
    public ComboBox sortChoiceBox;
    public Button previousPageButton;
    public Button nextPageButton;
    public TextField pageNumberField;
    public CheckBox showAvailableCheckBox;
    public Label priceLowLabel;
    public Label priceHighLabel;
    public Slider priceHighSlider;
    public Slider priceLowSlider;
    public TextField searchBrandName;
    public TextField searchSellerName;
    public VBox categoryFiltersVBox;
    public Button showCategoryButton;
    public HBox mainNode;


    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(ProductsMenuUI.class.getClassLoader().getResource("FXML/ProductsMenu.fxml"));
        return root;
    }

    public static void showProductsMenu() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        resetAllFields();
        initialSetup();
        setupBindings();
        setCategoryFiltersVBox();
        setRangeOfSliders();
        showProducts();

    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(ClientMainMenuController.currentUserUsername);
        signUpButton.disableProperty().bind(ClientMainMenuController.isLoggedIn);
        loginLogoutButton.textProperty().bind(ClientMainMenuController.loginLogoutButtonText);
    }

    public void setupBindings() {
        nextPageButton.setOnMouseClicked(event -> showNextPage());
        previousPageButton.setOnMouseClicked(event -> showPreviousPage());
        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
        showCategoryButton.setOnAction(event -> showCategories());
        offersButton.setOnAction((e) -> OffersMenuUI.showOffersMenu());
        supportPageButton.setOnAction((e) -> SupportPageUI.showSupportPage());

        userPageButton.setOnAction(e -> UserPageHandlerUI.handleEvent());

        mainMenuButton.setOnAction((e) -> {
            try {
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });


        searchString.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                searchQuery = "(.*)";
            } else {
                searchQuery = newValue;
            }
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            showProducts();
        });

        sortChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentSort = newValue.toString();
            showProducts();
        });

        pageNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (Integer.parseInt(newValue) <= totalPages && Integer.parseInt(newValue) >= 1) {
                    pageNumber = Integer.parseInt(newValue);
                    showProducts();
                    return;
                }
                pageNumberField.setText(Integer.toString(pageNumber));
            } catch (Exception exception) {
                // do nothing
            }
        });

        showAvailableCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            if (newValue) {
                availabilityFilter = "1";
            } else {
                availabilityFilter = "null";
            }
            showProducts();
        });

        priceHighSlider.setOnMouseReleased(event -> {
            priceHighFilter = priceHighSlider.getValue();
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            showProducts();
        });

        priceHighSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            priceHighLabel.setText("Price High: " + (int) newValue.doubleValue() + "$");
        });

        priceLowSlider.setOnMouseReleased(event -> {
            priceLowFilter = priceLowSlider.getValue();
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            showProducts();
        });

        priceLowSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            priceLowLabel.setText("Price Low:   " + (int) newValue.doubleValue() + "$");
        });

        searchBrandName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                brandFilter = "null";
            } else {
                brandFilter = newValue;
            }
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            showProducts();
        });

        searchSellerName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                sellerUsernameFilter = "null";
            } else {
                sellerUsernameFilter = newValue;
            }
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            showProducts();
        });

//        mainMenuButton.setOnAction((e) -> ProductsMenuUI.showProductsMenu());
//        searchButton.setOnMouseClicked((e) -> giveSearchedProducts());
    }

    private void showCategories() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("CSS/products_stylesheet.css");
        scrollPane.setPrefSize(400, 300);
        VBox vBox = new VBox();
        vBox.setPrefSize(400, 300);
        vBox.setAlignment(Pos.TOP_CENTER);
        Label base = new Label("All Categories");
        base.setId("categoryButton");
        base.setOnMouseClicked(event -> {
            Stage stage = (Stage) base.getScene().getWindow();
            stage.close();
            categoryFilter = "null";
            pageNumber = 1;
            pageNumberField.setText(Integer.toString(pageNumber));
            showProducts();
        });
        vBox.getChildren().addAll(base, new Separator(), new Separator());
        ArrayList<Map<String, Object>> allCategories = ClientProductsController.getAllCategories();
        for (Map<String, Object> category : allCategories) {
            Label categoryName = new Label((String) category.get("name"));
            categoryName.setId("categoryButton");
            categoryName.setOnMouseClicked(event -> {
                Stage stage = (Stage) categoryName.getScene().getWindow();
                stage.close();
                categoryFilter = (String) category.get("name");
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                showProducts();
            });
            HBox hBox = new HBox();
            for (String filter : new HashSet<String>((ArrayList) category.get("filters"))) {
                Button button = new Button(filter);
                button.setId("filter");
                Region region = new Region();
                region.setPrefHeight(5);
                hBox.getChildren().addAll(button, region);
            }
            Region region = new Region();
            region.setPrefHeight(10);
            vBox.getChildren().addAll(categoryName, hBox, new Separator(), region);
        }
        scrollPane.setContent(vBox);
        Main.setupOtherStage(new Scene(scrollPane), "Select Category");
    }

    private void showNextPage() {
        if (pageNumber == totalPages) {
            return;
        }
        pageNumber++;
        showProducts();
        pageNumberField.setText(Integer.toString(pageNumber));

    }

    private void showPreviousPage() {
        if (pageNumber == 1) {
            return;
        }
        pageNumber--;
        showProducts();
        pageNumberField.setText(Integer.toString(pageNumber));
    }


    private void showProducts() {
        Popup popup = Main.setProgressPopup();
        Stage stage = Main.getApplicationStage();
        popup.show(stage);
        mainNode.disableProperty().bind(popup.showingProperty());

        Runnable expensiveTask = () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setTobeShownProducts();
            VBox firstColumnProducts = new VBox();
            VBox secondColumnProducts = new VBox();
            VBox thirdColumnProducts = new VBox();

            int productsNumber = 0;
            for (Map<String, Object> product : productsToBeShown) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (productsNumber % 3) {
                    case 0:
                        setGraphicsOfProducts(firstColumnProducts, product);
                        break;
                    case 1:
                        setGraphicsOfProducts(secondColumnProducts, product);
                        break;
                    default:
                        setGraphicsOfProducts(thirdColumnProducts, product);
                }
                productsNumber++;
            }

            Platform.runLater(() -> {
                popup.hide();
                firstColumn.getChildren().clear();
                secondColumn.getChildren().clear();
                thirdColumn.getChildren().clear();
                firstColumn.getChildren().addAll(firstColumnProducts.getChildren());
                secondColumn.getChildren().addAll(secondColumnProducts.getChildren());
                thirdColumn.getChildren().addAll(thirdColumnProducts.getChildren());
            });
        };
        new Thread(expensiveTask).start();


    }

    private void setGraphicsOfProducts(VBox vBox, Map<String, Object> product) {
        VBox productInfo = new VBox();
        productInfo.setAlignment(Pos.TOP_CENTER);
        productInfo.setId("productInfo");
        Separator separator = new Separator(Orientation.HORIZONTAL);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(80);
//        product.getImagePath();
//        File file = null;
//        if (product.getImagePath().equals("")) {
//            file = new File("src/main/resources/Images/images.jpg");
//        } else {
//            file = new File(product.getImagePath());
//        }
        File file = new File("src/main/resources/Images/images.jpg");
        ImageView imageView = new ImageView(new Image(file.toURI().toString()));
        imageView.setFitWidth(225);
        imageView.setFitHeight(225);
        Label productName = new Label((String) product.get("name"));
        Label productPrice = new Label(product.get("price") + "$");
        Label productRating = new Label(product.get("averageRating") + "/5");
        Label isAvailable = new Label((Boolean) product.get("availability") ? "Available" : "Unavailable");

        isAvailable.setPrefWidth(vBox.getPrefWidth());
        isAvailable.setAlignment(Pos.CENTER);

        if (!(Boolean) product.get("availability")) {
            productInfo.setId("unavailableProduct");
        } else {
            isAvailable.setStyle((Boolean) product.get("availability") ? "-fx-background-color: #BFFF00;" : "-fx-background-color: #C40233;");
        }
        gridPane.add(productRating, 1, 0);
        gridPane.add(productPrice, 0, 0);
        productName.setId("productName");
        productPrice.setId("productPrice");
        productRating.setId("productRating");
        isAvailable.setId("availability");


        productInfo.getChildren().addAll(imageView, productName, gridPane, getProductRating(product), isAvailable, separator);
        vBox.getChildren().add(productInfo);
        productInfo.setOnMouseClicked(event -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

    }

    private void setTobeShownProducts() {
        productsToBeShown = ClientProductsController.getToBeShownProducts(filters, categoryFilter, priceLowFilter, priceHighFilter, brandFilter, nameFilter, sellerUsernameFilter, availabilityFilter, searchQuery, currentSort);
//        productsToBeShown = ProductsController.getFilteredList(filters);
//        productsToBeShown = ProductsController.handleStaticFiltering(productsToBeShown, categoryFilter, priceLowFilter,
//                priceHighFilter, brandFilter, nameFilter, sellerUsernameFilter, availabilityFilter);
//        productsToBeShown = ProductsController.filterProductsWithSearchQuery(productsToBeShown, searchQuery);
//        productsToBeShown = ProductsController.sort(currentSort, productsToBeShown);
        totalPages = (productsToBeShown.size() + 17) / 18;
        if (productsToBeShown.isEmpty()) {
            totalPages = 1;
        }
        ArrayList<Map<String, Object>> productsInThisPage = new ArrayList<>();
        try {
            for (int i = 0; i < 18; i++) {
                productsInThisPage.add(productsToBeShown.get(18 * (pageNumber - 1) + i));
            }
        } catch (Exception exception) {
            //do nothing
        }
        productsToBeShown = productsInThisPage;
    }

    private void setCategoryFiltersVBox() {
        categoryFiltersVBox.getChildren().clear();
        availableFilters = new ArrayList(ClientProductsController.getAllFilters(categoryFilter));
        for (String filter : availableFilters) {
            CheckBox checkBox = new CheckBox(filter);
            categoryFiltersVBox.getChildren().add(checkBox);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                if (newValue) {
                    filters.add(filter);
                } else {
                    filters.remove(filter);
                }
                showProducts();
            });
        }
    }

    private void resetAllFields() {
        sortChoiceBox.setItems(FXCollections.observableArrayList(availableSorts));
        filters = new ArrayList<>();
        availableFilters = new ArrayList<>();
        productsToBeShown = new ArrayList<>();
        currentSort = "";
        categoryFilter = "null";
        priceLowFilter = -1;
        priceHighFilter = -1;
        brandFilter = "null";
        nameFilter = "null";
        sellerUsernameFilter = "null";
        availabilityFilter = "null";
        searchQuery = "(.*)";
        pageNumber = 1;
    }

    private void setRangeOfSliders() {
        int priceHigh = (int) Math.round((Double) ClientProductsController.getPriceHigh());
        int priceLow = (int) Math.round((Double) ClientProductsController.getPriceLow());
        priceHighSlider.setMax(priceHigh);
        priceHighSlider.setValue(priceHighSlider.getMax());
        priceHighSlider.setMin(priceLow);
        priceLowSlider.setMax(priceHigh);
        priceLowSlider.setMin(priceLow);
        priceHighLabel.setText("Price High: " + ((int) priceHighSlider.getMax() + 1) + "$");
        priceLowLabel.setText("Price Low:   " + (int) priceHighSlider.getMin() + "$");
    }

    private HBox getProductRating(Map<String, Object> product) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.TOP_CENTER);
        int minRate = 0;
        if (!product.get("averageRating").equals("NaN")) {
            minRate = (int) Math.round(Double.parseDouble((String) product.get("averageRating")));
        }
        int stars = 1;
        File file;
        while (stars <= 5) {
            if (stars <= minRate) {
                file = new File("src/main/resources/Icons/StarSelected.png");
            } else {
                file = new File("src/main/resources/Icons/StarNotSelected.png");
            }
            ImageView imageView = new ImageView(new Image(file.toURI().toString()));
            hBox.getChildren().add(imageView);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            stars++;
        }
        return hBox;
    }


//    private void giveSearchedProducts() {
//        if (searchString.getText().isEmpty()) {
//            searchQuery = "(.*)";
//        } else {
//            searchQuery = searchString.getText();
//        }
//        showProducts();
//    }
}
