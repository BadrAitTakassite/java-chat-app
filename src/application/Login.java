package application;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {
    private Map<String, String> users;

    public Login() {
        users = new HashMap<>();
        // Initialize with some sample users (replace with your database logic)
        users.put("user1", "password1");
        users.put("user2", "password2");
    }

    public boolean authenticate(String username, String password) {
        // Check if the provided username exists and the password matches
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
