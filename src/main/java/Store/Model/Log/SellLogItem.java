package main.java.Store.Model.Log;

import java.util.Date;

public class SellLogItem extends LogItem {

    private double incomeValue;
    private double offValue;
    private String customerName;
    private boolean sendStatus;

    SellLogItem(int id, Date date, double incomeValue, double offValue, String customerName, boolean sendStatus) {
        super(id, date);
    }
}
