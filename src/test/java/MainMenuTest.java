import Store.View.MainMenu;
import Store.View.SignUpAndLoginMenu;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainMenuTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void helpTest() {  // should comment exitAll in MainMenu
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("help\nexit").getBytes()));
        MainMenu.init();
        Assert.assertTrue(outContent.toString().contains("List of main commands: "));
    }

    @Test
    public void logoutTest() {  // should comment exitAll in MainMenu
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("logout\nlogout\nexit").getBytes()));
        MainMenu.init();
        Assert.assertTrue(outContent.toString().contains("You haven't signed in!"));
    }

    @Test
    public void loginTest() {  // should comment exitAll in MainMenu
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("logout\nlogin\nback\nexit").getBytes()));
        MainMenu.init();
        Assert.assertTrue(outContent.toString().contains("Sign up and Login menu"));
    }

    @Test
    public void quitTest() {  // should comment system.quit(0) in MainMenu
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(("exit\nN").getBytes()));
        MainMenu.init();
        Assert.assertTrue(outContent.toString().contains("Save Current Database?"));
    }
}
