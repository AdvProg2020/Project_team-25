import Store.Model.Manager;
import Store.Model.User;
import Store.ResourceHandler;
import Store.View.SignUpAndLoginMenu;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ManagerModelTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void saveAndLoadManger() {
        Manager manager = new Manager("manager", "cloud", "Strife", "xx.yyy@jojo.com", "091111111", "1234");
        ResourceHandler.writeAll();
        ResourceHandler.readAll();
        Assert.assertEquals(Manager.isExist(manager), true);
    }

    @Test
    public void resetTest() {
        ResourceHandler.resetFile();
        File file = new File("src/resources/Resources.res");
        Assert.assertEquals(file.exists(), false);
    }

    @Test
    public void RegisterManagerTest() {
        System.setIn(new ByteArrayInputStream("create account manager cloudStrife\n1234\ncloud\nstrife\nlab@lab.com\n0912\nback".getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertEquals(Manager.getUserByUsername("cloudStrife"), new Manager("cloudStrife", "cloud", "strife", "lab@lab.com", "0912", "1234"));
    }



}
