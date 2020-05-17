package Store.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
        for (User user : User.getAllUsers()) {
            if(user instanceof Customer && this.isUserIncluded(user))
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

    public static OffCode randomOffCode(int offPercentage)
    {
        Random random = new Random();
        String code = "";
        int helpRand = 0;
        for (int i = 0; i < 8; i++)
        {
            helpRand = random.nextInt(62);
            if(helpRand <= 9)
                code += ((char)(helpRand + 48));
            else if(helpRand <= 35)
                code += ((char)(helpRand + 55));
            else
                code += ((char)(helpRand + 61));
        }
        OffCode offCode = new OffCode(code, offPercentage, 200, 3);
        offCode.setStartingTime(new Date());
        offCode.setEndingTime(new Date(new Date().getTime() + 2 * 24 * 3600 *1000));
        System.out.println("DATES: " + offCode.getStartingTime() + "  " + offCode.getEndingTime());
        Manager.addOffCode(offCode);
        return offCode;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

    public int getUsageCount() {
        return this.usageCount;
    }

    public boolean canBeUsedInDate(Date now) {
        return (this.startingTime.before(now) && this.endingTime.after(now));
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
