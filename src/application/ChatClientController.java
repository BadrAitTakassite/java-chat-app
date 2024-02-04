package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ChatClientController {
    private ChatClient chatClient;
    private User user;

    @FXML
    private TextArea chatArea;

    // Initialize method to pass the ChatClient and User instances
    public void init(ChatClient chatClient, User user) {
        this.chatClient = chatClient;
        this.user = user;
    }

    // Method to display messages in the chat area
    public void displayMessage(String message) {
        chatArea.appendText(message + "\n");
    }

    // Other methods as needed...
}
