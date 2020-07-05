package Store.Networking.Client.Controller;

import Store.Networking.Client.ClientHandler;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientSignUpAndLoginController {

    public static boolean isUsernameWithThisName(String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isUsernameWithThisName");
        hashMap.put("username", username);
        return (boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean isUsernameExistInRequests(String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isUsernameExistInRequests");
        hashMap.put("username", username);
        return (boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }


    public static void handleCreateAccount(String type, ArrayList<String> attributes) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "createOrdinaryAccount");
        hashMap.put("type", type);
        hashMap.put("attributes", attributes);
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static void createManager(ArrayList<String> attributes) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "createManagerAccount");
        hashMap.put("attributes", attributes);
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static Map getUserInfo(String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getUserInfo");
        hashMap.put("username", username);
        return (Map) ClientHandler.sendAndReceiveMessage(hashMap).get("content");

    }
}
