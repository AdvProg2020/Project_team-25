package Store.Model;

import java.util.ArrayList;

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

    public static ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public boolean validatePassword(String password) {
        return (this.password).equals(password);
    }

    public void delete() {
    }

    public static boolean isExist(User user)
    {
        return allUsers.contains(user);
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

    public String getType() {
        return type;
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
