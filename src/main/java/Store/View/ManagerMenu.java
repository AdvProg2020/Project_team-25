package Store.View;

import Store.Controller.ManagerController;
import Store.InputManager;
import Store.Model.*;
import com.google.gson.stream.JsonToken;
import sun.nio.cs.ext.MacArabic;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;


public class ManagerMenu {

    private static Manager currentUser;

    private static final String EDIT_PERSONAL_INFO = "^edit (password|family name|first name|email|phone number) ([^\\s]+)$";
    private static final String SHOW_USER_BY_NAME = "^view ([^\\s]+)$";
    private static final String DELETE_USER_BY_NAME = "^delete ([^\\s]+)$";
    private static final String REMOVE_PRODUCTS = "^remove (\\d+)$";
    private static final String VIEW_DISCOUNT_CODE = "^view discount code ([^\\s]+)$";
    private static final String EDIT_DISCOUNT_CODE = "^edit discount code ([^\\s]+) (maximumOff|offPercentage) ([^\\s]+)$";
    private static final String REMOVE_DISCOUNT_CODE = "^remove discount code ([^\\s]+)$";
    private static final String SHOW_REQUEST_DETAILS = "^details (\\d+)$";
    private static final String ACCEPT_REQUEST = "^accept (\\d+)$";
    private static final String DECLINE_REQUEST = "^decline (\\d+)$";
    private static final String ADD_CATEGORY = "^add ([^\\s]+)$";
    private static final String EDIT_CATEGORY = "^edit ([^\\s]+) (add filter|add product|change name|remove filter|add filter) ([^\\s]+)$";
    private static final String REMOVE_CATEGORY = "^remove ([^\\s]+)$";


    public static void init() {
        String input;
        currentUser = (Manager) MainMenu.currentUser;

        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("view personal info")) {
                viewPersonalInfo();
            } else if (input.equalsIgnoreCase("manage users")) {
                manageUsers();
            } else if (input.equalsIgnoreCase("manage all products")) {
                manageAllProducts();
            } else if (input.equalsIgnoreCase("create discount code")) {
                createOffCodeWrapper();
            } else if (input.equalsIgnoreCase("view discount codes")) {
                viewOffCodes();
            } else if (input.equalsIgnoreCase("manage requests")) {
                manageRequests();
            } else if (input.equalsIgnoreCase("manage categories")) {
                manageCategories();
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("offs")) {
                OffersMenu.init();
            } else if (input.equalsIgnoreCase("logout")) {
                MainMenu.currentUser = null;
                break;
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void viewPersonalInfo() {
        String input;
        Matcher matcher;
        System.out.println(currentUser);//
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, EDIT_PERSONAL_INFO)).find()) {
                editPersonalInfoWrapper(matcher.group(1), matcher.group(2));
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void editPersonalInfoWrapper(String field, String value) {
        System.out.println(ManagerController.editPersonalInfo(currentUser, field, value));
    }

    private static void manageUsers() {
        String input;
        Matcher matcher;
        showAllUsers();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_USER_BY_NAME)).find()) {
                viewUserByName(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, DELETE_USER_BY_NAME)).find()) {
                deleteUserByNameWrapper(matcher.group(1));
            } else if ((input.equalsIgnoreCase("create manager profile"))) {
                createManagerProfileWrapper();
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void showAllUsers() {
        for (User allUser : User.getAllUsers()) {
            System.out.println(allUser);
            System.out.println("*******");
        }
    }

    private static void viewUserByName(String username) {
        if (User.getUserByUsername(username) == null) {
            System.out.println("there isn't any user with this username");
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
            System.out.println("there is similar username and it's invalid");
        }
        System.out.println("Password: ");
        while ((password = InputManager.getNextLine()).matches("^[a-zA-Z]\\w{3,14}$")) {
            System.out.println("the format is invalid");
        }
        System.out.println("First Name: ");
        firstName = InputManager.getNextLine();
        System.out.println("Last Name: ");
        lastName = InputManager.getNextLine();
        System.out.println("Email: ");
        email = InputManager.getNextLine();
        System.out.println("Phone Number: ");
        while ((phoneNumber = InputManager.getNextLine()).matches("^[0-9]\\w{3,10}$")) {
            System.out.println("the format is invalid");
        }
        ManagerController.createManagerProfile(currentUser, username, firstName, lastName, email, phoneNumber, password);
    }

    private static void manageAllProducts() {
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, REMOVE_PRODUCTS)).find()) {
                removeProductsWrapper(matcher.group(1));
            } else {
                System.out.println("invalid command");
            }
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
                System.out.println("the format is invalid");
            }
        }
        System.out.println("maximumOff: ");
        while (true) {
            try {
                maximumOff = Double.parseDouble(InputManager.getNextLine());
                break;
            } catch (Exception exception) {
                System.out.println("the format is invalid");
            }
        }
        System.out.println("usageCount: ");
        while (true) {
            try {
                usageCount = Integer.parseInt(InputManager.getNextLine());
                break;
            } catch (Exception exception) {
                System.out.println("the format is invalid");
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
                System.out.println("the format is invalid");
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
                System.out.println("the format is invalid");
            }
        }
        ManagerController.createOffCode(currentUser, code, offPercentage, maximumOff, usageCount, startingDate, endingDate);
    }

    private static void viewOffCodes() {
        String input;
        Matcher matcher;
        showAllOffCodes();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, VIEW_DISCOUNT_CODE)).find()) {
                viewOffCode(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, REMOVE_DISCOUNT_CODE)).find()) {
                removeOffCodeWrapper(matcher.group(1));
            } else if ((matcher = InputManager.getMatcher(input, EDIT_DISCOUNT_CODE)).find()) {
                editOffCodeWrapper(matcher.group(1), matcher.group(2), matcher.group(3));
            } else {
                System.out.println("invalid command");
            }
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
        System.out.println("there isn't any offCode with this code");
    }

    private static void manageRequests() {
        String input;
        Matcher matcher;
        showAllRequests();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, SHOW_REQUEST_DETAILS)).find()) {
                manageRequestDetails(Integer.parseInt(matcher.group(1)));
            }
            if ((matcher = InputManager.getMatcher(input, ACCEPT_REQUEST)).find()) {
                handleRequestWrapper(true, Integer.parseInt(matcher.group(1)));
            }
            if ((matcher = InputManager.getMatcher(input, DECLINE_REQUEST)).find()) {
                handleRequestWrapper(false, Integer.parseInt(matcher.group(1)));
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void manageRequestDetails(int id) {
        if (Manager.getRequestById(id) == null) {
            System.out.println("there isn't any request with this id");
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
            System.out.println("there isn't any request with this id");
        }
        System.out.println("status saved");
        ManagerController.handleRequest(currentUser, status, Manager.getRequestById(id));
    }

    private static void manageCategories() {
        String input;
        Matcher matcher;
        showAllCategories();
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, ADD_CATEGORY)).find()) {
                addCategoryWrapper(matcher.group(1));
            }
            if ((matcher = InputManager.getMatcher(input, EDIT_CATEGORY)).find()) {
                editCategoryWrapper(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            if ((matcher = InputManager.getMatcher(input, REMOVE_CATEGORY)).find()) {
                removeCategoryWrapper(matcher.group(1));
            } else {
                System.out.println("invalid command");
            }
        }
    }

    private static void showAllCategories() {
        for (Category allCategory : Manager.getAllCategories()) {
            System.out.println(allCategory);
            System.out.println("*******");
        }
    }

    private static void addCategoryWrapper(String name) {
        if (Manager.catagoryByName(name) != null) {
            System.out.println("process failed: the category exists");
            return;
        }
        String parent;
        System.out.println("Parent: ");
        while (Manager.catagoryByName(parent = InputManager.getNextLine()) == null || !(parent.equalsIgnoreCase("null"))) {
            System.out.println("you should print null if it hasn't any parent or its valid parent name");
        }
        ManagerController.addCategory(currentUser, name, parent);
    }

    private static void editCategoryWrapper(String name, String field, String value) {
        ManagerController.editCategory(currentUser, Manager.catagoryByName(name), field, value);
    }

    private static void removeCategoryWrapper(String name) {
        System.out.println(ManagerController.removeCategory(currentUser, Manager.catagoryByName(name)));
    }

    private static void printHelp() {
        System.out.println("list of commands: ");
        System.out.println("view personal info");
        System.out.println("manage users");
        System.out.println("manage all products");
        System.out.println("create discount code");
        System.out.println("view discount codes");
        System.out.println("manage requests");
        System.out.println("manage categories");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the view personal info submenu: ");
        System.out.println("edit [password|family name|first name|email|phone number] [value]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the manage users submenu: ");
        System.out.println("view [username]");
        System.out.println("delete user [username]");
        System.out.println("create manager profile");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the manage all products: ");
        System.out.println("remove [productID]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the view discount codes submenu: ");
        System.out.println("view discount code [code]");
        System.out.println("edit discount code [code] [maximumOff|offPercentage] [value]");
        System.out.println("remove discount code [code]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the manage requests submenu: ");
        System.out.println("details [requestID]");
        System.out.println("[accept|decline] [requestID]");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nlist of commands in the manage categories submenu: ");
        System.out.println("add category [name]");
        System.out.println("edit category [code] [add filter|add product|change name|remove filter|add filter] [value]");
        System.out.println("remove category [name]");
        System.out.println("back");
        System.out.println("*******");
    }
}
