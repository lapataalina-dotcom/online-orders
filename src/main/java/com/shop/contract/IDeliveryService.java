package com.shop.contract;

import com.shop.domain.Order;

public interface IDeliveryService {
    void deliver(Order o);
    double cost();
}