package Store.Networking;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatClient {

    private static final String CHAT_SERVER_IP_ADDRESS = "127.0.0.1";
    private static final int CHAT_SERVER_PORT = 9880;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chatDataOutputStream.writeUTF("CONNECT " + username + " " + token);
                    chatDataOutputStream.flush();
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
                handleMessage(matcher.group(1), matcher.group(2));
            }
            else if (command.matches(GET_OPERATORS_REGEX)) {
                currentOperators = command.substring(10);
            }
            else if (command.equals("END ACK")) {
                break;
            }
        }
    }

    public void handleMessage(String username, String message) {
        for (PrivateChat privateChat : chats) {
            if (privateChat.getUsername().equals(username)) {
                privateChat.addMessage(message);
                return;
            }
        }

        PrivateChat newChat = new PrivateChat(username);
        newChat.addMessage(message);
        chats.add(newChat);
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
