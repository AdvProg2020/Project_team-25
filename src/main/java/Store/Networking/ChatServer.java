package Store.Networking;

import Store.Model.Operator;
import Store.Model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatServer {

    private static final int CHAT_SERVER_PORT = 9880;
    private static final String SEND_MESSAGE_REGEX = "^MESSAGE (\\S+) (\\S{50}) (\\S+)-(.+)$";
    private static final String GET_OPERATORS_REGEX = "^GET_OPERATORS (\\S+) (\\S{50})$";

    private ServerSocket serverSocket;
    private static HashMap<String, DataOutputStream> clientOutputStreams = new HashMap<String, DataOutputStream>();
    private static HashSet<String> operators = new HashSet<String>();
    private static Queue<String> commandQueue = new LinkedList<String>();

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(CHAT_SERVER_PORT);

        System.out.println("SERVER CREATED");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ChatServer.this.run();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ChatServer.this.handleCommands();
                }
                catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void run() throws IOException {
        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                new ChatServer.ClientHandler(clientSocket, dataOutputStream, dataInputStream).start();
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void handleCommands() throws IOException, InterruptedException {
        String command;
        Matcher matcher;
        while (true) {
            synchronized (reentrantLock) {
                if (commandQueue.isEmpty()) {
                    continue;
                }
                command = commandQueue.poll();

                System.out.println("CURRENT COMMAND: " + command);

                if ((matcher = getMatcher(command, GET_OPERATORS_REGEX)).find()) {
                    if (!clientOutputStreams.containsKey(matcher.group(1))) {
                        continue;
                    }
                    if (TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        String allOperatorsString = "OPERATORS ";
                        for (String operator : operators) {
                            allOperatorsString = allOperatorsString.concat(operator + " ");
                        }
                        System.out.println(allOperatorsString);
                        clientOutputStreams.get(matcher.group(1)).writeUTF(allOperatorsString);
                        clientOutputStreams.get(matcher.group(1)).flush();
                    }
                }

                if ((matcher = getMatcher(command, SEND_MESSAGE_REGEX)).find()) {
                    if (TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        if (!clientOutputStreams.containsKey(matcher.group(3))) {
                            continue;
                        }
                        clientOutputStreams.get(matcher.group(3)).writeUTF("MESSAGE " + matcher.group(1) + "-" + matcher.group(4));
                        clientOutputStreams.get(matcher.group(3)).flush();
                    }
                }
            }
        }
    }

    private class ClientHandler extends Thread {

        private static final String CONNECTION_REGEX = "^CONNECT (\\S+) (\\S{50})$";

        String username;
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        public ClientHandler(Socket clientSocket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
            this.clientSocket = clientSocket;
            this.dataOutputStream = dataOutputStream;
            this.dataInputStream = dataInputStream;

            System.out.println("CREATING NEW CLIENT HANDLER");
        }

        private boolean validateUsernameAndToken() throws IOException {
            String command = dataInputStream.readUTF();
            Matcher matcher;
            if (!(matcher = getMatcher(command, CONNECTION_REGEX)).find()) {
                return false;
            }
            System.out.println(matcher.group(1) + " " + matcher.group(2) + " " + TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2)));
            if (TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                synchronized (reentrantLock) {
                    username = matcher.group(1);
                    clientOutputStreams.put(username, dataOutputStream);
                    if (User.getUserByUsername(username) instanceof Operator) {
                        operators.add(username);
                    }
                }
                System.out.println("CLIENT CONNECTED WITH USERNAME: " + username);
                return true;
            }
            return false;
        }

        @Override
        public void run() {
            try {
                System.out.println("RUNNING CLIENT HANDLER");
                if (!validateUsernameAndToken()) {
                    return;
                }
                while (true) {
                    String command = dataInputStream.readUTF();
                    System.out.println(command);
                    if (command.equals("END")) {
                        dataOutputStream.writeUTF("END ACK");
                        dataOutputStream.flush();
                        synchronized (reentrantLock) {
                            clientOutputStreams.remove(username);
                        }
                        return;
                    }
                    synchronized (reentrantLock) {
                        commandQueue.add(command);
                        System.out.println(commandQueue.size());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Matcher getMatcher(String string, String regex) {
        return Pattern.compile(regex).matcher(string);
    }
}
