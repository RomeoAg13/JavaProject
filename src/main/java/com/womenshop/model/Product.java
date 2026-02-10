package com.womenshop.model;

public abstract class Product {
    
    private int id;
    private String name;
    private double purchasePrice; 
    private double salePrice;     
    private double discountPrice; 
    private int stockQuantity;    
    
    public Product(String name, double purchasePrice, double salePrice) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.discountPrice = 0;   
        this.stockQuantity = 0;
    }

    public double getEffectivePrice() {
        if (discountPrice > 0) {
            return discountPrice;
        }
        return salePrice;
    }
    
    public void sell(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        stockQuantity -= quantity;
    }
    
    public void purchase(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        stockQuantity += quantity;
    }
    
    public void applyDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        discountPrice = salePrice * (1 - percentage / 100.0);
    }
    
    public void removeDiscount() {
        discountPrice = 0;
    }
    
    public boolean canBeDeleted() {
        return stockQuantity == 0;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPurchasePrice() {
        return purchasePrice;
    }
    
    public void setPurchasePrice(double purchasePrice) {
        if (purchasePrice < 0) {
            throw new IllegalArgumentException("Purchase price cannot be negative");
        }
        if (purchasePrice > salePrice) {
            throw new IllegalArgumentException("Purchase price cannot be greater than sale price");
        }
        this.purchasePrice = purchasePrice;
    }
    
    public double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(double salePrice) {
        if (salePrice < 0) {
            throw new IllegalArgumentException("Sale price cannot be negative");
        }
        if (salePrice < purchasePrice) {
            throw new IllegalArgumentException("Sale price cannot be less than purchase price");
        }
        this.salePrice = salePrice;
    }
    
    public double getDiscountPrice() {
        return discountPrice;
    }
    
    public void setDiscountPrice(double discountPrice) {
        if (discountPrice < 0) {
            throw new IllegalArgumentException("Discount price cannot be negative");
        }
        this.discountPrice = discountPrice;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = stockQuantity;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", salePrice=" + salePrice +
                ", discountPrice=" + discountPrice +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
