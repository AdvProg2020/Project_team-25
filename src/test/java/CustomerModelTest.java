import Store.Main;
import Store.Model.*;
import Store.View.CustomerMenu;
import Store.View.MainMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CustomerModelTest {

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
    public void viewPersonalTest()
    {
        String input = "view personal info\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertTrue(outContent.toString().contains(MainMenu.currentUser.toString()));
    }
    @Test
    public void editPersonalTest()
    {
        String input = "view personal info\nedit email xx@gmail.com\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertEquals(MainMenu.currentUser.getEmail(), "xx@gmail.com");
    }
    @Test
    public void viewHelp()
    {
        String input = "help\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: \r\n" +
                "view personal info\r\n" +
                "view cart\r\n" +
                "purchase\r\n" +
                "view orders\r\n" +
                "view balance\r\n" +
                "view discount code\r\n" +
                "logout\r\n" +
                "help\r\n" +
                "back\r\n" +
                "*******\r\n" +
                "\n" +
                "List of commands in the view personal info submenu: \r\n" +
                "edit [password|family name|first name|email|phone number] [value]\r\n" +
                "back\r\n" +
                "\n" +
                "List of commands in the view cart submenu: \r\n" +
                "show products\r\n" +
                "view [ProductID]\r\n" +
                "increase [ProductID]\r\n" +
                "decrease [ProductID]\r\n" +
                "show total price\r\n" +
                "purchase\r\n" +
                "back\r\n" +
                "*******\r\n" +
                "\n" +
                "List of commands in the view orders submenu: \r\n" +
                "show order [orderID]\r\n" +
                "rate [Product ID] [1-5]\r\n" +
                "back\r\n" +
                "*******"));
    }
    @Test
    public void cartShowingTest()
    {
        String input = "view cart\nshow product\nback\nback\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer)MainMenu.currentUser).addToCart(Product.getAllProducts().get(0));
        ((Customer)MainMenu.currentUser).addToCart(Product.getAllProducts().get(1));
        CustomerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Product.getAllProducts().get(0) + "\r\n*******\r\n" + Product.getAllProducts().get(1)));
    }
    @Test
    public void cartAddingAndRemovingTest()
    {
        System.setOut(originalOut);
        String input = "view cart\nincrease 0\n increase 1\ndecrease 0\ndecrease 0\nback\nback\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertTrue(!((Customer)MainMenu.currentUser).isInCart(Product.getProductByID(0)) && ((Customer)MainMenu.currentUser).isInCart(Product.getProductByID(1)));
    }
    @Test
    public void cartTotalPriceTest()
    {
        System.setOut(originalOut);
        String input = "view cart\nincrease 0\nincrease 1\nincrease 2\nincrease 1\ndecrease 0\nshow total price\nback\nback\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertEquals(20,((Customer)MainMenu.currentUser).getTotalCartPrice(),3);
    }
    @Test
    public void showProductTest()
    {
        String input = "view cart\nview 0\nview 4\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Product.getAllProducts().get(0).toString()));
    }
    @Test
    public void purchaseWithoutOffCodeMoneyTest()
    {
        System.setOut(originalOut);
        String input = "purchase\nImamAliHighWay\n0912\nlab@lab.com\nnull\nview cart\nincrease 4\nshow product\nback\npurchase\nImamAliHighWay\n0912\nlab@lab.com\nnull\nback\nback\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(1));
        Seller seller1 = Product.getProductByID(0).getSeller();
        Seller seller2 = Product.getProductByID(1).getSeller();
        CustomerMenu.init();
        Assert.assertTrue(((Customer)MainMenu.currentUser).getMoney() == 975 && seller1.getMoney() == 1020 && seller2.getMoney() == 105);
    }
    @Test
    public void purchaseWithOffCodeMoneyTest()
    {
        System.setOut(originalOut);
        String input = "purchase\nImamAliHighWay\n0912\nlab@lab.com\nce98\nview cart\nincrease 6\nback\npurchase\nback\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        Seller seller1 = Product.getProductByID(0).getSeller();
        CustomerMenu.init();
        Assert.assertTrue(((Customer)MainMenu.currentUser).getMoney() == 979 && seller1.getMoney() == 1030);
    }
    @Test
    public void purchaseOtherTests()
    {
        System.setOut(originalOut);
        String input = "purchase\nImamAliHighWay\n0912\nlab@lab.com\nce98\nview cart\nincrease 6\nback\nback\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(1));
        Seller seller1 = Product.getProductByID(0).getSeller();
        Seller seller2 = Product.getProductByID(1).getSeller();
        CustomerMenu.init();
        Assert.assertTrue(((Customer)MainMenu.currentUser).hasBoughtProduct(Product.getProductByID(0)) && ((Customer)MainMenu.currentUser).hasBoughtProduct(Product.getProductByID(1)) && seller1.getBuyers().contains(MainMenu.currentUser.getName()) && seller2.getBuyers().contains(MainMenu.currentUser.getName()));
    }
    @Test
    public void viewsInCustomerMenu()
    {
        String input = "view balance\nview discount codes\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
        Assert.assertTrue(outContent.toString().contains("Your Balance: 1000") && outContent.toString().contains(Manager.getOffCodes().get(0) + "\r\nYour Number Of Usage: 0"));
    }
    @Test
    public void viewOrdersTest()
    {
        System.setOut(originalOut);
        String input = "purchase\nImamAliHighWay\n0912\nlab@lab.com\nce98\nview orders\nshow order 0\nshow order 1\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(1));
        CustomerMenu.init();
        Assert.assertTrue(true);
    }
    @Test
    public void rateTest()
    {
        System.setOut(originalOut);
        String input = "purchase\nImamAliHighWay\n0912\nlab@lab.com\nce98\nview orders\nrate 0 3\nrate 1 4\nrate 10 5\nrate 2 3\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(0));
        ((Customer) MainMenu.currentUser).addToCart(Product.getProductByID(1));
        CustomerMenu.init();
        Assert.assertEquals(Product.getProductByID(0).getAverageRating(), 3, 3);
    }
}