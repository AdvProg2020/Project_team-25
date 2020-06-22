package Store.Model.Log;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public class BuyLogItem extends LogItem {

    private double offValue;
    private String sellerName;
    private boolean received;
    private boolean showed;

    public BuyLogItem(int id, Date date, ArrayList<Product> products, double offValue, String sellerName, boolean received) {
        super(id, date, products);
        this.offValue = offValue;
        this.sellerName = sellerName;
        this.received = received;
    }

    public boolean isShowed()
    {
        return showed;
    }
    public void setReceived(boolean received) {
        this.received = received;
    }

    @Override
    public String toString() {
        showed = true;
        return "BuyLogItem{" +
                "offValue=" + offValue +
                ", sellerName=" + sellerName +
                ", received=" + received +
                ", id=" + id +
                ", date=" + date +
                ", productList=" + productList +
                '}';
    }
}
