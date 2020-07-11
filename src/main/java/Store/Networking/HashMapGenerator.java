package Store.Networking;

import Store.Model.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            hashMap.put("money", ((Customer) user).getMoney());
        }
        if (user instanceof Seller) {
            hashMap.put("money", ((Seller) user).getMoney());
            hashMap.put("companyName", ((Seller) user).getCompanyName());
            hashMap.put("companyDescription", ((Seller) user).getCompanyDescription());
        }
        hashMap.put("type", user.getType());
        return hashMap;
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
        hashMap.put("brand", product.getBrand());
        hashMap.put("description", product.getDescription());
        hashMap.put("id", product.getProductID() + "");
        hashMap.put("sellerName", product.getSeller().getName());
        hashMap.put("filters", product.getFilters());
        hashMap.put("startingTime", product.getStartingDate());
        hashMap.put("seller", getUserHashMap(product.getSeller()));
        hashMap.put("comments", getListOfComments(product.getComments()));
        hashMap.put("offer", getOfferHashMap(Offer.getOfferOfProduct(product)));
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

    public static ArrayList getListOfUsers(ArrayList<Seller> allUsers) {
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
            System.out.println(comment);
            arrayList.add(getCommentHashMap(comment));
        }
        return arrayList;
    }
}
