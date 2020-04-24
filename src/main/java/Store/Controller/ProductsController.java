package Store.Controller;

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
    public static ArrayList<Product> sort(String currentSort, ArrayList<Product> products)
    {
        /*extra: sort by off */
        if(currentSort.equalsIgnoreCase("rating"))
            return sortByRating(products);
        else if(currentSort.equalsIgnoreCase("lexicographical"))
            return sortByLexiographical(products);
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
    private static ArrayList<Product> sortByLexiographical(ArrayList<Product> products)
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
}
