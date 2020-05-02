import Store.Main;
import Store.Model.Manager;
import Store.Model.Product;
import Store.Model.Seller;
import Store.View.MainMenu;
import Store.View.ManagerMenu;
import Store.View.SellerMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class SellerModelTest {
    private Seller seller;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setSellerTest() {
        Main.setTest();
        seller = (Seller) Seller.getUserByUsername("seller2");
        MainMenu.currentUser = seller;
    }

    @Test
    public void editPhoneTest() {
        System.setIn(new ByteArrayInputStream("view personal info\nedit phone number\n123\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("123", seller.getPhoneNumber());
    }

    @Test
    public void viewEditEmailTest() {
        System.setIn(new ByteArrayInputStream("view personal info\nedit email\ntest@test.com\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("test@test.com", seller.getEmail());
    }

    @Test
    public void viewEditFirstNameTest() {
        System.setIn(new ByteArrayInputStream("view personal info\nedit name\nali\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("ali", seller.getName());
    }

    @Test
    public void viewEditLastNameTest() {
        System.setIn(new ByteArrayInputStream("view personal info\nedit family name\ntest\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("test", seller.getFamilyName());
    }

    @Test
    public void viewEditPasswordTest() {
        System.setIn(new ByteArrayInputStream("view personal info\nedit password\n1234\ntest\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("test", seller.getPassword());
    }

    @Test
    public void viewCompanyInfoTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("view company information\nback".getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains(seller.getCompanyDescription()));
    }

    @Test
    public void removeProduct() {
        System.setIn(new ByteArrayInputStream("add product\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals(Arrays.asList(Product.getProductByID(3)), seller.getProducts());
    }

    @Test
    public void addProductTest() {

    }

    @Test
    public void viewBalanceTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("view balance\nback".getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Double.toString(seller.getMoney())));
    }

    @Test
    public void addOffTest() {

    }

    @Test
    public void helpTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: "));
    }
}
