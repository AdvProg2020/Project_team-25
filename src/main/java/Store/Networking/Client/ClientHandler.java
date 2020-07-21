package Store.Networking.Client;

import Store.Main;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.View.MainMenuUI;
import com.google.gson.Gson;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler {
    private static int port;
    private static Socket socket;
    private static Socket chatAuctionSocket;
    public static String token = "";
    public static boolean hasLoggedIn = false;
    public static String username = "";

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ClientHandler.port = port;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        ClientHandler.socket = socket;
    }

    public static HashMap sendAndReceiveMessage(HashMap<String, Object> hashMap) {
        hashMap.put("token", token);
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(ClientHandler.getSocket().getOutputStream()));
            Gson gson = new Gson();
            dataOutputStream.writeUTF(gson.toJson(hashMap));
            dataOutputStream.flush();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(ClientHandler.getSocket().getInputStream()));
            hashMap = gson.fromJson(dataInputStream.readUTF(), HashMap.class);
            if (((String) hashMap.get("tokenStatus")).equals("expired") && hasLoggedIn) {
                hasLoggedIn = false;
                ClientMainMenuController.setCurrentUser("");
                forceLogout();
            }
        } catch (Exception exception) {
            hashMap = new HashMap<>();
        } finally {
            return hashMap;
        }
    }

    public static HashMap sendAndReceiveMessage(HashMap<String, Object> hashMap, int port) {
        try {
            chatAuctionSocket = new Socket("192.168.1.4", port);
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(chatAuctionSocket.getOutputStream()));
            Gson gson = new Gson();
            dataOutputStream.writeUTF(gson.toJson(hashMap));
            dataOutputStream.flush();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(chatAuctionSocket.getInputStream()));
            hashMap = gson.fromJson(dataInputStream.readUTF(), HashMap.class);
        } catch (Exception exception) {
            hashMap = new HashMap<>();
        } finally {
            return hashMap;
        }
    }


    public static void forceLogout() {
        try {
            Main.getApplicationStage().setScene(new Scene(MainMenuUI.getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
