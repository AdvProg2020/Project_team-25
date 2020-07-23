package Store.Model.Log;

import Store.Model.Product;

import java.util.ArrayList;
import java.util.Date;

public class SellLogItem extends LogItem {

    private double incomeValue;
    private double offValue;
    private String customerName;
    private boolean sendStatus;

    public double getIncomeValue() {
        return incomeValue;
    }

    public double getOffValue() {
        return offValue;
    }

    public boolean isSendStatus() {
        return sendStatus;
    }

    public SellLogItem(Date date, ArrayList<Product> products, double incomeValue, double offValue, String customerName, boolean sendStatus) {
        super(date, products);
        this.incomeValue = incomeValue;
        this.offValue = offValue;
        this.customerName = customerName;
        this.sendStatus = sendStatus;
    }

    public void setSendStatus(boolean status) {
        this.sendStatus = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        return "SellLogItem{" +
                "incomeValue=" + incomeValue +
                ", offValue=" + offValue +
                ", customerName='" + customerName + '\'' +
                ", sent=" + sendStatus +
                ", id=" + id +
                ", date=" + date +
                ", productList=" + productList +
                '}';
    }
}
