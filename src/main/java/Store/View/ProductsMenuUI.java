package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ProductsController;
import Store.Main;
import Store.Model.Product;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductsMenuUI {
    private static ArrayList<String> filters = new ArrayList<String>();
    private static ArrayList<String> availableFilters = new ArrayList<String>();
    private static ArrayList<Product> productsToBeShown = new ArrayList<Product>();
    private static String[] availableSorts = new String[]{"visit", "rating", "price", "lexicographical"};
    private static String currentSort = "visit";

    private static String categoryFilter = "null";
    private static double priceLowFilter = -1;
    private static double priceHighFilter = -1;
    private static String brandFilter = "null";
    private static String nameFilter = "null";
    private static String sellerUsernameFilter = "null";
    private static String availabilityFilter = "null";
    private static String searchQuery = "(.*)";
    private static int pageNumber = 1;
    private static int totalPages;

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


    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/ProductsMenu.fxml"));
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
        sortChoiceBox.setItems(FXCollections.observableArrayList(availableSorts));
        initialSetup();
        setupBindings();
        setCategoryFiltersVBox();
        setRangeOfSliders();
        showProducts();

    }

    private void setRangeOfSliders() {
        priceHighSlider.setMax((int) ProductsController.getPriceHigh() + 1);
        priceHighSlider.setValue(priceHighSlider.getMax());
        priceHighSlider.setMin((int) ProductsController.getPriceLow());
        priceLowSlider.setMax((int) ProductsController.getPriceHigh() + 1);
        priceLowSlider.setMin((int) ProductsController.getPriceLow());
        priceHighLabel.setText(Integer.toString((int) priceHighSlider.getMax() + 1) + "$");
        priceLowLabel.setText(Integer.toString((int) priceHighSlider.getMin()) + "$");
    }

    private void initialSetup() {
        loggedInStatusText.textProperty().bind(MainMenuUIController.currentUserUsername);
        signUpButton.disableProperty().bind(MainMenuUIController.isLoggedIn);
        loginLogoutButton.textProperty().bind(MainMenuUIController.loginLogoutButtonText);
    }

    public void setupBindings() {
        mainMenuButton.setOnAction((e) -> {
            try {
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        loginLogoutButton.setOnAction((e) -> LoginMenuUI.handleEvent());
        signUpButton.setOnAction((e) -> SignUpCustomerAndSellerMenuUI.showSignUpMenu());
//        searchButton.setOnMouseClicked((e) -> giveSearchedProducts());
        searchString.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    searchQuery = "(.*)";
                } else {
                    searchQuery = newValue;
                }
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                showProducts();
            }
        });

        sortChoiceBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                currentSort = newValue.toString();
                showProducts();
            }
        });

        pageNumberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
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
            }
        });

        showAvailableCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                if (newValue) {
                    availabilityFilter = "1";
                } else {
                    availabilityFilter = "null";
                }
                showProducts();
            }
        });

        nextPageButton.setOnMouseClicked(event -> showNextPage());
        previousPageButton.setOnMouseClicked(event -> showPreviousPage());

        priceHighSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                priceHighLabel.setText(Integer.toString((int) newValue.doubleValue()) + "$");
                priceHighFilter = newValue.doubleValue();
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                showProducts();
            }
        });

        priceLowSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                priceLowLabel.setText(Integer.toString((int) newValue.doubleValue()) + "$");
                priceLowFilter = newValue.doubleValue();
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                showProducts();
            }
        });

        searchBrandName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    brandFilter = "null";
                } else {
                    brandFilter = newValue;
                }
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                showProducts();
            }
        });

        searchSellerName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    sellerUsernameFilter = "null";
                } else {
                    sellerUsernameFilter = newValue;
                }
                pageNumber = 1;
                pageNumberField.setText(Integer.toString(pageNumber));
                showProducts();
            }
        });

//        mainMenuButton.setOnAction((e) -> ProductsMenuUI.showProductsMenu());
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

    private void giveSearchedProducts() {
        if (searchString.getText().isEmpty()) {
            searchQuery = "(.*)";
        } else {
            searchQuery = searchString.getText();
        }
        showProducts();
    }

    private void showProducts() {
        setTobeShownProducts();
        firstColumn.getChildren().clear();
        secondColumn.getChildren().clear();
        thirdColumn.getChildren().clear();

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
        product.getImagePath();
        File file = null;
        if (product.getImagePath().equals("")) {
            file = new File("src/main/resources/Images/images.jpg");
        } else {
            file = new File("src/main/resources/Images/" + product.getImagePath());
        }

        ImageView imageView = new ImageView(new Image(file.toURI().toString()));
        imageView.setFitWidth(225);
        imageView.setFitHeight(225);
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
        productInfo.setOnMouseClicked(event -> {
            try {
                Main.setPrimaryStageScene(new Scene(ProductMenuUI.getContent(product)));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void setTobeShownProducts() {
        productsToBeShown = ProductsController.getFilteredList(filters);
        productsToBeShown = ProductsController.handleStaticFiltering(productsToBeShown, categoryFilter, priceLowFilter,
                priceHighFilter, brandFilter, nameFilter, sellerUsernameFilter, availabilityFilter);
        productsToBeShown = ProductsController.filterProductsWithSearchQuery(productsToBeShown, searchQuery);
        productsToBeShown = ProductsController.sort(currentSort, productsToBeShown);
        totalPages = (productsToBeShown.size() + 17) / 18;
        if (productsToBeShown.isEmpty()) {
            totalPages = 1;
        }
        ArrayList<Product> productsInThisPage = new ArrayList<>();
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
        availableFilters = new ArrayList(Product.getAllFilters(categoryFilter));
        for (String filter : availableFilters) {
            CheckBox checkBox = new CheckBox(filter);
            categoryFiltersVBox.getChildren().add(checkBox);
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    pageNumber = 1;
                    pageNumberField.setText(Integer.toString(pageNumber));
                    if (newValue) {
                        filters.add(filter);
                    } else {
                        filters.remove(filter);
                    }
                    showProducts();
                }
            });
        }
    }

}
