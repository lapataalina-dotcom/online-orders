package com.shop.core;

import com.shop.contract.IDeliveryService;
import com.shop.contract.INotifier;
import com.shop.contract.IOrderService;
import com.shop.contract.IPayment;
import com.shop.domain.Order;
import com.shop.domain.User;

public class OrderProcessor {

    private final IOrderService orders;
    private final IPayment payment;
    private final IDeliveryService delivery;
    private final INotifier notifier;

    public OrderProcessor(IOrderService orders, IPayment payment, IDeliveryService delivery, INotifier notifier) {
        this.orders = orders;
        this.payment = payment;
        this.delivery = delivery;
        this.notifier = notifier;
    }

    public Order process(User u) {
        Order o = orders.create(u);
        notifier.notify("Создан заказ #" + o.getId());
        return o;
    }

    public void checkout(Order o) {
        double sum = o.total() + delivery.cost();
        if (payment.pay(sum)) {
            delivery.deliver(o);
            notifier.notify("Заказ #" + o.getId() + " оплачен");
        } else {
            notifier.notify("Ошибка оплаты заказа #" + o.getId());
        }
    }
}