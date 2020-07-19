package Store.Networking.P2P;

import Store.Networking.TokenHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P2PManager {
    private static final int P2P_MANAGER_PORT = 10880;
    private static final String ADD_REGEX = "^add (\\S+) (\\S{50}) (\\S+):(\\d+)$";
    private static final String REMOVE_REGEX = "^remove (\\S+) (\\S{50})$";
    private static final String GET_REGEX = "^get (\\S+) (\\S{50}) (\\S+)$";


    private static HashMap<String, String> usernameToAddress = new HashMap<>();
    private static HashMap<String, Integer> usernameToPort = new HashMap<>();
    private ServerSocket serverSocket;

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public P2PManager() throws IOException {
        serverSocket = new ServerSocket(P2P_MANAGER_PORT);

        System.out.println("CREATING NEW P2P SERVER");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    P2PManager.this.run();
                }
                catch (IOException e) {
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
                new P2PManager.ClientHandler(clientSocket, dataOutputStream, dataInputStream).start();
            }
            catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private class ClientHandler extends Thread {

        String username;
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        public ClientHandler(Socket clientSocket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
            this.clientSocket = clientSocket;
            this.dataOutputStream = dataOutputStream;
            this.dataInputStream = dataInputStream;

            System.out.println("CREATING NEW P2P CLIENT HANDLER");
        }

        @Override
        public void run() {
            try {
                System.out.println("RUNNING CLIENT HANDLER");
                String command = dataInputStream.readUTF();
                Matcher matcher;
                if ((matcher = getMatcher(command, ADD_REGEX)).find()) {
                    if (TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        synchronized (reentrantLock) {
                            usernameToAddress.put(matcher.group(1), matcher.group(3));
                            usernameToPort.put(matcher.group(1), Integer.parseInt(matcher.group(4)));
                        }
                        dataOutputStream.writeUTF("ACK");
                    }
                    else {
                        dataOutputStream.writeUTF("REJ1");
                    }
                }
                else if ((matcher = getMatcher(command, REMOVE_REGEX)).find()) {
                    System.out.println("VALIDATION: " + matcher.group(1) + " " + matcher.group(2));
                    if (TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        synchronized (reentrantLock) {
                            System.out.println("REMOVING: " + matcher.group(1));
                            usernameToAddress.remove(matcher.group(1));
                            usernameToPort.remove(matcher.group(1));
                        }
                        dataOutputStream.writeUTF("ACK");
                    }
                    else {
                        dataOutputStream.writeUTF("REJ2");
                    }
                }
                else if ((matcher = getMatcher(command, GET_REGEX)).find()) {
                    if (TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        synchronized (reentrantLock) {
                            if (usernameToAddress.containsKey(matcher.group(3))) {
                                dataOutputStream.writeUTF(usernameToAddress.get(matcher.group(3)) + ":" + usernameToPort.get(matcher.group(3)));
                            }
                            else {
                                dataOutputStream.writeUTF("NULL");
                            }
                        }
                    }
                    else {
                        dataOutputStream.writeUTF("REJ3");
                    }
                }
                else {
                    dataOutputStream.writeUTF("REJ4");
                }
                dataOutputStream.flush();
                System.out.println("HANDLED");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Matcher getMatcher(String string, String regex) {
        return Pattern.compile(regex).matcher(string);
    }

}
