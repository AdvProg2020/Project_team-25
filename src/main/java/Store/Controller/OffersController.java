package Store.Controller;

import Store.Model.Offer;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OffersController {

    private static ArrayList<Product> getFilteredList(ArrayList<String> filters) {
        ArrayList<Product> products = Offer.getAllOffProducts();
        try {
            return products.stream()
                    .filter(product -> product.getFilters().containsAll(filters))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    public static ArrayList<Product> sortProductsInOffers(String currentSort, ArrayList<String> filters) {
        ArrayList<Product> products = getFilteredList(filters);
        if (currentSort.equalsIgnoreCase("rating"))
            return sortByRating(products);
        else if (currentSort.equalsIgnoreCase("lexicographical"))
            return sortByLexicographical(products);
        else if (currentSort.equalsIgnoreCase("visit"))
            return sortByVisit(products);
        else if (currentSort.equalsIgnoreCase("price"))
            return sortByPrice(products);
        else
            return products;
    }

    private static ArrayList<Product> sortByRating(ArrayList<Product> products) {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getAverageRating))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> sortByPrice(ArrayList<Product> products) {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> sortByLexicographical(ArrayList<Product> products) {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> sortByVisit(ArrayList<Product> products) {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getVisited))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    public static ArrayList<Offer> sort(String mode, ArrayList<Offer> offers) {
        if (mode.equalsIgnoreCase("time of starting")) {
            return sortByStartingTime(offers);
        } else if (mode.equalsIgnoreCase("time of ending")) {
            return sortByEndingTime(offers);
        }
        return offers;
    }

    private static ArrayList<Offer> sortByStartingTime(ArrayList<Offer> offers) {
        ArrayList<Offer> result = new ArrayList<Offer>();
        result.addAll(offers);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(Offer::getStartingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<Offer> sortByEndingTime(ArrayList<Offer> offers) {
        ArrayList<Offer> result = new ArrayList<Offer>();
        result.addAll(offers);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(Offer::getEndingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }
}
