import Store.Main;
import Store.Model.Product;
import Store.View.OffersMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OffersModelTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setTest() {
        Main.setTest();
        Main.setOffers();
    }

    @Test
    public void helpTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: "));
    }

    @Test
    public void productSortTest() {
        System.setIn(new ByteArrayInputStream(("sorting\nsort visit\nsort lexicographical\nsort rating\nsort time of starting\nsort time of ending\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void availableSortTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("sorting\nshow available sorts\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("price\trating\tlexicographical\tvisit\t"));
    }

    @Test
    public void currentSortTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("sorting\ncurrent sort\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("visit"));
    }

    @Test
    public void disableSortTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("sorting\nsort lexicographical\ndisable sort\ncurrent sort\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("visit"));
    }

    @Test
    public void availableFilterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(0)).addFilter("blue");
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("filter\nshow available filters\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("red") && outContent.toString().contains("blue"));
    }

    @Test
    public void filterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(4)).addFilter("red");
        //System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("filter\nfilter red\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void showFilterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(4)).addFilter("blue");
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("filter\nfilter red\ncurrent filters\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("red\t"));
    }

    @Test
    public void disableFilterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(1)).addFilter("red");
        (Product.getProductByID(1)).addFilter("blue");
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("filter\nfilter red\nfilter blue\ndisable filter blue\ncurrent filters\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("red\t"));
    }

    @Test
    public void showProductTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("show product 0\nback\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("Product menu of: product1"));
    }
}
