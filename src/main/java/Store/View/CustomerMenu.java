package main.java.Store.View;

import main.java.Store.Controller.CustomerController;
import main.java.Store.Model.Customer;
import main.java.Store.Model.Seller;

public class CustomerMenu {

    public static void init() {
    }

    private static void viewPersonalInfo() {

    }

    private static void editPersonalInfoWrapper(String field, String value) {
        CustomerController.editPersonalInfo(null, null, null);
    }

    private static void viewCart() {

    }

    private static void showProduct(String attribute) {

    }

    private static void increaseProductWrapper(String attribute) {
        CustomerController.increaseProduct(null,null);
    }

    private static void decreaseProductWrapper(String attribute) {
        CustomerController.decreaseProduct(null,null);
    }

    private static void showTotalCartPrice() {

    }

    private static void purchaseWrapper() {
        CustomerController.purchase(null, null);
    }

    private static void viewOrders() {

    }

    private static void showOrder(int id) {

    }

    private static void rateProductWrapper(String attribute, double rating) {
        CustomerController.rateProduct(null,null,0);
    }

    private static void viewBalance() {

    }

    private static void viewDiscountCodes() {

    }

    private static void printHelp() {

    }
}
