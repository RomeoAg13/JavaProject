package com.womenshop.model;

public class Accessory extends Product {
    
    public Accessory(String name, double purchasePrice, double salePrice) {
        super(name, purchasePrice, salePrice);
    }
    
    @Override
    public String toString() {
        return "Accessory{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", purchasePrice=" + getPurchasePrice() +
                ", salePrice=" + getSalePrice() +
                ", discountPrice=" + getDiscountPrice() +
                ", stock=" + getStockQuantity() +
                '}';
    }
}