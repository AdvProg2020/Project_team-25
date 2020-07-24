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
        hashMap.put("auctionId", auctionToShow.get("auctionId"));
        String string = String.valueOf(ClientHandler.sendAndReceiveMessage(hashMap).get("content"));
        return new SimpleStringProperty(string);
    }

    public static SimpleStringProperty getCurrentBuyer(Map<String, Object> auctionToShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getCurrentBuyerAuction");
        hashMap.put("auctionId", auctionToShow.get("auctionId"));
        String string = (String)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        return new SimpleStringProperty(string);
    }

    public static SimpleStringProperty getCondition(Map<String, Object> auctionToShow) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getConditionAuction");
        hashMap.put("productId", ((Map)auctionToShow.get("product")).get("id"));
        String string = (String)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
        return new SimpleStringProperty(string);
    }

    public static ArrayList<String> getMessages(Map<String, Object> auctionToShow) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getAuctionMessages");
        hashMap.put("auctionId", auctionToShow.get("auctionId"));
        hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
        System.out.println(hashMap);
        return (ArrayList)hashMap.get("content");
    }

    public static void sendMessage(String message, Map<String, Object> auctionToShow) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "writeInChat");
        hashMap.put("auctionId", auctionToShow.get("auctionId"));
        hashMap.put("chatMessage", message);
        hashMap.put("username", ClientMainMenuController.currentUserUsername.getValue());
        hashMap = ClientHandler.sendAndReceiveMessage(hashMap);
        if (hashMap.equals("error"))
            throw new Exception((String)hashMap.get("type"));
    }
}