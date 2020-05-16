import Store.Main;
import Store.Model.*;
import Store.View.CustomerMenu;
import Store.View.MainMenu;
import Store.View.SellerMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        System.setIn(new ByteArrayInputStream("view personal info\nedit first name\nali\nback\nback".getBytes()));
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
    public void viewSalesHistory()
    {
        System.setOut(new PrintStream(outContent));
        String input = "purchase\nali\nHighway\nx@x\nnull\nback\nview sales history\nback\npurchase\nali\nHighway\nx@x\nnull\nback\nview sales history\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer)User.getUserByUsername("customer1")).addToCart(Product.getProductByID(3));
        ((Customer)User.getUserByUsername("customer1")).addToCart(Product.getProductByID(2));
        MainMenu.currentUser = User.getUserByUsername("customer1");
        CustomerMenu.init();
        MainMenu.currentUser = seller;
        SellerMenu.init();
        ((Customer)User.getUserByUsername("customer1")).addToCart(Product.getProductByID(1));
        MainMenu.currentUser = User.getUserByUsername("customer1");
        CustomerMenu.init();
        MainMenu.currentUser = seller;
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("SellLogItem{incomeValue=15.5") && outContent.toString().contains("SellLogItem{incomeValue=5.0"));
    }
    @Test
    public void viewProductTest()
    {
        System.setOut(new PrintStream(outContent));
        String input = "manage products\nview 1\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("Product menu of: product2"));
    }
    @Test
    public void sortTest()
    {
        System.setOut(new PrintStream(outContent));
        String input = "manage products\nsort by 1\nsort by rating\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("{product2 1, category2, available}\r\n" +
                "{product3 3, category1 -> category3, available}"));
    }
    @Test
    public void viewBuyerTest()
    {
        System.setOut(new PrintStream(outContent));
        String input = "purchase\nali\nHighway\nx@x\nnull\nback\nmanage products\nview buyers 3\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ((Customer)User.getUserByUsername("customer1")).addToCart(Product.getProductByID(3));
        ((Customer)User.getUserByUsername("customer1")).addToCart(Product.getProductByID(1));
        MainMenu.currentUser = User.getUserByUsername("customer1");
        CustomerMenu.init();
        MainMenu.currentUser = seller;
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("Buyers of selected product: \r\n" +
                "customer1"));
    }
    @Test
    public void editProductTest()
    {
        String input = "manage products\nedit 3\n1\nname\nbrand\n10.7\n1\nAB\nBC\n-1\ndescribe\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(Request.getAllRequests().size() >= 2);
    }
    @Test
    public void addFilterTest()
    {
        String input = "manage products\nadd filter 3 XY\nadd filter 3 AB\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(Product.getProductByID(3).hasFilter("XY"));
    }
    @Test
    public void removeFilterTest()
    {
        String input = "manage products\nremove filter 3 AX\nremove filter 3 AB\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(!Product.getProductByID(3).hasFilter("XY"));
    }
    @Test
    public void removeProduct() {
        String input = "remove product 100\nremove product 0\nremove product 1\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertFalse(seller.getProducts().contains(Product.getProductByID(1)));
    }

    @Test
    public void addProductTest() {
        String input = "add product\n1\nname\nbrand\n10.7\n1\nAB\nBC\n-1\ndescribe\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(Request.getAllRequests().size() >= 2);
    }

    @Test
    public void viewBalanceTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("view balance\nback".getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Double.toString(seller.getMoney())));
    }

    @Test
    public void showCategory()
    {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream("show categories\nback".getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("category1 -> category3"));
    }
    @Test
    public void viewOneOfferTest()
    {
        System.setOut(new PrintStream(outContent));
        Main.setOffers();
        String input = "view offs\n.dsa\nview 1\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("(product3 ID:3 Seller:seller2 Price:15.5, [AB, CD])"));
    }
    @Test
    public void sortOffsTest()
    {
        System.setOut(new PrintStream(outContent));
        Main.setOffers();
        String input = "view offs\nsort by time of starting\nsort by x\nsort by time of ending\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
    }
    @Test
    public void editOfferTest()
    {
        Main.setOffers();
        String input = "view offs\nedit 1\n11-1-2021 08:08:08\n11-1-2026 08:08:08\n60\n1\n2\n3\n1\n-1\nQR\nRS\n-1\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertEquals(Request.getAllRequests().size(), 2);
    }
    @Test
    public void addOffTest() {
        Main.setOffers();
        String input = "view offs\nadd off\n11-1-2021 08:08:08\n11-1-2026 08:08:08\n60\n1\n2\n3\n1\n-1\nQR\nRS\n-1\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertEquals(Request.getAllRequests().size(), 2);
    }

    @Test
    public void addFilterOffs()
    {
        Main.setOffers();
        String input = "view offs\nadd filter 10 HI\nadd filter 1 HI\nadd filter 1 IJ\nadd filter 1 HI\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(Offer.getOfferByID(1).hasFilter("HI") && Offer.getOfferByID(1).hasFilter("IJ"));
    }

    @Test
    public void removeFilterOffs()
    {
        Main.setOffers();
        String input = "view offs\nadd filter 1 HI\nadd filter 1 IJ\nremove filter 10 XY\nremove filter 1 HI\nremove filter 1 HI\nback\nback\nback";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        SellerMenu.init();
        Assert.assertTrue(!Offer.getOfferByID(1).hasFilter("HI") && Offer.getOfferByID(1).hasFilter("IJ"));
    }
    @Test
    public void helpTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        SellerMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: "));
    }
}
