package main.java.Store.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

    int id;
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

    public static User getUserByUsername(String username){
        for (User user: allUsers)
            if(username.equals(user.getUsername()))
                return user;
        return null;
    }

    public static ArrayList<User> getAllUsers()
    {
        return allUsers;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public void delete()
    {

    }

    public int getId() {
        return id;
    }

    public User getUserById(int id) {
        for(User user: allUsers)
            if(id == user.getId())
                return user;
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

    public String getUsername(){
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

}
