import Store.Main;
import Store.Model.Enums.VerifyStatus;
import Store.Model.Manager;
import Store.Model.Product;
import Store.Model.Request;
import Store.Model.User;
import Store.View.MainMenu;
import Store.View.ManagerMenu;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ManagerModelTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void editPhoneTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit phone number 123\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("123", manager.getPhoneNumber());
    }

    @Test
    public void ViewEditEmailTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit email test@test.com\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test@test.com", manager.getEmail());
    }

    @Test
    public void ViewEditFirstNameTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit first name ali\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("ali", manager.getName());
    }

    @Test
    public void ViewEditLastNameTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit family name test\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test", manager.getFamilyName());
    }

    @Test
    public void ViewEditPasswordTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream("view personal info\nedit password test\nback\nback".getBytes()));
        ManagerMenu.init();
        Assert.assertEquals("test", manager.getPassword());
    }

    @Test
    public void DeleteUserTest() {
        Main.setTest();
        Manager manager = (Manager) Manager.getUserByUsername("cloudStrife");
        MainMenu.currentUser = manager;
        System.setIn(new ByteArrayInputStream(("manage users\ndelete user customer1\nback\nback\n").getBytes()));
        ManagerMenu.init();
        Assert.assertEquals(null, Manager.getUserByUsername("customer1"));
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

}
