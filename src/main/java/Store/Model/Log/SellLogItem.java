package Store.Model.Log;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public class SellLogItem extends LogItem {

    private double incomeValue;
    private double offValue;
    private String customerName;
    private boolean sendStatus;

    public SellLogItem(int id, Date date, ArrayList<Product> products, double incomeValue, double offValue, String customerName, boolean sendStatus) {
        super(id, date, products);
        this.incomeValue = incomeValue;
        this.offValue = offValue;
        this.customerName = customerName;
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "SellLogItem{" +
                "incomeValue=" + incomeValue +
                ", offValue=" + offValue +
                ", customerName='" + customerName + '\'' +
                ", sendStatus=" + sendStatus +
                ", id=" + id +
                ", date=" + date +
                ", productList=" + productList +
                '}';
    }
}
