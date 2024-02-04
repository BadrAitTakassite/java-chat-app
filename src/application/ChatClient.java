package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            // Access the controller
            ChatClientController controller = loader.getController();

            // Set the stage
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Chat App");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Initialize the components after the FXML file has been loaded
            initializeComponents(primaryStage, controller);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeComponents(Stage primaryStage, ChatClientController controller) {
        // Pass the necessary elements or data to the controller if needed
        controller.setStage(primaryStage);
        controller.setChatClient(this);

        // Initialize the messageInput field from the controller
        messageInput = controller.getMessageInput();

        // Prompt the user for a username
        setUsername(controller);

        // Connect to the server
        connectToServer();
    }

    private void setUsername(ChatClientController controller) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Username Input");
        dialog.setHeaderText("Please enter your username:");
        dialog.setContentText("Username:");

        // Traditional way to get the response value.
        username = dialog.showAndWait().orElse("Unknown");

        // Access the chatArea from the controller
        chatArea = controller.getChatArea();

        // Display a custom message in the chat area
        if (chatArea != null) {
            chatArea.appendText("Welcome, " + username + "! You are now logged in.\n");
        } else {
            System.err.println("Error: chatArea is null.");
        }
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

    public void sendMessage() {
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

