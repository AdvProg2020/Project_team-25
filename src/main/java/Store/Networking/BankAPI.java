package Store.Networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BankAPI {
    public static final int PORT = 2222;
    public static final String IP = "192.168.1.4";

    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;
    private static String input;
    private static String output;

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
        new Thread(() -> {
            while (true) {
                try {
                    input = inputStream.readUTF();

                } catch (IOException e) {
                    System.out.println("disconnected");
                    System.exit(0);
                }
            }
        }).start();
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


}
