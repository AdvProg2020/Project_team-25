package Store.Networking.Chat;

import java.util.ArrayList;
import java.util.Date;

public class PrivateChat {

    public static class Message {
        private String message;
        private boolean owned;

        public Message(String message, boolean owned) {
            this.message = message;
            this.owned = owned;
        }

        public String getMessage() {
            return message;
        }

        public boolean isOwned() {
            return owned;
        }
    }

    private Date lastActivity;
    private Date lastOpened;
    private String username;
    private ArrayList<Message> messages;

    public PrivateChat(String username) {
        this.username = username;
        messages = new ArrayList<Message>();
        lastActivity = new Date();
        lastOpened = new Date();
    }

    public void addMessage(String message, boolean ownStatus) {
        messages.add(new Message(message, ownStatus));
        updateLastActivity();
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void updateLastActivity() {
        lastActivity = new Date();
    }

    public void updateLastOpened() {
        lastOpened = new Date();
    }

    public boolean hasChecked() {
        return lastOpened.compareTo(lastActivity) > 0;
    }

    @Override
    public String toString() {
        return  " username=" + getUsername()
                + ", lastActivity=" + getLastActivity();
    }
}
