package Store.View;

import Store.Controller.SignUpAndLoginController;
import Store.Main;
import Store.Model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class SignUpManagerMenuUI {

    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public PasswordField passwordConfirmationTextField;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public TextField emailTextField;
    public TextField phoneNumberTextField;
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
        if (User.getUserByUsername(username) != null) {
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

        ArrayList<String> attributes = new ArrayList<String>(Arrays.asList(username, firstName, lastName, email, phoneNumber, password));
        if (isValid) {
            SignUpAndLoginController.createManager(attributes);
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