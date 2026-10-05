package com.shop.storage;

import com.shop.domain.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private static final Path USERS = Path.of("data", "users.csv");

    public static void init() {
        try {
            Files.createDirectories(USERS.getParent());
            if (!Files.exists(USERS)) {
                Files.createFile(USERS);
            }
        } catch (IOException e) {
            System.out.println("Ошибка инициализации хранилища: " + e.getMessage());
        }
    }

    public static void saveUser(User u) {
        String line = u.getId() + "," + u.getName() + System.lineSeparator();
        try {
            Files.writeString(USERS, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения пользователя: " + e.getMessage());
        }
    }

    public static List<String> loadUsers() {
        List<String> users = new ArrayList<>();
        try {
            if (Files.exists(USERS)) {
                users = Files.readAllLines(USERS);
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения пользователей: " + e.getMessage());
        }
        return users;
    }

    public static int nextUserId() {
        return loadUsers().size() + 1;
    }
}