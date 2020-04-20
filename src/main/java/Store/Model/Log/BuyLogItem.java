package Store.Model.Log;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public class BuyLogItem extends LogItem {

    private double offValue;
    private String sellerName;
    private boolean received;

    public BuyLogItem(int id, Date date, ArrayList<Product> products, double offValue, String sellerName, boolean received) {
        super(id, date, products);
        this.offValue = offValue;
        this.sellerName = sellerName;
        this.received = received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    @Override
    public String toString() {
        return "BuyLogItem{" +
                "offValue=" + offValue +
                ", sellerName='" + sellerName + '\'' +
                ", received=" + received +
                ", id=" + id +
                ", date=" + date +
                ", productList=" + productList +
                '}';
    }
}
