package com.shop.service;

import com.shop.contract.IOrderService;
import com.shop.domain.Order;
import com.shop.domain.User;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements IOrderService {

    private final List<Order> all = new ArrayList<>();
    private int seq = 1;

    @Override
    public Order create(User u) {
        Order o = new Order(seq++, u);
        all.add(o);
        return o;
    }

    @Override
    public void cancel(int id) {
        all.removeIf(o -> o.getId() == id);
    }
}