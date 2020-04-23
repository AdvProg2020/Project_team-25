package Store.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class User {

    int id;
    protected String type;
    protected String username;
    protected String name;
    protected String familyName;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected static ArrayList<User> allUsers = new ArrayList<User>();
    protected static int idTillNow = 0;
    // Notification Array

    public User(String username, String name, String familyName, String email, String phoneNumber, String password) {
        this.username = username;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        id = ++idTillNow;
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers)
            if (username.equals(user.getUsername()))
                return user;
        return null;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public void delete() {

    }

    public int getId() {
        return id;
    }

    public User getUserById(int id) {
        for (User user : allUsers)
            if (id == user.getId())
                return user;
        return null;
    }

    public String getName() {
        return name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void readAllUsers() throws FileNotFoundException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All Users.txt");
            Scanner scanner = new Scanner(file);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String allUsersString = scanner.nextLine();
            ArrayList<String> usernames = gson.fromJson(allUsersString, ArrayList.class);
            for (String username : usernames)
                readEachUser(username);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    public static void readAllUsers(String path) throws FileNotFoundException {
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String allUsersString = scanner.nextLine();
            ArrayList<String> usernames = gson.fromJson(allUsersString, ArrayList.class);
            for (String username : usernames)
                readEachUser(username);
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    public String getType() {
        return type;
    }

    private static void readEachUser(String username) throws FileNotFoundException {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            File file = new File("..\\..\\..\\..\\..\\Resources\\" + username + ".txt");
            Scanner scanner = new Scanner(file);
            String userString = scanner.nextLine();
            User user = gson.fromJson(userString, User.class);
            if (user.getType().equals("Customer"))
                allUsers.add(gson.fromJson(userString, Customer.class));
            else if (user.getType().equals("Seller"))
                allUsers.add(gson.fromJson(userString, Seller.class));
            else if (user.getType().equals("Manager"))
                allUsers.add(gson.fromJson(userString, Manager.class));
        } catch (FileNotFoundException exception) {
            throw exception;
        }
    }

    public static void writeAllUsers() throws IOException {
        try {
            File file = new File("..\\..\\..\\..\\..\\Resources\\All Users.txt");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\All Users.txt");
            ArrayList<String> usernames = new ArrayList<String>();
            for (User user : allUsers) {
                usernames.add(user.getUsername());
            }
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            fileWriter.write(gson.toJson(usernames));
            fileWriter.close();
            for (User user : allUsers) {
                writeEachUser(user);
            }
        } catch (IOException exception) {
            throw exception;
        }
    }

    private static void writeEachUser(User user) throws IOException {
        File file = new File("..\\..\\..\\..\\..\\Resources\\" + user.getUsername() + ".txt");
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter("..\\..\\..\\..\\..\\Resources\\" + user.getUsername() + ".txt");
            fileWriter.write(gson.toJson(user));
            fileWriter.close();
        } catch (IOException exception) {
            throw exception;
        }
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

    @Override
    public boolean equals(Object object) {
        User user = (User) object;
        if(username.equals(user.getUsername()) && email.equals(user.getEmail()) && phoneNumber.equals(user.getPhoneNumber()) && familyName.equals(user.getFamilyName()) && name.equals(user.getName()) && id == user.getId() && type.equals(user.getType()))
            return true;
        return false;
    }
}
