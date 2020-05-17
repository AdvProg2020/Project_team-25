import Store.Main;
import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import Store.View.MainMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DirectTest {

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
    public void getCategoryByNameTest()
    {
        Assert.assertTrue(Manager.getAllCategories().contains(Category.getCategoryByName("category3")));
    }
    @Test
    public void removeProductFromParents()
    {
        Category.getCategoryByName("category3").removeProductFrom(Product.getProductByID(2));
        Category.getCategoryByName("category3").removeProductFrom(Product.getProductByID(3));
        Assert.assertFalse(Category.getCategoryByName("category1").include(Product.getProductByID(2)));
    }
    @Test
    public void periodicOffCode()
    {
        Manager.checkPeriodOffCode();
        Assert.assertTrue(Manager.getOffCodes().size() == 4);
    }
    @Test
    public void checkRepetitiousSellerRequests()
    {
        Seller seller1 = new Seller("check","check","check","check@check.com", "0912", "check", 10000, "", "");
        Seller seller2 = new Seller("check","check2","check2","check2@check2.com", "0912", "check", 10000, "", "");
        Request request1 = new Request(seller1);
        Request request2 = new Request(seller2);
        Manager.addRequest(request1);
        Manager.addRequest(request2);
        ((Manager)Manager.getUserByUsername("jojoRabbit")).handleRequest(request1, true);
        Assert.assertFalse(Manager.getPendingRequests().contains(request2));
    }

    @Test
    public void checkRepetitiousProductRequests()
    {
        Seller seller = (Seller)User.getUserByUsername("seller2");
        Product product1 = new Product(CheckingStatus.CREATION, null, "X", seller, "B", 980, true, "describe");
        Product product2 = new Product(CheckingStatus.CREATION, null, "Y", seller, "B", 980, true, "describe");
        Product product3 = new Product(CheckingStatus.CREATION, null, "Z", seller, "B", 980, true, "describe");
        Product product4 = new Product(CheckingStatus.CREATION, null, "T", seller, "B", 980, true, "describe");
        Product product5 = new Product(CheckingStatus.CREATION, null, "P", seller, "B", 980, true, "describe");
        Product product6 = new Product(CheckingStatus.CREATION, null, "Q", seller, "B", 980, true, "describe");
        product1.assignToSeller();
        product2.assignToSeller();
        product5.assignToSeller();
        Product.addProduct(product1);
        Product.addProduct(product2);
        Product.addProduct(product5);
        Request request1 = new Request(product6, false, null);
        Request request2 = new Request(product1, true, product6);
        Request request3 = new Request(product2, true, product3);
        Request request4 = new Request(product2, true, product4);
        Request request5 = new Request(product5, true, product4);
        Manager.addRequest(request1);
        Manager.addRequest(request2);
        Manager.addRequest(request3);
        Manager.addRequest(request4);
        Manager.addRequest(request5);
        ((Manager)Manager.getUserByUsername("jojoRabbit")).handleRequest(request1, true);
        ((Manager)Manager.getUserByUsername("jojoRabbit")).handleRequest(request4, true);
        Assert.assertFalse(Manager.getPendingRequests().contains(request2) || Manager.getPendingRequests().contains(request3) || Manager.getPendingRequests().contains(request5));
    }

    @Test
    public void checkRepetitiousOfferRequests()
    {
        Main.setOffers();
        Seller seller = (Seller)User.getUserByUsername("seller2");
        Product product1 = new Product(CheckingStatus.CREATION, null, "X", seller, "B", 980, true, "describe");
        Product product2 = new Product(CheckingStatus.CREATION, null, "Y", seller, "B", 980, true, "describe");
        Product product5 = new Product(CheckingStatus.CREATION, null, "P", seller, "B", 980, true, "describe");
        product1.assignToSeller();
        product2.assignToSeller();
        product5.assignToSeller();
        Product.addProduct(product1);
        Product.addProduct(product2);
        Product.addProduct(product5);
        Offer offer1 = new Offer(seller, CheckingStatus.CREATION, 20);
        Offer offer2 = new Offer(seller, CheckingStatus.CREATION, 25);
        Offer offer3 = new Offer(seller, CheckingStatus.CREATION, 10);
        offer1.addProduct(product1);
        offer1.addProduct(product2);
        offer2.addProduct(product5);
        offer2.addProduct(product2);
        offer3.addProduct(product1);
        offer3.addProduct(product5);
        Request request1 = new Request(seller, offer1, true, offer2);
        Request request2 = new Request(seller, offer2, false, null);
        Request request3 = new Request(seller, offer1, true, offer3);
        Manager.addRequest(request1);
        Manager.addRequest(request2);
        Manager.addRequest(request3);
        ((Manager)Manager.getUserByUsername("jojoRabbit")).handleRequest(request1, true);
        Assert.assertFalse(Manager.getPendingRequests().contains(request2) || Manager.getPendingRequests().contains(request3));
    }
    @Test
    public void removeOffCode()
    {
        Manager.getOffCodeByCode("ce98").remove();
        Assert.assertFalse(Manager.getOffCodeByCode("ce98").isUserIncluded(User.getUserByUsername("customer1")));
    }
}
