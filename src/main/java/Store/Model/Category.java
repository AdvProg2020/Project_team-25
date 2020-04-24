package Store.Model;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    private String name;
    private ArrayList<String> filters = new ArrayList<>(); // Probably will change :/
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
        if (this.parent == null) {
            return this.name;
        }
        return " -> " + this.parent.getFullName();
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
        this.immediateProducts = new ArrayList<Product>();
        this.children = new ArrayList<Category>();
    }

    public void addToFilter(String string) {
        filters.add(string);
    }

    public boolean isInFilter(String string) {
        return filters != null && filters.contains(string);
    }

    public void removeFromFilter(String string) {
        if (isInFilter(string)) {
            filters.remove(string);
        }
    }

    public ArrayList<Product> getImmediateProducts() {
        return immediateProducts;
    }
}
