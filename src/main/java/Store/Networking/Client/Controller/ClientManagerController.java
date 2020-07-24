package Store.Networking.Client.Controller;

import Store.Networking.Client.ClientHandler;
import com.google.gson.Gson;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientManagerController {
    public static Map getUserInfo(String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getUserInfo");
        hashMap.put("username", username);
        return (Map) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String editPersonalInfo(String username, String field, String newValue) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editManagerPersonalInfo");
        hashMap.put("username", username);
        hashMap.put("field", field);
        hashMap.put("newValue", newValue);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static List<Map<String, Object>> getSortedOffCodes(String currentOffCodesSort) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getSortedOffCodes");
        hashMap.put("sort", currentOffCodesSort);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String removeOffCode(String username, String code) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeOffCode");
        hashMap.put("username", username);
        hashMap.put("code", code);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String editOffCode(String username, String code, String field, String newValue) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editOffCode");
        hashMap.put("username", username);
        hashMap.put("code" , code);
        hashMap.put("field", field);
        hashMap.put("newValue", newValue);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean isOffCodeWithThisCode(String code) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isOffCodeWithThisCode");
        hashMap.put("code", code);
        return (boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void assignOffCodeToUser(String code, String assignedCustomer) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "assignOffCodeToUser");
        hashMap.put("code", code);
        hashMap.put("username", assignedCustomer);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void createOffCode(String username, String code, double offPercent, double maximumOffValue, int usageCount, Date startingDate, Date endingDate) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "createOffCode");
        hashMap.put("code", code);
        hashMap.put("username", username);
        hashMap.put("offPercent", offPercent);
        hashMap.put("maximumValue", maximumOffValue);
        hashMap.put("usageCount", usageCount + "");
        hashMap.put("startingDate", startingDate.toString());
        hashMap.put("endingDate", endingDate.toString());
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static List<Map<String, Object>> getAllUsers(String sort) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getAllUsers");
        hashMap.put("sort", sort);
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void deleteUserByName(String managerUsername, String username) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "deleteUserByName");
        hashMap.put("managerUsername", managerUsername);
        hashMap.put("username", username);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void removeProduct(String username, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeProduct");
        hashMap.put("username", username);
        hashMap.put("id", id);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static String removeCategory(String username, String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeCategory");
        hashMap.put("username", username);
        hashMap.put("name", name);
        return (String) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean isCategoryWithThisName(String name) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isCategoryWithThisName");
        hashMap.put("name", name);
        return (boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void editCategory(String username, String name, String field, String newValue) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "editCategory");
        hashMap.put("username", username);
        hashMap.put("name" , name);
        hashMap.put("field", field);
        hashMap.put("newValue", newValue);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static boolean isInFilter(String name, String filter) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isInFilter");
        hashMap.put("name" , name);
        hashMap.put("filter", filter);
        return (Boolean) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void addCategory(String username, String name, String parentName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "addCategory");
        hashMap.put("username", username);
        hashMap.put("name" , name);
        hashMap.put("parentName", parentName);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static List<Map<String, Object>> getPendingRequests() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getPendingRequests");
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void removeRequest(String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "removeRequest");
        hashMap.put("id", id);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void handleRequest(String username, boolean status, String id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "handleRequest");
        hashMap.put("id", id);
        hashMap.put("username", username);
        hashMap.put("status", status);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void setMinimum(double minimum) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "setMinimum");
        hashMap.put("minimum", minimum);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static void setKarmozd(double karmozd) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "setKarmozd");
        hashMap.put("karmozd", karmozd);
        ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }

    public static SimpleBooleanProperty isReceived(Map buyLog) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "isReceivedLog");
        hashMap.put("logId", buyLog.get("id"));
        return new SimpleBooleanProperty((boolean)ClientHandler.sendAndReceiveMessage(hashMap).get("content"));
    }

    public static void sendProduct(Map buyLog) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "sendProduct");
        hashMap.put("buyLogId", buyLog.get("id"));
        ClientHandler.sendAndReceiveMessage(hashMap);
    }

    public static List<Map<String, Object>> getBuyLogs() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "getBuyLogs");
        return (List<Map<String, Object>>) ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }


    public static boolean hasManager() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "hasManager?");
        return (boolean)ClientHandler.sendAndReceiveMessage(hashMap).get("content");
    }
}
