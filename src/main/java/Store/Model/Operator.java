package Store.Model;

public class Operator extends User {

    public Operator(String username, String name, String familyName, String email, String phoneNumber, String password) {
        super(username, name, familyName, email, phoneNumber, password);
        this.type = "Operator";
    }
}
