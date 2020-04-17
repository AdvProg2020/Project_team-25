package main.java.Store;

import java.util.Scanner;

public class InputManager {

    private static Scanner sc = new Scanner(System.in);

    public static String getNextLine() {
        return sc.nextLine().trim();
    }
}
