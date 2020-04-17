package main.java.Store.View;

import main.java.Store.Controller.ManagerController;
import main.java.Store.Model.Category;
import main.java.Store.Model.Manager;


public class ManagerMenu {

    public static void init() {

    }

    private static void viewPersonalInfo() {

    }

    private static void editPersonalInfoWrapper(String field, String value) {
        ManagerController.editPersonalInfo(null,null,null);
    }

    private static void manageUsers() {

    }

    private static void viewUserByName(String username) {

    }

    private static void deleteUserByNameWrapper(String username) {
        ManagerController.deleteUserByName(null, null);
    }

    private static void createManagerProfileWrapper(String[] attributes) {
        ManagerController.createManagerProfile(null,null);
    }

    private static void manageAllProducts() {

    }

    private static void removeProductsWrapper(String attribute) {
        ManagerController.removeProducts(null,null);
    }

    private static void createOffCodeWrapper(String[] attributes) {
        ManagerController.createOffCode(null,null);
    }

    private static void viewOffCodes() {

    }

    private static void removeOffCodeWrapper(String attribute) {
        ManagerController.removeOffCode(null,null);
    }

    private static void editOffCodeWrapper(String[] attributes) {
        ManagerController.editOffCode(null,null,null);
    }

    private static void viewOffCode(String attribute) {

    }

    private static void manageRequests() {

    }

    private static void manageRequestDetails(int id) {

    }

    private static void handleRequestWrapper(boolean status, int id) {
        ManagerController.handleRequest(null, false,null);
    }

    private static void manageCategories() {

    }

    private static void addCategoryWrapper(String[] attributes) {
        ManagerController.addCategory(null,null);
    }

    private static void editCategoryWrapper(String[] attributes) {
        ManagerController.editCategory(null,null, null);
    }

    private static void removeCategoryWrapper(String name) {
        ManagerController.removeCategory(null,null);
    }

    private static void printHelp() {

    }
}
