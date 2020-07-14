package Store.Networking;

import Store.Controller.*;
import Store.Model.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
        private Customer guest;
        private String token;
        private User user;

        private ClientThread(Socket socket) throws IOException {
            this.token = "";
            this.guest = new Customer("guest", "guest", "guest",
                    "guest@approject.com", "00000000000", "guest", 0.0);
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
                } catch (IOException exception) {
                    //exception.printStackTrace();
                }
            }
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
            ManagerController.editPersonalInfo(User.getUserByUsername((String) input.get("username")), (String) input.get("field"), (String) input.get("newValue"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ManagerController.editPersonalInfo(user, (String) input.get("field"), (String) input.get("newValue")));
            sendMessage(hashMap);
        }

        private void addToCartServer(HashMap input) {
            Product product = Product.getProductByID(Integer.parseInt((String) input.get("id")));
            Customer customer = (Customer) user;
            HashMap<String, Object> hashMap = new HashMap<>();
            if (customer == null) {
                guest.addToCart(product);
            } else {
                customer.addToCart(product);
            }
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
            Customer customer = (Customer) User.getUserByUsername((String) input.get("username"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", product.hasBeenRatedBefore(customer));
            sendMessage(hashMap);
        }

        private void commentProductServer(HashMap input) {
            ProductController.addComment(Product.getProductByID(Integer.parseInt((String) input.get("id"))), (Customer) User.getUserByUsername((String) input.get("username")), (String) input.get("title"), (String) input.get("content"));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", "Ok");
            sendMessage(hashMap);
        }

        private void rateProductServer(HashMap input) {
            CustomerController.rateProduct((Customer) User.getUserByUsername((String) input.get("username")), Product.getProductByID(Integer.parseInt((String) input.get("id"))), (Double) input.get("currentRating"));
//            check
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
            hashMap.put("content", HashMapGenerator.getUserHashMap(User.getUserByUsername(username)));
            sendMessage(hashMap);
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


    }
}
