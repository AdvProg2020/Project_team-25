package main.java.Store.Model;

import java.util.ArrayList;

import static main.java.Store.Model.Enums.VerifyStatus.ACCEPTED;

public class Manager extends User {

    public static ArrayList<Category> allCategories = new ArrayList<Category>();
    private static ArrayList<Request> pendingRequests = new ArrayList<Request>();
    private static ArrayList<OffCode> offCodes = new ArrayList<OffCode>();
    public static boolean hasManager = false;

    Manager(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        if(!hasManager)
            allUsers.add(this);
        hasManager = true;
    }

    public static boolean verifyOffCode(OffCode customerOffCode, Customer customer)
    {
        for(OffCode offCode: offCodes)
            if(offCode.isUserIncluded(customer))
                return true;
        return false;
    }

    public static ArrayList<Request> getPendingRequests() {
        return pendingRequests;
    }

    //showing categories handled in controller
    public void editCategory(Category oldCategory, Category newCategory)
    {
        allCategories.remove(oldCategory);
        allCategories.add(newCategory);
    }

    public void removeCategory(Category category)
    {
        category.removeInside();
        allCategories.remove(category);
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
            Seller.doRequest(product);
            request.setStatus(ACCEPTED);  //????
        }
        else {
            request.setStatus(ACCEPTED);  //????
        }
        pendingRequests.remove(request);
    }

    public void addNewManager(Manager manager) {
        allUsers.add(manager);
    }

    public static ArrayList<OffCode> getOffCodes() {
        return offCodes;
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
