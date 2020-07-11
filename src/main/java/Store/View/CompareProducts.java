package Store.View;

import Store.Main;

import Store.Networking.Client.Controller.ClientProductController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompareProducts {
    private static Map<String, Object> firstProduct, secondProduct;

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

    public static void showLoginMenu(Map<String, Object> product1, Map<String, Object> product2) {
        try {
            Main.setupOtherStage(new Scene(getContent(product1, product2)), "Login");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent getContent(Map<String, Object> product1, Map<String, Object> product2) throws IOException {
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
        filtersFirstTitle.setText(filtersFirstTitle.getText() + " " + firstProduct.get("name"));
        filtersSecondTitle.setText(filtersSecondTitle.getText() + " " + secondProduct.get("name"));

        ArrayList<String> filtersFirst = (ArrayList<String>) firstProduct.get("filters");
        ArrayList<String> filtersSecond = (ArrayList<String>) secondProduct.get("filters");

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
        nameFirst.setText(firstProduct.get("name") + "   (ID: " + firstProduct.get("id") + ")");
        brandFirst.setText((String) firstProduct.get("brand"));
        descriptionFirst.setText((String) firstProduct.get("description"));
        priceFirst.setText("" + firstProduct.get("price"));
        if (firstProduct.get("category") != null) {
            categoryFirst.setText((String) ((Map<String, Object>)firstProduct.get("category")).get("fullName"));
        }
        else {
            categoryFirst.setText("None");
        }

        sellersFirst.setText(getSellerNameTextList(firstProduct));

        ratingFirst.setText("" + firstProduct.get("averageRating"));

        if (firstProduct.get("startingDate") != null) {
            dateOfOfferFirst.setText(firstProduct.get("startingDate").toString());
        }
        else {
            dateOfOfferFirst.setText("None");
        }
        handleAvailabilityLabel(firstProduct, statusFirst);

        nameSecond.setText(secondProduct.get("name") + "   (ID: " + secondProduct.get("id") + ")");
        brandSecond.setText((String) secondProduct.get("brand"));
        descriptionSecond.setText((String) secondProduct.get("description"));
        priceSecond.setText("" + secondProduct.get("price"));
        if (secondProduct.get("category") != null) {
            categorySecond.setText((String) ((Map<String, Object>)secondProduct.get("category")).get("fullName"));
        }
        else {
            categorySecond.setText("None");
        }
        sellersSecond.setText(getSellerNameTextList(secondProduct));

        ratingSecond.setText("" + secondProduct.get("averageRating"));

        if (secondProduct.get("startingDate") != null) {
            dateOfOfferSecond.setText(secondProduct.get("startingDate").toString());
        }
        else {
            dateOfOfferSecond.setText("None");
        }
        handleAvailabilityLabel(secondProduct, statusSecond);
    }

    private String getSellerNameTextList(Map<String, Object> product) {
        List<Map<String, Object>> allSellersOfProduct;
        allSellersOfProduct = ClientProductController.getAllSellersOfProduct((String)product.get("id"));
        String result = "";
        for (Map<String, Object> seller : allSellersOfProduct) {
            result = result.concat(seller.get("username") + "   ");
        }
        return result;
    }

    private void handleAvailabilityLabel(Map<String, Object> productToShow, Label productStatusLabel) {
        if ((Boolean)productToShow.get("availability")) {
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
