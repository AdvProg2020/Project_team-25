package Store;

import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import Store.View.*;

import java.util.Date;

public class Main {

    public static void main(String[] args) {

//        ResourceHandler.resetFile();
//        ResourceHandler.writeAll();

        ResourceHandler.readAll();
        System.out.println(Manager.hasManager);
//        SignUpAndLoginMenu.init();
//        ProductsMenu.init();
//        if (MainMenu.currentUser instanceof Manager) {
//            ManagerMenu.init();
//        }
//        else if (MainMenu.currentUser instanceof Seller) {
//            SellerMenu.init();
//        }
//        else {
//            CustomerMenu.init();
//        }

        MainMenu.init();

        System.out.println("Save Current Database?");
        String input = InputManager.getNextLine();
        MainMenu.currentUser = null;
        if (input.equalsIgnoreCase("Y")) {
            ResourceHandler.resetFile();
            ResourceHandler.writeAll();
        }
    }

    public static void setTest() {
        Manager manager1 = new Manager("cloudStrife", "cloud", "strife", "lab@lab.com", "0912", "1234");
        Manager manager2 = new Manager("jojoRabbit", "jojo", "rabbit", "lab@lab.com", "0936", "1234");

        Seller seller1 = new Seller("jackRipper", "jack", "ripper", "lab@lab.com", "0950", "1234", 1000, "company1", "describe");
        Seller seller2 = new Seller("seller2", "seller2", "seller2", "lab@lab.com", "0930", "1234", 100, "company2", "describe");

        Customer customer = new Customer("customer1", "customer1", "customer1", "lab@lab.com", "0912", "1234", 1000);
        customer.setMoney(1000);

        Customer.addCustomer(customer);
        Seller.addSeller(seller1);
        Seller.addSeller(seller2);
        manager1.addNewManager(manager2);

        Product product1 = new Product(CheckingStatus.CREATION, null, "product1", seller1, "brand1", 10, true, "www", "describe");
        Product product2 = new Product(CheckingStatus.CREATION, null, "product2", seller2, "brand1", 5, true, "www", "describe");
        Product product3 = new Product(CheckingStatus.CREATION, null, "product3", seller1, "brand1", 10, true, "www", "describe");
        Product product4 = new Product(CheckingStatus.CREATION, null, "product3", seller2, "brand1", 15.5, true, "www", "describe");
        Product product5 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand1", 980, true, "www", "describe");

        OffCode offCode1 = new OffCode("ce98",30,10,2);
        offCode1.setEndingTime(new Date(2020, 5, 4));
        OffCode offCode2 = new OffCode("AP2",60,5,1);
        offCode2.setEndingTime(new Date(2020, 5, 4));
        manager1.addOffCode(offCode1);
        manager1.addOffCode(offCode2);
        offCode1.addUser(customer);
        offCode2.addUser(customer);
        customer.addOffCode(offCode1);
        customer.addOffCode(offCode2);

        Request request = new Request(seller1);
        Manager.addRequest(request);

        product1.assignToSeller();
        product2.assignToSeller();
        product3.assignToSeller();
        product4.assignToSeller();
        product5.assignToSeller();

        Product.addProduct(product1);
        Product.addProduct(product2);
        Product.addProduct(product3);
        Product.addProduct(product4);
        Product.addProduct(product5);
    }
}
