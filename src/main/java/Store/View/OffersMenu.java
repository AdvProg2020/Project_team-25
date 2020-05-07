package Store.View;

import Store.Controller.OffersController;
import Store.InputManager;
import Store.Main;
import Store.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class OffersMenu {

    private static ArrayList<String> filters = new ArrayList<String>();
    private static ArrayList<String> availableSorts = new ArrayList<String>(Arrays.asList("time of starting", "time of ending", "rating", "lexicographical", "visit"));
    private static ArrayList<String> availableFilters = new ArrayList<String>();
    private static String currentSort = "visit";

    private static String SHOW_PRODUCT_REGEX = "^show product (\\d+)$";
    private static String DISABLE_FILTER_REGEX = "^disable filter ([^\\s]+)$";
    private static String FILTER_REGEX = "^filter ([^\\s]+)$";
    private static String SORT_REGEX = "^sort (.+)$";

    public static void init() {
        currentSort = "visit";
        filters = new ArrayList<>();

        String input;
        Matcher matcher;
        System.out.println("\nOffers menu\n");
        viewOffs();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            availableFilters = new ArrayList<>(Product.getAllFilters());
            if ((matcher = InputManager.getMatcher(input, SHOW_PRODUCT_REGEX)).find()) {
                showProducts(matcher.group(1));
            } else if (input.equalsIgnoreCase("filter")) {
                filtering();
            } else if (input.equalsIgnoreCase("sorting")) {
                sorting();
            } else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nOffers menu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
                System.out.println("\nOffers menu\n");
            } else {
                System.out.println("Invalid command!");
            }
        }

    }

    public static void viewOffs() {
        for (Product product : OffersController.sortOffers(currentSort, filters)) {
            Offer offer = Offer.getOfferOfProduct(product);
            System.out.println("Name: " + product.getName());
            System.out.println("ID: " + product.getProductID());
            System.out.println("Actual Price: " + product.getPrice() + "You Have To Pay: " + (product.getPrice() - (product.getPrice() * offer.getOffPercent() / 100.0)));
            System.out.println("Offer Info :" + offer);
            System.out.println("*******");
        }
    }

    public static void showProducts(String attribute) {
        int productID = Integer.parseInt(attribute);
        Product product = Product.getProductByID(productID);
        if (product == null) {
            System.out.println("There isn't any product with this id!");
        }
        ProductMenu.init(product);
    }

    private static void filtering() {
        String input;
        Matcher matcher;
        System.out.println("\nOffers menu -> Filtering submenu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("current filters")) {
                printCurrentFilters();
            } else if (input.equalsIgnoreCase("show available filters")) {
                showAvailableFilters();
            } else if ((matcher = InputManager.getMatcher(input, DISABLE_FILTER_REGEX)).find()) {
                disableFilter(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, FILTER_REGEX)).find()) {
                filter(matcher.group(1));
            } else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nOffers menu -> Filtering submenu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else {
                System.out.println("Invalid command!");
            }
        }

    }

    private static void showAvailableFilters() {
        for (String availableFilter : availableFilters) {
            System.out.print(availableFilter + "\t");
        }
        System.out.println();
        System.out.println("*******");
    }

    private static void filter(String filter) {
        if (availableFilters != null && availableFilters.contains(filter)) {
            filters.add(filter);
            viewOffs();
        } else {
            System.out.println("The filter is invalid!");
        }
    }

    private static void disableFilter(String filter) {
        if (availableFilters == null || !(availableFilters.contains(filter))) {
            System.out.println("The filter is invalid!");
            return;
        }
        if (filters == null || !(filters.contains(filter))) {
            System.out.println("This filter hasn't been selected!");
        }
        filters.remove(filter);
        viewOffs();

    }

    private static void printCurrentFilters() {
        for (String filter : filters) {
            System.out.print(filter + "\t");
        }
        System.out.println();
        System.out.println("*******");
    }

    private static void sorting() {
        String input;
        Matcher matcher;
        System.out.println("\nOffers menu -> Sorting submenu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("current sort")) {
                currentSort();
            } else if (input.equalsIgnoreCase("disable sort")) {
                disableSort();
            } else if (input.equalsIgnoreCase("show available sorts")) {
                showAvailableSorts();
            } else if ((matcher = InputManager.getMatcher(input, SORT_REGEX)).find()) {
                sort(matcher.group(1));
            } else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nOffers menu -> Sorting submenu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else {
                System.out.println("Invalid command!");
            }
        }

    }

    private static void showAvailableSorts() {
        for (String availableSort : availableSorts) {
            System.out.print(availableSort + "\t");
        }
        System.out.println();
        System.out.println("*******");
    }

    private static void sort(String mode) {
        if (availableSorts == null || !(availableSorts.contains(mode))) {
            System.out.println("Mode isn't available!");
            return;
        }
        currentSort = mode;
        viewOffs();
    }

    private static void currentSort() {
        System.out.println("Current sort is: " + currentSort + ".");
    }

    private static void disableSort() {
        currentSort = "visit";
        viewOffs();
    }

    private static void printHelp() {
        System.out.println("List of main commands: ");
        System.out.println("show product [productId]");
        System.out.println("filter");
        System.out.println("sorting");
        System.out.println("help");
        System.out.println("product");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the filter submenu: ");
        System.out.println("filter");
        System.out.println("current filters");
        System.out.println("show available filters");
        System.out.println("disable filter [filter]");
        System.out.println("filter [filter]");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the sorting submenu: ");
        System.out.println("sort [an available sort]");
        System.out.println("current sort");
        System.out.println("disable sort");
        System.out.println("show available sorts");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");
    }

    private static void handleLogin() {
        if (MainMenu.currentUser == null) {
            SignUpAndLoginMenu.init();
        } else {
            System.out.println("You have signed in!");
        }
    }

    private static void handleLogout() {
        if (MainMenu.currentUser == null) {
            System.out.println("You haven't signed in!");
        } else {
            MainMenu.currentUser = null;
            MainMenu.init();
        }
    }
}
