package Store.View;

import Store.Controller.ProductController;
import Store.InputManager;
import Store.Model.Comment;
import Store.Model.Customer;
import Store.Model.Product;
import Store.Model.Seller;
import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ProductMenu {

    private static String sellerName = "";
    private static final int COLUMN_COUNT = 100;

    private static final String SELECT_SELLER_REGEX = "^select seller ([^\\s]+)$";
    private static final String COMPARE_PRODUCT_REGEX = "^compare (\\d+|[^\\s]+)$";

    public static void init(Product product) {
        product.setBrand("");
        String input;
        Matcher matcher;
        System.out.println("\nProduct menu of: " + product.getName() + "\n");
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("digest")) {
                digest(product);
                System.out.println("\nProduct menu of: " + product.getName() + "\n");
            }
            else if (input.equalsIgnoreCase("attributes")) {
                viewAttributes(product);
            }
            else if ((matcher = InputManager.getMatcher(input, COMPARE_PRODUCT_REGEX)).find()) {
                compareTo(product, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("comments")) {
                showComments(product);
                System.out.println("\nProduct menu of: " + product.getName() + "\n");
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nProduct menu of: " + product.getName() + "\n");
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else if (input.equalsIgnoreCase("help")) {
                printHelp();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static String getSellerNameTextList(Product product) {
        ArrayList<Seller> allSellersOfProduct = new ArrayList<>();
        allSellersOfProduct = ProductController.getAllSellersOfProduct(product);
        String result = "";
        for (Seller seller : allSellersOfProduct) {
            result = result.concat(seller.getUsername() + "   ");
        }
        return result;
    }

    private static void digest(Product product) {
        System.out.println("\nProduct menu of: " + product.getName() + " -> digest\n");
        System.out.println("Product Name: " + product.getName());
        System.out.println("Product Brand: " + product.getBrand());
        System.out.println("Description: " + product.getDescription());
        System.out.println("Price: " + product.getPrice());
        if(product.getCategory() != null)
            System.out.println("Category: " + product.getCategory().getFullName());
        else
            System.out.println("Category: empty");
        System.out.print("Sellers: " + getSellerNameTextList(product));
        /*ArrayList<Seller> allSellersOfProduct = ProductController.getAllSellersOfProduct(product);
        for (Seller seller : allSellersOfProduct) {
            System.out.print(seller.getName() + "   ");
        }*/
        System.out.println();
        System.out.println("Current seller: " + product.getSeller().getUsername());
        System.out.println("Average rating: " + product.getAverageRating());
        System.out.println("Date added: " + product.getStartingDate());
        if (!product.getAvailablity()) {
            System.out.printf("--- This product is not available");
        }
        System.out.println();

        String input;
        Matcher matcher;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("add to cart")) {
                addToCart(product);
            }
            else if ((matcher = InputManager.getMatcher(input, SELECT_SELLER_REGEX)).find()) {
                product = selectSeller(product, matcher.group(1));
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nProduct menu of: " + product.getName() + " -> digest\n");
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void addToCart(Product product) {
        if (MainMenu.currentUser instanceof Customer) {
            ((Customer) MainMenu.currentUser).addToCart(product);
        }
        else {
            System.out.println("Only customer type accounts can add products to their cart!");
        }
    }

    private static Product selectSeller(Product product, String attribute) {
        Product newProduct = ProductController.getProductWithDifferentSeller(product, attribute);
        if (Seller.getUserByUsername(attribute) == null) {
            System.out.println("No user with this username exists!");
        }
        else if (newProduct == null) {
            System.out.println("This product does not have a seller with the given name!");
            return product;
        }
        return newProduct;
    }

    private static void compareTo(Product product, String attribute) {
        Product other;
        if (StringUtils.isNumeric(attribute)) {
            other = Product.getProductByID(Integer.parseInt(attribute));
        }
        else {
            other = Product.getProductByName(attribute);
        }
        if (other == null) {
            System.out.println("There is no product with the attribute you entered!");
        }
        else if (other.getCategory() != product.getCategory()) {
            System.out.println("To compare to products, they must be from the same category!");
        }
        else {
            System.out.println("---------- Comparison ----------");
            printAlongside("Product Name: " + product.getName(), "Product Name: " + other.getName());
            printAlongside("Product Brand: " + product.getBrand(), "Product Brand: " + other.getBrand());
            printAlongside("Description: " + product.getDescription(), "Description: " + other.getDescription());
            printAlongside("Price: " + product.getPrice(), "Price: " + other.getPrice());
            if (product.getCategory() == null && other.getCategory() == null)
                printAlongside("Category: empty", "Category: empty");
            else if(product.getCategory() == null)
                printAlongside("Category: empty", "Category: " + other.getCategory().getFullName());
            else if(other.getCategory() == null)
                printAlongside("Category: " + product.getCategory().getFullName(), "Category: empty");
            else
                printAlongside("Category: " + product.getCategory().getFullName(), "Category: " + other.getCategory().getFullName());
            String firstString = getSellerNameTextList(product), secondString = getSellerNameTextList(other);
            printAlongside("Sellers: " + firstString, "Sellers: " + secondString);

            printAlongside("Average rating: " + product.getAverageRating(), "Average rating: " + other.getAverageRating());
            printAlongside("Date added: " + product.getStartingDate(), "Date added: " + other.getStartingDate());
        }
    }

    public static void printAlongside(String firstString, String secondString) {
        int len = Math.max(firstString.length(), secondString.length());
        String firstPart = "", secondPart = "";
        for (int character = 0; character < len; character++) {
            if (character < firstString.length()) {
                firstPart = firstPart.concat("" + firstString.charAt(character));
            }
            else {
                firstPart = firstPart.concat(" ");
            }

            if (character < secondString.length()) {
                secondPart = secondPart.concat("" + secondString.charAt(character));
            }
            else {
                secondPart = secondPart.concat(" ");
            }

            if (firstPart.length() == COLUMN_COUNT && secondPart.length() == COLUMN_COUNT) {
                System.out.println(firstPart + " | " + secondPart);
                firstPart = "";
                secondPart = "";
            }
        }
        if (firstPart.length() != 0) {
            while (firstPart.length() < COLUMN_COUNT) {
                firstPart = firstPart.concat(" ");
            }
            while (secondPart.length() < COLUMN_COUNT) {
                secondPart = secondPart.concat(" ");
            }
            System.out.printf("%s | %s\n", firstPart, secondPart);
        }
        for (int column = 0; column < 2 * COLUMN_COUNT + 3; column++) {
            System.out.print("_");
        }
        System.out.println();
    }

    private static void viewAttributes(Product product) {
        System.out.println(product.getDescription());
        ArrayList<String> filters = product.getFilters();
        System.out.print("Filters: ");
        for (String filter : filters) {
            System.out.print(filter + "   ");
        }
        System.out.println();
        filters = product.getCategory().getFilters();
        System.out.print("Category filters: ");
        for (String filter : filters) {
            System.out.print(filter + "   ");
        }
        System.out.println();
        System.out.println("Price: " + product.getPrice());
        System.out.println("Date added: " + product.getStartingDate());
    }

    private static void addComment(Product product) {
        System.out.print("Title: ");
        String title = InputManager.getNextLine();
        System.out.println("Content: ");
        String content = InputManager.getNextLine();
        ProductController.addComment(product, MainMenu.currentUser, title, content);
    }

    private static void showComments(Product product) {
        System.out.println("\nProduct menu of: " + product.getName() + " -> Show Comments\n");
        System.out.println("Comments for product " + product.getName() + " with seller " + product.getSeller().getName() + ": ");
        for (Comment comment : product.getComments()) {
            for (int column = 0; column < COLUMN_COUNT; column++) {
                System.out.print("_");
            }
            System.out.println("");
            System.out.printf("%150s %50s", "Commenting user: " + comment.getCommentingUser().getName(),
                    (comment.getHasBought() ? "--Has bought this product" : "--Has not bought this product"));
            System.out.println("---> " + comment.getCommentText());
        }
        for (int column = 0; column < COLUMN_COUNT; column++) {
            System.out.print("_");
        }
        System.out.println("");

        String input;
        while (!(input = InputManager.getNextLine()).equalsIgnoreCase("back")) {
            if (input.equalsIgnoreCase("add comment")) {
                addComment(product);
            }
            else if (input.equalsIgnoreCase("login")) {
                handleLogin();
                System.out.println("\nProduct menu of: " + product.getName() + " -> Show Comments\n");
            }
            else if (input.equalsIgnoreCase("logout")) {
                handleLogout();
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    private static void printHelp() {
        System.out.println("*******\n");
        System.out.println("List of main commands: ");
        System.out.println("digest");
        System.out.println("attributes");
        System.out.println("compare [productID | productName]");
        System.out.println("comments");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("help");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the digest submenu: ");
        System.out.println("add to cart");
        System.out.println("select seller [seller_username]");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");

        System.out.println("\nList of commands in the comments submenu: ");
        System.out.println("add comment");
        System.out.println("login");
        System.out.println("logout");
        System.out.println("back");
        System.out.println("*******");
    }

    private static void handleLogin() {
        if (MainMenu.currentUser == MainMenu.guest) {
            SignUpAndLoginMenu.init();
            if (MainMenu.currentUser != MainMenu.guest) {
                moveShoppingCart();
            }
        } else {
            System.out.println("You have signed in!");
        }
    }

    private static void moveShoppingCart() {
        if (MainMenu.currentUser instanceof Customer) {
            for (Product product : MainMenu.guest.getCart()) {
                ((Customer) MainMenu.currentUser).addToCart(product);
            }
        }
        MainMenu.guest.getCart().clear();
    }

    private static void handleLogout() {
        if (MainMenu.currentUser == MainMenu.guest) {
            System.out.println("You haven't signed in!");
        } else {
            MainMenu.currentUser = MainMenu.guest;
            MainMenu.init();
        }
    }
}
