package Store.Controller;

import Store.InputManager;
import Store.Model.Auction;
import Store.Model.Manager;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class AuctionsController {

    public static ArrayList<Product> getFilteredList(ArrayList<String> filters) {
        ArrayList<Product> products = Auction.getAllAuctionsProducts();
        try {
            return products.stream()
                    .filter(product -> product.getFilters().containsAll(filters))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    public static ArrayList<Product> handleStaticFiltering(ArrayList<Product> products, String category, String brand, String name, String sellerUsername, String availability) {
        ArrayList<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (!category.equalsIgnoreCase("null") && (product.getCategory() == null || !product.getCategory().getName().equalsIgnoreCase(category))) {
                continue;
            }
            if (!brand.equalsIgnoreCase("null") && !product.getBrand().equalsIgnoreCase(brand)) {
                continue;
            }
            if (!name.equalsIgnoreCase("null") && !product.getName().contains(name)) {
                continue;
            }
            if (!sellerUsername.equalsIgnoreCase("null") && !product.getSeller().getUsername().equalsIgnoreCase(sellerUsername)) {
                continue;
            }
            if (!availability.equalsIgnoreCase("null")) {
                if (availability.equalsIgnoreCase("1") && !product.getAvailablity()) {
                    continue;
                }
                if (availability.equalsIgnoreCase("0") && product.getAvailablity()) {
                    continue;
                }
            }
            result.add(product);
        }
        return result;
    }
}
