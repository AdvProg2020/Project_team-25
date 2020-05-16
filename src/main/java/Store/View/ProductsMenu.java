package Store.View;

import Store.Controller.ProductsController;
import Store.InputManager;
import Store.Model.Customer;
import Store.Model.Manager;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class ProductsMenu {

    private static final String SHOW_PRODUCT_REGEX = "^show product (\\d+)$";
    private static final String FILTER_REGEX = "^filter (\\w+)$";
    private static final String DISABLE_FILTER_REGEX = "^disable filter (\\w+)$";
    private static final String SORT_REGEX = "^sort (\\w+)$";
    private static final String SEARCH_REGEX = "^search (.+)$";

    private static ArrayList<String> filters = new ArrayList<String>();
    private static ArrayList<String> availableFilters = new ArrayList<String>();
    private static ArrayList<String> availableSorts = new ArrayList<String>(Arrays.asList("rating", "price", "visit", "lexicographical"));
    private static ArrayList<Product> productsToBeShown = new ArrayList<Product>();
    private static String currentSort = "visit";

    public static void init() {
        currentSort = "visit";
        filters = new ArrayList<>();

        System.out.println("\nProducts Menu\n");
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            availableFilters = new ArrayList(Product.getAllFilters());
            if (input.equalsIgnoreCase("view categories")) {
                viewCategories();
            } else if (input.equalsIgnoreCase("filter")) {
                filtering();
                System.out.println("\nProducts Menu\n");
            } else if (input.equalsIgnoreCase("sorting")) {
                sorting();
                System.out.println("\nProducts Menu\n");
            } else if (input.equalsIgnoreCase("show products")) {
                showAllProducts();
            } else if ((matcher = InputManager.getMatcher(input, SEARCH_REGEX)).find()) {
                showProductsWithSearchQuery(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, SHOW_PRODUCT_REGEX)).find()) {
                showProduct(matcher.group(1));
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
                System.out.println("\nProducts Menu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nProducts Menu\n");
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void viewCategories() {
        System.out.println(Manager.showCategories());
    } //

    private static void filtering() {
        String input;
        Matcher matcher;
        showAllProducts();
        System.out.println("\nProducts menu -> Filtering submenu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("show available filters")) {
                showAvailableFilters();
            } else if (input.equalsIgnoreCase("current filters")) {
                printCurrentFilter();
            } else if ((matcher = InputManager.getMatcher(input, FILTER_REGEX)).find()) {
                filter(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, DISABLE_FILTER_REGEX)).find()) {
                disableFilter(matcher.group(1));
            } else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nProducts menu -> Filtering submenu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void printCurrentFilter() {
        for (String filter : filters) {
            System.out.print(filter + "\t");
        }
        System.out.println();
        System.out.println("*******");
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
            if (!filters.contains(filter))
                filters.add(filter);
            else
                System.out.println("Filter is added before!");
        } else {
            System.out.println("The filter is invalid!");
        }
        showAllProducts();
    }

    private static void disableFilter(String filter) {
        if (availableFilters == null || !(availableFilters.contains(filter))) {
            System.out.println("The filter is invalid!");
        } else if (filters == null || !(filters.contains(filter))) {
            System.out.println("This filter hasn't been selected!");
        } else {
            filters.remove(filter);
        }
        showAllProducts();
    }

    private static void sorting() {
        String input;
        Matcher matcher;
        showAllProducts();
        System.out.println("\nOffers menu -> Sorting submenu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("show available sorts")) {
                showAvailableSorts();
            } else if (input.equalsIgnoreCase("current sort")) {
                printCurrentSort();
            } else if (input.equalsIgnoreCase("disable sort")) {
                disableSort();
            } else if ((matcher = InputManager.getMatcher(input, SORT_REGEX)).find()) {
                sort(matcher.group(1));
            } else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nProducts menu -> Sorting submenu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void showAvailableSorts() {
        for (String availableSort : availableSorts) {
            if(availableSorts.get(availableSorts.size() - 1).equals(availableSort))
                System.out.println(availableSort + ".");
            else
                System.out.print(availableSort + ", ");
        }
        System.out.println("*******");
    }

    private static void sort(String mode) {
        if (availableSorts == null || !(availableSorts.contains(mode))) {
            System.out.println("Mode isn't available!");
            return;
        }
        currentSort = mode;
        showAllProducts();
    }

    private static void printCurrentSort() {
        System.out.println("Current sort is: " + currentSort + ".");
    }

    private static void disableSort() {
        currentSort = "visit";
        showAllProducts();
    }

    public static void showAllProducts() {
        productsToBeShown = ProductsController.getFilteredList(filters);
        productsToBeShown = ProductsController.sort(currentSort, productsToBeShown);
        for (Product product : productsToBeShown)
            System.out.println(product);
        System.out.println("*******");
    }

    public static void showProductsWithSearchQuery(String query) {
        productsToBeShown = ProductsController.getFilteredList(filters);
        productsToBeShown = ProductsController.sort(currentSort, productsToBeShown);
        for (Product product : productsToBeShown) {
            if (InputManager.getMatcher(product.getName(), query).find()) {
                System.out.println(product);
            }
        }
        System.out.println("*******");
    }

    private static void showProduct(String attribute) {
        int productId = Integer.parseInt(attribute);
        Product product = Product.getProductByID(productId);
        if (product == null) {
            System.out.println("There isn't any product with this ID!");
            return;
        }
        ProductMenu.init(Product.getProductByID(productId));
    }

    private static void printHelp() {
        System.out.println("*******\n");
        System.out.println("List of main commands:");
        System.out.println("view categories");
        System.out.println("show products");
        System.out.println("show product [productId]");
        System.out.println("search [REGEX]");
        System.out.println("filter");
        System.out.println("sorting");
        System.out.println("offs");
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
        if (MainMenu.currentUser == MainMenu.guest) {
            SignUpAndLoginMenu.init();
            if (MainMenu.currentUser != MainMenu.guest) {
                moveShoppingCart();
            }
        } else {
            System.out.println("You have signed in!");
        }
    }

    private static void moveShoppingCart() {
        if (MainMenu.currentUser instanceof Customer) {
            for (Product product : MainMenu.guest.getCart()) {
                ((Customer) MainMenu.currentUser).addToCart(product);
            }
        }
        MainMenu.guest.getCart().clear();
    }

    private static void handleLogout() {
        if (MainMenu.currentUser == MainMenu.guest) {
            System.out.println("You haven't signed in!");
        } else {
            MainMenu.currentUser = MainMenu.guest;
            MainMenu.init();
        }
    }
}
