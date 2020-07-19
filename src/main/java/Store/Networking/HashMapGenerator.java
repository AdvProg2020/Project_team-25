package Store.Networking;

import Store.Model.*;
import Store.Model.Log.BuyLogItem;
import Store.Model.Log.SellLogItem;

import java.sql.Date;
import java.util.*;

public class HashMapGenerator {
    public static HashMap<String, Object> getUserHashMap(User user) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (user == null) {
            hashMap.put("type", "Customer");
            return hashMap;
        }
        hashMap.put("username", user.getUsername());
        hashMap.put("name", user.getName());
        hashMap.put("familyName", user.getFamilyName());
        hashMap.put("password", user.getPassword());
        hashMap.put("email", user.getEmail());
        hashMap.put("phoneNumber", user.getPhoneNumber());
        if (user instanceof Customer) {
            hashMap.put("money", ((Customer) user).getMoney() + "");
            hashMap.put("cart", getListOfProducts(((Customer) user).getCart()));
            hashMap.put("log", getListOfBuyLogItems(((Customer) user).getBuyLog()));
            hashMap.put("totalCartPrice", ((Customer) user).getTotalCartPrice() + "");
            hashMap.put("offCodes", getListOfCustomerOffCodes(((Customer) user).getOffCodes()));
        }
        if (user instanceof Seller) {
            hashMap.put("money", ((Seller) user).getMoney() + "");
            hashMap.put("companyName", ((Seller) user).getCompanyName());
            hashMap.put("companyDescription", ((Seller) user).getCompanyDescription());
            hashMap.put("log", getListOfSellLogItems(((Seller) user).getSellLog()));
            hashMap.put("buyers", getListOfBuyersOfSeller(((Seller) user).getBuyers()));
            hashMap.put("products", getListOfProducts(((Seller) user).getProducts()));
        }
        hashMap.put("type", user.getType());
        return hashMap;
    }

    private static ArrayList getListOfBuyersOfSeller(ArrayList<String> buyers) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        Set<Map<String, Object>> set = new HashSet<>();
        for (String buyer : buyers) {
            set.add(HashMapGenerator.getUserHashMap(User.getUserByUsername(buyer)));
        }
        arrayList.addAll(set);
        return arrayList;
    }

    public static ArrayList getListOfCategories(ArrayList<Category> allCategories) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (Category category : allCategories) {
            arrayList.add(getCategoryHashMap(category));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getCategoryHashMap(Category category) {
        if (category == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", category.getName());
        hashMap.put("filters", category.getFilters());
        hashMap.put("fullName", category.getFullName());
        hashMap.put("id", category.getId());
        return hashMap;
    }

    public static HashMap<String, Object> getProductHashMap(Product product) {
        if (product == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", product.getName());
        hashMap.put("price", product.getPrice() + "");
        hashMap.put("availability", product.getAvailablity());
        hashMap.put("category", getCategoryHashMap(product.getCategory()));
        hashMap.put("averageRating", product.getAverageRating() + "");
        hashMap.put("productStatus", product.getProductStatus());
        hashMap.put("brand", product.getBrand());
        hashMap.put("description", product.getDescription());
        hashMap.put("id", product.getProductID() + "");
        hashMap.put("sellerName", product.getSeller().getUsername());
        hashMap.put("filters", product.getFilters());
        hashMap.put("startingTime", product.getStartingDate());
        hashMap.put("comments", getListOfComments(product.getComments()));
        hashMap.put("offer", getOfferHashMap(Offer.getOfferOfProduct(product)));
        hashMap.put("status", product.getProductStatus());
        hashMap.put("filePath", product.getFilePath());
        return hashMap;
    }

    public static ArrayList getListOfProducts(ArrayList<Product> allProducts) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (Product product : allProducts) {
            arrayList.add(getProductHashMap(product));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getOfferHashMap(Offer offer) {
        if (offer == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("offPercent", offer.getOffPercent() + "");
        ArrayList<String> allProduct = new ArrayList<>();
        for (Product product : offer.getProducts()) {
            allProduct.add(product.getProductID() + "");
        }
        hashMap.put("products", allProduct);
        hashMap.put("username", offer.getUser().getUsername());
        hashMap.put("startingTime", new Date(offer.getStartingTime().getTime()).toLocalDate().toString());
        hashMap.put("endingTime", new Date(offer.getEndingTime().getTime()).toLocalDate().toString());
        hashMap.put("filters", offer.getFilters());
        hashMap.put("id", offer.getOffID() + "");
        hashMap.put("offerStatus", offer.getOfferStatus());
        return hashMap;
    }

    public static ArrayList getListOfSellers(ArrayList<Seller> allUsers) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (User user : allUsers) {
            arrayList.add(getUserHashMap(user));
        }
        return arrayList;
    }

    public static ArrayList getListOfUsers(ArrayList<User> allUsers) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (User user : allUsers) {
            arrayList.add(getUserHashMap(user));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getCommentHashMap(Comment comment) {
        if (comment == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", comment.getCommentTitle());
        hashMap.put("text", comment.getCommentText());
        hashMap.put("username", comment.getCommentingUser().getUsername());
        hashMap.put("acceptanceStatus", comment.getAcceptanceStatus());
        hashMap.put("hasBought", comment.getHasBought());
        return hashMap;
    }

    public static ArrayList getListOfComments(ArrayList<Comment> allComments) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (Comment comment : allComments) {
            arrayList.add(getCommentHashMap(comment));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getOffCodeHashMap(OffCode offCode) {
        if (offCode == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", offCode.getCode());
        hashMap.put("usageCount", offCode.getUsageCount() + "");
        hashMap.put("offPercentage", offCode.getOffPercentage() + "");
        hashMap.put("maximumOff", offCode.getMaximumOff() + "");
        hashMap.put("startingTime", new Date(offCode.getStartingTime().getTime()).toLocalDate().toString());
        hashMap.put("endingTime", new Date(offCode.getStartingTime().getTime()).toLocalDate().toString());
        return hashMap;
    }

    public static ArrayList getListOfOffCodes(ArrayList<OffCode> allOffCodes) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (OffCode offCode : allOffCodes) {
            arrayList.add(getOffCodeHashMap(offCode));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getRequestHashMap(Request request) {
        if (request == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", request.getId() + "");
        hashMap.put("sellerName", request.getSeller().getUsername());
        hashMap.put("status", request.getStatus());
        hashMap.put("requestType", request.getRequestType());
        return hashMap;
    }

    public static ArrayList getListOfOffRequests(ArrayList<Request> allRequests) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (Request request : allRequests) {
            arrayList.add(getRequestHashMap(request));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getBuyLogItemHashMap(BuyLogItem buyLogItem) {
        if (buyLogItem == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", buyLogItem.getId() + "");
        hashMap.put("sellerName", buyLogItem.getSellerName());
        hashMap.put("offValue", buyLogItem.getOffValue() + "");
        hashMap.put("date", new Date(buyLogItem.getDate().getTime()).toLocalDate().toString());
        hashMap.put("products", getListOfProducts(buyLogItem.getProducts()));
        hashMap.put("isReceived", buyLogItem.isReceived());
        hashMap.put("isShowed", buyLogItem.isShowed());
        return hashMap;
    }

    public static ArrayList getListOfBuyLogItems(ArrayList<BuyLogItem> allLogs) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (BuyLogItem logItem : allLogs) {
            arrayList.add(getBuyLogItemHashMap(logItem));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getSellLogItemHashMap(SellLogItem sellLogItem) {
        if (sellLogItem == null) {
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", sellLogItem.getId() + "");
        hashMap.put("customerName", sellLogItem.getCustomerName());
        hashMap.put("offValue", sellLogItem.getOffValue() + "");
        hashMap.put("date", new Date(sellLogItem.getDate().getTime()).toLocalDate().toString());
        hashMap.put("products", getListOfProducts(sellLogItem.getProducts()));
        hashMap.put("isSendStatus", sellLogItem.isSendStatus());
        hashMap.put("incomeValue", sellLogItem.getIncomeValue());
        return hashMap;
    }

    public static ArrayList getListOfSellLogItems(ArrayList<SellLogItem> allLogs) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (SellLogItem logItem : allLogs) {
            arrayList.add(getSellLogItemHashMap(logItem));
        }
        return arrayList;
    }

    public static ArrayList getListOfCustomerOffCodes(Map<OffCode, Integer> allOffCodes) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (OffCode offCode : allOffCodes.keySet()) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("offCode", getOffCodeHashMap(offCode));
            hashMap.put("number", allOffCodes.get(offCode));
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    public static ArrayList getListOfOffers(ArrayList<Offer> allOffers) {
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        for (Offer offer : allOffers) {
            arrayList.add(HashMapGenerator.getOfferHashMap(offer));
        }
        return arrayList;
    }
}
