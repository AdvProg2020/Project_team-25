package Store.Model.Log;

import Store.Model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public abstract class LogItem implements Serializable {
    protected int id;
    protected Date date;
    protected ArrayList<Product> productList = new ArrayList<Product>();

    private static int idCounter = 1;
    private static ArrayList<LogItem> allLogItems = new ArrayList<>();

    public static ArrayList<LogItem> getAllLogItems() {
        return allLogItems;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        LogItem.idCounter = idCounter;
    }

    public static void setAllLogItems(ArrayList<LogItem> allLogItems) {
        LogItem.allLogItems = allLogItems;
    }

    LogItem(Date date, ArrayList<Product> products) {
        this.date = date;
        id = idCounter++;
        this.productList.addAll(products);
        allLogItems.add(this);
    }

    public static LogItem getLogById(int id) {
        for (LogItem logItem: allLogItems)
            if (logItem.getId() == id)
                return logItem;
        return null;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Product> getProducts() {
        return productList;
    }

    public int getId() {
        return id;
    }

    @Override
    public abstract String toString();
}
