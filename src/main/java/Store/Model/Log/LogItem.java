package Store.Model.Log;

import Store.Model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public abstract class LogItem implements Serializable {
    protected int id;
    protected Date date;
    protected ArrayList<Product> productList = new ArrayList<Product>();

    LogItem(int id, Date date, ArrayList<Product> products) {
        this.date = date;
        this.id = id;
        this.productList.addAll(products);
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<Product> getProducts() {
        return productList;
    }


    @Override
    public abstract String toString();
}
