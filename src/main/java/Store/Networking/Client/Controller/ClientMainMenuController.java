package Store.Networking.Client.Controller;

import Store.Main;
import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.User;
import Store.Networking.Client.ClientHandler;
import Store.Networking.P2P.P2PServer;
import Store.View.MainMenuUI;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientMainMenuController {
    public static SimpleStringProperty currentUserUsername = new SimpleStringProperty("Not Logged In");
    public static SimpleBooleanProperty isLoggedIn = new SimpleBooleanProperty(false);
    public static SimpleStringProperty loginLogoutButtonText = new SimpleStringProperty("Login");

    private static P2PServer localFileServer;

    public static void setCurrentUser(String username) {
        ClientHandler.username = username;
        if (username.isEmpty()) {
            logout();
            ClientHandler.hasLoggedIn = false;
            ClientHandler.token = "";
            ClientHandler.forceLogout();
        }
        else {
            login();
            ClientHandler.hasLoggedIn = true;
        }

        isLoggedIn.setValue(ClientHandler.hasLoggedIn);
        loginLogoutButtonText.setValue((ClientHandler.hasLoggedIn ? "Logout" : "Login"));
        currentUserUsername.setValue((ClientHandler.hasLoggedIn ? ClientHandler.username : "Not Logged In"));
    }

    private static void login() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "login");
        hashMap.put("username", ClientHandler.username);
        ClientHandler.token = (String)ClientHandler.sendAndReceiveMessage(hashMap).get("content");

        localFileServer = new P2PServer(ClientHandler.username, ClientHandler.token);
        System.out.println("Created Server " + (localFileServer == null));
    }


    public static void logout () {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "logout");
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static void shutdownP2P() {
        try {
            System.out.println("Should Shutdown " + (localFileServer == null));
            localFileServer.cleanup(ClientHandler.username, ClientHandler.token);
        }
        catch (IOException e) {
            System.err.println("Couldn't Shut Down P2P Server");
        }
        localFileServer = null;
    }

    public static boolean hasManager() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "hasManager?");
        return (boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }
}
