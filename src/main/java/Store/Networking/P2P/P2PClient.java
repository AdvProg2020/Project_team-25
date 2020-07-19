package Store.Networking.P2P;

import java.io.*;
import java.net.Socket;

public class P2PClient {
    private static final int P2P_MANAGER_PORT = 10880;
    private static final String P2P_MANAGER_IP_ADDRESS = "127.0.0.1";
    private static final int FILE_SERVER_PORT = 9890;
    private static final String RESOURCE_PATH = "src/main/resources/";

    private static Socket managerSocket;
    private static DataOutputStream managerDataOutputStream;
    private static DataInputStream managerDataInputStream;

    private static Socket fileClientSocket;
    private static DataOutputStream fileDataOutputStream;
    private static DataInputStream fileDataInputStream;

    private static void handleConnection(String username, String token, String seller) throws Exception {
        Socket managerSocket = new Socket(P2P_MANAGER_IP_ADDRESS, P2P_MANAGER_PORT);
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(managerSocket.getInputStream()));
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(managerSocket.getOutputStream()));

        dataOutputStream.writeUTF("get " + username + " " + token + " " + seller);
        dataOutputStream.flush();

        String result = dataInputStream.readUTF();
        if (result.equals("NULL")) {
            throw new Exception("P2P Server not Found");
        }
        System.out.println(result);
        String[] results = result.split(":");

        try {
            fileClientSocket = new Socket(results[0], Integer.parseInt(results[1]));
            fileDataInputStream = new DataInputStream(new BufferedInputStream(fileClientSocket.getInputStream()));
            fileDataOutputStream = new DataOutputStream(new BufferedOutputStream(fileClientSocket.getOutputStream()));
        }
        catch (Exception e) {
            throw e;
        }
    }

    public static void receiveFile(String username, String token, String seller, String fileName) throws Exception {
        File file = null;
        try {
            String type = "F";
            file = new File(RESOURCE_PATH + "Downloads/" + fileName);
            if (file.exists()) {
                return;
            }
            System.out.println("PATH: " + RESOURCE_PATH + "Downloads/" + fileName);
            file.createNewFile();

            handleConnection(username, token, seller);

            Thread.sleep(100);

            fileDataOutputStream.writeUTF("DOWNLOAD " + type + " " + fileName);
            fileDataOutputStream.flush();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            long count = fileDataInputStream.readLong();
            System.out.println("SIZE: " + count);
            byte[] buffer = new byte[8192];
            while (count > 0) {
                fileDataInputStream.read(buffer);
                fileOutputStream.write(buffer, 0, (int) Math.min(count, buffer.length));
                count -= Math.min(count, buffer.length);
            }

            fileOutputStream.close();

            fileClientSocket.close();
        }
        catch (Exception e) {
            if (e.getMessage().equals("P2P Server not Found")) {
                file.delete();
                throw e;
            }
            else {
                e.printStackTrace();
            }
        }
    }
}
