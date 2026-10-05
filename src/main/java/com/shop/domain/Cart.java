package com.shop.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> items = new ArrayList<>();

    public void add(Product p) {
        items.add(p);
    }

    public void clear() {
        items.clear();
    }

    public List<Product> getItems() { return items; }
}