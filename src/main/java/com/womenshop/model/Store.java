package com.womenshop.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.womenshop.dao.ProductDAO;

public class Store {
    
    private ProductDAO clothingDAO;
    private ProductDAO shoesDAO;
    private ProductDAO accessoryDAO;
    
    private static final double INITIAL_CAPITAL = 30000.0;
    private double totalPurchaseCost = 0.0;
    private double totalSalesRevenue = 0.0;
    
    public Store() {
        this.clothingDAO = new ProductDAO("clothing");
        this.shoesDAO = new ProductDAO("shoes");
        this.accessoryDAO = new ProductDAO("accessory");
    }

    public void updateProduct(Product product) {
    try {
        ProductDAO dao = getDAOForProduct(product);
        Integer size = null;
        if (product instanceof Clothing) size = ((Clothing) product).getSize();
        if (product instanceof Shoes) size = ((Shoes) product).getSize();
        
        dao.update(product.getId(), product.getName(), size);
    } catch (Exception e) {
        throw new RuntimeException("DB Update Failed: " + e.getMessage());
    }
}
    
    public void addProduct(Product product) {
        try {
            if (product instanceof Clothing) {
                Clothing c = (Clothing) product;
                clothingDAO.insert(c.getName(), c.getPurchasePrice(), c.getSalePrice(), 
                                  c.getStockQuantity(), c.getSize());
            } else if (product instanceof Shoes) {
                Shoes s = (Shoes) product;
                shoesDAO.insert(s.getName(), s.getPurchasePrice(), s.getSalePrice(), 
                               s.getStockQuantity(), s.getSize());
            } else if (product instanceof Accessory) {
                Accessory a = (Accessory) product;
                accessoryDAO.insert(a.getName(), a.getPurchasePrice(), a.getSalePrice(), 
                                   a.getStockQuantity(), null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }
    
    public void removeProduct(Product product) {
        try {
            if (product.getStockQuantity() > 0) {
                throw new IllegalStateException("Produit impossible à supprimer, stock toujours disponible");
            }
            
            ProductDAO dao = getDAOForProduct(product);
            dao.delete(product.getId());
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public void purchaseStock(Product product, int quantity) {
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
            
            double cost = quantity * product.getPurchasePrice();
            
            if (getCurrentCapital() < cost) {
                throw new IllegalStateException("Insufficient budget");
            }
            
            ProductDAO dao = getDAOForProduct(product);
            int newStock = product.getStockQuantity() + quantity;
            dao.updateStock(product.getId(), newStock);
            
            totalPurchaseCost += cost;
            product.setStockQuantity(newStock);
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public void sellProduct(Product product, int quantity) {
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantité doit etre positive");
            }
            
            if (quantity > product.getStockQuantity()) {
                throw new IllegalStateException("Stock insuffisant");
            }
            
            double price = product.getEffectivePrice();
            double revenue = quantity * price;
            
            ProductDAO dao = getDAOForProduct(product);
            int newStock = product.getStockQuantity() - quantity;
            dao.updateStock(product.getId(), newStock);
            
            totalSalesRevenue += revenue;
            product.setStockQuantity(newStock);
            
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public void applyDiscount(String category, double percentage) {
        try {
            ProductDAO dao;
            
            if (category.equals("Clothing")) {
                dao = clothingDAO;
            } else if (category.equals("Shoes")) {
                dao = shoesDAO;
            } else if (category.equals("Accessory")) {
                dao = accessoryDAO;
            } else {
                throw new IllegalArgumentException("Unknown category: " + category);
            }
            
            dao.applyDiscountToAll(percentage);
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout des promos: " + e.getMessage());
        }
    }
    
    public void removeAllDiscounts() {
        try {
            clothingDAO.removeAllDiscounts();
            shoesDAO.removeAllDiscounts();
            accessoryDAO.removeAllDiscounts();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression des promos: " + e.getMessage());
        }
    }
    
    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        
        try {
            ResultSet rs = clothingDAO.findAll();
            while (rs.next()) {
                Clothing c = new Clothing(
                    rs.getString("name"),
                    rs.getDouble("price_buy"),
                    rs.getDouble("price_sell"),
                    rs.getInt("size")
                );
                c.setId(rs.getInt("id"));
                c.setStockQuantity(rs.getInt("stock"));
                c.setDiscountPrice(rs.getDouble("discount_price"));
                allProducts.add(c);
            }
            rs.close();
            
            rs = shoesDAO.findAll();
            while (rs.next()) {
                Shoes s = new Shoes(
                    rs.getString("name"),
                    rs.getDouble("price_buy"),
                    rs.getDouble("price_sell"),
                    rs.getInt("size")
                );
                s.setId(rs.getInt("id"));
                s.setStockQuantity(rs.getInt("stock"));
                s.setDiscountPrice(rs.getDouble("discount_price"));
                allProducts.add(s);
            }
            rs.close();
            
            rs = accessoryDAO.findAll();
            while (rs.next()) {
                Accessory a = new Accessory(
                    rs.getString("name"),
                    rs.getDouble("price_buy"),
                    rs.getDouble("price_sell")
                );
                a.setId(rs.getInt("id"));
                a.setStockQuantity(rs.getInt("stock"));
                a.setDiscountPrice(rs.getDouble("discount_price"));
                allProducts.add(a);
            }
            rs.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Error loading products: " + e.getMessage());
        }
        
        return allProducts;
    }
    
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        
        try {
            ProductDAO dao;
            
            if (category.equals("Clothing")) {
                dao = clothingDAO;
                ResultSet rs = dao.findAll();
                while (rs.next()) {
                    Clothing c = new Clothing(
                        rs.getString("name"),
                        rs.getDouble("price_buy"),
                        rs.getDouble("price_sell"),
                        rs.getInt("size")
                    );
                    c.setId(rs.getInt("id"));
                    c.setStockQuantity(rs.getInt("stock"));
                    c.setDiscountPrice(rs.getDouble("discount_price"));
                    products.add(c);
                }
                rs.close();
                
            } else if (category.equals("Shoes")) {
                dao = shoesDAO;
                ResultSet rs = dao.findAll();
                while (rs.next()) {
                    Shoes s = new Shoes(
                        rs.getString("name"),
                        rs.getDouble("price_buy"),
                        rs.getDouble("price_sell"),
                        rs.getInt("size")
                    );
                    s.setId(rs.getInt("id"));
                    s.setStockQuantity(rs.getInt("stock"));
                    s.setDiscountPrice(rs.getDouble("discount_price"));
                    products.add(s);
                }
                rs.close();
                
            } else if (category.equals("Accessory")) {
                dao = accessoryDAO;
                ResultSet rs = dao.findAll();
                while (rs.next()) {
                    Accessory a = new Accessory(
                        rs.getString("name"),
                        rs.getDouble("price_buy"),
                        rs.getDouble("price_sell")
                    );
                    a.setId(rs.getInt("id"));
                    a.setStockQuantity(rs.getInt("stock"));
                    a.setDiscountPrice(rs.getDouble("discount_price"));
                    products.add(a);
                }
                rs.close();
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error loading products by category: " + e.getMessage());
        }
        
        return products;
    }
    
    public List<Product> sortProductsByPrice(List<Product> products) {
        return products.stream()
                      .sorted(Comparator.comparing(Product::getSalePrice))
                      .collect(Collectors.toList());
    }
    
    private ProductDAO getDAOForProduct(Product product) {
        if (product instanceof Clothing) {
            return clothingDAO;
        } else if (product instanceof Shoes) {
            return shoesDAO;
        } else if (product instanceof Accessory) {
            return accessoryDAO;
        }
        throw new IllegalArgumentException("Unknown product type");
    }
    
    public double getCurrentCapital() {
        return INITIAL_CAPITAL + totalSalesRevenue - totalPurchaseCost;
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
    
    public double getInitialCapital() {
        return INITIAL_CAPITAL;
    }
}