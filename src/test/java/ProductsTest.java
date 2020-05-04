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
    public void sortTest()
    {
        String input = "sorting\nshow available sorts\ncurrent sort\nsort boz\nsort rating\ncurrent sort\ndisable sort\ncurrent sort\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductsMenu.init();
        Assert.assertTrue(outContent.toString().contains("rating, price, visit, lexicographical.\r\n" +
                "*******\r\n" +
                "Current sort is: visit.\r\n" +
                "Mode isn't available!\r\n" +
                "Current sort is: rating.\r\n" +
                "Current sort is: visit."));
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
        // This test should change, because the output format has changed
        String input = "compare 4\ncompare product1\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductMenu.init(Product.getProductByID(1));
        Assert.assertTrue(outContent.toString().contains("---------- Comparison ----------\r\n" +
                "                                                                                                                                                                                  Product Name: product2 |                                                                                                                                                                                   Product Name: product5\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                   Product Brand: brand1 |                                                                                                                                                                                    Product Brand: brand1\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                   Description: describe |                                                                                                                                                                                    Description: describe\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                            Price: 5.0   |                                                                                                                                                                                             Price: 980.0\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                     Category: category2 |                                                                                                                                                                                      Category: category2\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                  Sellers: seller2       |                                                                                                                                                                                   Sellers: jackRipper   \n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                     Average rating: NaN |                                                                                                                                                                                      Average rating: NaN\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                                                                                        Date added: null |                                                                                                                                                                                         Date added: null\n" +
                "___________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "To compare to products, they must be from the same category!"));
    }
    @Test
    public void commentsTest()
    {
      //  System.setOut(originalOut);
        String input = "comments\nadd comment\nQuality\nit was on of the worst i've ever seen\nback\ncomments\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ProductMenu.init(Product.getProductByID(1));
        Assert.assertTrue(!Product.getProductByID(1).getComments().isEmpty() && outContent.toString().contains("Comments for product product2 with seller seller2: \r\n" +
                "________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "Title: Content: \r\n" +
                "\n" +
                "Product menu of: product2\n\r" +
                "\n" +
                "\n" +
                "Product menu of: product2 -> Show Comments\n\r" +
                "\n" +
                "Comments for product product2 with seller seller2: \r\n" +
                "________________________________________________________________________________________________________________________________________________________________________________________________________\r\n" +
                "                                                                                                                            Commenting user: customer1                      --Has not bought this product---> it was on of the worst i've ever seen"));
    }
}
