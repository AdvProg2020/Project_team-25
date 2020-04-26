package Store.View;

import Store.Controller.SellerController;
import Store.InputManager;
import Store.Model.*;
import Store.Model.Log.SellLogItem;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

public class SellerMenu {

    public static final String EDIT_PERSONAL_INFO_REGEX = "^edit (.+)$";
    public static final String VIEW_PRODUCT_REGEX = "^view (\\d+)$";
    public static final String VIEW_PRODUCT_BUYERS_INFO_REGEX = "^view buyers (\\d+)$";
    public static final String EDIT_PRODUCT_REGEX = "^edit (\\d+)$";


    public static void init() {
        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("view personal info")) {
                viewPersonaInfo((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("view company information")) {
                viewCompanyInformation((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("view sales history")) {
                viewSalesHistory((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("manage products")) {
                manageProduct((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("view balance")) {
                viewBalance((Seller) MainMenu.currentUser);
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
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

    private static void viewPersonaInfo(Seller seller) {
        System.out.println(seller.toString());

        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if ((matcher = InputManager.getMatcher(input, EDIT_PERSONAL_INFO_REGEX)).find()) {
                editPersonalInfoWrapper(seller, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
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

    private static boolean isValidField(String field) {
        if (field.equalsIgnoreCase("name") || field.equalsIgnoreCase("family name")) {
            return true;
        }
        else if (field.equalsIgnoreCase("email") || field.equalsIgnoreCase("phone number")) {
            return true;
        }
        else if (field.equalsIgnoreCase("password")) {
            return true;
        }
        else if (field.equalsIgnoreCase("company name") || field.equalsIgnoreCase("company description")) {
            return true;
        }
        return false;
    }

    private static void editPersonalInfoWrapper(Seller seller, String field) {
        if (field.equalsIgnoreCase("username")) {
            System.out.println("This fiels cannot be edited");
        }
        else if (!isValidField(field)) {
            System.out.println("This is not a valid field");
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
                    System.out.println("The password you entered is incorrect");
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
            catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private static void viewCompanyInformation(Seller seller) {
        System.out.printf("Company name: " + seller.getCompanyName());
        if (!seller.getCompanyDescription().isEmpty()) {
            System.out.printf("Additional info: " + seller.getCompanyDescription());
        }
    }

    private static void viewSalesHistory(Seller seller) {
        for (SellLogItem sellLogItem : seller.getSellLog()) {
            System.out.println(sellLogItem.toString());
            System.out.println("________________________________");
        }
    }

    private static boolean isValidID(Seller seller, int id) {
        for (Product product : seller.getProducts()) {
            if (product.getProductID() == id) {
                return true;
            }
        }
        return false;
    }

    private static void manageProduct(Seller seller) {
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
            else if ((matcher = InputManager.getMatcher(input, VIEW_PRODUCT_BUYERS_INFO_REGEX)).find()) {
                viewBuyers(seller, matcher.group(1));
            }
            else if ((matcher = InputManager.getMatcher(input, EDIT_PRODUCT_REGEX)).find()) {
                handleEditProduct(seller, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
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

    private static ArrayList<String> makeBuyersUnique(ArrayList<String> buyers) {
        ArrayList<String> result = new ArrayList<String>();
        for (String buyer : buyers) {
            if (!result.contains(buyer)) {
                result.add(buyer);
            }
        }
        return result;
    }

    private static void handleEditProduct(Seller seller, String attribute) {

    }

    private static void viewProduct(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && isValidID(seller, Integer.parseInt(attribute))) {
            ProductMenu.init(Product.getProductByID(Integer.parseInt(attribute)));
        }
        else {
            System.out.println("Please enter a valid id");
        }
    }

    private static void viewBuyers(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && isValidID(seller, Integer.parseInt(attribute))) {
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
            buyers = makeBuyersUnique(buyers);
            System.out.println("Buyers of selected product: ");
            for (String buyer : buyers) {
                System.out.print(buyer + "   ");
            }
            System.out.println();
        }
        else {
            System.out.println("Please enter a valid id");
        }
    }

    private static void editProductWrapper(Seller seller, String attribute) {
        if (StringUtils.isNumeric(attribute) && isValidID(seller, Integer.parseInt(attribute))) {

        }
        else {
            System.out.println("Please enter a valid id");
        }
    }

    private static void addProductWrapper() {
        showCategories();
        System.out.println("Please enter the required information for this product: ");

    }

    private static void removeProductWrapper(String attribute) {
        SellerController.removeProduct(null,null);
    }

    private static void showCategories() {
        System.out.println("Categories: ");
        ArrayList<String> result = new ArrayList<String>();
        for (Category category : Manager.getAllCategories()) {
            result.add(category.getFullName() + "   " + category.getID());
        }
        Collections.sort(result);
        for (String categoryData : result) {
            System.out.println(categoryData);
        }
    }

    private static void viewOffs() {

    }

    private static void viewOff(int id) {

    }

    private static void editOffWrapper() {
        SellerController.editOff(null,null,null);
    }

    private static void addOffWrapper() {
        SellerController.addOff(null,null);
    }

    private static void viewBalance(Seller seller) {
        System.out.printf("Your current account balance: " + seller.getMoney());
    }

    private static void printHelp() {

    }

    private static void handleLogin() {
        if (MainMenu.currentUser == null) {
            SignUpAndLoginMenu.init();
        } else {
            System.out.println("you have signed in");
        }
    }

    private static void handleLogout() {
        if (MainMenu.currentUser == null) {
            System.out.println("you haven't signed in");
        } else {
            MainMenu.currentUser = null;
            MainMenu.init();
        }
    }
}
