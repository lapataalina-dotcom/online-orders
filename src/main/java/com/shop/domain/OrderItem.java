package com.shop.domain;

public class OrderItem {

    private final Product product;
    private int qty;

    public OrderItem(Product product, int qty) {
        this.product = product;
        this.qty = qty;
    }

    public double subtotal() {
        return product.getPrice() * qty;
    }

    public Product getProduct() { return product; }
}