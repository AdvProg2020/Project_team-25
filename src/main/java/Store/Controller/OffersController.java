package Store.Controller;

import Store.Model.Offer;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OffersController {

    public static ArrayList<Product> getFilteredList(ArrayList<String> filters) {
        ArrayList<Product> products = Offer.getAllOffProducts();
        try {
            return products.stream()
                    .filter(product -> product.getFilters().containsAll(filters))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }

    }

    public static ArrayList<Product> sortOffers(String mode, ArrayList<String> filters) {
        if (mode.equalsIgnoreCase("time of starting")) {
            return timeOfStartSort(filters);
        } else if (mode.equalsIgnoreCase("time of ending")) {
            return timeOfEndingSort(filters);
        } else if (mode.equalsIgnoreCase("rating")) {
            return ratingSort(filters);
        } else if (mode.equalsIgnoreCase("visit")) {
            return visitSort(filters);
        } else {
            return lexicographySort(filters);
        }

    }

    private static ArrayList<Product> timeOfStartSort(ArrayList<String> filters) {
        ArrayList<Product> products = new ArrayList<Product>();
        products.addAll(getFilteredList(filters));
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getStartingDate))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> timeOfEndingSort(ArrayList<String> filters) {
        ArrayList<Product> products = new ArrayList<Product>();
        products.addAll(getFilteredList(filters));
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getEndingDate))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> ratingSort(ArrayList<String> filters) {
        ArrayList<Product> products = new ArrayList<Product>();
        products.addAll(getFilteredList(filters));
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getAverageRating))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> lexicographySort(ArrayList<String> filters) {
        ArrayList<Product> products = new ArrayList<Product>();
        products.addAll(getFilteredList(filters));
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return products;
        }
    }

    private static ArrayList<Product> visitSort(ArrayList<String> filters) {
        ArrayList<Product> products = new ArrayList<Product>();
        products.addAll(getFilteredList(filters));
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
        }
        else if (mode.equalsIgnoreCase("time of ending")) {
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
