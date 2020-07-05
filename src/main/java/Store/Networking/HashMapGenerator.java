package Store.Networking;

import Store.Model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class HashMapGenerator {
    public static HashMap<String, Object> getUserHashMap(User user) {
        HashMap<String, Object> hashMap = new HashMap<>();
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
        return hashMap;
    }

    public static ArrayList getListOfCategories(ArrayList<Category> allCategories) {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (Category category : allCategories) {
            arrayList.add(getCategoryHashMap(category));
        }
        return arrayList;
    }

    public static HashMap<String, Object> getCategoryHashMap(Category category) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", category.getName());
        hashMap.put("filters", category.getFilters());
        hashMap.put("fullName", category.getFullName());
        hashMap.put("id", category.getId());
        return hashMap;
    }

    public static HashMap<String, Object> getProductHashMap(Product product) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", product.getName());
        hashMap.put("price", product.getPrice());
        hashMap.put("availability", product.getAvailablity());
        hashMap.put("averageRating", product.getAverageRating());
        hashMap.put("brand", product.getBrand());
        hashMap.put("description", product.getDescription());
        hashMap.put("id", product.getProductID());
        return hashMap;
    }

    public static ArrayList getListOfProducts(ArrayList<Product> allProducts) {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (Product product : allProducts) {
            arrayList.add(getProductHashMap(product));
        }
        return arrayList;
    }
}
