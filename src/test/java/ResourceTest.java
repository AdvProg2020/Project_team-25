import Store.Main;
import Store.Model.User;
import Store.ResourceHandler;
import org.junit.Assert;
import org.junit.Test;

public class ResourceTest {
    @Test
    public void setResourceTest() {
        //Main.setTest();
        ResourceHandler.writeAll();
        Assert.assertTrue(true);
    }

    @Test
    public void getResourceTest() {
        ResourceHandler.readAll();
        Assert.assertEquals("cloud", User.getUserByUsername("cloudStrife").getName());
    }
}
