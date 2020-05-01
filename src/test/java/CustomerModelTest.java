import Store.Main;
import Store.Model.User;
import Store.View.CustomerMenu;
import Store.View.MainMenu;
import org.junit.Assert;
import org.junit.Test;
import org.testng.annotations.BeforeTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CustomerModelTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeTest
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
    public void cartTest()
    {
        String input = "view cart\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        CustomerMenu.init();
    }
   /* @Test
    public void addCustomerTest()
    {
        Customer customer = new Customer("ali00","ali","abasi","HEY","33326214","pass",20.5);
        Customer.addCustomer(customer);
        Assert.assertTrue(User.isExist(customer));
    }

    @Test
    public void canBuyTest()
    {
        Customer customer = new Customer("ali00","ali","abasi","HEY","33326214","pass",20.5);
        Customer.addCustomer(customer);
        Seller seller = new Seller("bahram00","bahram","abasi","HEY","33326214","pass",10,"sibsazi","khobim");
        Seller.addSeller(seller);
        Product product = new Product(CheckingStatus.CREATION, null, "apple", seller, "nike", 20.6, true, "", "clean");
        seller.addProduct(product);
        customer.addToCart(product);
        Assert.assertFalse(customer.canBuy());
    }
    @Test
    public void buyTest()
    {
        Customer customer = new Customer("ali00","ali","abasi","HEY","33326214","pass",20.5);
        Customer.addCustomer(customer);
        Seller seller = new Seller("bahram00","bahram","abasi","HEY","33326214","pass",10,"sibsazi","khobim");
        Seller.addSeller(seller);
        Product product = new Product(CheckingStatus.CREATION, null, "apple", seller, "nike", 5.5, true, "", "clean");
        seller.addProduct(product);
        customer.addToCart(product);
        customer.buy();
        Assert.assertTrue((customer.getMoney() == 15) && (seller.getMoney() == 15.5) && (customer.getCart().size() == 0) && (!seller.getProducts().contains(product)));
    }
    @Test
    public void cartTest()
    {
        Customer customer = new Customer("ali00","ali","abasi","HEY","33326214","pass",20.5);
        Customer.addCustomer(customer);
        Seller seller = new Seller("bahram00","bahram","abasi","HEY","33326214","pass",10,"sibsazi","khobim");
        Seller.addSeller(seller);
        Product product = new Product(CheckingStatus.CREATION, null, "apple", seller, "nike", 5.5, true, "", "clean");
        seller.addProduct(product);
        customer.addToCart(product);
        if(customer.isInCart(new Product(CheckingStatus.CREATION, null, "bannana", seller, "nike", 5.5, true, "", "clean")))
            Assert.fail();
        customer.removeFromCart(product);
        Assert.assertFalse(customer.getCart().contains(product));
    }
    @Test
    public void logTest()
    {
        Customer customer = new Customer("ali00","ali","abasi","HEY","33326214","pass",20.5);
        Customer.addCustomer(customer);
        Seller seller = new Seller("bahram00","bahram","abasi","HEY","33326214","pass",10,"sibsazi","khobim");
        Seller.addSeller(seller);
        Product product = new Product(CheckingStatus.CREATION, null, "apple", seller, "nike", 5.5, true, "", "clean");
        seller.addProduct(product);
        customer.addToCart(product);
        customer.buy();
        System.out.println(customer.getBuyLog().get(0).toString() + "\n" + new ArrayList<Product>(Arrays.asList(product)));
        Assert.assertTrue(Pattern.matches("BuyLogItem\\{offValue=0\\.0, sellerName='bahram', received=false, id=(\\d+), date=(.+)," + " productList=" + "\\[\\(apple 4\\)\\]" + "}", customer.getBuyLog().get(0).toString()));
     }*/
}