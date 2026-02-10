package com.womenshop.util;

public class ProductValidator {

    public static void validateQuantity(int quantity, String operation) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity for " + operation + " must be positive, got: " + quantity
            );
        }
    }

    public static void validateStock(int requested, int available) {
        if (requested > available) {
            throw new IllegalArgumentException(
                    "Insufficient stock: requested " + requested + ", available " + available
            );
        }
    }

    public static void validatePrice(double price, String priceType) {
        if (price < 0) {
            throw new IllegalArgumentException(priceType + " cannot be negative");
        }
    }

    public static void validatePriceRelationship(double purchasePrice, double salePrice) {
        if (purchasePrice > salePrice) {
            throw new IllegalArgumentException(
                    "Purchase price (" + purchasePrice + ") cannot exceed sale price (" + salePrice + ")"
            );
        }
    }

    public static void validatePercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException(
                    "Percentage must be between 0 and 100, got: " + percentage
            );
        }
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    public static void validateClothingSize(int size) {
        if (size < 34 || size > 54) {
            throw new IllegalArgumentException(
                    "Clothing size must be between 34 and 54, got: " + size
            );
        }
    }

    public static void validateShoeSize(int size) {
        if (size < 36 || size > 50) {
            throw new IllegalArgumentException(
                    "Shoe size must be between 36 and 50, got: " + size
            );
        }
    }

    public static void validateDeletion(int stockQuantity) {
        if (stockQuantity > 0) {
            throw new IllegalStateException(
                    "Cannot delete product with stock: " + stockQuantity + " items remaining"
            );
        }
    }

    public static void validateBudget(double required, double available) {
        if (required > available) {
            throw new IllegalStateException(
                    "Insufficient budget: required " + String.format("%.2f", required) +
                            "€, available " + String.format("%.2f", available) + "€"
            );
        }
    }
}