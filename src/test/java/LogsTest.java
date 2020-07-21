import Store.Main;
import Store.Model.Log.BuyLogItem;
import Store.Model.Log.SellLogItem;
import Store.Model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class LogsTest {


    @Test
    public void sellLogTests() {
        ArrayList<Product> products = new ArrayList<>(Arrays.asList(Product.getProductByName("product1"), Product.getProductByName("product2")));
        SellLogItem test = new SellLogItem(new Date(), products, 10.9, 12, "testname", true);
        test.setSendStatus(false);
        Assert.assertEquals(test.toString(), "SellLogItem{" +
                "incomeValue=" + 10.9 +
                ", offValue=" + 12.0 +
                ", customerName='" + "testname" + '\'' +
                ", sendStatus=" + false +
                ", id=" + 1 +
                ", date=" + new Date() +
                ", productList=" + products +
                '}');
        Assert.assertEquals("testname", test.getCustomerName());
    }

    @Test
    public void buyLogItems() {
        ArrayList<Product> products = new ArrayList<>(Arrays.asList(Product.getProductByName("product1"), Product.getProductByName("product2")));
       // Main.setTest();
        BuyLogItem test = new BuyLogItem(new Date(), products, 1098.989, "testname", true, " ");
        test.setReceived(false);
        Assert.assertEquals(test.toString(), "BuyLogItem{" +
                "offValue=" + 1098.989 +
                ", sellerName='" + "testname" + '\'' +
                ", received=" + false +
                ", id=" + 10 +
                ", date=" + new Date() +
                ", productList=" + test.getProducts() +
                '}');
    }
}
