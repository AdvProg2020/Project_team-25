package Store.Model;

import java.util.ArrayList;
import java.util.Date;

import static Store.Model.Enums.VerifyStatus.ACCEPTED;
import static Store.Model.Enums.VerifyStatus.REJECTED;

public class Manager extends User {

    private static ArrayList<Category> allCategories = new ArrayList<Category>();
    private static ArrayList<Request> pendingRequests = new ArrayList<Request>();
    private static ArrayList<OffCode> offCodes = new ArrayList<OffCode>();
    public static boolean hasManager = false;

    public Manager(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        if(!hasManager)
            allUsers.add(this);
        hasManager = true;
        this.type = "Manager";
    }

    public static void setAllCategories(ArrayList<Category> allCategories) {
        Manager.allCategories = allCategories;
    }

    public static void setPendingRequests(ArrayList<Request> pendingRequests) {
        Manager.pendingRequests = pendingRequests;
    }

    public static void setOffCodes(ArrayList<OffCode> offCodes) {
        Manager.offCodes = offCodes;
    }

    public static String showCategories()
    {
        String output = null;
        for(Category category: allCategories)
            if(category.getParent() == null)
                output += showAllOfCategory(category, 1) + "\n";
        return output;
    }

    private static String showAllOfCategory(Category category, int numOfTabs)
    {
        String output = null;
        output = putTabs(null, numOfTabs);
        output += "-" + category.getFullName() + "\n";
        for(Category subCategory: allCategories)
            if(subCategory.getParent() == category)
                output += showAllOfCategory(subCategory, numOfTabs + 1);
        return output;
    }

    private static String putTabs(String output, int numOfTabs)
    {
        for(int i = 0; i < numOfTabs; i++)
            output += "\t";
        return output;
    }
    public static boolean verifyOffCode(OffCode customerOffCode, Customer customer) {
        for (OffCode offCode : offCodes)
            if (offCode.isUserIncluded(customer))
                return true;
        return false;
    }

    public static OffCode getOffCodeByCode(String code)
    {
        for(OffCode offCode: offCodes)
            if(offCode.getCode().equals(code))
                return offCode;
        return null;
    }

    public static ArrayList<Request> getPendingRequests() {
        return pendingRequests;
    }

    //showing categories handled in controller
    public void editCategory(Category oldCategory, Category newCategory) {
        Manager.removeCategory(oldCategory);
        allCategories.add(newCategory);
    }

    public static Category categoryByName(String name)
    {
        for(Category category: allCategories)
            if(category.getFullName().equals(name))
                return category;
        return null;
    }

    public static void removeCategory(Category category) {
        category.removeInside();
        allCategories.remove(category);
    }

    public static Request getRequestById(int id) {
        for (Request request : pendingRequests) {
            if (id == request.getId()) {
                return request;
            }
        }
        return null;
    }

    public static void addRequest(Request request) {
        pendingRequests.add(request);
    }

    public void handleRequest(Request request, boolean accepted) {
        if (accepted) {
            Seller.doRequest(request);
            request.setStatus(ACCEPTED);  //????
        } else {
            request.setStatus(REJECTED);  //????
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
        for(User user: allUsers)
            if(user instanceof Customer)
                if(offCode.isUserIncluded(user))
                    ((Customer) user).removeOffCodeOfUser(offCode);
        offCodes.remove(offCode);
    }

    public void changeOffCode(OffCode offCode, OffCode newOffCode) {
        Manager.removeOffCode(offCode);
        offCodes.add(newOffCode);
    }

    public void addOffCode(OffCode offCode) {
        offCodes.add(offCode);
        offCode.setStartingTime(new Date());
    }

    public void addCategory(Category category) {
        allCategories.add(category);
    }

    public void removeProduct(Product product) {
        Offer.removeProductFromOffer(product);
        Manager.removeProductFromCatagory(product);
        Product.deleteProduct(product);
    }

    private static void removeProductFromCatagory(Product product) {
        for (Category category : allCategories) {
            if (category.include(product))
                category.removeProductFrom(product);
        }
    }

    public void deleteUser(User user) {
        user.delete();
    }

    public static ArrayList<Category> getAllCategories() {
        return allCategories;
    }

    @Override
    public boolean equals(Object object) {
        User user = (User) object;
        return(super.equals(user));
    }

    @Override
    public void delete() {
        allUsers.remove(this);
    }

}
