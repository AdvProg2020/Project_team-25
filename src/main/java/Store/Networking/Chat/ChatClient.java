package Store.Networking.Chat;

import Store.View.SupportPageUI;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatClient {

    private static final String CHAT_SERVER_IP_ADDRESS = "127.0.0.1";
    private static final int CHAT_SERVER_PORT = 10010;

    private static final String MESSAGE_REGEX = "^MESSAGE (\\S+)-(.+)$";
    private static final String GET_OPERATORS_REGEX = "^OPERATORS (.+)$";

    private Socket chatClientSocket;
    private DataOutputStream chatDataOutputStream;
    private DataInputStream chatDataInputStream;
    public String currentOperators = "";
    boolean stillRunning = true;
    private ArrayList<PrivateChat> chats = new ArrayList<PrivateChat>();

    public ChatClient(String username, String token) throws IOException {
        chatClientSocket = new Socket(CHAT_SERVER_IP_ADDRESS, CHAT_SERVER_PORT);
        chatDataInputStream = new DataInputStream(new BufferedInputStream(chatClientSocket.getInputStream()));
        chatDataOutputStream = new DataOutputStream(new BufferedOutputStream(chatClientSocket.getOutputStream()));

        chatDataOutputStream.writeUTF("CONNECT " + username + " " + token);
        chatDataOutputStream.flush();
        String result = chatDataInputStream.readUTF();
        if (!result.equals("ACK")) {
            System.err.println("Server did not acknowledge");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    handleInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void endConnection() throws IOException {
        chatDataOutputStream.writeUTF("END");
        chatDataOutputStream.flush();

        chatClientSocket.close();

        stillRunning = false;
    }

    public void sendMessageTo(String username, String token, String destination, String message) throws IOException {
        handleMessage(destination, message, true);
        chatDataOutputStream.writeUTF("MESSAGE " + username + " " + token + " " + destination + "-" + message);
        chatDataOutputStream.flush();
    }

    public void requestCurrentOperators(String username, String token) throws IOException {
        chatDataOutputStream.writeUTF("GET_OPERATORS " + username + " " + token);
        chatDataOutputStream.flush();
    }

    private void handleInput() throws IOException {
        String command;
        Matcher matcher;
        while (stillRunning) {
            try {
                command = chatDataInputStream.readUTF();
            }
            catch (SocketException e) {
                break;
            }

            System.out.println("COMMAND: " + command);
            if ((matcher = getMatcher(command, MESSAGE_REGEX)).find()) {
                System.out.println("GOT MESSAGE: " + matcher.group(1) + " " + matcher.group(2));
                handleMessage(matcher.group(1), matcher.group(2), false);
            }
            else if (command.matches(GET_OPERATORS_REGEX)) {
                currentOperators = command.substring(10);
                makeChatsForOperators();
            }
            else if (command.equals("END ACK")) {
                break;
            }
        }
    }

    private void makeChatsForOperators() {
        String[] operators = currentOperators.split(" ");
        for (String operator : operators) {
            if (getChatWithUser(operator) == null) {
                chats.add(new PrivateChat(operator));
            }
        }
    }

    public void handleMessage(String username, String message, boolean ownerStatus) {
        for (PrivateChat privateChat : chats) {
            if (privateChat.getUsername().equals(username)) {
                privateChat.addMessage(message, ownerStatus);
                System.out.println("ADDING A MESSAGE WITH OWNERSHIP: " + ownerStatus);
                SupportPageUI.supportPageUI.updateInterface();
                return;
            }
        }

        PrivateChat newChat = new PrivateChat(username);
        newChat.addMessage(message, ownerStatus);
        chats.add(newChat);

        SupportPageUI.supportPageUI.updateInterface();
    }

    public ArrayList<PrivateChat> getSortedChats() {
        ArrayList<PrivateChat> result = new ArrayList<>(chats);

        result.sort(new Comparator<PrivateChat>() {
            @Override
            public int compare(PrivateChat privateChat, PrivateChat t1) {
                return privateChat.getLastActivity().compareTo(t1.getLastActivity());
            }
        });

        Collections.reverse(result);

        return result;
    }

    private static Matcher getMatcher(String string, String regex) {
        return Pattern.compile(regex).matcher(string);
    }

    public PrivateChat getChatWithUser(String username) {
        for (PrivateChat privateChat : chats) {
            if (privateChat.getUsername().equals(username)) {
                return privateChat;
            }
        }
        return null;
    }
}
