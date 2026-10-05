package com.shop.delivery;

import com.shop.contract.IDeliveryService;
import com.shop.domain.Order;

public class CourierDelivery implements IDeliveryService {

    private String addr;

    public CourierDelivery(String addr) {
        this.addr = addr;
    }

    @Override
    public void deliver(Order o) {
        System.out.printf("Курьер доставит заказ #%d по адресу %s%n", o.getId(), addr);
    }

    @Override
    public double cost() {
        return 350.0;
    }
}