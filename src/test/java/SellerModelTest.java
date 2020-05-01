import Store.Main;
import Store.Model.Manager;
import Store.Model.Seller;
import Store.View.MainMenu;
import Store.View.ManagerMenu;
import Store.View.SellerMenu;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class SellerModelTest {
    @Test
    public void editPhoneTest() {
        Main.setTest();
        Seller seller = (Seller) Seller.getUserByUsername("seller2");
        MainMenu.currentUser = seller;
        System.setIn(new ByteArrayInputStream("view personal info\nedit phone number\n123\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("123", seller.getPhoneNumber());
    }

    @Test
    public void ViewEditEmailTest() {
        Main.setTest();
        Seller seller = (Seller) Seller.getUserByUsername("seller2");
        MainMenu.currentUser = seller;
        System.setIn(new ByteArrayInputStream("view personal info\nedit email\ntest@test.com\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("test@test.com", seller.getEmail());
    }

    @Test
    public void ViewEditFirstNameTest() {
        Main.setTest();
        Seller seller = (Seller) Seller.getUserByUsername("seller2");
        MainMenu.currentUser = seller;
        System.setIn(new ByteArrayInputStream("view personal info\nedit name\nali\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("ali", seller.getName());
    }

    @Test
    public void ViewEditLastNameTest() {
        Main.setTest();
        Seller seller = (Seller) Seller.getUserByUsername("seller2");
        MainMenu.currentUser = seller;
        System.setIn(new ByteArrayInputStream("view personal info\nedit family name\ntest\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("test", seller.getFamilyName());
    }

    @Test
    public void ViewEditPasswordTest() {
        Main.setTest();
        Seller seller = (Seller) Seller.getUserByUsername("seller2");
        MainMenu.currentUser = seller;
        System.setIn(new ByteArrayInputStream("view personal info\nedit password\n1234\ntest\nback\nback".getBytes()));
        SellerMenu.init();
        Assert.assertEquals("test", seller.getPassword());
    }
}
