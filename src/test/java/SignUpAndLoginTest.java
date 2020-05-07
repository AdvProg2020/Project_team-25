import Store.Main;
import Store.View.OffersMenu;
import Store.View.SignUpAndLoginMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SignUpAndLoginTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void helpTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("create account [type] [username]"));
    }

    @Test
    public void createFirstManagerTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("create account manager newManager\n1234\n1234\n1234\nlab@lab.com\n1234\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("Register successfully."));
    }

    @Test
    public void createSecondManagerTest() {
        Main.setTest();
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("create account manager newManager\n1234\n1234\n1234\nlab@lab.com\n1234\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("You can only create the first manager via this method!"));
    }

    @Test
    public void createSellerTest() {
        Main.setTest();
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("create account seller newSeller\n1234\n1234\n1234\nlab@lab.com\n1234\n123\nali\nali\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("Register successfully."));
    }

    @Test
    public void createCustomerTest() {
        Main.setTest();
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("create account customer newCustomer\n1234\n1234\n1234\nlab@lab.com\n1234\n123\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("Register successfully."));
    }

    @Test
    public void loginTest() {
        Main.setTest();
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("login cloudStrife\n1234\nback\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("Login successful."));
    }
}
