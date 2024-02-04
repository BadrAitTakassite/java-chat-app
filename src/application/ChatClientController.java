package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ChatClientController {
    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    private Stage stage;
    private ChatClient chatClient;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public TextArea getChatArea() {
        return chatArea;
    }

    public TextField getMessageInput() {
        return messageInput;
    }

    @FXML
    private void initialize() {
        // You can perform any initialization here
    }

    @FXML
    private void sendMessage() {
        chatClient.sendMessage();
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }
}
