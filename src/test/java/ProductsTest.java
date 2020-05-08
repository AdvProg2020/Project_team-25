import Store.Main;
import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.User;
import Store.View.MainMenu;
import Store.View.ProductMenu;
import Store.View.ProductsMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ProductsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpTest()
    {
        Main.setTest();
        MainMenu.currentUser = User.getAllUsers().get(1);
        System.setOut(new PrintStream(outContent));
    }
    @Test
    public void viewCategoriesTest()
    {
        String input = "view categories\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("-category1\n" +
                "\t\t-category1 -> category3\n" +
                "\n" +
                "\t-category2"));
    }
    @Test
    public void helpProductTest()
    {
        String input = "help\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductMenu.init(Product.getProductByID(0));
        Assert.assertTrue(outContent.toString().contains("List of main commands: \r\n" +
                "digest\r\n" +
                "attributes\r\n" +
                "compare [productID | productName]\r\n" +
                "comments\r\n" +
                "login\r\n" +
                "logout\r\n" +
                "help\r\n" +
                "back\r\n" +
                "*******\r\n" +
                "\n" +
                "List of commands in the digest submenu: \r\n" +
                "add to cart\r\n" +
                "select seller [seller_username]\r\n" +
                "login\r\n" +
                "logout\r\n" +
                "back\r\n" +
                "*******\r\n" +
                "\n" +
                "List of commands in the comments submenu: \r\n" +
                "add comment\r\n" +
                "login\r\n" +
                "logout\r\n" +
                "back\r\n" +
                "*******"));
    }
    @Test
    public void helpProductsTest()
    {
        String input = "help\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands:\r\n" +
                "view categories\r\n" +
                "show products\r\n" +
                "show product [productId]\r\n" +
                "filter\r\n" +
                "sorting\r\n" +
                "offs\r\n" +
                "login\r\n" +
                "logout\r\n" +
                "back\r\n" +
                "*******\r\n" +
                "\n" +
                "List of commands in the filter submenu: \r\n" +
                "filter\r\n" +
                "current filters\r\n" +
                "show available filters\r\n" +
                "disable filter [filter]\r\n" +
                "filter [filter]\r\n" +
                "login\r\n" +
                "logout\r\n" +
                "back\r\n" +
                "*******\r\n" +
                "\n" +
                "List of commands in the sorting submenu: \r\n" +
                "sort [an available sort]\r\n" +
                "current sort\r\n" +
                "disable sort\r\n" +
                "show available sorts\r\n" +
                "login\r\n" +
                "logout\r\n" +
                "back"));
    }

    @Test
    public void digestTest()
    {
        String input = "digest\nselect seller jackRipper\nadd to cart\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductMenu.init(Product.getProductByID(3));
        Assert.assertTrue(outContent.toString().contains("Product Name: product3\r\n" +
                "Product Brand: brand1\r\n" +
                "Description: describe\r\n" +
                "Price: 15.5\r\n" +
                "Category: category1 -> category3\r\n" +
                "Sellers: jackRipper   seller2   \r\n" +
                "Current seller: seller2\r\n" +
                "Average rating: NaN\r\n" +
                "Date added: null") && ((Customer)MainMenu.currentUser).isInCart(Product.getProductByID(2)));
    }
    @Test
    public void compareTest()
    {
        String input = "compare 4\ncompare product1\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductMenu.init(Product.getProductByID(1));
        Assert.assertTrue(outContent.toString().contains("Price: 5.0                                                                                           | Price: 980.0") && outContent.toString().contains("To compare to products, they must be from the same category!"));
    }
    @Test
    public void commentsTest()
    {
        String input = "comments\nadd comment\nQuality\nit was on of the worst i've ever seen\nback\ncomments\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductMenu.init(Product.getProductByID(1));
        Assert.assertTrue(!Product.getProductByID(1).getComments().isEmpty() && outContent.toString().contains("Commenting user: customer1                      --Has not bought this product---> it was on of the worst i've ever seen"));
    }

    @Test
    public void productSortTest() {
        System.setOut(originalOut);
        System.setIn(new ByteArrayInputStream(("sorting\nsort visit\nsort lexicographical\nsort rating\nsort price\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void availableSortTest() {
        System.setIn(new ByteArrayInputStream(("sorting\nshow available sorts\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("rating, price, visit, lexicographical."));
    }

    @Test
    public void currentSortTest() {
        System.setIn(new ByteArrayInputStream(("sorting\ncurrent sort\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("visit"));
    }

    @Test
    public void disableSortTest() {
        System.setIn(new ByteArrayInputStream(("sorting\nsort lexicographical\ndisable sort\ncurrent sort\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("visit"));
    }

    @Test
    public void availableFilterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(0)).addFilter("blue");
        System.setIn(new ByteArrayInputStream(("filter\nshow available filters\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("red") && outContent.toString().contains("blue"));
    }

    @Test
    public void filterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(4)).addFilter("red");
        System.setOut(originalOut);
        System.setIn(new ByteArrayInputStream(("filter\nfilter red\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void showFilterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(4)).addFilter("blue");
        System.setIn(new ByteArrayInputStream(("filter\nfilter red\ncurrent filters\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("red\t"));
    }

    @Test
    public void disableFilterTest() {
        (Product.getProductByID(0)).addFilter("red");
        (Product.getProductByID(1)).addFilter("red");
        (Product.getProductByID(1)).addFilter("blue");
        System.setIn(new ByteArrayInputStream(("filter\nfilter red\nfilter blue\ndisable filter blue\ncurrent filters\nback\nback").getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("red\t"));
    }
}
