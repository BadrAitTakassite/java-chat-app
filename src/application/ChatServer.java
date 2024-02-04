package application;

//Server code
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345;
    private static List<ObjectOutputStream> clientOutputStreams = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(outputStream);

                // Start a thread for each connected client
                new Thread(() -> handleClient(clientSocket, outputStream)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, ObjectOutputStream outputStream) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                Object receivedObject = inputStream.readObject();

                if (receivedObject instanceof String) {
                    String message = (String) receivedObject;
                    System.out.println("Received message: " + message);

                    // Broadcast the message to all connected clients
                    broadcast(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Remove the client's output stream when they disconnect
            clientOutputStreams.remove(outputStream);
        }
    }

    private static void broadcast(String message) {
        // Send the message to all connected clients
        for (ObjectOutputStream outputStream : clientOutputStreams) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}