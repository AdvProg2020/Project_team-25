package Store.Networking.Client.Controller;

import Store.InputManager;
import Store.Model.Manager;
import Store.Networking.Client.ClientHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSellerController {
    public static boolean validatePassword(String password) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "validatePassword");
        hashMap.put("password", password);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void editPersonalInfo(String field, String value) throws Exception {
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

    public static String editInfo(String field, String newValue) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editSellerPersonalInfo");
        hashMap.put("field", field);
        hashMap.put("newValue", newValue);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addAds(int id) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addAds");
        hashMap.put("id", id);
        String string = (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        if (!string.equalsIgnoreCase("Ok")) {
            throw new Exception(string);
        }
    }

    public static void removeProduct(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeProductFromSeller");
        hashMap.put("id", id);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void removeFilterFromProduct(String id, String filter) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeFilterFromSeller");
        hashMap.put("id", id);
        hashMap.put("filter", filter);
        String string = (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        if (!string.equalsIgnoreCase("Ok")) {
            throw new Exception(string);
        }
    }

    public static void addFilterToProduct(String id, String filter) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addFilterToProductFromSeller");
        hashMap.put("id", id);
        hashMap.put("filter", filter);
        String string = (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        if (!string.equalsIgnoreCase("Ok")) {
            throw new Exception(string);
        }
    }

    public static boolean isProductWithThisID(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isProductWithThisID");
        hashMap.put("id", id);
        return (Boolean)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean isProductFromThisSeller(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isProductFromThisSeller");
        hashMap.put("id", id);
        return (Boolean)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean isProductInOffer(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isProductInOffer");
        hashMap.put("id", id);
        return (Boolean)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addProductToOffer(String offerID, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addProductToOffer");
        hashMap.put("productId", id);
        hashMap.put("offerId", offerID);
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static boolean isProductInCategory(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isProductInCategory");
        hashMap.put("id", id);
        return (Boolean)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static List<Map<String, Object>> sortProductsOfSeller(String currentSortProduct) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "sortProductsOfSeller");
        hashMap.put("sort", currentSortProduct);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static List<Map<String, Object>> sortOffersOfSeller(String currentSortOffer) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "sortOffersOfSeller");
        hashMap.put("sort", currentSortOffer);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addOfferFromSeller(Map<String, Object> offer) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addOfferFromSeller");
        hashMap.put("offer", offer);
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static void addProductFromSeller(Map<String, Object> product) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addProductFromSeller");
        hashMap.put("product", product);
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static void editOffer(String id, Map<String, Object> offer) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editOffer");
        hashMap.put("id", id);
        hashMap.put("offer", offer);
        String string = (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        if (!string.equalsIgnoreCase("Ok")) {
            throw new Exception(string);
        }
    }

    public static void editProduct(String id, Map<String, Object> product) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editProduct");
        hashMap.put("id", id);
        hashMap.put("product", product);
        String string = (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        if (!string.equalsIgnoreCase("Ok")) {
            throw new Exception(string);
        }
    }

    public static List<Map<String, Object>> getOfferOfThisSeller() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getOffersOfThisSeller");
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addAuction(Map<String, Object> product, LocalDateTime dateTime) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addAuction");
        hashMap.put("product", product);
        hashMap.put("date", dateTime);
        hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
        if (((String)hashMap.get("content")).equalsIgnoreCase("error"))
            throw new Exception((String)hashMap.get("type"));
    }
}
