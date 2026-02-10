package com.womenshop.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.womenshop.model.Accessory;
import com.womenshop.model.Clothing;
import com.womenshop.model.Product;
import com.womenshop.model.Shoes;

public class ProductFactory {

    public static Product createFromResultSet(ResultSet rs, String type) throws SQLException {
        Product product;

        switch (type) {
            case "Clothing":
                product = new Clothing(
                        rs.getString("name"),
                        rs.getDouble("price_buy"),
                        rs.getDouble("price_sell"),
                        rs.getInt("size")
                );
                break;

            case "Shoes":
                product = new Shoes(
                        rs.getString("name"),
                        rs.getDouble("price_buy"),
                        rs.getDouble("price_sell"),
                        rs.getInt("size")
                );
                break;

            case "Accessory":
                product = new Accessory(
                        rs.getString("name"),
                        rs.getDouble("price_buy"),
                        rs.getDouble("price_sell")
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown product type: " + type);
        }

        product.setId(rs.getInt("id"));
        product.setStockQuantity(rs.getInt("stock"));
        product.setDiscountPrice(rs.getDouble("discount_price"));

        return product;
    }

    public static String getProductType(Product product) {
        if (product instanceof Clothing) return "Clothing";
        if (product instanceof Shoes) return "Shoes";
        if (product instanceof Accessory) return "Accessory";
        throw new IllegalArgumentException("Unknown product type: " + product.getClass().getName());
    }
}