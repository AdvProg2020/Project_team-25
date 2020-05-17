package Store;

import Store.Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class ResourceHandler {

    public static void readAll() {
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/Resources.res");
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            User.setIdTillNow((int) objectInputStream.readObject());
            Product.setIdCounter((int) objectInputStream.readObject());
            Offer.setIdCounter((int) objectInputStream.readObject());
            Category.setIdCounter((int) objectInputStream.readObject());
            Manager.hasManager = (boolean) objectInputStream.readObject();
            Manager.setPeriodOffCodeDate((Date) objectInputStream.readObject());

            User.setAllUsers((ArrayList<User>) objectInputStream.readObject());
            Request.setAllRequests((ArrayList<Request>) objectInputStream.readObject());
            Product.setAllProducts((ArrayList<Product>) objectInputStream.readObject());
            Offer.setAllOffers((ArrayList<Offer>) objectInputStream.readObject());
            Offer.setAllOffProducts((ArrayList<Product>) objectInputStream.readObject());
            Manager.setAllCategories((ArrayList<Category>) objectInputStream.readObject());
            Manager.setOffCodes((ArrayList<OffCode>) objectInputStream.readObject());
            Manager.setPendingRequests((ArrayList<Request>) objectInputStream.readObject());

            Product.calculateAllFilters();

            objectInputStream.close();
            inputStream.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeAll() {
        try {
            OutputStream outputStream = new FileOutputStream("src/main/resources/Resources.res");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(User.getIdTillNow());
            objectOutputStream.writeObject(Product.getIdCounter());
            objectOutputStream.writeObject(Offer.getIdCounter());
            objectOutputStream.writeObject(Category.getIdCounter());
            objectOutputStream.writeObject(Manager.hasManager);
            objectOutputStream.writeObject(Manager.getPeriodOffCodeDate());

            objectOutputStream.writeObject(User.getAllUsers());
            objectOutputStream.writeObject(Request.getAllRequests());
            objectOutputStream.writeObject(Product.getAllProducts());
            objectOutputStream.writeObject(Offer.getAllOffers());
            objectOutputStream.writeObject(Offer.getAllOffProducts());
            objectOutputStream.writeObject(Manager.getAllCategories());
            objectOutputStream.writeObject(Manager.getOffCodes());
            objectOutputStream.writeObject(Manager.getPendingRequests());

            objectOutputStream.close();
            outputStream.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void resetFile() {
        File file = new File("src/main/resources/Resources.res");
        if (file.delete()) {
            System.out.println("Saves have been reset");
        }
    }
}
