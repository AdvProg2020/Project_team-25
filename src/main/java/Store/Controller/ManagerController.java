package Store.Controller;

import Store.InputManager;
import Store.Model.Product;
import Store.Model.*;

import java.util.Date;

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
        } else if (field.equalsIgnoreCase("last name")) {
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
            if (Product.getProductByName(value) == null) {
                return "There isn't any product with this name!";
            }
            if (category.include(Product.getProductByName(value))) {
                return "It's in the category right now!";
            }
            category.addToCategory(Product.getProductByName(value));
            return returnValue;
        } else if (field.equalsIgnoreCase("remove filter")) {
            if (category.isInFilter(value)) {
                category.removeFromFilter(value);
                return returnValue;
            }
            return "There isn't any filter with this name!";
        } else if (field.equalsIgnoreCase("remove product")) {
            if (category.include(Product.getProductByName(value))) {
                category.removeProductFrom(Product.getProductByName(value));
                return returnValue;
            }
            return "There isn't any product with this name in this category!";
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
}
