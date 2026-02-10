package com.womenshop.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.womenshop.dao.ProductDAO;
import com.womenshop.util.ProductFactory;
import com.womenshop.util.ProductValidator;
import com.womenshop.util.StoreFinance;

public class Store {

    private ProductDAO clothingDAO;
    private ProductDAO shoesDAO;
    private ProductDAO accessoryDAO;

    private StoreFinance finance;

    public Store() {
        this.clothingDAO = new ProductDAO("clothing");
        this.shoesDAO = new ProductDAO("shoes");
        this.accessoryDAO = new ProductDAO("accessory");
        this.finance = new StoreFinance();
    }

    public void updateProduct(Product product) {
        try {
            ProductDAO dao = getDAOForProduct(product);
            Integer size = getProductSize(product);
            dao.update(product.getId(), product.getName(), size);
        } catch (Exception e) {
            throw new RuntimeException("DB Update Failed: " + e.getMessage());
        }
    }

    public void addProduct(Product product) {
        try {
            ProductDAO dao = getDAOForProduct(product);
            Integer size = getProductSize(product);
            dao.insert(product.getName(), product.getPurchasePrice(),
                    product.getSalePrice(), product.getStockQuantity(), size);
        } catch (Exception e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    public void removeProduct(Product product) {
        try {
            ProductValidator.validateDeletion(product.getStockQuantity());
            getDAOForProduct(product).delete(product.getId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void purchaseStock(Product product, int quantity) {
        try {
            ProductValidator.validateQuantity(quantity, "purchase");

            double cost = finance.calculatePurchaseCost(product.getPurchasePrice(), quantity);
            ProductValidator.validateBudget(cost, finance.getCurrentCapital());

            int newStock = product.getStockQuantity() + quantity;
            getDAOForProduct(product).updateStock(product.getId(), newStock);

            finance.recordPurchase(cost);
            product.setStockQuantity(newStock);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sellProduct(Product product, int quantity) {
        try {
            ProductValidator.validateQuantity(quantity, "sell");
            ProductValidator.validateStock(quantity, product.getStockQuantity());

            double revenue = finance.calculateSaleRevenue(product.getEffectivePrice(), quantity);
            int newStock = product.getStockQuantity() - quantity;
            getDAOForProduct(product).updateStock(product.getId(), newStock);

            finance.recordSale(revenue);
            product.setStockQuantity(newStock);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void applyDiscount(String category, double percentage) {
        try {
            getDAOForCategory(category).applyDiscountToAll(percentage);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding discount: " + e.getMessage());
        }
    }

    public void removeAllDiscounts() {
        try {
            clothingDAO.removeAllDiscounts();
            shoesDAO.removeAllDiscounts();
            accessoryDAO.removeAllDiscounts();
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting discount: " + e.getMessage());
        }
    }

    private List<Product> loadFromDAO(ProductDAO dao, String type) throws Exception {
        List<Product> products = new ArrayList<>();
        ResultSet rs = dao.findAll();
        while (rs.next()) {
            products.add(ProductFactory.createFromResultSet(rs, type));
        }
        rs.close();
        return products;
    }

    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        try {
            allProducts.addAll(loadFromDAO(clothingDAO, "Clothing"));
            allProducts.addAll(loadFromDAO(shoesDAO, "Shoes"));
            allProducts.addAll(loadFromDAO(accessoryDAO, "Accessory"));
        } catch (Exception e) {
            throw new RuntimeException("Error loading products: " + e.getMessage());
        }
        return allProducts;
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            return loadFromDAO(getDAOForCategory(category), category);
        } catch (Exception e) {
            throw new RuntimeException("Error while loading product: " + e.getMessage());
        }
    }

    public List<Product> sortProductsByPrice(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparing(Product::getSalePrice))
                .collect(Collectors.toList());
    }

    private ProductDAO getDAOForProduct(Product product) {
        String type = ProductFactory.getProductType(product);
        return getDAOForCategory(type);
    }

    private ProductDAO getDAOForCategory(String category) {
        switch (category) {
            case "Clothing": return clothingDAO;
            case "Shoes": return shoesDAO;
            case "Accessory": return accessoryDAO;
            default: throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

    private Integer getProductSize(Product product) {
        if (product instanceof Clothing) return ((Clothing) product).getSize();
        if (product instanceof Shoes) return ((Shoes) product).getSize();
        return null;
    }

    public double getCurrentCapital() {
        return finance.getCurrentCapital();
    }

    public double getTotalPurchaseCost() {
        return finance.getTotalPurchaseCost();
    }

    public double getTotalSalesRevenue() {
        return finance.getTotalSalesRevenue();
    }

    public double getProfit() {
        return finance.getProfit();
    }

    public double getInitialCapital() {
        return finance.getInitialCapital();
    }

    public String getFinanceSummary() {
        return finance.getSummary();
    }
}