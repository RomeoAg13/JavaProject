package com.womenshop.util;

public class StoreFinance {

    private static final double INITIAL_CAPITAL = 30000.0;
    private double totalPurchaseCost;
    private double totalSalesRevenue;

    public StoreFinance() {
        this.totalPurchaseCost = 0.0;
        this.totalSalesRevenue = 0.0;
    }

    public void recordPurchase(double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Purchase cost cannot be negative");
        }
        totalPurchaseCost += cost;
    }

    public double calculatePurchaseCost(double unitPrice, int quantity) {
        return unitPrice * quantity;
    }

    public void recordSale(double revenue) {
        if (revenue < 0) {
            throw new IllegalArgumentException("Sales revenue cannot be negative");
        }
        totalSalesRevenue += revenue;
    }

    public double calculateSaleRevenue(double unitPrice, int quantity) {
        return unitPrice * quantity;
    }

    public double getCurrentCapital() {
        return INITIAL_CAPITAL + totalSalesRevenue - totalPurchaseCost;
    }

    public boolean hasSufficientBudget(double required) {
        return getCurrentCapital() >= required;
    }

    public double getInitialCapital() {
        return INITIAL_CAPITAL;
    }

    public double getTotalPurchaseCost() {
        return totalPurchaseCost;
    }

    public double getTotalSalesRevenue() {
        return totalSalesRevenue;
    }

    public double getProfit() {
        return totalSalesRevenue - totalPurchaseCost;
    }

    public double getProfitMargin() {
        if (totalSalesRevenue == 0) return 0;
        return (getProfit() / totalSalesRevenue) * 100;
    }

    public String getSummary() {
        return String.format(
                "Finance Summary:%n" +
                        "  Initial Capital: %.2f €%n" +
                        "  Current Capital: %.2f €%n" +
                        "  Total Purchases: %.2f €%n" +
                        "  Total Sales: %.2f €%n" +
                        "  Profit: %.2f € (%.1f%% margin)",
                INITIAL_CAPITAL, getCurrentCapital(), totalPurchaseCost,
                totalSalesRevenue, getProfit(), getProfitMargin()
        );
    }
}