package Store.Networking.Client;

import Store.Networking.Client.Controller.ClientMainMenuController;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler {
    private static int port;
    private static Socket socket;
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
            if (((String) hashMap.get("tokenStatus")).equals("expired")) {
                ClientMainMenuController.setCurrentUser("");
            }
        } catch (Exception exception) {
            hashMap = new HashMap<>();
        } finally {
            return hashMap;
        }
    }
}
