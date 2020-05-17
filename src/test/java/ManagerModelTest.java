import Store.Main;
import Store.Model.*;
import Store.Model.Enums.VerifyStatus;
import Store.View.MainMenu;
import Store.View.ManagerMenu;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class ManagerModelTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void editPhoneTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit phone number\n123\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("123", manager.getPhoneNumber());
    }

    @Test
    public void ViewEditEmailTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit email\ntest@test.com\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test@test.com", manager.getEmail());
    }

    @Test
    public void ViewEditFirstNameTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit first name\nali\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("ali", manager.getName());
    }

    @Test
    public void ViewEditLastNameTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit family name\ntest\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test", manager.getFamilyName());
    }

    @Test
    public void ViewEditPasswordTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit password\n1234\ntest\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test", manager.getPassword());
    }

    @Test
    public void DeleteCustomerTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage users\ndelete user customer1\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(null, Manager.getUserByUsername("customer1"));
    }

    @Test
    public void DeleteSellerTest() {
        Main.setTest();
        Main.setOffers();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage users\ndelete user seller2\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(null, Manager.getUserByUsername("seller2"));
    }

    @Test
    public void CreateManagerTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage users\ncreate manager profile\nali\n123\nali\nchekah\nali@ali.com\n0912\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("ali", User.getUserByUsername("ali").getName());
    }

    @Test
    public void manageAllProductsTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage all products\nremove 1\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(null, Product.getProductByID(1));
    }

    @Test
    public void createDiscountCodeTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("create discount code\ntest\n20\n123.4\n2\n2020/1/1\n2019/1/1\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test", Manager.getOffCodeByCode("test").getCode());
    }

    @Test
    public void removeDiscountCodeTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("create discount code\ntest\n20\n123.4\n2\n2020/1/1\n2019/1/1\nview discount codes\nremove discount code test\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(null, Manager.getOffCodeByCode("test"));
    }

    @Test
    public void editDiscountCodePercentTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("create discount code\ntest\n20\n123.4\n2\n2020/1/1\n2019/1/1\nview discount codes\nedit discount code test offPercentage 30\nback\nback\n").getBytes()));
        ManagerMenu.init();
        if (Manager.getOffCodeByCode("test").getOffPercentage() == 30.0) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void editMaximumOffTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("create discount code\ntest\n20\n123.4\n2\n2020/1/1\n2019/1/1\nview discount codes\nedit discount code test offPercentage 22.5\nback\nback\n").getBytes()));
        ManagerMenu.init();
        if (Manager.getOffCodeByCode("test").getOffPercentage() == 22.5) {
            Assert.assertTrue(true);
        } else {
            Assert.assertTrue(false);
        }
    }

    @Test
    public void ManageRequestTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage requests\naccept 1\nback\nback\n").getBytes()));
        Request request = Manager.getRequestById(1);
        ManagerMenu.init();
        Assert.assertEquals(request.getStatus(), VerifyStatus.ACCEPTED);
    }

    @Test
    public void addCategoryTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test", Manager.categoryByName("test").getFullName());
    }

    @Test
    public void removeCategoryTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nremove test\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(null, Manager.categoryByName("test"));
    }

    @Test
    public void changeCategoryNameTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nedit test change name test2\nback\nback\n").getBytes()));
        ManagerMenu.init();
        System.out.println(Manager.getAllCategories());
        Assert.assertEquals("test2", Manager.categoryByName("test2").getFullName());
    }

    @Test
    public void addFilterCategoryTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nedit test add filter filter1\nedit test add filter filter2\nback\nback\n").getBytes()));
        ManagerMenu.init();
        System.out.println(Manager.getAllCategories());
        Assert.assertEquals(Arrays.asList("filter1", "filter2"), Manager.categoryByName("test").getFilters());
    }

    @Test
    public void removeFilterCategoryTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nedit test add filter filter1\nedit test add filter filter2\nedit test remove filter filter1\nback\nback\n").getBytes()));
        ManagerMenu.init();
        System.out.println(Manager.getAllCategories());
        Assert.assertEquals(Arrays.asList("filter2"), Manager.categoryByName("test").getFilters());
    }

    @Test
    public void addProductCategoryTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nedit test add product 0\nback\nback\n").getBytes()));
        ManagerMenu.init();
        System.out.println(Manager.getAllCategories());
        Assert.assertEquals(Arrays.asList(Product.getProductByID(0)), Manager.categoryByName("test").getImmediateProducts());
    }

    @Test
    public void removeProductCategoryTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage categories\nadd test\nnull\nadd test2\ntest\nedit test2 add product 0\nedit test2 add product 1\nedit test remove product 0\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(Arrays.asList(Product.getProductByID(1)), Manager.categoryByName("test2").getImmediateProducts());
    }

    @Test
    public void helpTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: "));
    }

    @Test
    public void viewOffCodeTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("view discount codes\nview discount code ce98\nback\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Manager.getOffCodeByCode("ce98").toString()));
    }

    @Test
    public void viewUserTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("manage users\nview cloudStrife\nback\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Manager.getUserByUsername("cloudStrife").toString()));
    }

    @Test
    public void viewRequestTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("manage requests\ndetails 0\nback\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(outContent.toString().contains(Manager.getRequestById(1).toString()));
    }

    @Test
    public void userSortTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage users\nsort by phone number\nsort by name\nsort by family name\nsort by username\nsort by email\nback\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void productSortTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage all products\nsort by visit\nsort by lexicographical\nsort by price\nsort by rating\nback\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void offCodeSortTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("view discount codes\nsort by time of starting\nsort by time of ending\nsort by code\nsort by usage count\nsort by maximum off\nsort by off percentage\nback\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(true);
    }

    @Test
    public void logoutTest() {  // should comment exitAll in MainMenu
        System.setOut(new PrintStream(outContent));
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("logout\nlogout\nexit\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(outContent.toString().contains("You haven't signed in!"));
    }

    @Test
    public void assignOffCodeTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("assign discount code\nAP333\ncustomer1\nback").getBytes()));
        ManagerMenu.init();
        Assert.assertTrue(((Customer)User.getUserByUsername("customer1")).getOffCodes().containsKey(Manager.getOffCodeByCode("AP333")));
    }

}
