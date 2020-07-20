package Store.Networking.Client.Controller;

import Store.Networking.Client.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientProductController {
    public static List<Map<String, Object>> getAllSellersOfProduct(String productID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getAllSellersOfProduct");
        hashMap.put("id", productID);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Map<String, Object> getProductWithDifferentSeller(String productID, String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getProductWithDifferentSeller");
        hashMap.put("id", productID);
        hashMap.put("username", username);
        return (Map<String, Object>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Map<String, Object> getComparedProduct(String otherID) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getComparedProduct");
        hashMap.put("id", otherID);
        return (Map<String, Object>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void rateProduct(String username, Map<String, Object> productToShow, int currentRating) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "rateProduct");
        hashMap.put("username", username);
        hashMap.put("currentRating", currentRating);
        hashMap.put("id", productToShow.get("id"));
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addComment(Map<String, Object> productToShow, String username, String title, String content) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "commentProduct");
        hashMap.put("username", username);
        hashMap.put("title", title);
        hashMap.put("content", content);
        hashMap.put("id", productToShow.get("id"));
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean hasBeenRated(String id, String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "hasBeenRated?");
        hashMap.put("id", id);
        hashMap.put("username", username);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean hasBoughtProduct(String id, String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "hasBoughtProduct?");
        hashMap.put("id", id);
        hashMap.put("username", username);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addToCart(String username, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addToCart");
        hashMap.put("id", id);
        hashMap.put("username", username);
        ClientHandler.sendAndReceiveMessage(hashMap);
    }
}
