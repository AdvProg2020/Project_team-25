package Store.View;


import Store.Main;

import Store.Model.Manager;
import Store.Networking.Client.Controller.ClientManagerController;
import Store.Networking.Client.Controller.ClientSignUpAndLoginController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

public class SignUpManagerMenuUI {

    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public PasswordField passwordConfirmationTextField;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
    public TextField minimumMoneyTextField;
    public TextField karmozdTextField;
    public Button confirmButton;
    public Label errorMessage;


    public SignUpManagerMenuUI() {

    }

    public static void showSignUpMenu() {
        try {
            Main.setupOtherStage(new Scene(getContent()), "Register Manager");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpManagerMenuUI.class.getClassLoader().getResource("FXML/SignUp_Manager.fxml"));
        return root;
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();
    }


    private void initialSetup() {
    }

    public void setupBindings() {
        confirmButton.setOnAction((e) -> handleSignUpValidation());
        if (!ClientManagerController.hasManager()){
            karmozdTextField.setVisible(true);
            minimumMoneyTextField.setVisible(true);
        }
    }

    private boolean checkEmptyFields() {
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

        if (karmozdTextField.isVisible()) {
            if (minimumMoneyTextField.getText().isEmpty()) {
                setError(minimumMoneyTextField, true);
                result = false;
            }
            if (karmozdTextField.getText().isEmpty()) {
                setError(karmozdTextField, true);
                result = false;
            }
        }

        if (!result) {
            throwError("Please fill out all the needed fields!");
        }
        return result;
    }

    private void handleSignUpValidation() {
        boolean isValid = true;

        resetAllErrors();

        if (!checkEmptyFields()) {
            return;
        }
        String username = usernameTextField.getText();
        if (ClientSignUpAndLoginController.isUsernameWithThisName(username)) {
            throwError("A user with this username already exists!");
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
        if (karmozdTextField.isVisible()) {
            if (!Pattern.matches("(\\d+)", karmozdTextField.getText())) {
                throwError("Invalid karmozd!");
                setError(karmozdTextField, true);
                isValid = false;
            }

            if (!Pattern.matches("(\\d+)", minimumMoneyTextField.getText())) {
                throwError("Invalid minimum!");
                setError(minimumMoneyTextField, true);
                isValid = false;
            }
        }
        ArrayList<String> attributes = new ArrayList<String>(Arrays.asList(username, firstName, lastName, email, phoneNumber, password));
        if (isValid) {
            ClientSignUpAndLoginController.createManager(attributes);
            if (karmozdTextField.isVisible()) {
                double karmozd = 0;
                double minimum = 0;
                karmozd = Double.parseDouble(karmozdTextField.getText());
                minimum = Double.parseDouble(minimumMoneyTextField.getText());
                ClientManagerController.setKarmozd(karmozd);
                ClientManagerController.setMinimum(minimum);
            }
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
        karmozdTextField.setText("");
        minimumMoneyTextField.setText("");
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
        setError(karmozdTextField, false);
        setError(minimumMoneyTextField, false);
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