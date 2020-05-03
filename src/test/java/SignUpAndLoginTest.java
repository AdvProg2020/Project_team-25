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

    @Before
    public void setTest() {
        Main.setTest();
    }

    @Test
    public void helpTest() {
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nback").getBytes()));
        SignUpAndLoginMenu.init();
        Assert.assertTrue(outContent.toString().contains("create account [type] [username]"));
    }
}
