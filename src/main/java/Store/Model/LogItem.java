package Store.Model;

import java.util.ArrayList;

public class LogItem {
    protected int id;
    Date date;
    ArrayList<Product> productList = new ArrayList<Product>();

    LogItem(int id, Date date) {
    }
}
