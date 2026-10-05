package com.shop.notify;

import com.shop.contract.INotifier;

public class EmailNotifier implements INotifier {

    private String host;

    public EmailNotifier(String host) {
        this.host = host;
    }

    @Override
    public void notify(String msg) {
        System.out.printf("Email через %s: %s%n", host, msg);
    }
}