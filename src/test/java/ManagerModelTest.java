import Store.Model.Manager;
import Store.ResourceHandler;
import org.junit.Assert;
import org.junit.Test;

public class ManagerModelTest {

    @Test
    public void saveAndLoadManger() {
        Manager manager = new Manager("manager", "cloud", "Strife", "xx.yyy@jojo.com", "091111111", "1234");
        ResourceHandler.writeAll();
        ResourceHandler.readAll();
        Assert.assertEquals(Manager.isExist(manager), true);
    }


}
