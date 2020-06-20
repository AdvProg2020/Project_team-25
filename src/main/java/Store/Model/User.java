package Store.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    protected String profilePicturePath;

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
        this.profilePicturePath = "";
        this.username = username;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        id = ++idTillNow;
    }

    public User(String username, String name, String familyName, String email, String phoneNumber, String password, String profilePicturePath) {
        this.username = username;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        id = ++idTillNow;

        this.profilePicturePath = profilePicturePath;
    }

    public static void setAllUsers(ArrayList<User> allUsers) {
        User.allUsers = allUsers;
    }

    public static int getIdTillNow() {
        return idTillNow;
    }

    public static void setIdTillNow(int idTillNow) {
        User.idTillNow = idTillNow;
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers)
            if (username.equals(user.getUsername()))
                return user;
        return null;
    }

    public static int numberOfCustomers()
    {
        int numOfCustomers = 0;
        for (User user: allUsers) {
            if(user instanceof Customer)
                numOfCustomers++;
        }
        return numOfCustomers;
    }

    public static Customer findIndexOfNthCustomer(int num)
    {
        int count = 1;
        for(int i = 0; i < allUsers.size(); i++)
            if (allUsers.get(i) instanceof Customer)
            {
                if(count == num)
                    return (Customer)allUsers.get(i);
                count++;
            }
        return null;
    }

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public boolean validatePassword(String password) {
        return (this.password).equals(password);
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

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String output = "";
        output += "Username: " + username;
        output += "\nFirst Name: " + name;
        output += "\nFamily Name: " + familyName;
        output += "\nEmail: " + email;
        output += "\nPhone Number: " + phoneNumber;
        return output;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object object) {
        User user = (User) object;
        if (user == null && object == null) {
            return true;
        }
        if ((user != null && object != null)) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}
