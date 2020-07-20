package Store.Networking;

import java.io.*;
import java.net.Socket;

public class FileTransportClient {
    private static final String FILE_SERVER_IP_ADDRESS = "127.0.0.1";
    private static final int FILE_SERVER_PORT = 9890;
    private static final String RESOURCE_PATH = "src/main/resources/";

    private static Socket fileClientSocket;
    private static DataOutputStream fileDataOutputStream;
    private static DataInputStream fileDataInputStream;

    public static void sendFile(String username, String token, String type, String fileName) {
        try {
            File file = new File(RESOURCE_PATH + convertTypeToFolderName(type) + "/" + fileName);
            if (!file.exists()) {
                return;
            }
            long length = file.length();

            fileClientSocket = new Socket(FILE_SERVER_IP_ADDRESS, FILE_SERVER_PORT);
            fileDataInputStream = new DataInputStream(new BufferedInputStream(fileClientSocket.getInputStream()));
            fileDataOutputStream = new DataOutputStream(new BufferedOutputStream(fileClientSocket.getOutputStream()));

            fileDataOutputStream.writeUTF("UPLOAD " + username + " " + token + " "
                    + type + " " + fileName + " " + length);
            fileDataOutputStream.flush();

            FileInputStream fileInputStream = new FileInputStream(file);
            int count;
            byte[] buffer = new byte[8192];
            while ((count = fileInputStream.read(buffer)) > 0) {
                fileDataOutputStream.write(buffer, 0, count);
            }
            fileInputStream.close();

            fileClientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receiveFile(String username, String token, String type, String fileName) {
        try {
            File file = new File(RESOURCE_PATH + convertTypeToFolderName(type) + "/" + fileName);
            if (file.exists()) {
                return;
            }
            file.createNewFile();

            fileClientSocket = new Socket(FILE_SERVER_IP_ADDRESS, FILE_SERVER_PORT);
            fileDataInputStream = new DataInputStream(new BufferedInputStream(fileClientSocket.getInputStream()));
            fileDataOutputStream = new DataOutputStream(new BufferedOutputStream(fileClientSocket.getOutputStream()));

            Thread.sleep(100);

            fileDataOutputStream.writeUTF("DOWNLOAD " + username + " " + token + " " + type + " " + fileName);
            fileDataOutputStream.flush();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            long count = fileDataInputStream.readLong();
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
            e.printStackTrace();
        }
    }

    private static String convertTypeToFolderName(String type) {
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
}
