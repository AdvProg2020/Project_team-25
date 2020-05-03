package Store.Controller;

import Store.Model.Product;
import Store.Model.Seller;
import Store.Model.User;

import java.util.ArrayList;

public class ProductController {
    public static void addComment(Product product, User user, String title, String content) {
        product.addComment(user, title, content);
    }

    public static ArrayList<Seller> getAllSellersOfProduct(Product product) {
        ArrayList<Seller> sellers = new ArrayList<Seller>();
        for (Product currentProduct : Product.getAllProducts()) {
            if (product.equals(currentProduct)) {
                sellers.add(currentProduct.getSeller());
            }
        }
        return sellers;
    }

    public static Product getProductWithDifferentSeller(Product product, String sellerUsername) {
        for (Product currentProduct : Product.getAllProducts()) {
            if (product.equals(currentProduct) && currentProduct.getSeller().getUsername().equals(sellerUsername)) {
                return currentProduct;
            }
        }
        return null;
    }
}
