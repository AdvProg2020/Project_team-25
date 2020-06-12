package Store;

import Store.Model.*;
import Store.Model.Enums.CheckingStatus;
import Store.View.MainMenu;
import Store.View.MainMenuUI;
import Store.View.SignUpCustomerAndSellerMenuUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;

public class Main extends Application {
    private static Stage applicationStage;
    private static Stage otherStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception{
        otherStage.initModality(Modality.APPLICATION_MODAL);

        Parent root = MainMenuUI.getContent();
        applicationStage = primaryStage;
        primaryStage.setTitle("Shop");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
//        ResourceHandler.resetFile();
//        ResourceHandler.writeAll();

//        ResourceHandler.readAll();
//        Manager.checkPeriodOffCode();
        setTest();
        MainMenu.currentUser = MainMenu.guest;

        launch(args);
    }

    public static void setupOtherStage(Scene scene, String title) {
        otherStage.setScene(scene);
        otherStage.setTitle(title);
        otherStage.showAndWait();
    }

    public static void setApplicationStage(Scene scene) {
        applicationStage.setScene(scene);
        applicationStage.show();
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

        Product product1 = new Product(CheckingStatus.CREATION, null, "product1", seller1, "brand1", 10, true, "describe");
        Product product2 = new Product(CheckingStatus.CREATION, null, "product2", seller2, "brand1", 5, true, "describe");
        Product product3 = new Product(CheckingStatus.CREATION, null, "product3", seller1, "brand1", 10, true, "describe");
        Product product4 = new Product(CheckingStatus.CREATION, null, "product3", seller2, "brand1", 15.5, true, "describe");
        Product product5 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand1", 980, false, "describe");
        Product product6 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand1", 980, false, "describe");
        Product product7 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand2", 980, false, "describe");
        Product product8 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand2", 980, false, "describe");
        Product product9 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand2", 980, true, "describe");
        Product product10 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand2", 980, false, "describe");
        Product product11 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand2", 980, true, "describe");
        Product product12 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand2", 980, true, "describe");
        Product product13 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand3", 980, true, "describe");
        Product product14 = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand3", 980, false, "describe");

        Category category1 = new Category("category1", null);
        Category category2 = new Category("category2", null);
        Category category3 = new Category("category3", category1);

        OffCode offCode1 = new OffCode("ce98",30,10,2);
        offCode1.setEndingTime(new Date(2020, 5, 4));
        OffCode offCode2 = new OffCode("AP2",60,5,1);
        offCode2.setEndingTime(new Date(2020, 5, 4));
        OffCode offCode3 = new OffCode("AP333",60,5,1);
        offCode2.setEndingTime(new Date(2020, 5, 4));
        manager1.addOffCode(offCode1);
        manager1.addOffCode(offCode2);
        manager1.addOffCode(offCode3);
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
        product6.assignToSeller();
        product7.assignToSeller();
        product8.assignToSeller();
        product9.assignToSeller();
        product10.assignToSeller();
        product11.assignToSeller();
        product12.assignToSeller();
        product13.assignToSeller();
//        for (int i = 0; i < 1000; i++) {
//            Product product = new Product(CheckingStatus.CREATION, null, "product5", seller1, "brand1", i, false, "describe");
//            Product.addProduct(product1);
//            product.assignToSeller();
//        }

        category1.addToCategory(product1);
        category2.addToCategory(product2);
        category3.addToCategory(product3);
        category3.addToCategory(product4);
        category2.addToCategory(product5);

        manager1.addCategory(category1);
        manager1.addCategory(category3);
        manager2.addCategory(category2);

        Product.addProduct(product1);
        Product.addProduct(product2);
        Product.addProduct(product3);
        Product.addProduct(product4);
        Product.addProduct(product5);
        Product.addProduct(product6);
        Product.addProduct(product7);
        Product.addProduct(product8);
        Product.addProduct(product9);
        Product.addProduct(product10);
        Product.addProduct(product11);
        Product.addProduct(product12);
        Product.addProduct(product13);

        Product.addFilterToAllFilters("AB");
        Product.addFilterToAllFilters("BC");
        Product.addFilterToAllFilters("CD");
        Product.addFilterToAllFilters("DE");
        product1.addFilter("AB");
        product2.addFilter("BC");
        product2.addFilter("AB");
        product2.addFilter("CD");
        product1.addFilter("CD");
        product3.addFilter("AB");
        product4.addFilter("AB");
        product3.addFilter("CD");
        product4.addFilter("CD");
    }

    public static void setOffers() {
        Offer offer1 = new Offer(User.getUserByUsername("seller2"), CheckingStatus.CREATION, 20);
        offer1.addProduct(Product.getProductByID(0));
        offer1.addProduct((Product.getProductByID(1)));
        offer1.addProduct((Product.getProductByID(2)));
        Offer offer2 = new Offer(User.getUserByUsername("seller2"), CheckingStatus.CREATION, 20);
        offer2.addProduct((Product.getProductByID(3)));
        offer2.addProduct((Product.getProductByID(4)));
        Offer.addOfferToAllOffers(offer1);
        Offer.addOfferToAllOffers(offer2);
        ((Seller) User.getUserByUsername("seller2")).forceAddOffer(offer1);
        ((Seller) User.getUserByUsername("seller2")).forceAddOffer(offer2);
        offer1.setStartingTime(new Date());
        offer2.setStartingTime(new Date());
        offer1.setEndingTime(new Date(2020,6,10));
        offer2.setEndingTime(new Date(2020,6,9));
    }
}
