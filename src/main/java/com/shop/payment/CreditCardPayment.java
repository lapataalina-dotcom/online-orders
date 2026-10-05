package com.shop.payment;

import com.shop.contract.IPayment;

public class CreditCardPayment implements IPayment {

    private String card;

    public CreditCardPayment(String card) {
        this.card = card;
    }

    @Override
    public boolean pay(double sum) {
        System.out.printf("Оплата картой %s на %.2f%n", card, sum);
        return true;
    }
}