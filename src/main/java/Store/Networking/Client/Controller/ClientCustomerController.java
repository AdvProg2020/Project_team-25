package Store.Networking.Client.Controller;

import Store.InputManager;
import Store.Model.*;
import Store.Networking.Client.ClientHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClientCustomerController {
    public static Map getUserInfo(String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getUserInfo");
        hashMap.put("username", username);
        return (Map) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void editPersonalInfo(Map<String, Object> user, String field, String value) throws Exception {
        if (field.equalsIgnoreCase("email")) {
            if (!isValidEmail(value)) {
                throw (new Exception("Email field has not filled correctly"));
            }
        } else if (field.equalsIgnoreCase("phone number")) {
            if (!InputManager.getMatcher(value, "^[0-9]+$").find()) {
                throw (new Exception("Phone number field has not filled correctly"));
            }
        } else if (field.equalsIgnoreCase("password")) {
            if (!value.matches("^[a-zA-Z0-9]\\w{3,14}$")) {
                throw (new Exception("Password field has not filled correctly"));
            }
        }
        editInfo(field, value);
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static void increaseProduct(Map<String, Object> customer, Map<String, Object> product) throws Exception {
        if ((Boolean) product.get("availability")) {
            ClientProductController.addToCart((String) customer.get("username"), (String) product.get("id"));
        } else
            throw (new Exception("The product is not available"));
    }

    public static void decreaseProduct(Map<String, Object> customer, Map<String, Object> product) throws Exception {
        if (!removeProduct((String) product.get("id"))) {
            throw (new Exception("You haven't selected this product!"));
        }
    }

    public static void rateProduct(Map<String, Object> customer, Map<String, Object> product, int rating) throws Exception {
        if (ClientProductController.hasBeenRated((String) product.get("id"), (String) customer.get("username"))) {
            throw (new Exception("You have already rated this product!"));
        }
        if (!ClientProductController.hasBoughtProduct((String) product.get("id"), (String) customer.get("username"))) {
            throw (new Exception("You haven't bought this product!"));
        }
        ClientProductController.rateProduct((String) customer.get("username"), product, rating);
    }

    public static String purchase(Map<String, Object> customer, String input) throws Exception {
        if (input.isEmpty()) {
            if (canBuy()) {
                return buy();
            }
            throw (new Exception("You don't have enough money!"));
        } else {
            if (canBuy(input)) {
                return buy(input);
            }
            throw (new Exception("You don't have enough money! Or Your offcode is invalid"));
        }
    }


    public static boolean validatePassword(String password) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "validatePassword");
        hashMap.put("password", password);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean canOfferBeUsedInDate(Date date, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "canOfferBeUsedInDate");
        hashMap.put("date", date);
        hashMap.put("id", id);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String editInfo(String field, String newValue) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editCustomerPersonalInfo");
        hashMap.put("field", field);
        hashMap.put("newValue", newValue);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean canBuy() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "canBuyWithoutOffCode");
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Boolean canBuy(String code) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "canBuyWithOffCode");
        hashMap.put("code", code);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String buy() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "buyWithoutOffCode");
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String buy(String code) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "buyWithOffCode");
        hashMap.put("code", code);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Boolean removeProduct(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeProductFromCart");
        hashMap.put("id", id);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }
}
