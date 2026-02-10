package com.womenshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.womenshop.util.DatabaseConnection;

public class ProductDAO {
    private String tableName;

    public ProductDAO(String tableName) {
        this.tableName = tableName;
    }

    public void insert(String name, double buy, double sell, int stock, Integer size) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt;

        if (size == null) {
            String sql = "INSERT INTO " + tableName +
                    " (name, price_buy, price_sell, stock) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, buy);
            pstmt.setDouble(3, sell);
            pstmt.setInt(4, stock);
        } else {
            String sql = "INSERT INTO " + tableName +
                    " (name, price_buy, price_sell, stock, size) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, buy);
            pstmt.setDouble(3, sell);
            pstmt.setInt(4, stock);
            pstmt.setInt(5, size);
        }

        pstmt.executeUpdate();
        pstmt.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public ResultSet findAll() throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public void updateStock(int id, int newStock) throws SQLException {
        String sql = "UPDATE " + tableName + " SET stock = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newStock);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                System.err.println("Alerte : Aucun produit trouvé avec l'ID " + id + " dans la table " + tableName);
            } else {
                System.out.println("Stock mis à jour avec succès dans " + tableName);
            }
        }
    }

    public void applyDiscountToAll(double discountPercent) throws SQLException {
        String sql = "UPDATE " + tableName + " SET discount_price = price_sell * (1 - ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, discountPercent / 100.0);
            pstmt.executeUpdate();
        }
    }

    public void removeAllDiscounts() throws SQLException {
        String sql = "UPDATE " + tableName + " SET discount_price = 0";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
    }


    public void update(int id, String name, Integer size) throws SQLException {
        String sql;
        if (size == null) {
            sql = "UPDATE " + tableName + " SET name = ? WHERE id = ?";
        } else {
            sql = "UPDATE " + tableName + " SET name = ?, size = ? WHERE id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            if (size == null) {
                pstmt.setInt(2, id);
            } else {
                pstmt.setInt(2, size);
                pstmt.setInt(3, id);
            }
            pstmt.executeUpdate();
        }
    }
}