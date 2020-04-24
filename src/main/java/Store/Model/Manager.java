package Store.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static Store.Model.Enums.VerifyStatus.ACCEPTED;
import static Store.Model.Enums.VerifyStatus.REJECTED;

public class Manager extends User {

    private static ArrayList<Category> allCategories = new ArrayList<Category>();
    private static ArrayList<Request> pendingRequests = new ArrayList<Request>();
    private static ArrayList<OffCode> offCodes = new ArrayList<OffCode>();
    public static boolean hasManager = false;

    Manager(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        if(!hasManager)
            allUsers.add(this);
        hasManager = true;
        this.type = "Manager";
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
        putTabs(output, numOfTabs);
        output += "-" + category.getFullName() + "\n";
        for(Category subCategory: allCategories)
            if(subCategory.getParent() == category)
                output += showAllOfCategory(subCategory, numOfTabs + 1);
        return output;
    }

    private static void putTabs(String output, int numOfTabs)
    {
        for(int i = 0; i < numOfTabs; i++)
            output += "\t";
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

    public static Category catagoryByName(String name)
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
        for (Request request : pendingRequests)
            if (id == request.getId())
                return request;
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

    public static void readAllOffCodes() throws FileNotFoundException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All OffCodes.txt");
            Scanner scanner = new Scanner(file);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String allOffCodesString = scanner.nextLine();
            ArrayList<String> offCodeNames = gson.fromJson(allOffCodesString, ArrayList.class);
            for (String offCodeName : offCodeNames)
                readEachOffCode(offCodeName);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    private static void readEachOffCode(String offCodeName) throws FileNotFoundException {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            File file = new File("..\\..\\..\\..\\..\\Resources\\" + offCodeName + ".txt");
            Scanner scanner = new Scanner(file);
            String offCodeString = scanner.nextLine();
            OffCode offCode = gson.fromJson(offCodeString, OffCode.class);
            offCodes.add(offCode);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    public static void writeAllOffCodes() throws IOException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All OffCodes.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\All OffCodes.txt");
            ArrayList<String> offCodeNames = new ArrayList<String>();
            for (OffCode offCode : offCodes) {
                offCodeNames.add(offCode.getCode());
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            fileWriter.write(gson.toJson(offCodeNames));
            fileWriter.close();
            for (OffCode offCode : offCodes) {
                writeEachOffCode(offCode);
            }
        } catch (IOException exception) {
            throw exception;
        }
    }

    private static void writeEachOffCode(OffCode offCode) throws IOException {
        File file = new File("..\\..\\..\\..\\..\\Resources\\" + offCode.getCode() + ".txt");
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\" + offCode.getCode() + ".txt");
            fileWriter.write(gson.toJson(offCode));
            fileWriter.close();
        } catch (IOException exception) {
            throw exception;
        }
    }

    public static void readAllRequests() throws FileNotFoundException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All Requests.txt");
            Scanner scanner = new Scanner(file);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String allRequestsString = scanner.nextLine();
            ArrayList<String> requestNames = gson.fromJson(allRequestsString, ArrayList.class);
            for (String requestName : requestNames)
                readEachRequest(requestName);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    private static void readEachRequest(String requestName) throws FileNotFoundException {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            File file = new File("..\\..\\..\\..\\..\\Resources\\" + requestName + ".txt");
            Scanner scanner = new Scanner(file);
            String requestString = scanner.nextLine();
            Request request = gson.fromJson(requestString, Request.class);
            pendingRequests.add(request);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    public static void writeAllRequests() throws IOException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All Requests.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\All Requests.txt");
            ArrayList<String> requestNames = new ArrayList<String>();
            for (Request request : pendingRequests) {
                requestNames.add(request.getId() + "");
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            fileWriter.write(gson.toJson(requestNames));
            fileWriter.close();
            for (Request request : pendingRequests) {
                writeEachRequest(request);
            }
        } catch (IOException exception) {
            throw exception;
        }
    }

    private static void writeEachRequest(Request request) throws IOException {
        File file = new File("..\\..\\..\\..\\..\\Resources\\" + request.getId() + ".txt");
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\" + request.getId() + ".txt");
            fileWriter.write(gson.toJson(request));
            fileWriter.close();
        } catch (IOException exception) {
            throw exception;
        }
    }

    public static void readAllCategories() throws FileNotFoundException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All Categories.txt");
            Scanner scanner = new Scanner(file);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String allCategoriesString = scanner.nextLine();
            ArrayList<String> categoryNames = gson.fromJson(allCategoriesString, ArrayList.class);
            for (String categoryName : categoryNames)
                readEachCategory(categoryName);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    private static void readEachCategory(String categoryName) throws FileNotFoundException {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            File file = new File("..\\..\\..\\..\\..\\Resources\\" + categoryName + ".txt");
            Scanner scanner = new Scanner(file);
            String categoryString = scanner.nextLine();
            Category category = gson.fromJson(categoryString, Category.class);
            allCategories.add(category);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    public static void writeAllCategory() throws IOException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All Categories.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\All Categories.txt");
            ArrayList<String> categoryNames = new ArrayList<String>();
            for (Category category : allCategories) {
                categoryNames.add(category.getFullName());
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            fileWriter.write(gson.toJson(categoryNames));
            fileWriter.close();
            for (Category category : allCategories) {
                writeEachUser(category);
            }
        } catch (IOException exception) {
            throw exception;
        }
    }

    private static void writeEachUser(Category category) throws IOException {
        File file = new File("..\\..\\..\\..\\..\\Resources\\" + category.getFullName() + ".txt");
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\" + category.getFullName() + ".txt");
            fileWriter.write(gson.toJson(category));
            fileWriter.close();
        } catch (IOException exception) {
            throw exception;
        }
    }

    @Override
    public boolean equals(Object object) {
        Manager manager = (Manager) object;
        return(super.equals(manager));
    }

    @Override
    public void delete() {
        allUsers.remove(this);
    }

    @Override
    public String toString()
    {
        String output = null;
        output += "Username: " + username;
        output += "\nFirst Name: " + name;
        output += "\nFamily Name: " + familyName;
        output += "\nEmail: " + email;
        output += "\nPhone Number: " + phoneNumber;
        return output;
    }
}
