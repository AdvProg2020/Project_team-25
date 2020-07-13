package Store.Networking.Client.Controller;

import Store.Networking.Client.ClientHandler;

import java.util.*;

public class ClientAuctionsController {

    public static List<Map<String, Object>> getToBeShownProducts(ArrayList<String> filters, String categoryFilter, String brandFilter, String nameFilter, String sellerUsernameFilter, String availabilityFilter, String searchQuery, String currentSort) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getAuctionsProducts");
        hashMap.put("filters", filters);
        hashMap.put("categoryFilter", categoryFilter);
        hashMap.put("brandFilter", brandFilter);
        hashMap.put("nameFilter", nameFilter);
        hashMap.put("sellerUsernameFilter", sellerUsernameFilter);
        hashMap.put("searchQuery",searchQuery);
        hashMap.put("currentSort", currentSort);
        hashMap.put("availabilityFilter", availabilityFilter);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }
}
