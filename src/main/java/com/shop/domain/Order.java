package com.shop.domain;

import com.shop.contract.IDeliveryService;
import com.shop.contract.IPayment;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final int id;
    private final User customer;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(int id, User customer) {
        this.id = id;
        this.customer = customer;
    }

    public void add(OrderItem it) {
        items.add(it);
    }

    public double total() {
        return items.stream().mapToDouble(OrderItem::subtotal).sum();
    }

    public void checkout(IPayment pay) {
        pay.pay(total());
    }

    public void ship(IDeliveryService d) {
        d.deliver(this);
    }

    public int getId() { return id; }
    public User getCustomer() { return customer; }
}