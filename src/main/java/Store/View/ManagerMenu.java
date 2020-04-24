package Store.View;

import Store.Controller.ManagerController;
import Store.InputManager;
import Store.Model.*;
import com.google.gson.stream.JsonToken;
import sun.nio.cs.ext.MacArabic;

import javax.jws.soap.SOAPBinding;
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
            } else if (input.equalsIgnoreCase("manage category")) {
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
        if (MainMenu.currentUser == null) {
            MainMenu.init();
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
        ManagerController.createManagerProfile(currentUser);
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
        ManagerController.createOffCode(currentUser);
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
        for (Category allCategory : Manager.allCategories) {
            System.out.println(allCategory);
            System.out.println("*******");
        }
    }

    private static void addCategoryWrapper(String name) {
        ManagerController.addCategory(currentUser, name);
    }

    private static void editCategoryWrapper(String name, String field, String value) {
        ManagerController.editCategory(currentUser, Manager.catagoryByName(name), field, value);
    }

    private static void removeCategoryWrapper(String name) {
        System.out.println(ManagerController.removeCategory(currentUser, Manager.catagoryByName(name)));
    }

    private static void printHelp() {

    }
}
