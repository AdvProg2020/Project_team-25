package Store.Model.Log;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public abstract class LogItem {
    protected int id;
    protected Date date;
    protected ArrayList<Product> productList = new ArrayList<Product>();

    LogItem(int id, Date date, ArrayList<Product> products) {
        this.date = date;
        this.id = id;
        this.productList = products;
    }

    protected ArrayList<Product> getProducts()
    {
        return productList;
    }


    @Override
    public abstract String toString();
}
