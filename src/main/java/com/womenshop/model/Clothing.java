package com.womenshop.model;

public class Clothing extends Product {
    
    private int size;
    
    public Clothing(String name, double purchasePrice, double salePrice, int size) {
        super(name, purchasePrice, salePrice);
        setSize(size);
    }
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size < 34 || size > 54) {
            throw new IllegalArgumentException("Clothing size must be between 34 and 54");
        }
        this.size = size;
    }
    
    @Override
    public String toString() {
        return "Clothing{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", size=" + size +
                ", purchasePrice=" + getPurchasePrice() +
                ", salePrice=" + getSalePrice() +
                ", discountPrice=" + getDiscountPrice() +
                ", stock=" + getStockQuantity() +
                '}';
    }
}