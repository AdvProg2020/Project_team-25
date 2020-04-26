package Store;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManager {

    private static Scanner sc = new Scanner(System.in);

    public static String getNextLine() {
        return sc.nextLine().trim();
    }

    public static double getNextDouble() { return sc.nextDouble(); }

    public static Matcher getMatcher(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(string);
    }
}
