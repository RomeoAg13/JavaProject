package com.womenshop.model;

import com.womenshop.util.ProductValidator;

public abstract class Product {

    private int id;
    private String name;
    private double purchasePrice;
    private double salePrice;
    private double discountPrice;
    private int stockQuantity;

    public Product(String name, double purchasePrice, double salePrice) {
        setName(name);
        setPurchasePrice(purchasePrice);
        setSalePrice(salePrice);
        this.discountPrice = 0;
        this.stockQuantity = 0;
    }

    public double getEffectivePrice() {
        return discountPrice > 0 ? discountPrice : salePrice;
    }

    public void sell(int quantity) {
        ProductValidator.validateQuantity(quantity, "sell");
        ProductValidator.validateStock(quantity, stockQuantity);
        stockQuantity -= quantity;
    }

    public void purchase(int quantity) {
        ProductValidator.validateQuantity(quantity, "purchase");
        stockQuantity += quantity;
    }

    public void applyDiscount(double percentage) {
        ProductValidator.validatePercentage(percentage);
        discountPrice = salePrice * (1 - percentage / 100.0);
    }

    public void removeDiscount() {
        discountPrice = 0;
    }

    public boolean canBeDeleted() {
        return stockQuantity == 0;
    }

    public boolean hasDiscount() {
        return discountPrice > 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getSalePrice() { return salePrice; }
    public double getDiscountPrice() { return discountPrice; }
    public int getStockQuantity() { return stockQuantity; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        ProductValidator.validateName(name);
        this.name = name;
    }

    public void setPurchasePrice(double purchasePrice) {
        ProductValidator.validatePrice(purchasePrice, "Purchase price");
        if (salePrice > 0) {
            ProductValidator.validatePriceRelationship(purchasePrice, salePrice);
        }
        this.purchasePrice = purchasePrice;
    }

    public void setSalePrice(double salePrice) {
        ProductValidator.validatePrice(salePrice, "Sale price");
        if (purchasePrice > 0) {
            ProductValidator.validatePriceRelationship(purchasePrice, salePrice);
        }
        this.salePrice = salePrice;
    }

    public void setDiscountPrice(double discountPrice) {
        ProductValidator.validatePrice(discountPrice, "Discount price");
        this.discountPrice = discountPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        ProductValidator.validateQuantity(stockQuantity, "stock update");
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, name='%s', purchase=%.2f, sale=%.2f, discount=%.2f, stock=%d}",
                getClass().getSimpleName(), id, name, purchasePrice, salePrice, discountPrice, stockQuantity);
    }
}