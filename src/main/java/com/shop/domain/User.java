package com.shop.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final int id;
    private final String name;
    private final List<Order> orders = new ArrayList<>();

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void place(Order o) {
        orders.add(o);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Order> getOrders() { return orders; }
}