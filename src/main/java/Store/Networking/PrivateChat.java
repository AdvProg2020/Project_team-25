package Store.Networking;

import java.util.ArrayList;
import java.util.Date;

public class PrivateChat {

    Date lastActivity;
    private String username;
    private ArrayList<String> messages;

    public PrivateChat(String username) {
        this.username = username;
        messages = new ArrayList<String >();
        lastActivity = new Date();
    }

    public void addMessage(String message) {
        messages.add(message);
        updateLastActivity();
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void updateLastActivity() {
        lastActivity = new Date();
    }

    @Override
    public String toString() {
        return  " username=" + getUsername()
                + ", lastActivity=" + getLastActivity();
    }
}
