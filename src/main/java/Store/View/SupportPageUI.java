package Store.View;

import Store.Controller.CustomerController;
import Store.Controller.MainMenuUIController;
import Store.Controller.ProductController;
import Store.Main;
import Store.Model.*;
import Store.Networking.Chat.ChatClient;
import Store.Networking.Chat.PrivateChat;
import Store.Networking.Client.ClientHandler;
import Store.Networking.Client.Controller.ClientMainMenuController;
import Store.View.AdditionalUtils.PannableCanvas;
import Store.View.AdditionalUtils.SceneGestures;
import Store.View.AdditionalUtils.VideoPlayer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SupportPageUI {

    private static final double MAX_MESSAGE_WIDTH = 500;

    public Button mainMenuButton;
    public Button productsButton;
    public Button offersButton;
    public Button userPageButton;
    public Button supportPageButton;
    public Button auctionPageButton;

    public Label loggedInStatusText;
    public Button signUpButton;
    public Button loginLogoutButton;

    public VBox availableOperatorsVBox;
    public VBox chatDisplayVBox;
    public TextArea messageTextArea;
    public Button sendButton;

    private ChatClient chatClient = null;
    private String currentChat = "";

    public static SupportPageUI supportPageUI;

    public SupportPageUI() {
        supportPageUI = this;
    }

    public static Parent getContent() throws IOException {
        Parent root = FXMLLoader.load(SignUpCustomerAndSellerMenuUI.class.getClassLoader().getResource("FXML/SupportPage.fxml"));
        return root;
    }

    public static void showSupportPage() {
        try {
            Main.setPrimaryStageScene(new Scene(getContent()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        initialSetup();
        setupBindings();

        if (ClientHandler.hasLoggedIn) {
            updateInterface();
        }
    }


    private void initialSetup() {
        loggedInStatusText.textProperty().bind(ClientMainMenuController.currentUserUsername);
        signUpButton.disableProperty().bind(ClientMainMenuController.isLoggedIn);
        loginLogoutButton.textProperty().bind(ClientMainMenuController.loginLogoutButtonText);

        try {
            if (ClientHandler.hasLoggedIn) {
                chatClient = new ChatClient(ClientHandler.username, ClientHandler.token);
                chatClient.requestCurrentOperators(ClientHandler.username, ClientHandler.token);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupBindings() {
        auctionPageButton.setOnAction(e -> AuctionsMenuUI.showAuctionsMenu());
        mainMenuButton.setOnAction((e) -> {
            try {
                Main.setPrimaryStageScene(new Scene(MainMenuUI.getContent()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        loginLogoutButton.setOnAction((e) -> { cleanUp(); LoginMenuUI.handleEvent(); });
        signUpButton.setOnAction((e) -> { cleanUp(); SignUpCustomerAndSellerMenuUI.showSignUpMenu(); });
        productsButton.setOnAction((e) -> { cleanUp(); ProductsMenuUI.showProductsMenu(); });
        offersButton.setOnAction((e) -> { cleanUp(); OffersMenuUI.showOffersMenu(); });
        userPageButton.setOnAction((e) -> { cleanUp(); UserPageHandlerUI.handleEvent(); });

        if (ClientHandler.hasLoggedIn) {
            sendButton.setOnAction((e) -> sendMessage());
        }
    }

    private void cleanUp() {
        try {
            chatClient.endConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        chatClient = null;
    }

    public void updateInterface() {
        Platform.runLater(() -> {
            updateChatDisplay();
            updateAvailableOperators();
        });
    }

    private void updateAvailableOperators() {
        availableOperatorsVBox.getChildren().clear();
        ArrayList<PrivateChat> chats = chatClient.getSortedChats();

        for (PrivateChat privateChat : chats) {
            if (privateChat.getUsername().equals(ClientHandler.username)) {
                continue;
            }

            Button chatButton = new Button(privateChat.getUsername());
            chatButton.getStylesheets().add(getClass().getResource("/CSS/support_page.css").toExternalForm());

            if (privateChat.getUsername().equals(currentChat)) {
                chatButton.getStyleClass().add("selectedChatOption");
            }
            else if (privateChat.hasChecked()) {
                chatButton.getStyleClass().add("checkedChatOption");
                chatButton.setOnAction((e) -> {
                    currentChat = privateChat.getUsername();
                    privateChat.updateLastOpened();
                    updateInterface();
                });
            }
            else {
                chatButton.getStyleClass().add("uncheckedChatOption");
                chatButton.setOnAction((e) -> {
                    currentChat = privateChat.getUsername();
                    privateChat.updateLastOpened();
                    updateInterface();
                });
            }

            availableOperatorsVBox.getChildren().add(chatButton);
        }
    }

    private void updateChatDisplay() {
        if (currentChat.isEmpty()) {
            return;
        }
        chatDisplayVBox.getChildren().clear();
        ArrayList<PrivateChat.Message> messages = chatClient.getChatWithUser(currentChat).getMessages();

        for (PrivateChat.Message message : messages) {
            addMessageToChatDisplay(message);
        }
    }

    private void addMessageToChatDisplay(PrivateChat.Message message) {
        Label currentMessageLabel = new Label(message.getMessage());
        currentMessageLabel.setWrapText(true);
        currentMessageLabel.setMaxWidth(MAX_MESSAGE_WIDTH);

        currentMessageLabel.getStylesheets().add(getClass().getResource("/CSS/support_page.css").toExternalForm());
        currentMessageLabel.getStyleClass().add("messageText");

        HBox messageBox = new HBox(currentMessageLabel);
        messageBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
        messageBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
        messageBox.getStylesheets().add(getClass().getResource("/CSS/support_page.css").toExternalForm());

        HBox currentMessageHBox = new HBox(messageBox);
        currentMessageHBox.setPrefHeight(Control.USE_COMPUTED_SIZE);

        if (message.isOwned()) {
            currentMessageHBox.setAlignment(Pos.CENTER_RIGHT);
            messageBox.getStyleClass().add("ownedMessage");
        }
        else {
            currentMessageHBox.setAlignment(Pos.CENTER_LEFT);
            messageBox.getStyleClass().add("notOwnedMessage");
        }

        chatDisplayVBox.getChildren().add(currentMessageHBox);
    }

    private void sendMessage() {
        String message = messageTextArea.getText();
        if (message.isEmpty() || currentChat.isEmpty()) {
            return;
        }

        try {
            chatClient.sendMessageTo(ClientHandler.username, ClientHandler.token, currentChat, message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        updateInterface();

        messageTextArea.clear();
    }
}
