package Store.Networking;

import Store.Controller.*;
import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import Store.Model.Log.BuyLogItem;
import Store.Model.Log.LogItem;
import com.google.gson.Gson;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class MainServer {
    private final HashMap<Integer, Integer> accounts = new HashMap<>();
    private ServerSocket serverSocket;
    private int port;
    private static Socket bankAPISocket;
    private static HashMap<Auction, Integer> chatAuctionPorts;
    private static HashMap<Auction, Integer> chatWriteAuctionPort;
    private static HashMap<Socket, ArrayList<Auction>> socketAuctionsAvailable;
    private static HashMap<Auction, ArrayList<ChatAuctionClientThread>> chatAuctionArray;

    static {
        try {
            bankAPISocket = new Socket("127.0.0.1", 15000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static DataOutputStream dataOutputStream1;

    static {
        try {
            dataOutputStream1 = new DataOutputStream(new BufferedOutputStream(bankAPISocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DataInputStream dataInputStream1;

    static {
        try {
            dataInputStream1 = new DataInputStream(new BufferedInputStream(bankAPISocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MainServer() throws IOException {
        this.port = 12000;//nextFreePort(9000, 20000);
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
        for (Auction auction: Auction.getAllAuctions()) {
            chatAuctionPorts.put(auction, nextFreePort(9000, 20000));
            chatWriteAuctionPort.put(auction, nextFreePort(9000, 20000));
            chatAuctionArray.put(auction, new ArrayList<>());
        }
        for (Auction auction: Auction.getAllAuctions()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket = new ServerSocket(chatAuctionPorts.get(auction));
                        Socket socket;
                        while (true) {
                            socket = serverSocket.accept();
                            socketAuctionsAvailable.put(socket, new ArrayList<>());
                            chatAuctionArray.get(auction).add(new ChatAuctionClientThread(socket, auction));
                            chatAuctionArray.get(auction).get(chatAuctionArray.get(auction).size() - 1).start();
                        }
                    }catch (Exception e){


                    }
                }
            }).start();
        }

        for (Auction auction: Auction.getAllAuctions()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket = new ServerSocket(chatWriteAuctionPort.get(auction));
                        Socket socket;
                        while (true) {
                            socket = serverSocket.accept();
                            new ChatAuctionWriteClientThread(socket, auction).start();
                        }
                    }catch (Exception e){

                    }
                }
            }).start();
        }
        Thread checkAuctionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for (Auction auction: Auction.getAllAuctions())
                        if ((LocalDateTime.now()).isAfter(auction.getEndingTime()))
                            auction.finish();
                }
            }
        });
        checkAuctionThread.start();
    }

    public static Object sendAndReceiveToBankAPICreateAccount() throws IOException {
        String output = "createAccount";
        dataOutputStream1.writeUTF(output);
        dataOutputStream1.flush();
        String input = dataInputStream1.readUTF();
        System.out.println(input);
        if (Pattern.matches("\\d+", input))
            return Integer.parseInt(input);
        else
            return null;
    }
    public static Object sendAndReceiveToBankAPIBalance() throws IOException {
        String output = "balance";
        dataOutputStream1.writeUTF(output);
        dataOutputStream1.flush();
        String input = dataInputStream1.readUTF();
        if (Pattern.matches("\\d+(\\.(\\d+))?", input))
            return input;
        else
            return null;
    }
    public static Object sendAndReceiveToBankAPIMove(double money, int source, int dest, String description) throws IOException {
        String output = "move" + " " + money + " " + source + " " + dest + " " + description;
        dataOutputStream1.writeUTF(output);
        dataOutputStream1.flush();
        return dataInputStream1.readUTF();
    }
    public static Object sendAndReceiveToBankAPIDeposit(double money, int source, String description) throws IOException {
        String output = "deposit" + " " + money + " " + source + " " + description;
        dataOutputStream1.writeUTF(output);
        dataOutputStream1.flush();
        return dataInputStream1.readUTF();
    }

    public static void portAuction(Auction auction) {
        chatAuctionPorts.put(auction, nextFreePort(9000, 20000));
        chatWriteAuctionPort.put(auction, nextFreePort(9000, 20000));
        chatAuctionArray.put(auction, new ArrayList<>());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(chatAuctionPorts.get(auction));
                    Socket socket;
                    while (true) {
                        socket = serverSocket.accept();
                        chatAuctionArray.get(auction).add(new ChatAuctionClientThread(socket, auction));
                        chatAuctionArray.get(auction).get(chatAuctionArray.get(auction).size() - 1).start();
                    }
                }catch (Exception e){

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(chatWriteAuctionPort.get(auction));
                    Socket socket;
                    while (true) {
                        socket = serverSocket.accept();
                        new ChatAuctionWriteClientThread(socket, auction).start();
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public int getPort() {
        return port;
    }

    private static int nextFreePort(int from, int to) {
        int port = ThreadLocalRandom.current().nextInt(from, to);
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to);
            }
        }
    }

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static class ChatAuctionClientThread extends Thread{
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private Auction auction;

        public ChatAuctionClientThread(Socket socket, Auction auction) throws IOException{
            this.clientSocket = socket;
            dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            this.auction = auction;
        }

        public DataOutputStream getDataOutputStream() {
            return dataOutputStream;
        }

        public DataInputStream getDataInputStream() {
            return dataInputStream;
        }

        public Socket getClientSocket() {
            return clientSocket;
        }

        @Override
        public void run() {
            while (true){
                String string = null;
                try {
                    string = dataInputStream.readUTF();
                    Gson gson = new Gson();
                    HashMap input = gson.fromJson(string, HashMap.class);
                    if (input.get("message").equals("connect")) {
                        if (!socketAuctionsAvailable.get(clientSocket).contains(auction))
                            socketAuctionsAvailable.get(clientSocket).add(auction);
                    }
                    if (input.get("message").equals("disconnect")) {
                        if (socketAuctionsAvailable.get(clientSocket).contains(auction))
                            socketAuctionsAvailable.get(clientSocket).remove(auction);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("content", "disconnected");
                        try {
                            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                            dataOutputStream.writeUTF(gson.toJson(hashMap));
                            dataOutputStream.flush();
                        } catch(Exception exception){
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private static class ChatAuctionWriteClientThread extends Thread{
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private Auction auction;

        public ChatAuctionWriteClientThread(Socket socket, Auction auction) throws IOException{
            this.clientSocket = socket;
            dataInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            this.auction = auction;
        }

        @Override
        public void run() {
            while (true){
                String string = "";
                try {
                    string = dataInputStream.readUTF();
                    Gson gson = new Gson();
                    HashMap input = gson.fromJson(string, HashMap.class);
                    if (input.get("message").equals("chatMessage")){
                        chatMessage(input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void chatMessage(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            User user = User.getUserByUsername((String)input.get("username"));
            if (user instanceof Customer) {
                String message = (String) input.get("chatMessage");
                message = user.getUsername() + ": " + message;
                auction.getMessages().add(message);
                hashMap.put("content", "done");
                try {
                    Gson gson = new Gson();
                    dataOutputStream.writeUTF(gson.toJson(hashMap));
                    dataOutputStream.flush();
                } catch(Exception exception){
                }
                sendToAllConnectedSockets();
            }
            else{
                hashMap.put("content", "error");
                hashMap.put("type", "You Are Not Allowed To Chat!");
                try {
                    Gson gson = new Gson();
                    dataOutputStream.writeUTF(gson.toJson(hashMap));
                    dataOutputStream.flush();
                } catch(Exception exception){
                }
            }
        }

        private void sendToAllConnectedSockets() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", auction.getMessages());
            for (ChatAuctionClientThread chatAuctionClientThread: chatAuctionArray.get(auction)){
                if (socketAuctionsAvailable.get(chatAuctionClientThread.getClientSocket()).contains(auction)) {
                    try {
                        Gson gson = new Gson();
                        chatAuctionClientThread.getDataOutputStream().writeUTF(gson.toJson(hashMap));
                        chatAuctionClientThread.getDataOutputStream().flush();
                    } catch (Exception exception) {
//
                    }
                }
            }
        }
    }


    private class ClientThread extends Thread {
        private Socket clientSocket;
        private DataOutputStream dataOutputStream;
        private DataInputStream dataInputStream;
        private Customer guest;
        private String token;
        private User user;

        private ClientThread(Socket socket) throws IOException {
            this.token = "";
            this.guest = new Customer("guest", "guest", "guest",
                    "guest@approject.com", "00000000000", "guest");
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
                    token = (String) input.get("token");
                    if (token.isEmpty() || TokenHandler.getUsernameOfToken(token) == null || token == null) {
                        user = guest;
                    } else {
                        user = User.getUserByUsername(TokenHandler.getUsernameOfToken(token));
                    }
//                    System.out.println("Token: " + token + "Command: " + input.get("message"));
                    System.out.println(input.get("message"));
                    if (input.get("message").equals("login")) {
                        moveShoppingCartAndLoginServer((String) input.get("username"));
                    }
                    if (input.get("message").equals("isUsernameWithThisName")) {
                        isUsernameWithThisNameServer((String) input.get("username"));
                    }
                    if (input.get("message").equals("isUsernameExistInRequests")) {
                        isUsernameExistInRequestsServer((String) input.get("username"));
                    }
                    if (input.get("message").equals("createOrdinaryAccount")) {
                        handleCreateOrdinaryAccountServer((String) input.get("type"), (ArrayList<String>) input.get("attributes"));
                    }
                    if (input.get("message").equals("createManagerAccount")) {
                        handleCreateManagerAccountServer((ArrayList<String>) input.get("attributes"));
                    }
                    if (input.get("message").equals("getUserInfo")) {
                        getUserByUsernameServer((String) input.get("username"));
                    }
                    if (input.get("message").equals("getAllCategories")) {
                        getAllCategoriesServer();
                    }
                    if (input.get("message").equals("getProducts")) {
                        getProductsServer(input);
                    }
                    if (input.get("message").equals("getAuctionsProducts")){
                        getAuctionsServer(input);
                    }
                    if (input.get("message").equals("getOfferedProducts")) {
                        getOfferedProductsServer(input);
                    }
                    if (input.get("message").equals("getPriceHigh")) {
                        getPriceHighServer();
                    }
                    if (input.get("message").equals("getPriceLow")) {
                        getPriceLowServer();
                    }
                    if (input.get("message").equals("getAllFilters")) {
                        getAllFiltersServer((String) input.get("categoryFilter"));
                    }
                    if (input.get("message").equals("logout")) {
                        logoutServer();
                    }
                    if (input.get("message").equals("hasManager?")) {
                        hasManagerServer();
                    }
                    if (input.get("message").equals("getAllSellersOfProduct")) {
                        getAllSellerOfProductServer((String) input.get("id"));
                    }
                    if (input.get("message").equals("getProductWithDifferentSeller")) {
                        getProductWithDifferentSellerServer((String) input.get("id"), (String) input.get("username"));
                    }
                    if (input.get("message").equals("getComparedProduct")) {
                        getComparedProductServer((String) input.get("id"));
                    }
                    if (input.get("message").equals("rateProduct")) {
                        rateProductServer(input);
                    }
                    if (input.get("message").equals("commentProduct")) {
                        commentProductServer(input);
                    }
                    if (input.get("message").equals("hasBeenRated?")) {
                        hasBeenRatedServer(input);
                    }
                    if (input.get("message").equals("hasBoughtProduct?")) {
                        hasBoughtProductServer(input);
                    }
                    if (input.get("message").equals("addToCart")) {
                        addToCartServer(input);
                    }
                    if (input.get("message").equals("editManagerPersonalInfo")) {
                        editManagerPersonalInfoServer(input);
                    }
                    if (input.get("message").equals("getSortedOffCodes")) {
                        getSortedOffCodesServer(input);
                    }
                    if (input.get("message").equals("removeOffCode")) {
                        removeOffCodeServer(input);
                    }
                    if (input.get("message").equals("editOffCode")) {
                        editOffCodeServer(input);
                    }
                    if (input.get("message").equals("isOffCodeWithThisCode")) {
                        isOffCodeWithThisCodeServer(input);
                    }
                    if (input.get("message").equals("assignOffCodeToUser")) {
                        assignOffCodeToUserServer(input);
                    }
                    if (input.get("message").equals("createOffCode")) {
                        createOffCodeServer(input);
                    }
                    if (input.get("message").equals("createOperator")){
                        createOperator(input);
                    }
                    if (input.get("message").equals("getAllUsers")) {
                        getAllUsersServer(input);
                    }
                    if (input.get("message").equals("deleteUserByName")) {
                        deleteUserByNameServer(input);
                    }
                    if (input.get("message").equals("removeProduct")) {
                        removeProductServer(input);
                    }
                    if (input.get("message").equals("removeCategory")) {
                        removeCategoryServer(input);
                    }
                    if (input.get("message").equals("isCategoryWithThisName")) {
                        isCategoryWithThisNameServer(input);
                    }
                    if (input.get("message").equals("editCategory")) {
                        editCategoryServer(input);
                    }
                    if (input.get("message").equals("isInFilter")) {
                        isInFilterServer(input);
                    }
                    if (input.get("message").equals("addCategory")) {
                        addCategoryServer(input);
                    }
                    if (input.get("message").equals("getPendingRequests")) {
                        getPendingRequestsServer();
                    }
                    if (input.get("message").equals("removeRequest")) {
                        removeRequestsServer(input);
                    }
                    if (input.get("message").equals("handleRequest")) {
                        handleRequestsServer(input);
                    }
                    if (input.get("message").equals("validatePassword")) {
                        validatePasswordServer(input);
                    }
                    if (input.get("message").equals("canOfferBeUsedInDate")) {
                        canOfferBeUsedInDateServer(input);
                    }
                    if (input.get("message").equals("editCustomerPersonalInfo")) {
                        editCustomerPersonalInfoServer(input);
                    }
                    if (input.get("message").equals("canBuy")) {
                        if (input.get("by").equals("wallet"))
                            purchaseByWallet(input);
                        else if (input.get("by").equals("bank"))
                            purchaseByBank(input);
                    }
                    if (input.get("message").equals("buy")) {
                        if (input.get("by").equals("wallet"))
                            purchaseByWallet(input);
                        else if (input.get("by").equals("bank")) {
                            purchaseByBank(input);
                        }
                    }
                    if (input.get("message").equals("removeProductFromCart")) {
                        removeProductFromCartServer(input);
                    }
                    if (input.get("message").equals("chargeWallet")){
                        chargeWallet(input);
                    }
                    if (input.get("message").equals("dechargeWallet")){
                        dechargeWallet(input);
                    }
                    if (input.get("message").equals("purchase")){
                        if (input.get("by").equals("wallet"))
                            purchaseByWallet(input);
                        else if (input.get("by").equals("bank"))
                            purchaseByBank(input);
                    }
                    /*if (input.get("message").equals("enterAuction")){
                        chatAuction(input);
                    }*/
                    if (input.get("message").equals("increaseAuctionPrice")){
                        increaseAuctionPrice(input);
                    }
                    if (input.get("message").equals("setMinimum")){
                        setMinimum(input);
                    }
                    if (input.get("message").equals("setKarmozd")){
                        setKarmozd(input);
                    }
                    if (input.get("message").equals("editSellerPersonalInfo")) {
                        editSellerPersonalInfoServer(input);
                    }
                    if (input.get("message").equals("removeProductFromSeller")) {
                        removeProductFromSellerServer(input);
                    }
                    if (input.get("message").equals("removeFilterFromSeller")) {
                        removeFilterFromSellerServer(input);
                    }
                    if (input.get("message").equals("addAds")) {
                        addAdsServer(input);
                    }
                    if (input.get("message").equals("addFilterToProductFromSeller")) {
                        addFilterToProductFromSellerServer(input);
                    }
                    if (input.get("message").equals("isProductFromThisSeller")) {
                        isProductFromThisSellerServer(input);
                    }
                    if (input.get("message").equals("isProductWithThisID")) {
                        isProductWithThisIDServer(input);
                    }
                    if (input.get("message").equals("isProductInOffer")) {
                        isProductInOfferServer(input);
                    }
                    if (input.get("message").equals("addProductToOffer")) {
                        addProductToOfferServer(input);
                    }
                    if (input.get("message").equals("isProductInCategory")) {
                        isProductInCategoryServer(input);
                    }
                    if (input.get("message").equals("sortProductsOfSeller")) {
                        sortProductsOfSellerServer(input);
                    }
                    if (input.get("message").equals("sortOffersOfSeller")) {
                        sortOffersOfSellerServer(input);
                    }
                    if (input.get("message").equals("addOfferFromSeller")) {
                        addOfferFromSellerServer(input);
                    }
                    if (input.get("message").equals("addProductFromSeller")) {
                        addProductFromSellerServer(input);
                    }
                    if (input.get("message").equals("editOffer")) {
                        editOfferServer(input);
                    }
                    if (input.get("message").equals("editProduct")) {
                        editProductServer(input);
                    }
                    if (input.get("message").equals("getOffersOfThisSeller")) {
                        getOffersOfThisSellerServer();
                    }
                    if (input.get("message").equals("addAuction")){
                        addAuction(input);
                    }
                    if (input.get("message").equals("isInAuction")){
                        isInAuction(input);
                    }
                    if (input.get("message").equals("getHighestPriceAuction")){
                        getHighestPriceAuction(input);
                    }
                    if (input.get("message").equals("getCurrentBuyerAuction")){
                        getCurrentBuyerAuction(input);
                    }
                    if (input.get("message").equals("getConditionAuction")){
                        getConditionAuction(input);
                    }
                    if (input.get("message").equals("getAuctionOfProduct")){
                        getAuctionOfProduct(input);
                    }
                    if (input.get("message").equals("isReceivedLog")) {
                        isReceivedLog(input);
                    }
                    if (input.get("message").equals("getBuyLogs")){
                        getBuyLogs();
                    }
                    if (input.get("message").equals("sendProduct")){
                        sendProduct(input);
                    }
                    if (input.get("message").equals("portAuctionChat")){
                        portAuctionChat(input);
                    }
                    if (input.get("message").equals("portAuctionChatWrite")){
                        portAuctionChatWrite(input);
                    }
                }catch (IOException exception) {
                    //exception.printStackTrace();
                }
            }
        }

        private void setMinimum(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Manager.setMinimumRemaining((Double)input.get("minimum"));
            hashMap.put("content", "done");
            sendMessage(hashMap);
        }

        private void setKarmozd(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Manager.setKarmozd((Double)input.get("karmozd"));
            hashMap.put("content", "done");
            sendMessage(hashMap);
        }

        private void createOperator(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            String creator = (String)input.get("creator");
            String username = (String)input.get("username");
            String firstName = (String)input.get("firstName");
            String lastName = (String)input.get("lastName");
            String email = (String)input.get("email");
            String phoneNumber = (String)input.get("phoneNumber");
            String password = (String)input.get("password");
            ManagerController.createOperatorProfile((Manager)User.getUserByUsername(creator), username, firstName, lastName, email, phoneNumber, password);
            hashMap.put("content", "done");
            sendMessage(hashMap);
        }

        private void portAuctionChatWrite(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Auction auction = Auction.getAuctionByID((Integer)input.get("auctionId"));
            hashMap.put("content", chatWriteAuctionPort.get(auction));
            sendMessage(hashMap);
        }

        private void portAuctionChat(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Auction auction = Auction.getAuctionByID((Integer)input.get("auctionId"));
            hashMap.put("content", chatAuctionPorts.get(auction));
            sendMessage(hashMap);
        }

        synchronized private void sendProduct(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            BuyLogItem buyLogItem = (BuyLogItem)input.get("buyLogId");
            buyLogItem.setReceived(true);
            hashMap.put("content", "done");
            sendMessage(hashMap);
        }

        private void getBuyLogs() {
            HashMap<String, Object> hashMap = new HashMap<>();
            HashMap<String, ArrayList> content = new HashMap<>();
            for (User user: User.getAllUsers()){
                if (user instanceof Customer){
                    content.put(user.getUsername(), HashMapGenerator.getListOfBuyLogItems(((Customer)user).getBuyLog()));
                }
            }
            hashMap.put("content", content);
            sendMessage(hashMap);
        }

        private void isReceivedLog(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            int id = (Integer)input.get("id");
            if (LogItem.getLogById(id) != null)
                hashMap.put("content", ((BuyLogItem) LogItem.getLogById(id)).isReceived());
            else
                hashMap.put("content", "error");
        }

        private void getAuctionOfProduct(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID((Integer)input.get("productId"));
            Auction auction = Auction.getAuctionOfProduct(product);
            if (auction != null)
                hashMap.put("content", HashMapGenerator.getAuctionHashMap(auction));
            else
                hashMap.put("content", "error");
            sendMessage(hashMap);
        }

        private void getConditionAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            if (Auction.getAuctionByID((Integer)input.get("auctionId")) == null)
                hashMap.put("content", "Sold");
            else
                hashMap.put("content", "In Progress");
            sendMessage(hashMap);
        }

        private void getCurrentBuyerAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Auction auction = Auction.getAuctionByID((Integer)input.get("auctionId"));
            hashMap.put("content", auction.getCurrentBuyer().getUsername());
            sendMessage(hashMap);
        }

        private void getHighestPriceAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Auction auction = Auction.getAuctionByID((Integer)input.get("auctionId"));
            hashMap.put("content", auction.getHighestPrice());
            sendMessage(hashMap);
        }

        private void isInAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID((Integer)((Map)input.get("product")).get("id"));
            if (Auction.getAuctionOfProduct(product) == null)
                hashMap.put("content", "false");
            else
                hashMap.put("content", "true");
            sendMessage(hashMap);
        }

        synchronized private void addAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID(Integer.parseInt((String)(((Map)input.get("product")).get("id"))));
            LocalDateTime dateTime = (LocalDateTime)input.get("date");
            try {
                SellerUIController.addAuction(product, dateTime);
                hashMap.put("content", "done");
            }catch (Exception e){
                hashMap.put("content", "error");
                hashMap.put("type", e.getMessage());
            }
            sendMessage(hashMap);
        }

        synchronized private void increaseAuctionPrice(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Auction auction = (Auction) Auction.getAuctionByID((Integer)input.get("auctionId"));
            double newPrice = (Double) input.get("newPrice");
            User tryer = User.getUserByUsername((String)input.get("buyer"));
            if (!(tryer instanceof Customer)) {
                hashMap.put("content", "error");
                hashMap.put("type", "You are not customer!");
            }else{
                Customer customer = (Customer) tryer;
                if (customer.getMoney() - Manager.getMinimumRemaining() - Auction.getMoneyInAuctions(customer) < newPrice) {
                    hashMap.put("content", "error");
                    hashMap.put("type", "Your money is not enough!");
                }else if (newPrice < auction.getHighestPrice() + 5){
                    hashMap.put("content", "error");
                    hashMap.put("type", "Your bet should be higher!");
                } else if (newPrice < auction.getBasePrice()){
                    hashMap.put("content", "error");
                    hashMap.put("type", "Your bet should be more than base price!");
                }
                else{
                    auction.changeBet(customer, newPrice);
                    hashMap.put("content", "done");
                }
            }
            sendMessage(hashMap);
        }

        /*private void chatAuction(HashMap input) throws IOException {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Auction auction = (Auction) input.get("auction");
                    auction.setMessagePort(nextFreePort(9000, 20000));
                    try {
                        ServerSocket chatAuctionServerSocket = new ServerSocket(auction.getMessegePort());
                        while (true){

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }*/

        synchronized private void purchaseByBank(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Customer customer = (Customer)User.getUserByUsername((String) input.get("username"));
            String offCode = (String)input.get("offCode");
            String address = (String)input.get("address");
            try {
                hashMap.put("content", CustomerUIController.purchase(customer, offCode, true, address));
            } catch (Exception e) {
                hashMap.put("content", "error");
                hashMap.put("type", e.getMessage());
            }
            sendMessage(hashMap);
        }

        //karmozd va hesabe forooshgah?????
        synchronized private void purchaseByWallet(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Customer customer = (Customer)User.getUserByUsername((String) input.get("username"));
            System.out.println(customer);
            String offCode = (String)input.get("offCode");
            String address = (String)input.get("address");
            try {
                hashMap.put("content", CustomerUIController.purchase(customer, offCode, false, address));
            } catch (Exception e) {
                System.out.println(customer);
                hashMap.put("content", "error");
                hashMap.put("type", e.getMessage());
            }
            sendMessage(hashMap);
        }

        synchronized private void chargeWallet(HashMap input) throws IOException {
            HashMap<String, Object> hashMap = new HashMap<>();
            User user = User.getUserByUsername((String) input.get("username"));
            double money = (Double) input.get("money");
            String result = "";
            if (user instanceof Seller)
                result = sendAndReceiveToBankAPI("move", money, ((Seller)user).getBankAccount(), Manager.getBankAccount(), "");
            else
                result = sendAndReceiveToBankAPI("move", money, ((Customer)user).getBankAccount(), Manager.getBankAccount(), "");
            if (!result.equalsIgnoreCase("done successfully")) {
                hashMap.put("content", "error");
                hashMap.put("type", result);
            }
            else{
                if (user instanceof Seller)
                    ((Seller)user).setMoney(((Seller)user).getMoney() + money);
                else
                    ((Customer)user).setMoney(((Customer)user).getMoney() + money);
                hashMap.put("content", "ok");
            }
            sendMessage(hashMap);
        }

        synchronized private void dechargeWallet(HashMap input) throws IOException {
            HashMap<String, Object> hashMap = new HashMap<>();
            Seller seller = (Seller)User.getUserByUsername((String) input.get("username"));
            double money = (Double) input.get("money");
            String result = "";
            if (money <= seller.getMoney() - Manager.getMinimumRemaining()) {
                result = sendAndReceiveToBankAPI("move", money, Manager.getBankAccount(), seller.getBankAccount(), "");
                if (!result.equalsIgnoreCase("done")) {
                    hashMap.put("content", "error");
                    hashMap.put("type", result);
                }
                else{
                    seller.setMoney(seller.getMoney() - money);
                    hashMap.put("content", "ok");
                }
            }else
            {
                hashMap.put("content", "error");
                hashMap.put("type", "not enough money in wallet");
            }
            sendMessage(hashMap);
        }

        private void getOffersOfThisSellerServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfOffers(SellerController.getOffersOfThisSeller((Seller) user)));
            sendMessage(hashMap);
        }

        private void editProductServer(HashMap input) {
            Map<String, Object> productHashMap = (Map<String, Object>) input.get("product");
            Product product = new Product(CheckingStatus.CREATION, Manager.categoryByName((String) productHashMap.get("category")), (String) productHashMap.get("name"), (Seller) user, (String) productHashMap.get("brand"), (Double) productHashMap.get("price"), (Boolean) productHashMap.get("availability"), (String) productHashMap.get("description"));
            for (String filterToAdd : (List<String>) productHashMap.get("filters")) {
                product.addFilter(filterToAdd);
            }
            SellerController.editProduct((Seller) user, Product.getProductByID(Integer.parseInt((String) input.get("id"))), product);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", SellerController.editProduct((Seller) user, Product.getProductByID(Integer.parseInt((String) input.get("id"))), product));
            sendMessage(hashMap);
        }

        private void editOfferServer(HashMap input) {
            Map<String, Object> offerHashMap = (Map<String, Object>) input.get("offer");
            Offer offer = new Offer(user, CheckingStatus.CREATION, (Double)offerHashMap.get("offPercentage"));
            offer.setStartingTime(new Date(Long.parseLong((String) offerHashMap.get("startingTime"))));
            offer.setEndingTime(new Date(Long.parseLong((String) offerHashMap.get("endingTime"))));
            for (String product : (List<String>) offerHashMap.get("products")) {
                offer.addProduct(Product.getProductByID(Integer.parseInt(product)));
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", SellerController.editOff((Seller) user, Offer.getOfferByID(Integer.parseInt((String) input.get("id"))), offer));
            sendMessage(hashMap);
        }

        private void addProductFromSellerServer(HashMap input) {
            Map<String, Object> productHashMap = (Map<String, Object>) input.get("product");
            Product product = new Product(CheckingStatus.CREATION, Manager.categoryByName((String) productHashMap.get("category")), (String) productHashMap.get("name"), (Seller) user, (String) productHashMap.get("brand"), (Double) productHashMap.get("price"), (Boolean) productHashMap.get("availability"), (String) productHashMap.get("description"));
            for (String filterToAdd : (List<String>) productHashMap.get("filters")) {
                product.addFilter(filterToAdd);
            }
            SellerController.addProduct((Seller) user, product);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void addOfferFromSellerServer(HashMap input) {
            Map<String, Object> offerHashMap = (Map<String, Object>) input.get("offer");
            Offer offer = new Offer(user, CheckingStatus.CREATION, (Double)offerHashMap.get("offPercentage"));
//            offer.setStartingTime((Date) offerHashMap.get("startingTime"));
//            offer.setEndingTime((Date) offerHashMap.get("endingTime"));
            offer.setStartingTime(new Date(Long.parseLong((String) offerHashMap.get("startingTime"))));
            offer.setEndingTime(new Date(Long.parseLong((String) offerHashMap.get("endingTime"))));
            for (String product : (List<String>) offerHashMap.get("products")) {
                offer.addProduct(Product.getProductByID(Integer.parseInt(product)));
            }
            SellerController.addOff((Seller) user, offer);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void sortOffersOfSellerServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfOffers(OffersController.sort((String) input.get("sort"), ((Seller) user).getOffers())));
            sendMessage(hashMap);
        }

        private void sortProductsOfSellerServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfProducts(ProductsController.sort((String) input.get("sort"), ((Seller) user).getProducts())));
            sendMessage(hashMap);
        }

        private void isProductInCategoryServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Product.getProductByID(Integer.parseInt((String) input.get("id"))).getCategory() != null);
            sendMessage(hashMap);
        }

        private void addProductToOfferServer(HashMap input) {
            Offer offer = Offer.getOfferByID(Integer.parseInt((String) input.get("offerId")));
            offer.addProduct(Product.getProductByID(Integer.parseInt((String) input.get("productId"))));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void isProductInOfferServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Offer.getOfferOfProduct(Product.getProductByID(Integer.parseInt((String) input.get("id")))) != null);
            sendMessage(hashMap);
        }

        private void isProductWithThisIDServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Product.getProductByID(Integer.parseInt((String) input.get("id"))) != null);
            sendMessage(hashMap);
        }

        private void isProductFromThisSellerServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            hashMap.put("content", ((Seller) user).equals(product.getSeller()));
            sendMessage(hashMap);
        }

        private void addFilterToProductFromSellerServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", SellerController.addFilterToProduct((Seller) user, (String) input.get("id"), (String) input.get("filter")));
            sendMessage(hashMap);
        }

        private void addAdsServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", SellerController.addAds((Seller) user, (int) Math.round((Double) input.get("id"))));
            sendMessage(hashMap);
        }

        private void removeFilterFromSellerServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", SellerController.removeFilterFromProduct((Seller) user, (String) input.get("id"), (String) input.get("filter")));
            sendMessage(hashMap);
        }

        private void removeProductFromSellerServer(HashMap input) {
            SellerController.removeProduct((Seller) user, Product.getProductByID(Integer.parseInt((String) input.get("id"))));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void editSellerPersonalInfoServer(HashMap input) {
            try {
                SellerController.editPersonalInfo((Seller) user, (String) input.get("field"), (String) input.get("newValue"));
            } catch (SellerController.InvalidValueException e) {
                //do nothing
            } finally {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("content", "Ok");
                sendMessage(hashMap);
            }
        }

        private void removeProductFromCartServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Customer customer = (Customer) user;
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            if (customer.isInCart(product)) {
                customer.removeFromCart(product);
                hashMap.put("content", true);
            } else {
                hashMap.put("content", false);
            }
            sendMessage(hashMap);
        }

        private void editCustomerPersonalInfoServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ManagerController.editPersonalInfo(user, (String) input.get("field"), (String) input.get("newValue")));
            sendMessage(hashMap);
        }

        private void canOfferBeUsedInDateServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Offer.getOfferOfProduct(Product.getProductByID(Integer.parseInt((String) input.get("id")))).canBeUsedInDate(new Date()));
            sendMessage(hashMap);
        }

        private void validatePasswordServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", user.validatePassword((String) input.get("password")));
            sendMessage(hashMap);
        }

        private void handleRequestsServer(HashMap input) {
            ManagerController.handleRequest((Manager) user, (Boolean) input.get("status"), Manager.getRequestById(Integer.parseInt((String) input.get("id"))));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void removeRequestsServer(HashMap input) {
            Manager.removeRequest(Manager.getRequestById(Integer.parseInt((String) input.get("id"))));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void getPendingRequestsServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfOffRequests(Manager.getPendingRequests()));
            sendMessage(hashMap);
        }

        private void addCategoryServer(HashMap input) {
            ManagerController.addCategory((Manager) user, (String) input.get("name"), (String) input.get("parentName"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void isInFilterServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Manager.categoryByName((String) input.get("name")).isInFilter((String) input.get("filter")));
            sendMessage(hashMap);
        }

        private void editCategoryServer(HashMap input) {
            ManagerController.editCategory((Manager) user, Manager.categoryByName((String) input.get("name")), (String) input.get("field"), (String) input.get("newValue"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void isCategoryWithThisNameServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Manager.categoryByName((String) input.get("name")) != null);
            sendMessage(hashMap);
        }

        private void removeCategoryServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ManagerController.removeCategory((Manager) user, Manager.categoryByName((String) input.get("name"))));
            sendMessage(hashMap);
        }

        private void removeProductServer(HashMap input) {
            ManagerController.removeProducts((Manager) user, Product.getProductByID(Integer.parseInt((String) input.get("id"))));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void deleteUserByNameServer(HashMap input) {
            ManagerController.deleteUserByName((Manager) user, (String) input.get("username"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void getAllUsersServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfUsers(ManagerController.sortUsers((String) input.get("sort"), User.getAllUsers())));
            sendMessage(hashMap);
        }

        private void createOffCodeServer(HashMap input) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date startingDate = format.parse((String) input.get("startingDate"));
                Date endingDate = format.parse((String) input.get("endingDate"));
                ManagerController.createOffCode((Manager) user, (String) input.get("code"), (Double) input.get("offPercent"), (Double) input.get("maximumValue"), Integer.parseInt((String) input.get("usageCount")), startingDate, endingDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void assignOffCodeToUserServer(HashMap input) {
            Manager.assignOffCodeToUser(Manager.getOffCodeByCode((String) input.get("code")), (Customer) User.getUserByUsername((String) input.get("username")));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void isOffCodeWithThisCodeServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Manager.getOffCodeByCode((String) input.get("code")) != null);
            sendMessage(hashMap);
        }

        private void editOffCodeServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ManagerController.editOffCode((Manager) user, Manager.getOffCodeByCode((String) input.get("code")), (String) input.get("field"), (String) input.get("newValue")));
            sendMessage(hashMap);
        }

        private void removeOffCodeServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ManagerController.removeOffCode((Manager) user, (String) input.get("code")));
            sendMessage(hashMap);
        }

        private void getSortedOffCodesServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfOffCodes(ManagerController.sortOffCodes((String) input.get("sort"), Manager.getOffCodes())));
            sendMessage(hashMap);
        }

        private void editManagerPersonalInfoServer(HashMap input) {
            ManagerController.editPersonalInfo(user, (String) input.get("field"), (String) input.get("newValue"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ManagerController.editPersonalInfo(user, (String) input.get("field"), (String) input.get("newValue")));
            sendMessage(hashMap);
        }

        private void addToCartServer(HashMap input) {
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            Customer customer = (Customer) user;
            HashMap<String, Object> hashMap = new HashMap<>();
            customer.addToCart(product);
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void hasBoughtProductServer(HashMap input) {
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            Customer customer = (Customer) User.getUserByUsername((String) input.get("username"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", customer.hasBoughtProduct(product));
            sendMessage(hashMap);
        }

        private void getOfferedProductsServer(HashMap input) {
            ArrayList<Product> productsToBeShown = OffersController.getFilteredList((ArrayList<String>) input.get("filters"));
            productsToBeShown = ProductsController.handleStaticFiltering(productsToBeShown, (String) input.get("categoryFilter"), (Double) input.get("priceLowFilter"),
                    (Double) input.get("priceHighFilter"), (String) input.get("brandFilter"), (String) input.get("nameFilter"), (String) input.get("sellerUsernameFilter"), (String) input.get("availabilityFilter"));
            productsToBeShown = ProductsController.filterProductsWithSearchQuery(productsToBeShown, (String) input.get("searchQuery"));
            productsToBeShown = ProductsController.sort((String) input.get("currentSort"), productsToBeShown);

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("content", HashMapGenerator.getListOfProducts(productsToBeShown));

            sendMessage(hashMap);
        }

        private void hasBeenRatedServer(HashMap input) {
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            Customer customer = (Customer) user;
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", product.hasBeenRatedBefore(customer));
            sendMessage(hashMap);
        }

        private void commentProductServer(HashMap input) {
            ProductController.addComment(Product.getProductByID(Integer.parseInt((String) input.get("id"))), user, (String) input.get("title"), (String) input.get("content"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void rateProductServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            try {
                CustomerUIController.rateProduct((Customer) user, Product.getProductByID(Integer.parseInt((String) input.get("id"))), (Double) input.get("currentRating"));
                hashMap.put("content", "Ok");
            }catch (Exception e){
                hashMap.put("content", "error");
                hashMap.put("type", e.getMessage());
            }
            sendMessage(hashMap);
        }

        private void getComparedProductServer(String productID) {
            Product product = Product.getProductByID(Integer.parseInt(productID));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getProductHashMap(product));
            sendMessage(hashMap);
        }

        private void getProductWithDifferentSellerServer(String productID, String username) {
            Product product = ProductController.getProductWithDifferentSeller(Product.getProductByID(Integer.parseInt(productID)), username);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getProductHashMap(product));
            sendMessage(hashMap);
        }

        private void getAllSellerOfProductServer(String productID) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfSellers(ProductController.getAllSellersOfProduct(Product.getProductByID(Integer.parseInt(productID)))));
            sendMessage(hashMap);
        }


        private void hasManagerServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Manager.hasManager);
            sendMessage(hashMap);
        }

        private void logoutServer() {
            TokenHandler.removeEntryByToken(token);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void moveShoppingCartAndLoginServer(String username) {
            User user = User.getUserByUsername(username);
            if (user instanceof Customer) {
                for (Product product : guest.getCart()) {
                    ((Customer) user).addToCart(product);
                }
                guest.getCart().clear();
                if (((Customer)user).getBankAccount() == 0) {
                    try {
                        ((Customer)user).setBankAccount((Integer)sendAndReceiveToBankAPICreateAccount());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (user instanceof Seller){
                if (((Seller)user).getBankAccount() == 0) {
                    try {
                        ((Seller)user).setBankAccount((Integer)sendAndReceiveToBankAPICreateAccount());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            TokenHandler.createToken(username);
            hashMap.put("content", TokenHandler.getTokenOfUsername(username));
            sendMessage(hashMap);
        }

        private void getAllFiltersServer(String categoryFilter) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", Product.getAllFilters(categoryFilter));
            sendMessage(hashMap);
        }

        private void getPriceLowServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ProductsController.getPriceLow());
            sendMessage(hashMap);
        }

        private void getPriceHighServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ProductsController.getPriceHigh());
            sendMessage(hashMap);
        }

        private void getProductsServer(HashMap input) {
            ;
            ArrayList<Product> productsToBeShown = ProductsController.getFilteredList((ArrayList<String>) input.get("filters"));
            productsToBeShown = ProductsController.handleStaticFiltering(productsToBeShown, (String) input.get("categoryFilter"), (Double) input.get("priceLowFilter"),
                    (Double) input.get("priceHighFilter"), (String) input.get("brandFilter"), (String) input.get("nameFilter"), (String) input.get("sellerUsernameFilter"), (String) input.get("availabilityFilter"));
            productsToBeShown = ProductsController.filterProductsWithSearchQuery(productsToBeShown, (String) input.get("searchQuery"));
            productsToBeShown = ProductsController.sort((String) input.get("currentSort"), productsToBeShown);

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("content", HashMapGenerator.getListOfProducts(productsToBeShown));
            sendMessage(hashMap);
        }

        private void getAuctionsServer(HashMap input) {
            ArrayList<Product> productsToBeShown = AuctionsController.getFilteredList((ArrayList<String>) input.get("filters"));
            productsToBeShown = AuctionsController.handleStaticFiltering(productsToBeShown, (String) input.get("categoryFilter"), (String) input.get("brandFilter"), (String) input.get("nameFilter"), (String) input.get("sellerUsernameFilter"), (String) input.get("availabilityFilter"));
            productsToBeShown = ProductsController.filterProductsWithSearchQuery(productsToBeShown, (String) input.get("searchQuery"));
            productsToBeShown = ProductsController.sort((String) input.get("currentSort"), productsToBeShown);

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("content", HashMapGenerator.getListOfProducts(productsToBeShown));
            sendMessage(hashMap);
        }

        private void getAllCategoriesServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfCategories(Manager.getAllCategories()));
            sendMessage(hashMap);
        }

        private void getUserByUsernameServer(String username) {
            HashMap<String, Object> hashMap = new HashMap<>();
            User user = User.getUserByUsername(username);
            if (user == null) {
                user = guest;
            }
            hashMap.put("content", HashMapGenerator.getUserHashMap(user));
            sendMessage(hashMap);
        }

        private void handleCreateManagerAccountServer(ArrayList<String> attributes) {
            HashMap<String, Object> hashMap = new HashMap<>();
            try {
                SignUpAndLoginController.createManager(attributes);
                hashMap.put("content", "Ok");
            }catch (Exception exception){
                hashMap.put("content", "error");
                hashMap.put("type", exception.getMessage());
            }
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
            HashMap<String, Object> hashMap = new HashMap<>();
            try {
                SignUpAndLoginController.handleCreateAccount(type, attributes);
                hashMap.put("content", "Ok");
            } catch (Exception exception) {
                hashMap.put("content", "error");
                hashMap.put("type", exception.getMessage());
            }
            sendMessage(hashMap);
        }

        private void sendMessage(HashMap<String, Object> hashMap) {
            if (!token.isEmpty()) {
                if (!TokenHandler.validateToken(token)) {
                    hashMap.put("tokenStatus", "expired");
                } else {
                    hashMap.put("tokenStatus", "ok");
                }
            } else {
                hashMap.put("tokenStatus", "ok");
            }
            try {
                System.out.println(hashMap.get("content"));
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                Gson gson = new Gson();

                dataOutputStream.writeUTF(gson.toJson(hashMap));
                dataOutputStream.flush();
            } catch (Exception exception) {
//
            }
        }

        private String sendAndReceiveToBankAPI(String type, double money, int source, int dest, String description) throws IOException {
            String output = type + " " + money + " " + source + " " + dest + " " + description;
            DataOutputStream dataOutputStream1 = new DataOutputStream(new BufferedOutputStream(bankAPISocket.getOutputStream()));
            DataInputStream dataInputStream1 = new DataInputStream(new BufferedInputStream(bankAPISocket.getInputStream()));
            dataOutputStream1.writeUTF(output);
            dataOutputStream1.flush();
            return (String)dataInputStream1.readUTF();
        }

    }
}
