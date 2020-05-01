package Store.View;

import Store.Controller.ManagerController;
import Store.Controller.ProductsController;
import Store.InputManager;
import Store.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;


public class ManagerMenu {

    private static Manager currentUser;

    private static final String EDIT_PERSONAL_INFO = "^edit (password|family name|first name|email|phone number) ([^\\s]+)$";
    private static final String SHOW_USER_BY_NAME = "^view ([^\\s]+)$";
    private static final String DELETE_USER_BY_NAME = "^delete user ([^\\s]+)$";
    private static final String REMOVE_PRODUCTS = "^remove (\\d+)$";
    private static final String VIEW_DISCOUNT_CODE = "^view discount code ([^\\s]+)$";
    private static final String EDIT_DISCOUNT_CODE = "^edit discount code ([^\\s]+) (maximumOff|offPercentage) ([^\\s]+)$";
    private static final String REMOVE_DISCOUNT_CODE = "^remove discount code ([^\\s]+)$";
    private static final String SHOW_REQUEST_DETAILS = "^details (\\d+)$";
    private static final String ACCEPT_REQUEST = "^accept (\\d+)$";
    private static final String DECLINE_REQUEST = "^decline (\\d+)$";
    private static final String ADD_CATEGORY = "^add ([^\\s]+)$";
    private static final String EDIT_CATEGORY = "^edit ([^\\s]+) (add filter|add product|change name|remove filter|remove product) ([^\\s]+)$";
    private static final String REMOVE_CATEGORY = "^remove ([^\\s]+)$";
    private static final String SORT_BY_REGEX = "^sort by (\\w+)$";
    private static ArrayList<String> availableSortsProducts = new ArrayList<String>(Arrays.asList("rating", "price", "visit", "lexicographical"));
    private static ArrayList<String> availableSortsOffCodes = new ArrayList<String>(Arrays.asList("time of starting", "time of ending", "code", "off percentage", "maximum off", "usage count"));
    private static ArrayList<String> availableSortsUsers = new ArrayList<String>(Arrays.asList("name", "family name", "phone number", "username", "email"));


    public static void init() {
        String input;
        currentUser = (Manager) MainMenu.currentUser;

        System.out.println("\nManager menu\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("view personal info")) {
                viewPersonalInfo();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("manage users")) {
                manageUsers();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("manage all products")) {
                manageAllProducts();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("create discount code")) {
                createOffCodeWrapper();
            } else if (input.equalsIgnoreCase("view discount codes")) {
                viewOffCodes();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("manage requests")) {
                manageRequests();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("manage categories")) {
                manageCategories();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("products")) {
                ProductsMenu.init();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
                System.out.println("\nManager menu\n");
            } else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void viewPersonalInfo() {
        String input;
        Matcher matcher;
        System.out.println("\nManager menu -> View Personal Info\n");
        System.out.println(currentUser);//
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, EDIT_PERSONAL_INFO)).find()) {
                editPersonalInfoWrapper(matcher.group(1), matcher.group(2));
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void editPersonalInfoWrapper(String field, String value) {
        System.out.println(ManagerController.editPersonalInfo(currentUser, field, value));
    }

    private static void manageUsers() {
        String input;
        Matcher matcher;
        System.out.println("\nManager menu -> Manage Users\n");
        showAllUsers();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_USER_BY_NAME)).find()) {
                viewUserByName(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, SORT_BY_REGEX)).find()) {
                sortUsersAndOutput(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, DELETE_USER_BY_NAME)).find()) {
                deleteUserByNameWrapper(matcher.group(1));
            } else if ((input.equalsIgnoreCase("create manager profile"))) {
                createManagerProfileWrapper();
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void sortUsersAndOutput(String mode) {
        if (availableSortsUsers == null || !availableSortsUsers.contains(mode)) {
            System.out.println("Mode isn't available!");
            return;
        }
        for (User user : ManagerController.sortUsers(mode, User.getAllUsers())) {
            System.out.println(user);
            System.out.println("*******");
        }
    }

    private static void showAllUsers() {
        for (User user : User.getAllUsers()) {
            System.out.println(user);
            System.out.println("*******");
        }
    }

    private static void viewUserByName(String username) {
        if (User.getUserByUsername(username) == null) {
            System.out.println("There isn't any user with this username!");
            return;
        }
        System.out.println(User.getUserByUsername(username));
    }

    private static void deleteUserByNameWrapper(String username) {
        System.out.println(ManagerController.deleteUserByName(currentUser, username));
    }

    private static void createManagerProfileWrapper() {
        String username, password, firstName, lastName, email, phoneNumber;
        System.out.println("Username: ");
        while (User.getUserByUsername(username = InputManager.getNextLine()) != null) {
            System.out.println("There is similar username and it's invalid!");
        }
        System.out.println("Password: ");
        while ((password = InputManager.getNextLine()).matches("^[a-zA-Z]\\w{3,14}$")) {
            System.out.println("The format is invalid!");
        }
        System.out.println("First Name: ");
        firstName = InputManager.getNextLine();
        System.out.println("Last Name: ");
        lastName = InputManager.getNextLine();
        System.out.println("Email: ");
        email = InputManager.getNextLine();
        System.out.println("Phone Number: ");
        while (!(phoneNumber = InputManager.getNextLine()).matches("^[0-9]+$")) {
            System.out.println("Invalid format!");
        }
        ManagerController.createManagerProfile(currentUser, username, firstName, lastName, email, phoneNumber, password);
    }

    private static void manageAllProducts() {
        String input;
        Matcher matcher;
        System.out.println("\nManager menu -> Manage All Products\n");
        ProductsMenu.showAllProducts();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, REMOVE_PRODUCTS)).find()) {
                removeProductsWrapper(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, SORT_BY_REGEX)).find()) {
                sortProductsAndOutput(matcher.group(1));
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void sortProductsAndOutput(String mode) {
        if (availableSortsProducts == null || !availableSortsProducts.contains(mode)) {
            System.out.println("Mode isn't available!");
            return;
        }
        for (Product product : ProductsController.sort(mode, Product.getAllProducts())) {
            System.out.println("{" + product.getName() + " " + product.getProductID() + ", " + product.getCategory().getFullName()
                    + ", " + (product.getAvailablity() ? "available" : "unavailable") + "}");
        }
    }

    private static void removeProductsWrapper(String productID) {
        System.out.println(ManagerController.removeProducts(currentUser, Product.getProductByID(Integer.parseInt(productID))));
    }

    private static void createOffCodeWrapper() {
        double offPercentage, maximumOff;
        int usageCount;
        String[] time;
        Date startingDate, endingDate;
        String code;
        System.out.println("Code: ");
        code = InputManager.getNextLine();
        System.out.println("offPercentage: ");
        while (true) {
            try {
                offPercentage = Double.parseDouble(InputManager.getNextLine());
                break;
            } catch (Exception exception) {
                System.out.println("Invalid format!");
            }
        }
        System.out.println("maximumOff: ");
        while (true) {
            try {
                maximumOff = Double.parseDouble(InputManager.getNextLine());
                break;
            } catch (Exception exception) {
                System.out.println("Invalid format!");
            }
        }
        System.out.println("usageCount: ");
        while (true) {
            try {
                usageCount = Integer.parseInt(InputManager.getNextLine());
                break;
            } catch (Exception exception) {
                System.out.println("Invalid format!");
            }
        }
        System.out.println("Print The Starting Date With This Format (year/month/day): ");
        while (true) {
            try {
                time = InputManager.getNextLine().split("/");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
                startingDate = calendar.getTime();
                break;
            } catch (Exception exception) {
                System.out.println("Invalid format!");
            }
        }
        System.out.println("Print The Ending Date With This Format (year/month/day): ");
        while (true) {
            try {
                time = InputManager.getNextLine().split("/");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
                endingDate = calendar.getTime();
                break;
            } catch (Exception exception) {
                System.out.println("Invalid format!");
            }
        }
        ManagerController.createOffCode(currentUser, code, offPercentage, maximumOff, usageCount, startingDate, endingDate);
    }

    private static void viewOffCodes() {
        String input;
        Matcher matcher;
        System.out.println("\nManager menu -> View Off Codes\n");
        showAllOffCodes();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, VIEW_DISCOUNT_CODE)).find()) {
                viewOffCode(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, SORT_BY_REGEX)).find()) {
                sortOffCodesAndOutput(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, REMOVE_DISCOUNT_CODE)).find()) {
                removeOffCodeWrapper(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, EDIT_DISCOUNT_CODE)).find()) {
                editOffCodeWrapper(matcher.group(1), matcher.group(2), matcher.group(3));
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void sortOffCodesAndOutput(String mode) {
        if (availableSortsOffCodes == null || !availableSortsOffCodes.contains(mode)) {
            System.out.println("Mode isn't available!");
            return;
        }
        for (OffCode offCode : ManagerController.sortOffCodes(mode, Manager.getOffCodes())) {
            System.out.println(offCode);
            System.out.println("*******");
        }
    }

    private static void showAllOffCodes() {
        for (OffCode offCode : currentUser.getOffCodes()) {
            System.out.println(offCode);
            System.out.println("*******");
        }
    }

    private static void removeOffCodeWrapper(String attribute) {
        System.out.println(ManagerController.removeOffCode(currentUser, attribute));
    }

    private static void editOffCodeWrapper(String code, String field, String value) {
        System.out.println(ManagerController.editOffCode(currentUser, Manager.getOffCodeByCode(code), field, value));
    }

    private static void viewOffCode(String attribute) {
        if (Manager.getOffCodeByCode(attribute) == null) {
            System.out.println(Manager.getOffCodeByCode(attribute));
            return;
        }
        System.out.println("There isn't any offCode with this code!");
    }

    private static void manageRequests() {
        String input;
        Matcher matcher;
        System.out.println("\nManager menu -> Manage Requests\n");
        showAllRequests();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_REQUEST_DETAILS)).find()) {
                manageRequestDetails(Integer.parseInt(matcher.group(1)));
            }
            else if ((matcher = InputManager.getMatcher(input, ACCEPT_REQUEST)).find()) {
                handleRequestWrapper(true, Integer.parseInt(matcher.group(1)));
            }
            else if ((matcher = InputManager.getMatcher(input, DECLINE_REQUEST)).find()) {
                handleRequestWrapper(false, Integer.parseInt(matcher.group(1)));
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void manageRequestDetails(int id) {
        if (Manager.getRequestById(id) == null) {
            System.out.println("There isn't any request with this id!");
        }
        System.out.println(Manager.getRequestById(id));
    }

    private static void showAllRequests() {
        for (Request pendingRequest : Manager.getPendingRequests()) {
            System.out.println(pendingRequest);
            System.out.println("*******");
        }
    }

    private static void handleRequestWrapper(boolean status, int id) {
        if (Manager.getRequestById(id) == null) {
            System.out.println("There isn't any request with this id!");
            return;
        }
        System.out.println("Status saved.");
        ManagerController.handleRequest(currentUser, status, Manager.getRequestById(id));
    }

    private static void manageCategories() {
        String input;
        Matcher matcher;
        System.out.println("\nManager menu -> Manage Categories\n");
        showAllCategories();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, ADD_CATEGORY)).find()) {
                addCategoryWrapper(matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, EDIT_CATEGORY)).find()) {
                editCategoryWrapper(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            else if ((matcher = InputManager.getMatcher(input, REMOVE_CATEGORY)).find()) {
                removeCategoryWrapper(matcher.group(1));
            } else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void showAllCategories() {
        for (Category category : Manager.getAllCategories()) {
            System.out.println(category);
            System.out.println("*******");
        }
    }

    private static void addCategoryWrapper(String name) {
        if (Manager.categoryByName(name) != null) {
            System.out.println("Process failed: the category exists");
            return;
        }
        String parent;
        System.out.println("Parent: ");
        while (Manager.categoryByName(parent = InputManager.getNextLine()) == null && !(parent.equalsIgnoreCase("null"))) {
            System.out.println("You should print null if it hasn't any parent or its valid parent name");
        }
        ManagerController.addCategory(currentUser, name, parent);
    }

    private static void editCategoryWrapper(String name, String field, String value) {
        ManagerController.editCategory(currentUser, Manager.categoryByName(name), field, value);
    }

    private static void removeCategoryWrapper(String name) {
        System.out.println(ManagerController.removeCategory(currentUser, Manager.categoryByName(name)));
    }

    private static void printHelp() {
        System.out.println("List of main commands: ");
        System.out.println("view personal info");
        System.out.println("manage users");
        System.out.println("manage all products");
        System.out.println("create discount code");
        System.out.println("view discount codes");
        System.out.println("manage requests");
        System.out.println("manage categories");
        System.out.println("offs");
        System.out.println("products");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the view personal info submenu: ");
        System.out.println("edit [password|family name|first name|email|phone number] [value]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the manage users submenu: ");
        System.out.println("view [username]");
        System.out.println("delete user [username]");
        System.out.println("create manager profile");
        System.out.println("sort by [name | family name | phone number | username | email]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the manage all products: ");
        System.out.println("remove [productID]");
        System.out.println("sort by [rating | price | visit | lexicographical]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the view discount codes submenu: ");
        System.out.println("view discount code [code]");
        System.out.println("edit discount code [code] [maximumOff|offPercentage] [value]");
        System.out.println("remove discount code [code]");
        System.out.println("sort by [time of starting | time of ending | code | off percentage | maximum off | usage count]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the manage requests submenu: ");
        System.out.println("details [requestID]");
        System.out.println("[accept|decline] [requestID]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the manage categories submenu: ");
        System.out.println("add category [name]");
        System.out.println("edit category [code] [add filter|add product|change name|remove filter|remove product] [value]");
        System.out.println("remove category [name]");
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
