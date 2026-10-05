package com.shop.contract;

import com.shop.domain.Order;
import com.shop.domain.User;

public interface IOrderService {
    Order create(User u);
    void cancel(int id);
}