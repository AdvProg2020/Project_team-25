import Store.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserModelTest {

    ArrayList<User> allUsers = new ArrayList<>();

    @Test
    public void initializingAllUsers() throws IOException {
        File file = new File("Test Resources\\allUsers.txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Scanner scan = new Scanner(file);
        while (scan.hasNext())
            allUsers.add(gson.fromJson(scan.nextLine(), User.class));
    }

    @Test
    public void writeOnTestResource() throws IOException {
        allUsers.add(new User("bala","boz","oof", "fsasfa@gamil.com", "0214536844","pashimoonam"));
        FileWriter file = new FileWriter("Test Resources\\allUsers.txt");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        for (User user: allUsers) {
            file.write(gson.toJson(user));
        }
        file.close();
    }
    @Test
    public void validatePass() throws IOException {
        initializingAllUsers();
        Assert.assertTrue(allUsers.get(0).validatePassword("pashimoonam"));
    }

    @Test
    public void getters() throws IOException {
        initializingAllUsers();
        User user = allUsers.get(0);
        Assert.assertTrue(user.equals(new User(user.getUsername(), user.getName(), user.getFamilyName(), user.getEmail(), user.getPhoneNumber(), "pashimoonam")));
    }

    @Test
    public void setters() throws IOException {
        initializingAllUsers();
        User user = allUsers.get(0);
        user.setName("mamad");
        user.setEmail("HEY");
        user.setFamilyName("valiai");
        user.setPassword("8585");
        user.setPhoneNumber("789456");
        Assert.assertEquals(user, new User("bala", "mamad", "valiai", "HEY", "789456", "8585"));
    }
}
