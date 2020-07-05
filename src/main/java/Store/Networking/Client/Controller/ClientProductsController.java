package Store.Networking.Client.Controller;

import Store.Networking.Client.ClientHandler;

import java.util.*;

public class ClientProductsController {

    public static ArrayList<Map<String, Object>> getAllCategories() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getAllCategories");
        return (ArrayList<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static List<Map<String, Object>> getToBeShownProducts(ArrayList<String> filters, String categoryFilter, double priceLowFilter, double priceHighFilter, String brandFilter, String nameFilter, String sellerUsernameFilter, String availabilityFilter, String searchQuery, String currentSort) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getProducts");
        hashMap.put("filters", filters);
        hashMap.put("categoryFilter", categoryFilter);
        hashMap.put("priceLowFilter", priceLowFilter);
        hashMap.put("priceHighFilter", priceHighFilter);
        hashMap.put("brandFilter", brandFilter);
        hashMap.put("nameFilter", nameFilter);
        hashMap.put("sellerUsernameFilter", sellerUsernameFilter);
        hashMap.put("searchQuery",searchQuery);
        hashMap.put("currentSort", currentSort);
        hashMap.put("availabilityFilter", availabilityFilter);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Object getPriceHigh() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getPriceHigh");
        return ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Object getPriceLow() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getPriceLow");
        return ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static Set<String> getAllFilters(String categoryFilter) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getAllFilters");
        hashMap.put("categoryFilter", categoryFilter);
        return new HashSet<>((ArrayList<String>) ClientHandler.sendAndReceiveMessage(hashMap).get("content"));
    }
}
