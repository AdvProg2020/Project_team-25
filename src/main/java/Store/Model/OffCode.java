package main.java.Store.Model;

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

    public boolean isUserIncluded(User user) {
        for (User currentUser : this.users) {
            if (currentUser == user) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        this.users.add(user);
        this.usageCountRemaining.add(this.usageCount);
    }

    public void removeUser(User user) {
        for (int userIndex = 0; userIndex < this.users.size(); userIndex++) {
            if (this.users.get(userIndex) == user) {
                this.users.remove(userIndex);
                this.usageCountRemaining.remove(userIndex);
                break;
            }
        }
    }

    public User getUserByIndex(int index) {
        return this.users.get(index);
    }

    public boolean canBeUsedInDate(Date now) {
        return (this.startingTime.before(now) && this.endingTime.after(now));
    }


    public int getAmountOfUsers() {
        return this.users.size();
    }

    public String getCode() {
        return this.code;
    }

    public double getOffPercentage() {
        return this.offPercentage;
    }

    public Date getStartingTime() {
        return this.startingTime;
    }

    public Date getEndingTime() {
        return this.endingTime;
    }

    public double getMaximumOff() {
        return maximumOff;
    }



    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }

    public void setMaximumOff(double maximumOff) {
        this.maximumOff = maximumOff;
    }

    public void setOffPercentage(double offPercentage) {
        this.offPercentage = offPercentage;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
}
