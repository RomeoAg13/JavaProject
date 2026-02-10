package com.womenshop.model;

import com.womenshop.util.ProductValidator;

public class Shoes extends Product {

    private int size;

    public Shoes(String name, double purchasePrice, double salePrice, int size) {
        super(name, purchasePrice, salePrice);
        setSize(size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        ProductValidator.validateShoeSize(size);
        this.size = size;
    }

    @Override
    public String toString() {
        return String.format("Shoes{id=%d, name='%s', size=%d, purchase=%.2f, sale=%.2f, discount=%.2f, stock=%d}",
                getId(), getName(), size, getPurchasePrice(), getSalePrice(), getDiscountPrice(), getStockQuantity());
    }
}