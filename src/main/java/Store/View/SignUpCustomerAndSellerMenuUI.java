package Store.View;

import Store.Main;

import Store.Networking.Client.Controller.ClientSignUpAndLoginController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SignUpCustomerAndSellerMenuUI {

    public ComboBox accountTypeComboBox;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public PasswordField passwordConfirmationTextField;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
    public TextField companyNameTextField;
    public TextArea companyDescriptionTextArea;
    public Button signUpButton;
    public Label errorMessage;


    public SignUpCustomerAndSellerMenuUI() {

    }

    public static void showSignUpMenu() {
        try {
            Main.setupOtherStage(new Scene(getContent()), "Sign Up");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/SignUp_CustomerAndSeller.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
    }


    private void initialSetup() {
        if (accountTypeComboBox == null) {
            System.out.println("NUUUUULLL");
        }
        if (accountTypeComboBox.getItems() == null) {
            System.out.println("NAAAAA");
        }
        accountTypeComboBox.getItems().add("Customer");
        accountTypeComboBox.getItems().add("Seller");
        accountTypeComboBox.getSelectionModel().selectFirst();
        companyNameTextField.setDisable(true);
        companyDescriptionTextArea.setDisable(true);
    }

    public void setupBindings() {
        accountTypeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                companyNameTextField.setDisable(newValue.equals("Customer"));
                companyDescriptionTextArea.setDisable(newValue.equals("Customer"));
            }
        });
        signUpButton.setOnAction((e) -> {
            handleSignUpValidation();});
    }

    private boolean checkEmptyFields(boolean isSeller) {
        boolean result = true;
        if (usernameTextField.getText().isEmpty()) {
            setError(usernameTextField, true);
            result = false;
        }
        if (passwordTextField.getText().isEmpty()) {
            setError(passwordTextField, true);
            result = false;
        }
        if (passwordConfirmationTextField.getText().isEmpty()) {
            setError(passwordConfirmationTextField, true);
            result = false;
        }
        if (firstNameTextField.getText().isEmpty()) {
            setError(firstNameTextField, true);
            result = false;
        }
        if (lastNameTextField.getText().isEmpty()) {
            setError(lastNameTextField, true);
            result = false;
        }
        if (emailTextField.getText().isEmpty()) {
            setError(emailTextField, true);
            result = false;
        }
        if (phoneNumberTextField.getText().isEmpty()) {
            setError(phoneNumberTextField, true);
            result = false;
        }
        if (isSeller && companyNameTextField.getText().isEmpty()) {
            setError(companyNameTextField, true);
            result = false;
        }
        if (!result) {
            throwError("Please fill out all the needed fields!");
        }
        return result;
    }

    private void handleSignUpValidation() {
        boolean isValid = true;

        resetAllErrors();

        String type = (String) accountTypeComboBox.getValue();
        if (!checkEmptyFields(type.equalsIgnoreCase("Seller"))) {
            return;
        }
        String username = usernameTextField.getText();
        if (ClientSignUpAndLoginController.isUsernameWithThisName(username)) {
            throwError("A user with this username already exists!");
            setError(usernameTextField, true);
            isValid = false;
        }
        if (ClientSignUpAndLoginController.isUsernameExistInRequests(username)) {
            throwError("A user is waiting for manager on this username!");
            setError(usernameTextField, true);
            isValid = false;
        }

        String password = passwordTextField.getText();
        String passwordConfirmation = passwordConfirmationTextField.getText();
        if (!password.equals(passwordConfirmation)) {
            throwError("The password and its confirmation do not match!");
            setError(passwordTextField, true);
            setError(passwordConfirmationTextField, true);
            isValid = false;
        }

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        if (!email.matches("[^\\s]+@[^\\s]+(\\.)[^\\s]{3}")) {
            throwError("Invalid email format!");
            setError(emailTextField, true);
            isValid = false;
        }

        String phoneNumber = phoneNumberTextField.getText();
        if (!phoneNumber.matches("^[0-9]+$")) {
            throwError("Invalid phone number!");
            setError(emailTextField, true);
            isValid = false;
        }

        ArrayList<String> attributes = new ArrayList<String>(Arrays.asList(username, firstName, lastName, email, phoneNumber, password));
        if (type.equalsIgnoreCase("Seller")) {
            attributes.add(companyNameTextField.getText());
            attributes.add(companyDescriptionTextArea.getText());
        }
        if (isValid) {
            ClientSignUpAndLoginController.handleCreateAccount(type, attributes);
            resetAllFields();
            System.out.println(password);
            throwError("Register Successful!");
        }
    }

    private void resetAllFields() {
        resetAllErrors();
        usernameTextField.setText("");
        passwordTextField.setText("");
        passwordConfirmationTextField.setText("");
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        companyNameTextField.setText("");
        companyDescriptionTextArea.setText("");
    }

    private void resetAllErrors() {
        errorMessage.setVisible(false);
        setError(usernameTextField, false);
        setError(passwordTextField, false);
        setError(passwordConfirmationTextField, false);
        setError(firstNameTextField, false);
        setError(lastNameTextField, false);
        setError(emailTextField, false);
        setError(phoneNumberTextField, false);
        setError(companyNameTextField, false);
    }

    private void throwError(String message) {
        errorMessage.setVisible(true);
        errorMessage.setText(message);
    }

    private void setError(TextField field, boolean isError) {
        ObservableList<String> styleClass = field.getStyleClass();
        if (isError) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        }
        else {
            styleClass.removeAll(Collections.singleton("error"));
        }
    }
}
