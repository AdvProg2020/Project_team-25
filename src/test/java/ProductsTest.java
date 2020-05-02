import Store.Main;
import Store.Model.User;
import Store.View.MainMenu;
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
    public void helpTest()
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
    /*@Test
    public void*/
}
