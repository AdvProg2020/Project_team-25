package Store.View;

import Store.Controller.MainMenuUIController;
import Store.Controller.ProductController;
import Store.Main;
import Store.Model.Product;
import Store.Model.Seller;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;

public class CompareProducts {
    private static Product firstProduct, secondProduct;

    public Label nameFirst;
    public Label brandFirst;
    public Label descriptionFirst;
    public Label priceFirst;
    public Label categoryFirst;
    public Label sellersFirst;
    public Label ratingFirst;
    public Label dateOfOfferFirst;
    public Label statusFirst;

    public Label nameSecond;
    public Label brandSecond;
    public Label descriptionSecond;
    public Label priceSecond;
    public Label categorySecond;
    public Label sellersSecond;
    public Label ratingSecond;
    public Label dateOfOfferSecond;
    public Label statusSecond;

    public Label filtersFirstTitle;
    public Label filtersSecondTitle;
    public Label filtersOnlyFirst;
    public Label filtersOnlySecond;
    public Label filtersBoth;


    public CompareProducts() {

    }

    public static void showLoginMenu(Product product1, Product product2) {
        try {
            Main.setupOtherStage(new Scene(getContent(product1, product2)), "Login");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent getContent(Product product1, Product product2) throws IOException {
        firstProduct = product1;
        secondProduct = product2;
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/CompareProducts.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        setupLabels();
        setupFilterLabels();
    }

    private void setupFilterLabels() {
        filtersFirstTitle.setText(filtersFirstTitle.getText() + " " + firstProduct.getName());
        filtersSecondTitle.setText(filtersSecondTitle.getText() + " " + secondProduct.getName());

        ArrayList<String> filtersFirst = firstProduct.getFilters();
        ArrayList<String> filtersSecond = secondProduct.getFilters();

        ArrayList<String> allFilters = new ArrayList<>();
        allFilters.addAll(filtersFirst);
        allFilters.addAll(filtersSecond);

        String onlyFirst = "", onlySecond = "", both = "";
        for (String filter : allFilters) {
            if (filtersFirst.contains(filter) && filtersSecond.contains(filter)) {
                both = both.concat(filter + "   ");
            }
            else if (filtersFirst.contains(filter)) {
                onlyFirst = onlyFirst.concat(filter + "   ");
            }
            else {
                onlySecond = onlySecond.concat(filter + "   ");
            }
        }

        filtersOnlyFirst.setText(onlyFirst);
        filtersOnlySecond.setText(onlySecond);
        filtersBoth.setText(both);
    }

    private void setupLabels() {
        nameFirst.setText(firstProduct.getName() + "   (ID: " + firstProduct.getProductID() + ")");
        brandFirst.setText(firstProduct.getBrand());
        descriptionFirst.setText(firstProduct.getDescription());
        priceFirst.setText("" + firstProduct.getPrice());
        if (firstProduct.getCategory() != null) {
            categoryFirst.setText(firstProduct.getCategory().getFullName());
        }
        else {
            categoryFirst.setText("None");
        }

        sellersFirst.setText(getSellerNameTextList(firstProduct));

        ratingFirst.setText("" + firstProduct.getAverageRating());

        if (firstProduct.getStartingDate() != null) {
            dateOfOfferFirst.setText(firstProduct.getStartingDate().toString());
        }
        else {
            dateOfOfferFirst.setText("None");
        }
        handleAvailabilityLabel(firstProduct, statusFirst);

        nameSecond.setText(secondProduct.getName() + "   (ID: " + secondProduct.getProductID() + ")");
        brandSecond.setText(secondProduct.getBrand());
        descriptionSecond.setText(secondProduct.getDescription());
        priceSecond.setText("" + secondProduct.getPrice());
        if (secondProduct.getCategory() != null) {
            categorySecond.setText(secondProduct.getCategory().getFullName());
        }
        else {
            categorySecond.setText("None");
        }
        sellersSecond.setText(getSellerNameTextList(secondProduct));

        ratingSecond.setText("" + secondProduct.getAverageRating());

        if (secondProduct.getStartingDate() != null) {
            dateOfOfferSecond.setText(secondProduct.getStartingDate().toString());
        }
        else {
            dateOfOfferSecond.setText("None");
        }
        handleAvailabilityLabel(secondProduct, statusSecond);
    }

    private String getSellerNameTextList(Product product) {
        ArrayList<Seller> allSellersOfProduct = new ArrayList<>();
        allSellersOfProduct = ProductController.getAllSellersOfProduct(product);
        String result = "";
        for (Seller seller : allSellersOfProduct) {
            result = result.concat(seller.getUsername() + "   ");
        }
        return result;
    }

    private void handleAvailabilityLabel(Product productToShow, Label productStatusLabel) {
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
}
