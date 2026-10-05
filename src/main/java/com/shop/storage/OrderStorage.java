package com.shop.storage;

import com.shop.domain.Order;
import com.shop.domain.OrderItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class OrderStorage {

    private static final Path ORDERS = Path.of("data", "orders.csv");
    private static final Path ITEMS  = Path.of("data", "items.csv");

    public static void init() {
        try {
            Files.createDirectories(ORDERS.getParent());
            if (!Files.exists(ORDERS)) Files.createFile(ORDERS);
            if (!Files.exists(ITEMS))  Files.createFile(ITEMS);
        } catch (IOException e) {
            System.out.println("Ошибка инициализации хранилища заказов: " + e.getMessage());
        }
    }

    public static void save(Order o) {
        String head = o.getId() + "," + o.getCustomer().getId() + ","
                + o.total() + ",CREATED" + System.lineSeparator();
        try {
            Files.writeString(ORDERS, head, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            List<String> rows = new ArrayList<>();
            for (OrderItem it : o.getItems()) {
                rows.add(o.getId() + "," + it.getProduct().getId() + ","
                        + it.getQty() + "," + it.getProduct().getPrice());
            }
            Files.write(ITEMS, rows, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения заказа: " + e.getMessage());
        }
    }

    public static List<String> loadOrders() {
        try {
            return Files.exists(ORDERS) ? Files.readAllLines(ORDERS) : List.of();
        } catch (IOException e) {
            System.out.println("Ошибка чтения заказов: " + e.getMessage());
            return List.of();
        }
    }
}