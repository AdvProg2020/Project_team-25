package Store.Model;

import java.util.ArrayList;

public class OffCode {
    private String code;
    // StartingTime and EndingTime?
    private double offPercentage;
    private double maximumOff;
    private int usageCount;
    private ArrayList<User> users = new ArrayList<User>();

    public OffCode(String code, double offPercentage, double maximumOff, int usageCount) {
        this.code = code;
        this.offPercentage = offPercentage;
        this.maximumOff = maximumOff;
        this.usageCount = usageCount;
    }
}
