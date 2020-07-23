package Store.Networking;

import Store.InputManager;
import Store.Model.Manager;
import javafx.css.Match;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Store.InputManager.getMatcher;

public class BankAPI {
    public static final int PORT = 2222;
    public static final String IP = "192.168.1.4";

    private static ServerSocket serverSocket;

    static {
        try {
            serverSocket = new ServerSocket(15000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStreamServer;
    private static DataInputStream inputStreamServer;
    private static String input;
    private static String output;
    //   private static Scanner scanner = new Scanner(System.in);

    public static void main(){
        try {
            connectToBankServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startListeningOnInput();
    }

    public static void connectToBankServer() throws IOException {
        try {
            Socket socket = new Socket(IP, PORT);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new IOException("Exception while initiating connection:");
        }
    }

    public static void startListeningOnInput() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                outputStreamServer = new DataOutputStream(socket.getOutputStream());
                inputStreamServer = new DataInputStream(socket.getInputStream());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String password = "", username = "";
                        try {
                            input = inputStreamServer.readUTF();
                            if (input.startsWith("move")) {
                                Matcher matcher = getMatcher(input, "move ((\\d+)(\\.(\\d+))?) (\\d+) (\\d+) (.+)");
                                System.out.println("Username: ");
                                username = InputManager.getNextLine();
                                System.out.println("Password: ");
                                password = InputManager.getNextLine();
                                move(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)), matcher.group(7));
                            } else if (input.startsWith("deposit")) {
                                Matcher matcher = getMatcher(input, "deposit ((\\d+)(\\.(\\d+))?) (\\d+) (.+)");
                                System.out.println("Username: ");
                                username = InputManager.getNextLine();
                                System.out.println("Password: ");
                                password = InputManager.getNextLine();
                                deposit(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(5)), matcher.group(6));
                            } else if (input.startsWith("withdraw")) {
                                Matcher matcher = getMatcher(input, "withdraw ((\\d+)(\\.(\\d+))?) (\\d+) (.+)");
                                System.out.println("Username: ");
                                username = InputManager.getNextLine();
                                System.out.println("Password: ");
                                password = InputManager.getNextLine();
                                withdraw(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(5)), matcher.group(6));
                            } else if (input.startsWith("balance")) {
                                System.out.println("Username: ");
                                username = InputManager.getNextLine();
                                System.out.println("Password: ");
                                password = InputManager.getNextLine();
                                getBalance(getToken(username, password));
                            } else if (input.startsWith("createAccount")) {
                                System.out.println("Username: ");
                                username = InputManager.getNextLine();
                                System.out.println("Password: ");
                                password = InputManager.getNextLine();
                                createAccount(username, password);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }).start();
            } catch (IOException e) {
                System.out.println("disconnected");
                System.exit(0);
            }
        }
    }

    public static Object sendMessageAndReceive(Object msg) throws Exception {
        try {
            outputStream.writeUTF((String)msg);
            outputStream.flush();
            input = inputStream.readUTF();
            return input;
        } catch (IOException e) {
            throw new IOException("Exception while sending message:");
        }
    }

    public static void sendMessageToServer(Object msg) {
        try {
            outputStreamServer.writeUTF((String) msg);
            outputStreamServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deposit(String token, double money, int dest, String s){
        try {
            createReceipt(token, "deposit", money, -1, dest, s);
            sendMessageToServer("done");
        } catch (Exception e) {
            sendMessageToServer(e.getMessage());
        }
    }

    public static void withdraw(String token, double money, int source, String s){
        try {
            createReceipt(token, "withdraw", money, source, -1, s);
            sendMessageToServer("done");
        } catch (Exception e) {
            sendMessageToServer(e.getMessage());
        }
    }

    public static void move(String token, double money, int source, int dest, String s) {
        try {
            createReceipt(token, "move", money, source, dest, s);
            sendMessageToServer("done");
        } catch (Exception e) {
            sendMessageToServer(e.getMessage());
        }
    }

    private static void createReceipt(String token, String type, double money, int source, int dest, String s) throws Exception {
        int id = -1;
        String output = "create_receipt " + token + " " + type + " " + money + " " + source + " " + dest + " " + s;
        String input = (String)sendMessageAndReceive(output);
        if (Pattern.matches("\\d+", input))
            id = Integer.parseInt(input);
        else
            throw new Exception(input);
        pay(id);
    }

    private static void pay(int id) throws Exception {
        String output = "pay " + id;
        String input = (String)sendMessageAndReceive(output);
        if (!input.equalsIgnoreCase("done"))
            throw new Exception(input);
    }

    public static String getToken(String username, String password) throws Exception {
        String output = "get_token " + username + " " + password;
        String input = (String) sendMessageAndReceive(output);
        return input;
    }

    public static void getBalance(String token) throws Exception {
        String output = "get_balance " + token;
        String input = (String) sendMessageAndReceive(output);
        sendMessageToServer(input);
    }

    public static void createAccount(String username, String bankPassSeller) throws Exception {
        String output = "create_account " + username + " " + bankPassSeller;
        String input = (String) sendMessageAndReceive(output);
        sendMessageToServer(input);
    }
}