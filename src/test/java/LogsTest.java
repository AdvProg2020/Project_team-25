import Store.Model.Log.BuyLogItem;
import Store.Model.Log.SellLogItem;
import Store.Model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class LogsTest {
    @Test
    public void sellLogTests() {
        SellLogItem test = new SellLogItem(1, new Date(), new ArrayList<Product>(), 10.9, 12, "testname", true);
        test.setSendStatus(false);
        Assert.assertEquals(test.toString(), "SellLogItem{" +
                "incomeValue=" + 10.9 +
                ", offValue=" + 12.0 +
                ", customerName='" + "testname" + '\'' +
                ", sendStatus=" + false +
                ", id=" + 1 +
                ", date=" + new Date() +
                ", productList=" + new ArrayList<Product>() +
                '}');
    }

    @Test
    public void buyLogItems() {
        BuyLogItem test = new BuyLogItem(10, new Date(), new ArrayList<Product>(), 1098.989, "testname", true);
        test.setReceived(false);
        Assert.assertEquals(test.toString(), "BuyLogItem{" +
                "offValue=" + 1098.989 +
                ", sellerName='" + "testname" + '\'' +
                ", received=" + false +
                ", id=" + 10 +
                ", date=" + new Date() +
                ", productList=" + new ArrayList<Product>() +
                '}');
    }
}
