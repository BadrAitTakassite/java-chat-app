package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClient extends Application {
    private TextArea chatArea;
    private TextField messageInput;
    private ObjectOutputStream outputStream;
    private String username;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        chatArea = new TextArea();
        messageInput = new TextField();
        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> sendMessage());

        VBox root = new VBox(chatArea, messageInput, sendButton);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Chat App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Prompt the user for a username
        setUsername();

        // Connect to the server
        connectToServer();
    }

    private void setUsername() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Username Input");
        dialog.setHeaderText("Please enter your username:");
        dialog.setContentText("Username:");

        // Traditional way to get the response value.
        username = dialog.showAndWait().orElse("Unknown");

        // Display a custom message in the chat area
        chatArea.appendText("Welcome, " + username + "! You are now logged in.\n");
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            // Send the username to the server
            outputStream.writeObject(username);

            // Start a thread for receiving messages from the server
            new Thread(() -> {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        Object receivedObject = inputStream.readObject();

                        if (receivedObject instanceof String) {
                            String receivedMessage = (String) receivedObject;
                            Platform.runLater(() -> chatArea.appendText(receivedMessage + "\n"));
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {
            String message = messageInput.getText();
            String formattedMessage = username + ": " + message;
            outputStream.writeObject(formattedMessage);
            outputStream.flush();
            messageInput.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

