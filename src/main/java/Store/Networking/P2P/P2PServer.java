package Store.Networking.P2P;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P2PServer {
    private static final int P2P_MANAGER_PORT = 10880;
    private static final String P2P_MANAGER_IP_ADDRESS = "127.0.0.1";
    private static final String RESOURCE_PATH = "src/main/resources/";

    private static final String DOWNLOAD_REGEX = "^DOWNLOAD ([IVF]) (\\S+)$";

    private ServerSocket serverSocket;

    public P2PServer(String username, String token) {
        try {
            serverSocket = new ServerSocket(0);
            setup(username, token);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    P2PServer.this.run();
                } catch (IOException e) {
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
                new P2PServer.ClientHandler(clientSocket, dataOutputStream, dataInputStream).start();
            }
            catch (Exception e) {
//                e.printStackTrace();
                break;
            }
        }
    }

    private class ClientHandler extends Thread {

        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;

        public ClientHandler(Socket clientSocket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
            this.clientSocket = clientSocket;
            this.dataOutputStream = dataOutputStream;
            this.dataInputStream = dataInputStream;

        }

        @Override
        public void run() {
            try {
                System.out.println("HANDLING TRANSFER");
                String metaData = dataInputStream.readUTF();
                Matcher matcher;
                if ((matcher = getMatcher(metaData, DOWNLOAD_REGEX)).find()) {
                    System.out.println(matcher.group(1) + "______________" + matcher.group(2));
                    sendFile(matcher.group(1), matcher.group(2));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String convertTypeToFolderName(String type) {
            String middleFolderName;
            if (type.equals("I")) {
                middleFolderName = "Images";
            }
            else if (type.equals("V")) {
                middleFolderName = "Videos";
            }
            else {
                middleFolderName = "Files";
            }
            return middleFolderName;
        }

        private void sendFile(String type, String fileName) throws IOException {
            String middleFolderName = convertTypeToFolderName(type);
            File requestedFile = new File(RESOURCE_PATH + middleFolderName + "/" + fileName);
            if (!requestedFile.exists()) {
                return;
            }

            dataOutputStream.writeLong(requestedFile.length());
            dataOutputStream.flush();

            FileInputStream fileInputStream = new FileInputStream(requestedFile);
            int count;
            byte[] buffer = new byte[8192];
            while ((count = fileInputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, count);
            }
            dataOutputStream.flush();
            fileInputStream.close();
        }

    }

    private void setup(String username, String token) throws IOException {
        Socket managerSocket = new Socket(P2P_MANAGER_IP_ADDRESS, P2P_MANAGER_PORT);
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(managerSocket.getInputStream()));
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(managerSocket.getOutputStream()));

        String address = serverSocket.getInetAddress().getHostAddress();
        if (address.equals("0.0.0.0")) {
            address = "127.0.0.1";
        }

        dataOutputStream.writeUTF("add " + username + " " + token + " "
                + address + ":" + serverSocket.getLocalPort());
        dataOutputStream.flush();

        dataInputStream.readUTF();
    }

    public void cleanup(String username, String token) throws IOException {
        Socket managerSocket = new Socket(P2P_MANAGER_IP_ADDRESS, P2P_MANAGER_PORT);
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(managerSocket.getInputStream()));
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(managerSocket.getOutputStream()));

        System.out.println("remove " + username + " " + token);
        dataOutputStream.writeUTF("remove " + username + " " + token);
        dataOutputStream.flush();

        System.out.println(dataInputStream.readUTF());

        if (serverSocket != null) {
            serverSocket.close();
        }
        serverSocket = null;
    }

    private static Matcher getMatcher(String string, String regex) {
        return Pattern.compile(regex).matcher(string);
    }
}
