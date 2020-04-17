package main.java.Store.View;

import main.java.Store.Controller.SignUpAndLoginController;
import main.java.Store.Model.User;

public class SignUpAndLoginMenu {

    public static void init() {
    }

    private static void createAccountWrapper(String type, String username, String password) {
        SignUpAndLoginController.createAccount(null,null,null);
    }

    private static User login(String username, String password) {
        return null;
    }

    private static void printHelp() {

    }
}
