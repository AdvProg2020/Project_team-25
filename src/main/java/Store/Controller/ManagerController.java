package Store.Controller;

import Store.InputManager;
import Store.Model.Product;
import Store.Model.*;

import java.util.Calendar;
import java.util.Date;

public class ManagerController {

    public static String editPersonalInfo(Manager manager, String field, String value) {
        String returnValue = "info has changed successfully";
        if (field.equalsIgnoreCase("email")) {
            manager.setEmail(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("phone number")) {
            if (InputManager.getMatcher(value, "^[a-zA-Z]\\w{3,14}$").find()) {
                manager.setPhoneNumber(value);
                return returnValue;
            } else {
                return "password type is incorrect";
            }
        } else if (field.equalsIgnoreCase("last name")) {
            manager.setFamilyName(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("first name")) {
            manager.setName(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("password")) {
            if (InputManager.getMatcher(value, "^[0-9]\\w{3,10}$").find()) {
                manager.setPassword(value);
                return returnValue;
            } else {
                return "password type is incorrect";
            }
        }
        return "couldn't change info";
    }

    public static void createManagerProfile(Manager manager) {
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
        manager.addNewManager(new Manager(username, firstName, lastName, email, phoneNumber, password));

    }

    public static String removeProducts(Manager manager, Product product) {
        if (product == null) {
            return "there isn't any product with this ID";
        }
        manager.removeProduct(product);
        return "product was deleted successfully";
    }

    public static void createOffCode(Manager manager) {
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
        OffCode offCode = new OffCode(code, offPercentage, maximumOff, usageCount);
        offCode.setEndingTime(endingDate);
        offCode.setStartingTime(startingDate);
        manager.addOffCode(offCode);

    }

    public static String deleteUserByName(Manager manager, String username) {
        if (User.getUserByUsername(username) == null) {
            return "there isn't any user with this username";
        }
        manager.deleteUser(User.getUserByUsername(username));
        return "user account was deleted";
    }

    public static String removeOffCode(Manager manager, String code) {
        if (Manager.getOffCodeByCode(code) == null) {
            return "there isn't any offCode with this code";
        }
        Manager.removeOffCode(Manager.getOffCodeByCode(code));
        return "offCode was deleted";
    }

    public static String editOffCode(Manager manager, OffCode offCode, String field, String value) {
        String returnValue = "info has changed successfully";
        if (offCode == null) {
            return "there isn't any offCode with this code";
        } else {
            if (field.equalsIgnoreCase("maximumOff")) {
                try {
                    offCode.setMaximumOff(Double.parseDouble(value));
                    return returnValue;
                } catch (Exception exception) {
                    return "the format is invalid";
                }
            } else if (field.equalsIgnoreCase("offPercentage")) {
                try {
                    offCode.setOffPercentage(Double.parseDouble(value));
                    return returnValue;
                } catch (Exception exception) {
                    return "the format is invalid";
                }
            }
        }
        return "invalid command";
    }

    public static void handleRequest(Manager manager, boolean status, Request request) {
        manager.handleRequest(request, status);
    }

    public static void addCategory(Manager manager, String name) {
        String parent;
        System.out.println("name: ");
        if (Manager.catagoryByName(name = InputManager.getNextLine()) != null) {
            System.out.println("process failed: the category exists");
            return;
        }
        System.out.println("Parent: ");
        while (Manager.catagoryByName(parent = InputManager.getNextLine()) == null || !(parent.equalsIgnoreCase("null"))) {
            System.out.println("you should print null if it hasn't any parent or its valid parent name");
        }
        if (parent.equalsIgnoreCase("null")) {
            manager.addCategory(new Category(name, null));
        }
        manager.addCategory(new Category(name, Manager.catagoryByName(parent)));
    }

    public static String editCategory(Manager manager, Category category, String field, String value) {
        if (category == null) {
            return "there isn't any category with this name";
        }
        String returnValue = "info has changed successfully";
        if (field.equalsIgnoreCase("add filter")) {
            if (category.isInFilter(value)) {
                return "there is a same filter";
            }
            category.addToFilter(value);
            return returnValue;
        } else if (field.equalsIgnoreCase("add product")) {
            if (Product.getProductByName(value) == null) {
                return "there isn't any product with this name";
            }
            if (category.include(Product.getProductByName(value))) {
                return "it's in the category right now";
            }
            category.addToCategory(Product.getProductByName(value));
            return returnValue;
        } else if (field.equalsIgnoreCase("remove filter")) {
            if (category.isInFilter(value)) {
                category.removeFromFilter(value);
                return returnValue;
            }
            return "there isn't any filter with this name";
        } else if (field.equalsIgnoreCase("remove product")) {
            if (category.include(Product.getProductByName(value))) {
                category.removeProductFrom(Product.getProductByName(value));
                return returnValue;
            }
            return "there isn't any product with this name in category";
        } else if (field.equalsIgnoreCase("change name")) {
            if (Manager.catagoryByName(value) == null) {
                category.setName(value);
                return returnValue;
            } else {
                return "there is a category with this name";
            }
        }
        return "couldn't change info";
    }

    public static String removeCategory(Manager manager, Category category) {
        if (category == null) {
            return "there isn't any category with this name";
        }
        Manager.removeCategory(category);
        return "category was deleted successfully";
    }
}
