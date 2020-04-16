package main.java.Store.View;

import main.java.Store.Controller.ProductController;
import main.java.Store.Controller.ProductsController;
import main.java.Store.Model.Product;

import java.util.ArrayList;

public class ProductsMenu {

    private static ArrayList<String> filters = new ArrayList<String>();
    private static String currentSort = "-";

    public static void init() {
        ProductMenu.init(null);
    }

    private static void viewProducts() {

    }

    private static void viewCategories() {

    }

    private static void filtering() {

    }

    private static void showAvailableFilters() {
        //doubt
        ProductsController.getFilteredList(filters);
    }

    private static void filter(String filter) {

    }

    private static void disableFilter(String filter) {

    }

    private static void printCurrentFilters() {

    }

    private static void sorting() {

    }

    private static void showAvailableSorts() {

    }

    private static void sort(String mode) {

    }

    private static void currentSort() {

    }

    private static void disableSort() {

    }

    private static void showAllProducts() {

    }

    private static void showProduct(String attribute) {

    }

    private static void printHelp() {

    }
}
