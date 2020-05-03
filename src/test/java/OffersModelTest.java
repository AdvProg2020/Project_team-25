import Store.Main;
import Store.Model.Manager;
import Store.Model.User;
import Store.View.MainMenu;
import Store.View.ManagerMenu;
import Store.View.OffersMenu;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class OffersModelTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setTest() {
        Main.setTest();
    }

    @Test
    public void helpTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        OffersMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: "));
    }
}
