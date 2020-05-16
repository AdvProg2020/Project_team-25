package Store.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static Store.Model.Enums.VerifyStatus.ACCEPTED;
import static Store.Model.Enums.VerifyStatus.REJECTED;

public class Manager extends User {

    private static ArrayList<Category> allCategories = new ArrayList<Category>();
    private static ArrayList<Request> pendingRequests = new ArrayList<Request>();
    private static ArrayList<OffCode> offCodes = new ArrayList<OffCode>();
    private static Date periodOffCodeDate;
    private static boolean periodOffTillNow;
    public static boolean hasManager = false;

    public Manager(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        if(!hasManager)
            allUsers.add(this);
        hasManager = true;
        this.type = "Manager";
    }

    public static void setPeriodOffCodeDate(Date nextPeriodOffCodeDate)
    {
        periodOffCodeDate = nextPeriodOffCodeDate;
    }

    public static Date getPeriodOffCodeDate()
    {
        return periodOffCodeDate;
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
        String output = "";
        for(Category category: allCategories)
            if(category.getParent() == null)
                output += showAllOfCategory(category, 1) + "\n";
        return output;
    }

    private static String showAllOfCategory(Category category, int numOfTabs)
    {
        String output = "";
        output = putTabs("", numOfTabs);
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

    public static void checkPeriodOffCode()
    {
        if (periodOffTillNow == false || !new Date().before(periodOffCodeDate)) {
            periodOffTillNow = true;
            int rand, numberOfCustomers = User.numberOfCustomers();
            Random random = new Random();
            ArrayList<Integer> helpRand = new ArrayList<>();
            if(numberOfCustomers < 3)
            {
                for(int i = 0; i < numberOfCustomers; i++)
                    helpRand.add(helpRand.size());
                for (int i = 0; i < numberOfCustomers; i++)
                    assignOffCodeToUser(OffCode.randomOffCode(10), User.findIndexOfNthCustomer(helpRand.get(i) + 1));
            }
            else {
                while (helpRand.size() <= 2)
                    if (!helpRand.contains(rand = random.nextInt(numberOfCustomers)))
                        helpRand.add(rand);
                for (int i = 0; i < 3; i++)
                    assignOffCodeToUser(OffCode.randomOffCode(10), User.findIndexOfNthCustomer(helpRand.get(i) + 1));
            }
            setPeriodOffCodeDate(new Date(new Date().getTime() + /*24*3600*/2*60*1000));
        }
    }

    public static ArrayList<Request> getPendingRequests() {
        return pendingRequests;
    }

    public static Category categoryByName(String name)
    {
        for(Category category: allCategories)
            if(category.getName().equals(name))
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
        if(!Manager.isInPendingRequests(request))
            pendingRequests.add(request);
    }

    public static boolean isInPendingRequests(Request request)
    {
        for(Request request1: pendingRequests)
            if(request1.equals(request))
                return true;
        return false;
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

    public static void addOffCode(OffCode offCode) {
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

    public static void assignOffCodeToUser(OffCode offcode, Customer customer)
    {
        customer.addOffCode(offcode);
        offcode.addUser(customer);
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
