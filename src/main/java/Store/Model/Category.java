package Store.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private int id;
    private String name;
    private ArrayList<String> filters = new ArrayList<>(); // Probably will change :/
    private ArrayList<Category> children = new ArrayList<Category>();
    private ArrayList<Product> immediateProducts = new ArrayList<Product>();
    private Category parent;

    private static int idCounter = 0;

    public Category(String name, Category parent) {
        this.id = idCounter++;
        this.name = name;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public static void setIdCounter(int idCounter) {
        Category.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean hasCategoryWithId(int id) {
        for (Category category : Manager.getAllCategories()) {
            if (category.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static Category getCategoryById(int id) {
        for (Category category : Manager.getAllCategories()) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }

    public void addToCategory(Product product) {
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

    public Category getParent() {
        return parent;
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
        if (parent != null) {
            for (Product immediateProduct : immediateProducts) {
                parent.removeProductFrom(immediateProduct);
            }
        }
        this.immediateProducts = new ArrayList<Product>();
        this.children = new ArrayList<Category>();
    }

    public void addToFilter(String string) {
        filters.add(string);
    }

    public boolean isInFilter(String string) {
        return filters != null && filters.contains(string);
    }

    public ArrayList<String> getFilters() {
        return filters;
    }

    public void removeFromFilter(String string) {
        if (isInFilter(string)) {
            filters.remove(string);
        }
    }

    public ArrayList<Product> getImmediateProducts() {
        return immediateProducts;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'';
    }
}
