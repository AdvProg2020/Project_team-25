package main.java.Store.Controller;

import main.java.Store.Model.Customer;
import main.java.Store.Model.Product;
import main.java.Store.Model.Rating;

public class CustomerController {

    public static void editPersonalInfo(Customer customer, String field, String value) {
        customer.buy();
    }

    public static void increaseProduct(Customer customer, Product product) {
        customer.addToCart(product);
    }

    public static void decreaseProduct(Customer customer, Product product) {
        customer.addToCart(null);
    }

    public static void rateProduct(Customer customer, Product product, double rating) {
        product.rate(customer,null);
    }

    public static void purchase(Customer customer, String[] attributes) {
        customer.buy();
    }
}
