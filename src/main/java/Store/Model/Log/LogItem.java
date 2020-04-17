package main.java.Store.Model.Log;

import main.java.Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public abstract class LogItem {
    protected int id;
    protected Date date;
    protected ArrayList<Product> productList = new ArrayList<Product>();

    LogItem(int id, Date date) {
    }
}
