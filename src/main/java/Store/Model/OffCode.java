package Store.Model;

import java.util.ArrayList;
import java.util.Date;

public class OffCode {
    private String code;
    private Date startingTime, endingTime;
    // StartingTime and EndingTime?
    private double offPercentage;
    private double maximumOff;
    private int usageCount;
    private ArrayList<User> users = new ArrayList<User>();

    private ArrayList<Integer> usageCountRemaining = new ArrayList<Integer>();

    public OffCode(String code, double offPercentage, double maximumOff, int usageCount) {
        this.code = code;
        this.offPercentage = offPercentage;
        this.maximumOff = maximumOff;
        this.usageCount = usageCount;
    }

    public void addUser(User user) {

    }
}
