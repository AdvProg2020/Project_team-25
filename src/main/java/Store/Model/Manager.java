package main.java.Store.Model;

import java.util.ArrayList;

public class Manager extends User {

    private static ArrayList<Category> allCategories = new ArrayList<Category>();
    private static ArrayList<Request> pendingRequests = new ArrayList<Request>();
    private static ArrayList<OffCode> offCodes = new ArrayList<OffCode>();
    public static boolean hasManager = false;

    Manager(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        allUsers.add(this);
        hasManager = true;
    }

    public String viewUser(User user)
    {
        String returnString = null;
        returnString = user.viewPersonalInfo();
        if(user instanceof Customer)
            returnString += "\nmoney:" + ((Customer) user).getMoney();
        else if(user instanceof Seller) {
            returnString += "\nmoney:" + ((Seller) user).getMoney();
            returnString += "\ncompany name:" + ((Seller) user).getCompanyName();
            returnString += "\ncompany discription:" + ((Seller) user).getCompanyDiscription();
        }
         return returnString;
    }

    public String viewRequests()
    {
        String returnString = null;
        for(Request request: pendingRequests)
        {
            returnString += getRequestType() + "\n";
        }
        return returnString;
    }

    public static Request getRequestById(int id)
    {
        for(Request request: pendingRequests)
            if(id == request.getId())
                return request;
        return null;
    }

    public static void addRequest(Request request)
    {
        pendingRequests.add(request);
    }
    public void handleRequest(Request request, boolean accepted)
    {
        if(accepted)
        {

        }
        else
        {

        }
    }

    public void addNewManager(Manager manager) {
        //nothing needed
    }

    public static String viewAllOffCodes()
    {
        String returnString = null;
        for(OffCode offCode: offCodes)
        {
            returnString += offCode.getCode() + "\n";
        }
        return returnString;
    }

    public static void removeOffCode(OffCode offCode) {
        offCode.remove();
        offCodes.remove(offCode);
    }

    public void changeOffCode(OffCode offCode, OffCode newOffCode) {
        offCodes.remove(offCode);
        offCodes.add(newOffCode);
    }

    public void addOffCode(OffCode offCode) {
        offCodes.add(offCode);
    }

    public void addCategory(Category category) {
        allCategories.add(category);
    }

    public void removeProduct(Product product)
    {
        Offer.removeProductFromOffer(product);
        Manager.removeProductFromCatagory(product);
        Product.deleteProduct(product);
    }

    private static void removeProductFromCatagory(Product product)
    {
        for(Category category: allCategories)
        {
            if( category.include(product) )
                category.removeProductFrom(product);
        }
    }

    public void deleteUser(User user) {
        user.delete();
    }

    @Override
    public void delete()
    {
        allUsers.remove(this);
    }
}
