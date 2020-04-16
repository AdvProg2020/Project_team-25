package main.java.Store.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Category {
    String name;
    HashMap<String, String> specialAttributes = new HashMap<String, String>(); // Probably will change :/
    ArrayList<Category> children = new ArrayList<Category>();
    ArrayList<Product> immediateProducts = new ArrayList<Product>();
    Category parent;

    Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getFullName() {
        return "";
    }
}
