package Store.Model;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private String name;
    private HashMap<String, String> specialAttributes = new HashMap<String, String>(); // Probably will change :/
    private ArrayList<Category> children = new ArrayList<Category>();
    private ArrayList<Product> immediateProducts = new ArrayList<Product>();
    private Category parent;

    Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
        parent.children.add(this);
    }

    private void addToCategory(Product product) {
        if (this.parent == null) {
            this.immediateProducts.add(product);
            return;
        }
        parent.addToCategory(product);
        this.immediateProducts.add(product);
    }

    public String getFullName() {
        return "";
    }

    public boolean include(Product product) {
        for (Product immediateProduct : immediateProducts) {
            if (immediateProduct.equals(product)) {
                return true;
            }
        }
        return false;
    }

    public void removeProductFrom(Product product) {
        if (parent == null) {
            immediateProducts.remove(product);
            return;
        }
        parent.removeProductFrom(product);
        this.immediateProducts.remove(product);
    }

    public void removeInside() {
        try {
            this.parent.immediateProducts.remove(this);
        } catch (NullPointerException exception) {
            // do nothing
        }
    }

}
