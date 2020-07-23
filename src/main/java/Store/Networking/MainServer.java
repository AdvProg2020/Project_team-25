package Store.Networking;

import Store.Controller.*;
import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
                        if (input.get("message").equals("validatePassword")) {
                            validatePasswordServer(input);
                        }
                        if (input.get("message").equals("canOfferBeUsedInDate")) {
                            canOfferBeUsedInDateServer(input);
                        }
                        if (input.get("message").equals("editCustomerPersonalInfo")) {
                            editCustomerPersonalInfoServer(input);
                        }
                        if (input.get("message").equals("canBuyWithoutOffCode")) {
                            canBuyWithoutOffCodeServer();
                        }
                        if (input.get("message").equals("canBuyWithOffCode")) {
                            canBuyWithOffCodeServer(input);
                        }
                        if (input.get("message").equals("buyWithoutOffCode")) {
                            buyWithoutOffCodeServer();
                        }
                        if (input.get("message").equals("buyWithOffCode")) {
                            buyWithOffCodeServer(input);
                        }
                        if (input.get("message").equals("removeProductFromCart")) {
                            removeProductFromCartServer(input);
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
                        if (input.get("message").equals("createOperator")) {
                            createOperatorServer(input);
                        }
                    }
                } catch (IOException exception) {
                    //exception.printStackTrace();
                }
            }
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

        private void buyWithOffCodeServer(HashMap input) {
            ((Customer) user).buy(Manager.getOffCodeByCode((String) input.get("code")));
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ((Customer) user).getNewFactor());
            sendMessage(hashMap);
        }

        private void buyWithoutOffCodeServer() {
            ((Customer) user).buy();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ((Customer) user).getNewFactor());
            sendMessage(hashMap);
        }

        private void canBuyWithOffCodeServer(HashMap input) {
            OffCode offCode = Manager.getOffCodeByCode((String) input.get("code"));
            HashMap<String, Object> hashMap = new HashMap<>();
            if (offCode != null && offCode.canBeUsedInDate(new Date()) && offCode.isUserIncluded((Customer) user)) {
                hashMap.put("content", ((Customer) user).canBuy());
            } else {
                hashMap.put("content", false);
            }
            sendMessage(hashMap);
        }

        private void canBuyWithoutOffCodeServer() {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("content", ((Customer) user).canBuy());
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
            Category category = Manager.categoryByName((String) input.get("name"));
            synchronized (category) {
                ManagerController.editCategory((Manager) user, category, (String) input.get("field"), (String) input.get("newValue"));
            }
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
            if (Manager.hasManager) {
                ((Manager) user).addNewManager(new Manager(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5)));
            } else {
                new Manager(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4), attributes.get(5));
            }
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
