package Store.Networking.Client;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler {
    private static int port;
    private static Socket socket;

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
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(ClientHandler.getSocket().getOutputStream()));
            Gson gson = new Gson();
            dataOutputStream.writeUTF(gson.toJson(hashMap));
            dataOutputStream.flush();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(ClientHandler.getSocket().getInputStream()));
            hashMap = gson.fromJson(dataInputStream.readUTF(), HashMap.class);
        }
        catch (Exception exception) {
            hashMap = new HashMap<>();
        }
        finally {
            return hashMap;
        }
    }
}
