package Store.Networking;

import Store.Controller.SignUpAndLoginController;
import Store.Model.Manager;
import Store.Model.User;
import Store.Networking.Client.ClientHandler;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MainServer {
    private final HashMap<Integer, Integer> accounts = new HashMap<>();
    private ServerSocket serverSocket;
    private int port;

    public MainServer() throws IOException {
        this.port = nextFreePort(9000, 20000);

        serverSocket = new ServerSocket(port);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        new ClientThread(socket).start();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        thread.start();

    }

    public int getPort() {
        return port;
    }

    private int nextFreePort(int from, int to) {
        int port = ThreadLocalRandom.current().nextInt(from, to);
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to);
            }
        }
    }

    private boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    private class ClientThread extends Thread {
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        private ClientThread(Socket socket) throws IOException {
            this.clientSocket = socket;
            dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String string = dataInputStream.readUTF();
                    Gson gson = new Gson();
                    HashMap input = gson.fromJson(string, HashMap.class);

                    if (input.get("message").equals("login")) {
                        System.out.println("login");
                    }
                    if (input.get("message").equals("isUsernameWithThisName")) {
                        isUsernameWithThisNameServer((String)input.get("username"));
                    }
                    if (input.get("message").equals("isUsernameExistInRequests")) {
                        isUsernameExistInRequestsServer((String)input.get("username"));
                    }
                    if (input.get("message").equals("createOrdinaryAccount")) {
                        handleCreateOrdinaryAccountServer((String)input.get("type"), (ArrayList<String>)input.get("attributes"));
                    }
                    if (input.get("message").equals("createManagerAccount")) {
                        handleCreateManagerAccountServer((ArrayList<String>)input.get("attributes"));
                    }
                } catch (IOException exception) {
                    //exception.printStackTrace();
                }
            }
        }

        private void handleCreateManagerAccountServer(ArrayList<String> attributes) {
            SignUpAndLoginController.createManager(attributes);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void isUsernameExistInRequestsServer(String username) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Manager.isUsernameExistInRequests(username));
            sendMessage(hashMap);
        }

        private void isUsernameWithThisNameServer(String username) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", User.getUserByUsername(username) != null);
            sendMessage(hashMap);
        }

        private void handleCreateOrdinaryAccountServer(String type, ArrayList<String> attributes) {
            SignUpAndLoginController.handleCreateAccount(type, attributes);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void sendMessage(HashMap<String, Object> hashMap) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                Gson gson = new Gson();
                dataOutputStream.writeUTF(gson.toJson(hashMap));
                dataOutputStream.flush();
            }
            catch (Exception exception) {
//
            }
        }

    }
}
