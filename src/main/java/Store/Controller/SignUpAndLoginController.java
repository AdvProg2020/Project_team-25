package Store.Controller;

import Store.InputManager;
import Store.Model.Customer;
import Store.Model.Seller;
import Store.Model.User;

public class SignUpAndLoginController {

    public static String createAccount(String type, String username, String password) {
        if(type.equalsIgnoreCase("customer"))
        {
            getCustomerTraits(username, password);
            return "Successfully account created";
        }
        else if(type.equalsIgnoreCase("seller"))
        {
            getSellerTraits(username, password);
            return "Successfully request sent";
        }
        else if(type.equalsIgnoreCase("manager"))
        {
            return "you have to go to manager menu to create new manager profile";
        }
        return null;
    }

    private static void getCustomerTraits(String username, String password)
    {
        Customer.addCustomer(new Customer(getPersonal(username, password), password, getMoneyByUser()));
    }

    private static void getSellerTraits(String username, String password)
    {
        new Seller(getPersonal(username, password), password, getMoneyByUser(), getStringByUser("Company Name"), getStringByUser("Company Discription")).requestRegisterSeller();
    }

    private static User getPersonal(String username, String password)
    {
        String firstName, lastName, email, phoneNumber;
        System.out.println("First Name: ");
        firstName = InputManager.getNextLine();
        System.out.println("Last Name: ");
        lastName = InputManager.getNextLine();
        System.out.println("Email: ");
        while (!(email = InputManager.getNextLine()).matches("(\\w+)@(\\w+)(\\.)com")) {
            System.out.println("the format is invalid");
        }
        System.out.println("Phone Number: ");
        while ((phoneNumber = InputManager.getNextLine()).matches("^[0-9]\\w{3,10}$")) {
            System.out.println("the format is invalid");
        }
        return (new User(username, firstName, lastName, email, phoneNumber, password));
    }

    private static double getMoneyByUser()
    {
        System.out.println("Money: ");
        return (InputManager.getNextDouble());
    }

    private static String getStringByUser(String field)
    {
        System.out.println(field + ": ");
        return (InputManager.getNextLine());
    }
}
