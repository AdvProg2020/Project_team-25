package Store.Controller;

import Store.InputManager;
import Store.Model.Product;
import Store.Model.*;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public class ManagerController {

    public static String editPersonalInfo(User user, String field, String value) {
        String returnValue = "Info has changed successfully.";
        if (field.equalsIgnoreCase("email")) {
            if (isValidEmail(value)) {
                user.setEmail(value);
                return returnValue;
            }
            else {
                return "Invalid email format!";
            }
        } else if (field.equalsIgnoreCase("phone number")) {
            if (InputManager.getMatcher(value, "^[0-9]+$").find()) {
                user.setPhoneNumber(value);
                return returnValue;
            } else {
                return "Phone number format is incorrect!";
            }
        } else if (field.equalsIgnoreCase("family name")) {
            user.setFamilyName(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("first name")) {
            user.setName(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("password")) {
            if (value.matches("^[a-zA-Z]\\w{3,14}$")) {
                user.setPassword(value);
                return returnValue;
            }
            else {
                return "Invalid password format!";
            }
        }
        return "Couldn't change info!";
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static void createManagerProfile(Manager manager, String username, String firstName, String lastName, String email, String phoneNumber, String password) {
        manager.addNewManager(new Manager(username, firstName, lastName, email, phoneNumber, password));
    }

    public static String removeProducts(Manager manager, Product product) {
        if (product == null) {
            return "There isn't any product with this ID!";
        }
        manager.removeProduct(product);
        return "Product deleted successfully.";
    }

    public static void createOffCode(Manager manager, String code, double offPercentage, double maximumOff, int usageCount, Date startingDate, Date endingDate) {
        OffCode offCode = new OffCode(code, offPercentage, maximumOff, usageCount);
        offCode.setEndingTime(endingDate);
        offCode.setStartingTime(startingDate);
        manager.addOffCode(offCode);
    }

    public static String deleteUserByName(Manager manager, String username) {
        if (User.getUserByUsername(username) == null) {
            return "There isn't any user with this username!";
        }
        manager.deleteUser(User.getUserByUsername(username));
        return "User account deleted.";
    }

    public static String removeOffCode(Manager manager, String code) {
        if (Manager.getOffCodeByCode(code) == null) {
            return "There isn't any offCode with this code!";
        }
        Manager.removeOffCode(Manager.getOffCodeByCode(code));
        return "OffCode deleted.";
    }

    public static String editOffCode(Manager manager, OffCode offCode, String field, String value) {
        String returnValue = "Info has changed successfully.";
        if (offCode == null) {
            return "There isn't any offCode with this code!";
        } else {
            if (field.equalsIgnoreCase("maximumOff")) {
                try {
                    offCode.setMaximumOff(Double.parseDouble(value));
                    return returnValue;
                } catch (Exception exception) {
                    return "Invalid format!";
                }
            } else if (field.equalsIgnoreCase("offPercentage")) {
                try {
                    offCode.setOffPercentage(Double.parseDouble(value));
                    return returnValue;
                } catch (Exception exception) {
                    return "Invalid format!";
                }
            }
        }
        return "Invalid command!";
    }

    public static void handleRequest(Manager manager, boolean status, Request request) {
        manager.handleRequest(request, status);
    }

    public static void addCategory(Manager manager, String name, String parent) {
        if (parent.equalsIgnoreCase("null")) {
            manager.addCategory(new Category(name, null));
        }
        else {
            manager.addCategory(new Category(name, Manager.categoryByName(parent)));
        }
    }

    public static String editCategory(Manager manager, Category category, String field, String value) {
        if (category == null) {
            return "There isn't any category with this name!";
        }
        String returnValue = "Info has changed successfully.";
        if (field.equalsIgnoreCase("add filter")) {
            if (category.isInFilter(value)) {
                return "There is an identical filter!";
            }
            category.addToFilter(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("add product")) {
            if (!StringUtils.isNumeric(value)) {
                return "Invalid input, ID expected!";
            }
            int id = Integer.parseInt(value);
            if (Product.getProductByID(id) == null) {
                return "There isn't any product with this ID!";
            }
            if (category.include(Product.getProductByID(id))) {
                return "It's in the category right now!";
            }
            category.addToCategory(Product.getProductByID(id));
            return returnValue;
        } else if (field.equalsIgnoreCase("remove filter")) {
            if (category.isInFilter(value)) {
                category.removeFromFilter(value);
                return returnValue;
            }
            return "There isn't any filter with this name!";
        } else if (field.equalsIgnoreCase("remove product")) {
            if (!StringUtils.isNumeric(value)) {
                return "Invalid input, ID expected!";
            }
            int id = Integer.parseInt(value);
            if (category.include(Product.getProductByID(id))) {
                category.removeProductFrom(Product.getProductByID(id));
                return returnValue;
            }
            return "There isn't any product with this ID in this category!";
        } else if (field.equalsIgnoreCase("change name")) {
            if (Manager.categoryByName(value) == null) {
                category.setName(value);
                return returnValue;
            } else {
                return "There is a category with this name!";
            }
        }
        return "Couldn't change info!";
    }

    public static String removeCategory(Manager manager, Category category) {
        if (category == null) {
            return "There isn't any category with this name!";
        }
        Manager.removeCategory(category);
        return "Category deleted successfully.";
    }

    public static ArrayList<OffCode> sortOffCodes(String mode, ArrayList<OffCode> offCodes) {
        if (mode.equalsIgnoreCase("time of starting")) {
            return sortOffCodesByStartingTime(offCodes);
        }
        else if (mode.equalsIgnoreCase("time of ending")) {
            return sortOffCodesByEndingTime(offCodes);
        }
        else if (mode.equalsIgnoreCase("code")) {
            return sortOffCodesByCode(offCodes);
        }
        else if (mode.equalsIgnoreCase("off percentage")) {
            return sortOffCodesByOffPercentage(offCodes);
        }
        else if (mode.equalsIgnoreCase("maximum off")) {
            return sortOffCodesByMaxOff(offCodes);
        }
        else if (mode.equalsIgnoreCase("usage count")) {
            return sortOffCodesByUsageCount(offCodes);
        }
        return offCodes;
    }

    private static ArrayList<OffCode> sortOffCodesByStartingTime(ArrayList<OffCode> offCodes) {
        ArrayList<OffCode> result = new ArrayList<OffCode>();
        result.addAll(offCodes);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(OffCode::getStartingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<OffCode> sortOffCodesByEndingTime(ArrayList<OffCode> offCodes) {
        ArrayList<OffCode> result = new ArrayList<OffCode>();
        result.addAll(offCodes);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(OffCode::getEndingTime))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<OffCode> sortOffCodesByCode(ArrayList<OffCode> offCodes) {
        ArrayList<OffCode> result = new ArrayList<OffCode>();
        result.addAll(offCodes);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(OffCode::getCode))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<OffCode> sortOffCodesByOffPercentage(ArrayList<OffCode> offCodes) {
        ArrayList<OffCode> result = new ArrayList<OffCode>();
        result.addAll(offCodes);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(OffCode::getOffPercentage))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<OffCode> sortOffCodesByMaxOff(ArrayList<OffCode> offCodes) {
        ArrayList<OffCode> result = new ArrayList<OffCode>();
        result.addAll(offCodes);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(OffCode::getMaximumOff))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<OffCode> sortOffCodesByUsageCount(ArrayList<OffCode> offCodes) {
        ArrayList<OffCode> result = new ArrayList<OffCode>();
        result.addAll(offCodes);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(OffCode::getUsageCount))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    public static ArrayList<User> sortUsers(String mode, ArrayList<User> users) {
        if (mode.equalsIgnoreCase("name")) {
            return sortUsersByName(users);
        }
        else if (mode.equalsIgnoreCase("family name")) {
            return sortUsersByFamilyName(users);
        }
        else if (mode.equalsIgnoreCase("phone number")) {
            return sortUsersByPhoneNumber(users);
        }
        else if (mode.equalsIgnoreCase("username")) {
            return sortUsersByUsername(users);
        }
        else if (mode.equalsIgnoreCase("email")) {
            return sortUsersByEmail(users);
        }
        return users;
    }

    private static ArrayList<User> sortUsersByName(ArrayList<User> users) {
        ArrayList<User> result = new ArrayList<User>();
        result.addAll(users);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(User::getName))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<User> sortUsersByFamilyName(ArrayList<User> users) {
        ArrayList<User> result = new ArrayList<User>();
        result.addAll(users);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(User::getFamilyName))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<User> sortUsersByPhoneNumber(ArrayList<User> users) {
        ArrayList<User> result = new ArrayList<User>();
        result.addAll(users);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(User::getPhoneNumber))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<User> sortUsersByUsername(ArrayList<User> users) {
        ArrayList<User> result = new ArrayList<User>();
        result.addAll(users);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(User::getUsername))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }

    private static ArrayList<User> sortUsersByEmail(ArrayList<User> users) {
        ArrayList<User> result = new ArrayList<User>();
        result.addAll(users);
        try {
            return result.stream()
                    .sorted(Comparator.comparing(User::getEmail))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception exception) {
            return result;
        }
    }
}
