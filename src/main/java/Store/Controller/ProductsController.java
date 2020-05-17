package Store.Controller;

import Store.InputManager;
import Store.Model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ProductsController {

    public static ArrayList<Product> getFilteredList(ArrayList<String> filters) {
        ArrayList<Product> products = Product.getAllProducts();
        try {
            return products.stream()
                    .filter(product -> product.getFilters().containsAll(filters))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        catch(Exception exception){
            return products;
        }
    }

    public static ArrayList<Product> handleStaticFiltering(ArrayList<Product> products, String category, double priceLow, double priceHigh, String brand, String name, String sellerUsername, String availability) {
        ArrayList<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (!category.equalsIgnoreCase("null") && !product.getCategory().getName().equalsIgnoreCase(category)) {
                continue;
            }
            if (priceLow >= 0 || priceHigh >= 0) {
                if (priceLow >= 0 && priceHigh >= 0) {
                    if (!(priceLow <= product.getPrice() && product.getPrice() <= priceHigh)) {
                        continue;
                    }
                }
                else if (priceHigh >= 0) {
                    if (!(product.getPrice() <= priceHigh)) {
                        continue;
                    }
                }
                else {
                    if (!(priceLow <= product.getPrice())) {
                        continue;
                    }
                }
            }
            if (!brand.equalsIgnoreCase("null") && !product.getBrand().equalsIgnoreCase(brand)) {
                continue;
            }
            if (!name.equalsIgnoreCase("null") && !product.getName().contains(name)) {
                continue;
            }
            if (!sellerUsername.equalsIgnoreCase("null") && !product.getSeller().getUsername().equalsIgnoreCase(sellerUsername)) {
                continue;
            }
            if (!availability.equalsIgnoreCase("null")) {
                if (availability.equalsIgnoreCase("1") && !product.getAvailablity()) {
                    continue;
                }
                if (availability.equalsIgnoreCase("0") && product.getAvailablity()) {
                    continue;
                }
            }
            result.add(product);
        }
        return result;
    }

    public static ArrayList<Product> sort(String currentSort, ArrayList<Product> products)
    {
        /*extra: sort by off */
        if(currentSort.equalsIgnoreCase("rating"))
            return sortByRating(products);
        else if(currentSort.equalsIgnoreCase("lexicographical"))
            return sortByLexicographical(products);
        else if(currentSort.equalsIgnoreCase("visit"))
            return sortByVisit(products);
        else if(currentSort.equalsIgnoreCase("price"))
            return sortByPrice(products);
        else
            return products;
    }

    private static ArrayList<Product> sortByRating(ArrayList<Product> products)
    {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getAverageRating))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        catch (Exception exception)
        {
            return products;
        }
    }
    private static ArrayList<Product> sortByPrice(ArrayList<Product> products)
    {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        catch (Exception exception)
        {
            return products;
        }
    }
    private static ArrayList<Product> sortByLexicographical(ArrayList<Product> products)
    {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        catch (Exception exception)
        {
            return products;
        }
    }
    private static ArrayList<Product> sortByVisit(ArrayList<Product> products)
    {
        try {
            return products.stream()
                    .sorted(Comparator.comparing(Product::getVisited))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        catch (Exception exception)
        {
            return products;
        }
    }

    public static ArrayList<Product> filterProductsWithSearchQuery(ArrayList<Product> productsToBeShown, String query) {
        ArrayList<Product> result = new ArrayList<Product>();
        for (Product product : productsToBeShown) {
            if (InputManager.getMatcher(product.getName(), query).find()) {
                result.add(product);
            }
        }
        return result;
    }
}
