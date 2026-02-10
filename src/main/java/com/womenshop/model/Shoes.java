package com.womenshop.model;

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
        if (size < 36 || size > 50) {
            throw new IllegalArgumentException("Shoes size must be between 36 and 50");
        }
        this.size = size;
    }
    
    @Override
    public String toString() {
        return "Shoes{" +
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