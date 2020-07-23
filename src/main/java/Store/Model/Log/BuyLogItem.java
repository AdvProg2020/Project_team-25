package Store.Model.Log;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public class BuyLogItem extends LogItem {

    private double offValue;
    private String sellerName;
    private boolean received;
    private boolean showed;
    private String address;

    public BuyLogItem(Date date, ArrayList<Product> products, double offValue, String sellerName, boolean received, String address) {
        super(date, products);
        this.offValue = offValue;
        this.sellerName = sellerName;
        this.received = received;
        this.address = address;
    }

    public boolean isShowed()
    {
        return showed;
    }
    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getOffValue() {
        return offValue;
    }

    public String getSellerName() {
        return sellerName;
    }

    public boolean isReceived() {
        return received;
    }

    @Override
    public String toString() {
        showed = true;
        return "BuyLogItem{" +
                "offValue=" + offValue +
                ", sellerName=" + sellerName +
                ", sent=" + received +
                ", id=" + id +
                ", date=" + date +
                ", productList=" + productList +
                '}';
    }
}
