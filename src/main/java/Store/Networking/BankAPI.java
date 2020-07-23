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
    public static final int PORT = 22222;
    public static final String IP = "127.0.0.1";

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

    public BankAPI(){
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        new ClientBankAPIHandler(socket).start();
                    } catch (IOException e) {
                        System.out.println("disconnected");
                        System.exit(0);
                    }
                }
            }
        }).start();
    }

    static class ClientBankAPIHandler extends Thread{
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        private ClientBankAPIHandler(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }catch (Exception e){
                System.out.println("something happens");
            }
        }

        @Override
        public void run() {
            String password = "", username = "", repeatPassword = "", firstName = "", lastName = "";
            while (true) {
                password = ""; username = ""; repeatPassword = ""; firstName = ""; lastName = "";
                try {
                    input = dataInputStream.readUTF();
                    System.out.println(input);
                    if (input.startsWith("move")) {
                        Matcher matcher = InputManager.getMatcher(input, "move ((\\d+)\\.(\\d+)) (\\d+) (\\d+) (.*)");
                        matcher.find();
                        System.out.println("Username: ");
                        username = InputManager.getNextLine();
                        System.out.println("Password: ");
                        password = InputManager.getNextLine();
                        move(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), matcher.group(6));
                    } else if (input.startsWith("deposit")) {
                        Matcher matcher = InputManager.getMatcher(input, "deposit ((\\d+)\\.(\\d+)) (\\d+) (.*)");
                        matcher.find();
                        System.out.println("Username: ");
                        username = InputManager.getNextLine();
                        System.out.println("Password: ");
                        password = InputManager.getNextLine();
                        deposit(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(4)), matcher.group(5));
                    } else if (input.startsWith("withdraw")) {
                        Matcher matcher = InputManager.getMatcher(input, "withdraw ((\\d+)\\.(\\d+)) (\\d+) (.*)");
                        matcher.find();
                        System.out.println("Username: ");
                        username = InputManager.getNextLine();
                        System.out.println("Password: ");
                        password = InputManager.getNextLine();
                        withdraw(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(4)), matcher.group(5));
                    } else if (input.startsWith("balance")) {
                        System.out.println("Username: ");
                        username = InputManager.getNextLine();
                        System.out.println("Password: ");
                        password = InputManager.getNextLine();
                        getBalance(getToken(username, password));
                    } else if (input.startsWith("createAccount")) {
                        do {
                            System.out.println("First Name: ");
                            firstName = InputManager.getNextLine();
                            System.out.println("Last Name: ");
                            lastName = InputManager.getNextLine();
                            System.out.println("Username: ");
                            username = InputManager.getNextLine();
                            System.out.println("Password: ");
                            password = InputManager.getNextLine();
                            System.out.println("Repeat Password: ");
                            repeatPassword = InputManager.getNextLine();
                        } while (!createAccount(firstName, lastName, username, password, repeatPassword));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        public void sendMessageToServer(Object msg) {
            try {
                System.out.println(msg);
                dataOutputStream.writeUTF((String) msg);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void deposit(String token, double money, int dest, String s){
            try {
                createReceipt(token, "deposit", money, -1, dest, s);
                sendMessageToServer("done");
            } catch (Exception e) {
                sendMessageToServer(e.getMessage());
            }
        }

        public void withdraw(String token, double money, int source, String s){
            try {
                createReceipt(token, "withdraw", money, source, -1, s);
                sendMessageToServer("done");
            } catch (Exception e) {
                sendMessageToServer(e.getMessage());
            }
        }

        public void move(String token, double money, int source, int dest, String s) {
            try {
                createReceipt(token, "move", money, source, dest, s);
                sendMessageToServer("done");
            } catch (Exception e) {
                sendMessageToServer(e.getMessage());
            }
        }

        private void createReceipt(String token, String type, double money, int source, int dest, String s) throws Exception {
            int id = -1;
            String output = "create_receipt " + token + " " + type + " " + (int)money + " " + source + " " + dest + " " + s;
            String input = (String)sendMessageAndReceive(output);
            if (Pattern.matches("\\d+", input))
                id = Integer.parseInt(input);
            else
                throw new Exception(input);
            pay(id);
        }

        private void pay(int id) throws Exception {
            String output = "pay " + id;
            String input = (String)sendMessageAndReceive(output);
            if (!input.equalsIgnoreCase("done"))
                throw new Exception(input);
        }

        public String getToken(String username, String password) throws Exception {
            String output = "get_token " + username + " " + password;
            String input = (String) sendMessageAndReceive(output);
            return input;
        }

        public void getBalance(String token) throws Exception {
            String output = "get_balance " + token;
            String input = (String) sendMessageAndReceive(output);
            sendMessageToServer(input);
        }

        public boolean createAccount(String firstName, String lastName, String username, String pass, String repPass) throws Exception {
            String output = "create_account " + firstName + " " + lastName + " " + username + " " + pass + " " + repPass;
            String input = (String) sendMessageAndReceive(output);
            sendMessageToServer(input);
            if (Pattern.matches("\\d+", input))
                return true;
            return false;
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

    public static void handleCommandAPI(){
        Scanner scanner = new Scanner(System.in);
        String password = "", username = "", repeatPassword = "", firstName = "", lastName = "";
        while (true) {
            password = ""; username = ""; repeatPassword = ""; firstName = ""; lastName = "";
            try {
                System.out.println("Order:");
                input = scanner.nextLine();
                if (input.startsWith("move")) {
                    Matcher matcher = InputManager.getMatcher(input, "move ((\\d+)\\.(\\d+)) (\\d+) (\\d+) (.*)");
                    matcher.find();
                    System.out.println("Username: ");
                    username = InputManager.getNextLine();
                    System.out.println("Password: ");
                    password = InputManager.getNextLine();
                    move(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), matcher.group(6));
                } else if (input.startsWith("deposit")) {
                    Matcher matcher = InputManager.getMatcher(input, "deposit ((\\d+)\\.(\\d+)) (\\d+) (.*)");
                    matcher.find();
                    System.out.println("Username: ");
                    username = InputManager.getNextLine();
                    System.out.println("Password: ");
                    password = InputManager.getNextLine();
                    deposit(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(4)), matcher.group(5));
                } else if (input.startsWith("withdraw")) {
                    Matcher matcher = InputManager.getMatcher(input, "withdraw ((\\d+)\\.(\\d+)) (\\d+) (.*)");
                    matcher.find();
                    System.out.println("Username: ");
                    username = InputManager.getNextLine();
                    System.out.println("Password: ");
                    password = InputManager.getNextLine();
                    withdraw(getToken(username, password), Double.parseDouble(matcher.group(1)), Integer.parseInt(matcher.group(4)), matcher.group(5));
                } else if (input.startsWith("balance")) {
                    System.out.println("Username: ");
                    username = InputManager.getNextLine();
                    System.out.println("Password: ");
                    password = InputManager.getNextLine();
                    getBalance(getToken(username, password));
                } else if (input.startsWith("createAccount")) {
                    do {
                        System.out.println("First Name: ");
                        firstName = InputManager.getNextLine();
                        System.out.println("Last Name: ");
                        lastName = InputManager.getNextLine();
                        System.out.println("Username: ");
                        username = InputManager.getNextLine();
                        System.out.println("Password: ");
                        password = InputManager.getNextLine();
                        System.out.println("Repeat Password: ");
                        repeatPassword = InputManager.getNextLine();
                    } while (!createAccount(firstName, lastName, username, password, repeatPassword));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    public static void deposit(String token, double money, int dest, String s){
        try {
            createReceipt(token, "deposit", money, -1, dest, s);
            System.out.println("done");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void withdraw(String token, double money, int source, String s){
        try {
            createReceipt(token, "withdraw", money, source, -1, s);
            System.out.println("done!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void move(String token, double money, int source, int dest, String s) {
        try {
            createReceipt(token, "move", money, source, dest, s);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createReceipt(String token, String type, double money, int source, int dest, String s) throws Exception {
        int id = -1;
        String output = "create_receipt " + token + " " + type + " " + (int)money + " " + source + " " + dest + " " + s;
        String input = (String)sendMessageAndReceive(output);
        System.out.println(input);
        if (Pattern.matches("\\d+", input))
            id = Integer.parseInt(input);
        else
            throw new Exception(input);
        pay(id);
    }

    private static void pay(int id) throws Exception {
        String output = "pay " + id;
        String input = (String)sendMessageAndReceive(output);
        System.out.println(input);
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
        System.out.println(input);
    }

    public static boolean createAccount(String firstName, String lastName, String username, String pass, String repPass) throws Exception {
        String output = "create_account " + firstName + " " + lastName + " " + username + " " + pass + " " + repPass;
        String input = (String) sendMessageAndReceive(output);
        System.out.println(input);
        if (Pattern.matches("\\d+", input))
            return true;
        return false;
    }
}
