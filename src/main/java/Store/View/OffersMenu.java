package main.java.Store.View;

import main.java.Store.Controller.OffersController;

import java.util.ArrayList;

public class OffersMenu {

    private static ArrayList<String> filters = new ArrayList<String>();
    private static String currentSort = "-";

    public static void init() {

    }

    public static void viewOffs() {

    }

    public static void showProducts(String attribute) {
        ProductMenu.init(null);
    }

    private static void filtering() {

    }

    private static void showAvailableFilters() {
        //doubt
        OffersController.getFilteredList(filters);
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

    private static void printHelp() {

    }
}
