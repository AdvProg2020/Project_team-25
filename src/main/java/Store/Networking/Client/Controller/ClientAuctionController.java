package Store.Networking.Client.Controller;

import Store.Networking.Client.ClientHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ClientAuctionController {

    public static SimpleStringProperty getHighestPrice(Map<String, Object> auctionToShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getHighestPriceAuction");
        hashMap.put("auctionId", auctionToShow.get("id"));
        String string = (String)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        return new SimpleStringProperty(string);
    }

    public static SimpleStringProperty getCurrentBuyer(Map<String, Object> auctionToShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getCurrentBuyerAuction");
        hashMap.put("auctionId", auctionToShow.get("id"));
        String string = (String)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        return new SimpleStringProperty(string);
    }

    public static SimpleStringProperty getCondition(Map<String, Object> auctionToShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getConditionAuction");
        hashMap.put("auctionId", auctionToShow.get("id"));
        String string = (String)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        return new SimpleStringProperty(string);
    }

    public static Queue<String> getMessages(Map<String, Object> auctionToShow) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "portAuctionChat");
        hashMap.put("auctionId", auctionToShow.get("id"));
        int port = (Integer)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        hashMap.clear();
        hashMap.put("message", "connect");
        return (Queue)ClientHandler.sendAndReceiveMessage(hashMap, port).get("content");
    }

    public static void disconnect(Map<String, Object> auctionToShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "portAuctionChat");
        hashMap.put("auctionId", auctionToShow.get("id"));
        int port = (Integer)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        hashMap.clear();
        hashMap.put("message", "disconnect");
        ClientHandler.sendAndReceiveMessage(hashMap, port).get("content");
    }

    public static void sendMessage(TextArea message, Map<String, Object> auctionToShow) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "portAuctionChatWrite");
        hashMap.put("auctionId", auctionToShow.get("id"));
        int port = (Integer)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        hashMap.clear();
        hashMap.put("message", "chatMessage");
        hashMap.put("chatMessage", message);
        hashMap.put("username", ClientMainMenuController.currentUserUsername.getValue());
        hashMap = ClientHandler.sendAndReceiveMessage(hashMap, port);
        if (hashMap.equals("error"))
            throw new Exception((String)hashMap.get("type"));
    }
}