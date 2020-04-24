package Store.View;

import Store.Controller.OffersController;
import Store.InputManager;
import Store.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class OffersMenu {

    private static ArrayList<String> filters = new ArrayList<String>();
    private static ArrayList<String> availableSorts = new ArrayList<String>(Arrays.asList("time of starting", "time of ending", "rating", "lexicographical", "visit"));
    private static ArrayList<String> availableFilters = new ArrayList<String>();
    private static String currentSort = "visit";

    private static String SHOW_PRODUCT = "^show product (\\d+)$";
    private static String DISABLE_FILTER = "^disable filter ([^\\s]+)$";
    private static String FILTER = "^filter ([^\\s]+)$";
    private static String SORT = "^sort ([^\\s]+)$";

    public static void init() {
        String input;
        Matcher matcher;
        viewOffs();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_PRODUCT)).find()) {
                showProducts(matcher.group(1));
            } else if (input.equalsIgnoreCase("filter")) {
                filtering();
            } else if (input.equalsIgnoreCase("sorting")) {
                sorting();
            } else if (input.equalsIgnoreCase("login")) {
                if (MainMenu.currentUser == null) {
                    SignUpAndLoginMenu.init();
                } else {
                    System.out.println("you have signed in");
                }
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else {
                System.out.println("invalid command");
            }
        }

    }

    public static void viewOffs() {
        for (Product product : OffersController.sortOffers(currentSort, filters)) {
            Offer offer = Offer.getOfferOfProduct(product);
            System.out.println(product.getName() + ": ");
            System.out.println("ID: " + product.getProductID());
            System.out.println("Actual Price: " + product.getPrice() + "You Have To Pay: " + (product.getPrice()- (product.getPrice() - offer.getOffPercent())));
            System.out.println("Offer Info :" + offer);
        }
    }

    public static void showProducts(String attribute) {
        int productID = Integer.parseInt(attribute);
        Product product = Product.getProductByID(productID);
        if (product == null) {
            System.out.println("there isn't any product with this id");
        }
        ProductMenu.init(product);
    }

    private static void filtering() {
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("current filters")) {
                printCurrentFilters();
            } else if (input.equalsIgnoreCase("show available filters")) {
                showAvailableFilters();
            } else if ((matcher = InputManager.getMatcher(input, DISABLE_FILTER)).find()) {
                disableFilter(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, FILTER)).find()) {
                filter(matcher.group(1));
            } else {
                System.out.println("invalid command");
            }
        }

    }

    private static void showAvailableFilters() {
        for (String availableFilter : availableFilters) {
            System.out.print(availableFilter + " ");
        }
        System.out.println("");
    }

    private static void filter(String filter) {
        if (availableFilters != null && availableFilters.contains(filter)) {
            filters.add(filter);
            viewOffs();
        } else {
            System.out.println("the filter is invalid");
        }
    }

    private static void disableFilter(String filter) {
        if (availableFilters == null || !(availableFilters.contains(filter))) {
            System.out.println("the filter is invalid");
            return;
        }
        if (filters == null || !(filters.contains(filter))) {
            System.out.println("this filter hasn't been selected");
        }
        filters.remove(filter);
        OffersController.getFilteredList(filters);

    }

    private static void printCurrentFilters() {
        for (String filter : filters) {
            System.out.print(filter + " ");
        }
        System.out.println("");
    }

    private static void sorting() {
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("current sort")) {
                currentSort();
            } else if (input.equalsIgnoreCase("disable sort")) {
                disableSort();
            } else if (input.equalsIgnoreCase("show available sorts")) {
                showAvailableSorts();
            } else if ((matcher = InputManager.getMatcher(input, SORT)).find()) {
                sort(matcher.group(1));
            } else {
                System.out.println("invalid command");
            }
        }

    }

    private static void showAvailableSorts() {
        for (String availableSort : availableSorts) {
            System.out.print(availableSort + " ");
        }
        System.out.println("");
    }

    private static void sort(String mode) {
        if (availableSorts == null || !(availableSorts.contains(mode))) {
            System.out.println("mode isn't available");
            return;
        }
        currentSort = mode;
        viewOffs();
    }

    private static void currentSort() {
        System.out.println(currentSort);
    }

    private static void disableSort() {
        currentSort = "visit";
        viewOffs();
    }

    private static void printHelp() {
        System.out.println("list of main commands");
        System.out.println("show product [productId]");
        System.out.println("back");
        System.out.println("login");
        System.out.println("help");
        System.out.println("*******");
        System.out.println("\n list of filtering commands");
        System.out.println("filter");
        System.out.println("back");
        System.out.println("current filters");
        System.out.println("show available filters");
        System.out.println("disable filter [filter]");
        System.out.println("filter [filter]");
        System.out.println("*******");
        System.out.println("\n list of sort commands");
        System.out.println("sort [an available sort]");
        System.out.println("current sort");
        System.out.println("disable sort");
        System.out.println("show available sorts");
        System.out.println("back");
        System.out.println("*******");
    }
}
