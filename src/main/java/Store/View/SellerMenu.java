package Store.View;

import Store.Controller.OffersController;
import Store.Controller.ProductsController;
import Store.Controller.SellerController;
import Store.InputManager;
import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import Store.Model.Log.SellLogItem;
import org.codehaus.plexus.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;

public class SellerMenu {

    private static final String EDIT_PERSONAL_INFO_REGEX = "^edit (.+)$";
    private static final String VIEW_PRODUCT_REGEX = "^view (\\d+)$";
    private static final String VIEW_PRODUCT_BUYERS_INFO_REGEX = "^view buyers (\\d+)$";
    private static final String EDIT_PRODUCT_REGEX = "^edit (\\d+)$";
    private static final String ADD_FILTER_REGEX = "^add filter (\\d+) ([^\\s]+)$";
    private static final String REMOVE_FILTER_REGEX = "^remove filter (\\d+) ([^\\s]+)$";
    private static final String REMOVE_PRODUCT_REGEX = "^remove product (\\d+)$";
    private static final String VIEW_OFFER_REGEX = "^view (\\d+)$";
    private static final String EDIT_OFFER_REGEX = "^edit (\\d+)$";
    private static final String SORT_BY_REGEX = "^sort by (\\w+)$";
    private static ArrayList<String> availableSortsProducts = new ArrayList<String>(Arrays.asList("rating", "price", "visit", "lexicographical"));
    private static ArrayList<String> availableSortsOffs = new ArrayList<String>(Arrays.asList("time of starting", "time of ending"));


    public static void init() {
        String input;
        Matcher matcher;
        System.out.println("\nSeller menu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("view personal info")) {
                viewPersonaInfo((Seller) MainMenu.currentUser);
                System.out.println("\nSeller menu\n");
            }
            else if (input.equalsIgnoreCase("view company information")) {
                viewCompanyInformation((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("view sales history")) {
                viewSalesHistory((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("manage products")) {
                manageProduct((Seller) MainMenu.currentUser);
                System.out.println("\nSeller menu\n");
            }
            else if (input.equalsIgnoreCase("add product")) {
                addProductWrapper((Seller) MainMenu.currentUser);
            }
            else if ((matcher = InputManager.getMatcher(input, REMOVE_PRODUCT_REGEX)).find()) {
                removeProductWrapper((Seller) MainMenu.currentUser, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("show categories")) {
                showCategories();
            }
            else if (input.equalsIgnoreCase("view offs")) {
                viewOffs((Seller) MainMenu.currentUser);
                System.out.println("\nSeller menu\n");
            }
            else if (input.equalsIgnoreCase("view balance")) {
                viewBalance((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
                System.out.println("\nSeller menu\n");
            }
            else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
                System.out.println("\nSeller menu\n");
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void viewPersonaInfo(Seller seller) {
        System.out.println(seller.toString());

        String input;
        Matcher matcher;
        System.out.println("\nSeller menu -> View Personal Info\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, EDIT_PERSONAL_INFO_REGEX)).find()) {
                editPersonalInfoWrapper(seller, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else {
                System.out.println("Invalid command");
            }
        }
    }

    private static void editPersonalInfoWrapper(Seller seller, String field) {
        if (field.equalsIgnoreCase("username")) {
            System.out.println("This field cannot be edited!");
        }
        else if (!SellerController.isValidField(field)) {
            System.out.println("This is not a valid field!");
        }
        else {
            String value;
            if (field.equalsIgnoreCase("password")) {
                System.out.println("Please enter your password: ");
                String passwordGuess = InputManager.getNextLine();
                if (seller.validatePassword(passwordGuess)) {
                    System.out.println("Please enter your new password: ");
                    value = InputManager.getNextLine();
                }
                else {
                    System.out.println("The password you entered is incorrect!");
                    return;
                }
            }
            else {
                System.out.println("Value: ");
                value = InputManager.getNextLine();
            }
            try {
                SellerController.editPersonalInfo(seller, field, value);
            }
            catch (SellerController.InvalidValueException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private static void viewCompanyInformation(Seller seller) {
        System.out.println("Company name: " + seller.getCompanyName());
        if (!seller.getCompanyDescription().isEmpty()) {
            System.out.println("Additional info: " + seller.getCompanyDescription());
        }
    }

    private static void viewSalesHistory(Seller seller) {
        for (SellLogItem sellLogItem : seller.getSellLog()) {
            System.out.println(sellLogItem.toString());
            System.out.println("________________________________");
        }
    }

    private static void manageProduct(Seller seller) {
        System.out.println("\nSeller menu -> Manage Products\n");
        for (Product product : seller.getProducts()) {
            System.out.println("{" + product.getName() + " " + product.getProductID() + ", " + product.getCategory().getFullName()
                                + ", " + (product.getAvailablity() ? "available" : "unavailable") + "}");
        }

        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, VIEW_PRODUCT_REGEX)).find()) {
                viewProduct(seller, matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, SORT_BY_REGEX)).find()) {
                sortProductsAndOutput(seller, matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, VIEW_PRODUCT_BUYERS_INFO_REGEX)).find()) {
                viewBuyers(seller, matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, EDIT_PRODUCT_REGEX)).find()) {
                editProductWrapper(seller, matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, ADD_FILTER_REGEX)).find()) {
                System.out.println(SellerController.addFilterToProduct(seller, matcher.group(1), matcher.group(2)));
            }
            else if ((matcher = InputManager.getMatcher(input, REMOVE_FILTER_REGEX)).find()) {
                System.out.println(SellerController.removeFilterFromProduct(seller, matcher.group(1), matcher.group(2)));
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void sortProductsAndOutput(Seller seller, String mode) {
        if (availableSortsProducts == null || !availableSortsProducts.contains(mode)) {
            System.out.println("Mode isn't available!");
            return;
        }
        for (Product product : ProductsController.sort(mode, seller.getProducts())) {
            System.out.println("{" + product.getName() + " " + product.getProductID() + ", " + product.getCategory().getFullName()
                    + ", " + (product.getAvailablity() ? "available" : "unavailable") + "}");
        }
    }

    private static void viewProduct(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && SellerController.isValidID(seller, Integer.parseInt(attribute))) {
            ProductMenu.init(Product.getProductByID(Integer.parseInt(attribute)));
        }
        else {
            System.out.println("Invalid ID!");
        }
    }

    private static void viewBuyers(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && SellerController.isValidID(seller, Integer.parseInt(attribute))) {
            ArrayList<String> buyers = new ArrayList<String>();
            Product key = Product.getProductByID(Integer.parseInt(attribute));
            for (SellLogItem sellLogItem : seller.getSellLog()) {
                for (Product product : sellLogItem.getProducts()) {
                    if (product == key) {
                        buyers.add(sellLogItem.getCustomerName());
                        break;
                    }
                }
            }
            buyers = SellerController.makeBuyersUnique(buyers);
            System.out.println("Buyers of selected product: ");
            for (String buyer : buyers) {
                System.out.print(buyer + "   ");
            }
            System.out.println();
        }
        else {
            System.out.println("Invalid ID!");
        }
    }

    private static void editProductWrapper(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && SellerController.isValidID(seller, Integer.parseInt(attribute))) {
            Product product = createProduct(seller);
            if (product != null) {
                SellerController.editProduct(seller, Product.getProductByID(Integer.parseInt(attribute)), product);
            }
        }
        else {
            System.out.println("Invalid ID!");
        }
    }

    private static Product createProduct(Seller seller) {
        System.out.println("Category ID: ");
        String attribute = InputManager.getNextLine();
        if (!StringUtils.isNumeric(attribute) || !Category.hasCategoryWithId(Integer.parseInt(attribute))) {
            System.out.println("Invalid category ID!");
            return null;
        }
        Category parent = Category.getCategoryById(Integer.parseInt(attribute));

        System.out.println("Product Name: ");
        String name = InputManager.getNextLine();

        System.out.println("Product brand: ");
        String brand = InputManager.getNextLine();

        System.out.println("Product price: ");
        attribute = InputManager.getNextLine();
        if (!attribute.matches("^\\d+\\.\\d+$")) {
            System.out.println("Invalid price value!");
            return null;
        }
        double price = Double.parseDouble(attribute);

        System.out.println("Product availability (0 | 1): ");
        attribute = InputManager.getNextLine();
        if (!attribute.matches("^(0|1)$")) {
            System.out.println("Invalid availability value!");
            return null;
        }
        boolean availability = (attribute.equals("1"));

        System.out.println("Filters: (end with inputting -1)");
        String filter;
        ArrayList<String> filters = new ArrayList<String>();
        while ((filter = InputManager.getNextLine()).equalsIgnoreCase("-1")) {
            if (filters.contains(filter)) {
                System.out.println("You have already added this filter!");
            }
            else {
                filters.add(filter);
            }
        }

        System.out.println("Description: ");
        String description = InputManager.getNextLine();

        Product product = new Product(CheckingStatus.CREATION, parent, name, seller, brand, price, availability, description);
        for (String filterToAdd : filters) {
            product.addFilter(filter);
        }
        return product;
    }

    private static void addProductWrapper(Seller seller) {
        showCategories();
        System.out.println("Please enter the required information for this product: ");
        Product product = createProduct(seller);
        if (product != null) {
            SellerController.addProduct(seller, product);
        }
    }

    private static void removeProductWrapper(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && SellerController.isValidID(seller, Integer.parseInt(attribute))) {
            SellerController.removeProduct(seller, Product.getProductByID(Integer.parseInt(attribute)));
        }
        else {
            System.out.println("Invalid ID!");
        }
    }

    private static void showCategories() {
        System.out.println("--- Categories: ");
        ArrayList<String> result = new ArrayList<String>();
        for (Category category : Manager.getAllCategories()) {
            result.add(category.getFullName() + "   " + category.getId());
        }
        Collections.sort(result);
        for (String categoryData : result) {
            System.out.println(categoryData);
        }
    }

    private static void viewOffs(Seller seller) {
        System.out.println("\nSeller menu -> View Offs\n");
        System.out.println("Your offers: ");
        for (Offer offer : SellerController.getOffersOfThisSeller(seller)) {
            System.out.println(offer.toString());
        }

        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, VIEW_OFFER_REGEX)).find()) {
                viewOff(matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, SORT_BY_REGEX)).find()) {
                sortOffsAndOutput(seller, matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, EDIT_OFFER_REGEX)).find()) {
                editOffWrapper(seller, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("add off")) {
                addOffWrapper(seller);
            }
            else if ((matcher = InputManager.getMatcher(input, ADD_FILTER_REGEX)).find()) {
                System.out.println(SellerController.addFilterToOffer(seller, matcher.group(1), matcher.group(2)));
            }
            else if ((matcher = InputManager.getMatcher(input, REMOVE_FILTER_REGEX)).find()) {
                System.out.println(SellerController.removeFilterFromOffer(seller, matcher.group(1), matcher.group(2)));
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void sortOffsAndOutput(Seller seller, String mode) {
        if (availableSortsOffs == null || !availableSortsOffs.contains(mode)) {
            System.out.println("Mode isn't available!");
            return;
        }
        for (Offer offer : OffersController.sort(mode, seller.getOffers())) {
            System.out.println(offer.toString());
        }
    }

    private static void getOfferProducts(Seller seller, Offer offer) {
        String input;

        System.out.println("Your products: ");
        for (Product product : seller.getProducts()) {
            System.out.println("{" + product.getName() + " " + product.getProductID() + ", " + product.getCategory().getFullName()
                    + ", " + (product.getAvailablity() ? "available" : "unavailable") + "}");
        }

        System.out.println("Select products for this off by inputting their ids, finish by inputting -1");
        while (!(input = InputManager.getNextLine()).equals("-1")) {
            if (!StringUtils.isNumeric(input) || !SellerController.isProductFromThisSeller(seller, Product.getProductByID(Integer.parseInt(input)))) {
                System.out.println("Please enter a valid ID");
            }
            Product product = Product.getProductByID(Integer.parseInt(input));
            if (offer.containsProduct(product)) {
                System.out.println("Already added this product");
            }
            else {
                offer.addProduct(product);
            }
        }
    }

    private static Offer createOffer(Seller seller) {
        System.out.println("Please enter dates and times in the following format: dd-M-yyyy hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        System.out.println("Starting time: ");
        String input = InputManager.getNextLine();
        Date startingTime, endingTime;
        try {
            startingTime = dateFormat.parse(input);
        }
        catch (ParseException exception) {
            System.out.println("Invalid date format!");
            return null;
        }

        System.out.println("Ending time: ");
        input = InputManager.getNextLine();
        try {
            endingTime = dateFormat.parse(input);
        }
        catch (ParseException exception) {
            System.out.println("Invalid date format!");
            return null;
        }

        System.out.println("Off Percentage: ");
        input = InputManager.getNextLine();
        if (!input.matches("\\d+\\.\\d+")) {
            System.out.println("Invalid percentage!");
            return null;
        }
        double offPercentage = Double.parseDouble(input);

        Offer offer = new Offer(seller, CheckingStatus.CREATION, offPercentage);
        offer.setStartingTime(startingTime);
        offer.setEndingTime(endingTime);

        getOfferProducts(seller, offer);

        System.out.println("Select filters for this off, finish by inputting -1");
        while (!(input = InputManager.getNextLine()).equals("-1")) {
            offer.addFilter(input);
        }

        return offer;
    }

    private static void viewOff(String attribute) {
        if (StringUtils.isNumeric(attribute) && Offer.hasOfferByID(Integer.parseInt(attribute))) {
            Offer offer = Offer.getOfferByID(Integer.parseInt(attribute));
            System.out.println(offer);
            System.out.println("Products: ");
            for (Product product : offer.getProducts()) {
                System.out.println(product);
            }
        }
        else {
            System.out.println("Invalid ID!");
        }
    }

    private static void editOffWrapper(Seller seller, String attribute) {
        if (!StringUtils.isNumeric(attribute) || !Offer.hasOfferByID(Integer.parseInt(attribute))) {
            System.out.println("Invalid ID!");
            return;
        }
        Offer offer = createOffer(seller);
        if (offer != null) {
            SellerController.editOff(seller, Offer.getOfferByID(Integer.parseInt(attribute)), createOffer(seller));
        }
    }

    private static void addOffWrapper(Seller seller) {
        Offer offer = createOffer(seller);
        if (offer != null) {
            SellerController.addOff(seller, createOffer(seller));
        }
    }

    private static void viewBalance(Seller seller) {
        System.out.println("Your current account balance: " + seller.getMoney());
    }

    private static void printHelp() {
        System.out.println("List of main commands: ");
        System.out.println("view personal info");
        System.out.println("view company information");
        System.out.println("view sales history");
        System.out.println("manage products");
        System.out.println("add product");
        System.out.println("remove product [productId]");
        System.out.println("show categories");
        System.out.println("view offs");
        System.out.println("view balance");
        System.out.println("offs");
        System.out.println("products");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the 'view personal info' submenu: ");
        System.out.println("edit [field]");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the 'manage products' submenu: ");
        System.out.println("view [productId]");
        System.out.println("view buyers [productId]");
        System.out.println("edit [productId]");
        System.out.println("add filter");
        System.out.println("remove filter");
        System.out.println("sort by [rating | price | visit | lexicographical]");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the 'view offs' submenu: ");
        System.out.println("view [offId]");
        System.out.println("edit [offId]");
        System.out.println("add off");
        System.out.println("add filter");
        System.out.println("remove filter");
        System.out.println("sort by [time of starting | time of ending]");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");
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
