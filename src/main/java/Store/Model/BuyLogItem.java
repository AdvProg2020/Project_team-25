package Store.Model;

public class BuyLogItem extends LogItem {

    private double offValue;
    private String sellerName;
    private boolean recieved;

    BuyLogItem(int id, Date date, double offValue, String sellerName, boolean recieved) {
        super(id, date);
    }
}
