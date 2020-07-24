package Store.Networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTransportServer {

    private static final int FILE_SERVER_PORT = 44444;
    private static final String UPLOAD_REGEX = "^UPLOAD (\\S+) (\\S{50}) ([IVF]) (\\S+) (\\d+)$";
    private static final String DOWNLOAD_REGEX = "^DOWNLOAD (\\S+) (\\S{50}) ([IVF]) (\\S+)$";
    private static final String RESOURCE_PATH = "src/main/resources/";

    private ServerSocket serverSocket;

    public FileTransportServer() throws IOException {
        serverSocket = new ServerSocket(FILE_SERVER_PORT);

        System.out.println("SERVER CREATED");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileTransportServer.this.run();
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
                System.out.println("CLIENT ACCEPTED");
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                new FileTransportServer.ClientHandler(clientSocket, dataOutputStream, dataInputStream).start();
            }
            catch (Exception e) {
                e.printStackTrace();
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
                String metaData = dataInputStream.readUTF();
                Matcher matcher;
                if ((matcher = getMatcher(metaData, DOWNLOAD_REGEX)).find()) {
                    if (!TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        return;
                    }
                    sendFile(matcher.group(3), matcher.group(4));
                }
                else if ((matcher = getMatcher(metaData, UPLOAD_REGEX)).find()) {
                    System.out.println("SHOULD GET");
                    if (!TokenHandler.checkUsernameAndToken(matcher.group(1), matcher.group(2))) {
                        return;
                    }
                    System.out.println("HERE " + matcher.group(3) + " " + matcher.group(4) + " " + Long.parseLong(matcher.group(5)));
                    receiveFile(matcher.group(3), matcher.group(4), Long.parseLong(matcher.group(5)));
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

            FileInputStream fileInputStream = new FileInputStream(requestedFile);
            int count;
            byte[] buffer = new byte[8192];
            while ((count = fileInputStream.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, count);
            }
            dataOutputStream.flush();
            fileInputStream.close();
        }

        private void receiveFile(String type, String fileName, long fileLength) throws IOException {
            String middleFolderName = convertTypeToFolderName(type);
            File newFile = new File(RESOURCE_PATH + middleFolderName + "/" + fileName);
            if (newFile.exists()) {
                dataOutputStream.writeUTF("REJ");
                dataOutputStream.flush();
                return;
            }
            newFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            dataOutputStream.writeUTF("ACK");
            dataOutputStream.flush();

            long count = fileLength;
            byte[] buffer = new byte[18192];
            while (count > 0) {
                int read = dataInputStream.read(buffer);
                fileOutputStream.write(buffer, 0, read);
                count -= read;
            }
            System.out.println("DONE");

            fileOutputStream.close();
        }
    }

    private static Matcher getMatcher(String string, String regex) {
        return Pattern.compile(regex).matcher(string);
    }

}
