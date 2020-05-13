package Store.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class OffCode implements Serializable {
    private String code;
    private Date startingTime, endingTime;
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

    public void remove() {
        for (User user : this.users) {
            ((Customer) user).removeOffCodeOfUser(this);
        }
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
    }

    public boolean hasUser(User user) {
        return this.users.contains(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public int getUsageCount() {
        return this.usageCount;
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

    @Override
    public String toString() {
        return "code='" + code + '\'' +
                ", startingTime=" + startingTime +
                ", endingTime=" + endingTime +
                ", offPercentage=" + offPercentage +
                ", maximumOff=" + maximumOff +
                ", usageCount=" + usageCount +
                '}';
    }
}
