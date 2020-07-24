package Store.Networking;
/*
22347

@echo off
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_202jdk-14
set JAVA_HOME=C:\Program Files\Java\jdk-14
set Path=%JAVA_HOME%\bin;%Path%
@echo on
 */
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class MainServer {
    private final HashMap<Integer, Integer> accounts = new HashMap<>();
    private ServerSocket serverSocket;
    private int port;
    private static Socket bankAPISocket;
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
//        this.port = nextFreePort(9000, 20000);
        this.port = 15050;

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

        Thread checkAuctionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Auction.getAllAuctions());
                    for (int i = 0; i < Auction.getAllAuctions().size(); i++)
                    {
                        System.out.println(Auction.getAllAuctions());
                        if ((LocalDateTime.now()).isAfter(Auction.getAllAuctions().get(i).getEndingTime()))
                        {
                            Auction.getAllAuctions().get(i).finish();
                            System.out.println(Auction.getAllAuctions());
                        }
                        System.out.println(Auction.getAllAuctions());
                    }
                }
            }
        });
        checkAuctionThread.setPriority(1);
        checkAuctionThread.start();
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

    public static Object sendAndReceiveToBankAPICreateAccount() throws Exception {
        String output = "createAccount";
        dataOutputStream1.writeUTF(output);
        dataOutputStream1.flush();
        String input = dataInputStream1.readUTF();
        System.out.println(input);
        if (Pattern.matches("\\d+", input))
            return Integer.parseInt(input);
        else
            throw new Exception(input);
    }
    public static Object sendAndReceiveToBankAPIBalance() throws IOException {
        String output = "balance";
        dataOutputStream1.writeUTF(output);
        dataOutputStream1.flush();
        String input = dataInputStream1.readUTF();
        if (Pattern.matches("\\d+(\\.\\d+)?", input))
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
                    System.out.println("Token: " + token + "Command: " + input.get("message"));
                    synchronized (user) {
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
                            createOperatorServer(input);
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
                        if (input.get("message").equals("getAuctionMessages")){
                            getAuctionMessages(input);
                        }
                        if (input.get("message").equals("writeInChat")){
                            writeInChat(input);
                        }
                    }
                } catch (IOException exception) {
                    //exception.printStackTrace();
                }
            }
        }

        private void writeInChat(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            double id = ((Double)input.get("auctionId"));
            Auction auction = Auction.getAuctionByID((int) id);
            String message = (String)input.get("username") + ": " + (String)input.get("chatMessage");
            auction.getMessages().add(message);
            System.out.println(auction.getMessages());
            sendMessage(hashMap);
        }

        private void getAuctionMessages(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            double id = ((Double)input.get("auctionId"));
            Auction auction = Auction.getAuctionByID((int) id);
            hashMap.put("content", auction.getMessages());
            sendMessage(hashMap);
        }

        private void purchaseByBank(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Customer customer = (Customer)User.getUserByUsername((String) input.get("username"));
            String offCode = (String)input.get("offCode");
            String address = (String)input.get("address");
            if (offCode == null)
                offCode = "";
            if (address == null)
                address = "";
            try {
                hashMap.put("content", CustomerUIController.purchase(customer, offCode, true, address));
            } catch (Exception e) {
                System.out.println(e);
                hashMap.put("content", "error");
                hashMap.put("type", e.getMessage());
            }
            sendMessage(hashMap);
        }

        //karmozd?
        private void purchaseByWallet(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Customer customer = (Customer) user;
            System.out.println("Step 1");
            String offCode = (String)input.get("offCode");
            String address = (String)input.get("address");
            if (offCode == null)
                offCode = "";
            if (address == null)
                address = "";
            try {
                hashMap.put("content", CustomerUIController.purchase(customer, offCode, false, address));
                System.out.println("Step 2-1");
            } catch (Exception e) {
                System.out.println("Step 2-2");
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
            if (!result.equalsIgnoreCase("done")) {
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

        private void dechargeWallet(HashMap input) throws IOException {
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

        private void increaseAuctionPrice(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            int id = (Integer.parseInt((String)input.get("productId")));
            Product product = Product.getProductByID(id);
            Auction auction = Auction.getAuctionOfProduct(product);
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

        private void getAuctionOfProduct(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID(Integer.parseInt((String)input.get("productId")));
            Auction auction = Auction.getAuctionOfProduct(product);
            if (auction != null)
                hashMap.put("content", HashMapGenerator.getAuctionHashMap(auction));
            else
                hashMap.put("content", "error");
            sendMessage(hashMap);
        }

        private void getConditionAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            int id = (Integer.parseInt((String)input.get("productId")));
            Product product = Product.getProductByID(id);
            if (Auction.getAuctionOfProduct(product) == null)
                hashMap.put("content", "Sold");
            else
                hashMap.put("content", "In Progress");
            sendMessage(hashMap);
        }

        private void getCurrentBuyerAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            double id = ((Double)input.get("auctionId"));
            Auction auction = Auction.getAuctionByID((int) id);
            if (auction.getCurrentBuyer() == null)
                hashMap.put("content", "NOBODY");
            else
                hashMap.put("content", auction.getCurrentBuyer().getUsername());
            sendMessage(hashMap);
        }

        private void getHighestPriceAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            double id = ((Double)input.get("auctionId"));
            Auction auction = Auction.getAuctionByID((int) id);
            hashMap.put("content", auction.getHighestPrice());
            sendMessage(hashMap);
        }

        private void isInAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID(Integer.parseInt((String)((Map)input.get("product")).get("id")));
            if (Auction.getAuctionOfProduct(product) == null)
                hashMap.put("content", "false");
            else
                hashMap.put("content", "true");
            sendMessage(hashMap);
        }

        synchronized private void addAuction(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID(Integer.parseInt((String)(((Map)input.get("product")).get("id"))));
            double year = (Double) input.get("year");
            double month = (Double) input.get("month");
            double day = (Double) input.get("day");
            double hour = (Double) input.get("hour");
            double minute = (Double) input.get("minute");
            double second = (Double) input.get("second");
            LocalDateTime dateTime = LocalDateTime.of((int)year, (int)month, (int)day, (int)hour, (int)minute, (int)second);
            try {
                SellerUIController.addAuction(product, dateTime);
                hashMap.put("content", "done");
            }catch (Exception e){
                hashMap.put("content", "error");
                hashMap.put("type", e.getMessage());
            }
            sendMessage(hashMap);
        }

        private void getAuctionsServer(HashMap input) {
            ArrayList<Product> productsToBeShown = Auction.getAllAuctionsProducts();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("content", HashMapGenerator.getListOfProducts(productsToBeShown));
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

        synchronized private void sendProduct(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            BuyLogItem buyLogItem = (BuyLogItem)LogItem.getLogById(Integer.parseInt((String)input.get("buyLogId")));
            buyLogItem.setReceived(true);
            hashMap.put("content", "done");
            sendMessage(hashMap);
        }

        private void getBuyLogs() {
            HashMap<String, Object> hashMap = new HashMap<>();
            ArrayList<BuyLogItem> arrayList = new ArrayList<>();
            for (LogItem logItem: LogItem.getAllLogItems()){
                if (logItem instanceof BuyLogItem){
                    arrayList.add((BuyLogItem) logItem);
                }
            }
            hashMap.put("content", HashMapGenerator.getListOfBuyLogItems(arrayList));
            sendMessage(hashMap);
        }

        private void isReceivedLog(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            int id = Integer.parseInt((String)input.get("logId"));
            if (LogItem.getLogById(id) != null)
                hashMap.put("content", ((BuyLogItem) LogItem.getLogById(id)).isReceived());
            else
                hashMap.put("content", "error");
        }

        private void createOperatorServer(HashMap input) {
            ManagerController.createOperatorProfile((Manager) user, (String) input.get("username"), (String) input.get("firstName"), (String) input.get("lastName"), (String) input.get("email"), (String) input.get("phoneNumber"), (String) input.get("password"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void getOffersOfThisSellerServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", HashMapGenerator.getListOfOffers(SellerController.getOffersOfThisSeller((Seller) user)));
            sendMessage(hashMap);
        }

        private void editProductServer(HashMap input) {
            Map<String, Object> productHashMap = (Map<String, Object>) input.get("product");
            Product newProduct = new Product(CheckingStatus.CREATION, Manager.categoryByName((String) productHashMap.get("category")), (String) productHashMap.get("name"), (Seller) user, (String) productHashMap.get("brand"), (Double) productHashMap.get("price"), (Boolean) productHashMap.get("availability"), (String) productHashMap.get("description"));
            System.out.println("READING FILE PATH: " + (String) productHashMap.get("filePath"));
            newProduct.setFilePath((String) productHashMap.get("filePath"));
            for (String filterToAdd : (List<String>) productHashMap.get("filters")) {
                newProduct.addFilter(filterToAdd);
            }
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            synchronized (product) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("content", SellerController.editProduct((Seller) user, Product.getProductByID(Integer.parseInt((String) input.get("id"))), newProduct));
                sendMessage(hashMap);
            }
        }

        private void editOfferServer(HashMap input) {
            Map<String, Object> offerHashMap = (Map<String, Object>) input.get("offer");
            Offer newOffer = new Offer(user, CheckingStatus.CREATION, (Double) offerHashMap.get("offPercentage"));
            newOffer.setStartingTime(new Date(Long.parseLong((String) offerHashMap.get("startingTime"))));
            newOffer.setEndingTime(new Date(Long.parseLong((String) offerHashMap.get("endingTime"))));
            for (String product : (List<String>) offerHashMap.get("products")) {
                newOffer.addProduct(Product.getProductByID(Integer.parseInt(product)));
            }
            Offer offer = Offer.getOfferByID(Integer.parseInt((String) input.get("id")));
            synchronized (offer) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("content", SellerController.editOff((Seller) user, Offer.getOfferByID(Integer.parseInt((String) input.get("id"))), newOffer));
                sendMessage(hashMap);
            }
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
            Offer offer = new Offer(user, CheckingStatus.CREATION, (Double) offerHashMap.get("offPercentage"));
//            offer.setStartingTime((Date) offerHashMap.get("startingTime"));
//            offer.setEndingTime((Date) offerHashMap.get("endingTime"));
            offer.setStartingTime(new Date(Long.parseLong((String) offerHashMap.get("startingTime"))));
            offer.setEndingTime(new Date(Long.parseLong((String) offerHashMap.get("endingTime"))));
            for (String product : (List<String>) offerHashMap.get("products")) {
                synchronized (product) {
                    offer.addProduct(Product.getProductByID(Integer.parseInt(product)));
                }
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
            synchronized (offer) {
                Product product = Product.getProductByID(Integer.parseInt((String) input.get("productId")));
                synchronized (product) {
                    offer.addProduct(product);
                }
            }
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
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            synchronized (product) {
                hashMap.put("content", SellerController.addFilterToProduct((Seller) user, (String) input.get("id"), (String) input.get("filter")));
                sendMessage(hashMap);
            }

        }

        private void addAdsServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", SellerController.addAds((Seller) user, (int) Math.round((Double) input.get("id"))));
            sendMessage(hashMap);
        }

        private void removeFilterFromSellerServer(HashMap input) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            synchronized (product) {
                hashMap.put("content", SellerController.removeFilterFromProduct((Seller) user, (String) input.get("id"), (String) input.get("filter")));
                sendMessage(hashMap);
            }
        }

        private void removeProductFromSellerServer(HashMap input) {
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            synchronized (product) {
                SellerController.removeProduct((Seller) user, product);
            }
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

//        private void buyWithOffCodeServer(HashMap input) {
//            ((Customer) user).buy(Manager.getOffCodeByCode((String) input.get("code")));
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("content", ((Customer) user).getNewFactor());
//            sendMessage(hashMap);
//        }

//        private void buyWithoutOffCodeServer() {
//            ((Customer) user).buy();
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("content", ((Customer) user).getNewFactor());
//            sendMessage(hashMap);
//        }

//        private void canBuyWithOffCodeServer(HashMap input) {
//            OffCode offCode = Manager.getOffCodeByCode((String) input.get("code"));
//            HashMap<String, Object> hashMap = new HashMap<>();
//            if (offCode != null && offCode.canBeUsedInDate(new Date()) && offCode.isUserIncluded((Customer) user)) {
//                hashMap.put("content", ((Customer) user).canBuy());
//            } else {
//                hashMap.put("content", false);
//            }
//            sendMessage(hashMap);
//        }

//        private void canBuyWithoutOffCodeServer() {
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("content", ((Customer) user).canBuy());
//            sendMessage(hashMap);
//        }

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
            Request request = Manager.getRequestById(Integer.parseInt((String) input.get("id")));
            synchronized (request) {
                ManagerController.handleRequest((Manager) user, (Boolean) input.get("status"), Manager.getRequestById(Integer.parseInt((String) input.get("id"))));
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void removeRequestsServer(HashMap input) {
            Request request = Manager.getRequestById(Integer.parseInt((String) input.get("id")));
            synchronized (request) {
                Manager.removeRequest(request);
            }
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
            Category category = Manager.categoryByName((String) input.get("name"));
            synchronized (category) {
                ManagerController.editCategory((Manager) user, category, (String) input.get("field"), (String) input.get("newValue"));
            }
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
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            synchronized (product) {
                ProductController.addComment(product, user, (String) input.get("title"), (String) input.get("content"));
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void rateProductServer(HashMap input) {
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            synchronized (product) {
                CustomerController.rateProduct((Customer) user, product, (Double) input.get("currentRating"));
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (user instanceof Seller){
                if (((Seller)user).getBankAccount() == 0) {
                    try {
                        ((Seller)user).setBankAccount((Integer)sendAndReceiveToBankAPICreateAccount());
                    } catch (Exception e) {
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
            if (Manager.hasManager)
            {
                ((Manager)user).addNewManager(new Manager(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5)));
                hashMap.put("content", "Ok");
            }
            else {
                try {
                    SignUpAndLoginController.handleCreateAccount("manager", attributes);
                    hashMap.put("content", "Ok");
                } catch (Exception exception) {
                    hashMap.put("content", "error");
                    hashMap.put("type", exception.getMessage());
                }
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
            try {
                SignUpAndLoginController.handleCreateAccount(type, attributes);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
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
            return (String) dataInputStream1.readUTF();
        }
    }
}
