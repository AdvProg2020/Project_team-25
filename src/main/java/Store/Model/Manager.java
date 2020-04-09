package Store.Model;

import java.util.ArrayList;

public class Manager extends User {

    private static ArrayList<Category> allCategories = new ArrayList<Category>();
    private static ArrayList<Request> pendingRequests = new ArrayList<Request>();
    private static ArrayList<OffCode> offCodes = new ArrayList<OffCode>();
    private static boolean hasManager = false;

    Manager(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        super(username, name, familyName, email, phoneNumber, password, money);
        allUsers.add(this);
        hasManager = true;
    }

    private void handleRequest(Request request, boolean accepted) {

    }

    public void addNewManager(Manager manager) {

    }

    public void changeOffCode(OffCode offCode, OffCode newOffCode) {

    }

    public void addOffCode(OffCode offCode) {

    }

    public void addCategory(Category category) {

    }

    public void deleteUser(User user) {

    }
}
