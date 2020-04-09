package Store.Model;

import java.util.ArrayList;

public abstract class User {

    protected String username;
    protected String name;
    protected String familyName;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected static ArrayList<User> allUsers = new ArrayList<User>();
    protected double money;

    // Notification Array

    public User(String username, String name, String familyName, String email, String phoneNumber, String password, double money) {
        this.username = username;
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.money = money;
    }
}
