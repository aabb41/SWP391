/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Products;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DAOProducts extends DBConnect{
    public int updateProduct(Products product) {
        int n = 0;
        String sql = "UPDATE [Products] SET "
                + "[ProductName] = ?, "
                + "[EntryPrice] = ?, "
                + "[RetailPrice] = ?, "
                + "[Description] = ?, "
                + "[WHOSALEPRICE] = ?, "
                + "[Unit] = ?, "
                + "[Quantity] = ?, "
                + "[CategoryId] = ?, "
                + "[SupplierID] = ?, "
                + "[Update] = ?, "
                + "[status] = ? "
                + "WHERE ProductId = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, product.getProductName());
            pre.setBigDecimal(2, product.getEntryPrice());
            pre.setBigDecimal(3, product.getRetailPrice());
            pre.setString(4, product.getDescription());
            pre.setBigDecimal(5, product.getWHOSALEPRICE());
            pre.setString(6, product.getUnit());
            pre.setInt(7, product.getQuantity());
            pre.setInt(8, product.getCategoryId());
            pre.setInt(9, product.getSupplierID());
            pre.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pre.setInt(11, product.getStatus());
            pre.setInt(12, product.getProductId());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    public int insertProduct(Products product) {
        int n = 0;
        String sql = "INSERT INTO [Products]("
                + "[ProductName], "
                + "[EntryPrice], "
                + "[RetailPrice], "
                + "[Description], "
                + "[WHOSALEPRICE], "
                + "[Unit], "
                + "[Quantity], "
                + "[CategoryId], "
                + "[SupplierID], "
                + "[CreateAt], "
                + "[Update], "
                + "[status]) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, product.getProductName());
            pre.setBigDecimal(2, product.getEntryPrice());
            pre.setBigDecimal(3, product.getRetailPrice());
            pre.setString(4, product.getDescription());
            pre.setBigDecimal(5, product.getWHOSALEPRICE());
            pre.setString(6, product.getUnit());
            pre.setInt(7, product.getQuantity());
            pre.setInt(8, product.getCategoryId());
            pre.setInt(9, product.getSupplierID());
            
            LocalDateTime now = LocalDateTime.now();
            pre.setTimestamp(10, Timestamp.valueOf(now));
            pre.setTimestamp(11, Timestamp.valueOf(now));
            pre.setInt(12, product.getStatus());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    public int removeProduct(int productId) {
        int n = 0;
        String sql = "DELETE FROM Products WHERE ProductId = " + productId;
        
        try {
            // Check if product is referenced in other tables (e.g., OrderDetails)
            String sqlCheck = "SELECT OrderID FROM [OrderDetails] WHERE ProductID = " + productId;
            if (getData(sqlCheck).next()) {
                return -1; // Product is referenced, can't delete
            }
            
            // If not referenced, proceed with deletion
            n = conn.createStatement().executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    public int addProduct(Products product) {
        int n = 0;
        String sql = "INSERT INTO [Products]("
                + "[ProductName], "
                + "[EntryPrice], "
                + "[RetailPrice], "
                + "[Description], "
                + "[WHOSALEPRICE], "
                + "[Unit], "
                + "[Quantity], "
                + "[CategoryId], "
                + "[SupplierID], "
                + "[CreateAt], "
                + "[Update], "
                + "[status]) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, product.getProductName());
            pre.setBigDecimal(2, product.getEntryPrice());
            pre.setBigDecimal(3, product.getRetailPrice());
            pre.setString(4, product.getDescription());
            pre.setBigDecimal(5, product.getWHOSALEPRICE());
            pre.setString(6, product.getUnit());
            pre.setInt(7, product.getQuantity());
            pre.setInt(8, product.getCategoryId());
            pre.setInt(9, product.getSupplierID());
            
            LocalDateTime now = LocalDateTime.now();
            pre.setTimestamp(10, Timestamp.valueOf(now));
            pre.setTimestamp(11, Timestamp.valueOf(now));
            pre.setInt(12, product.getStatus());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    public Vector<Products> getProducts(String sql) {
        Vector<Products> vector = new Vector<Products>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                int productId = rs.getInt(1);
                String productName = rs.getString("ProductName");
                BigDecimal entryPrice = rs.getBigDecimal("EntryPrice");
                BigDecimal retailPrice = rs.getBigDecimal("RetailPrice");
                String description = rs.getString("Description");
                BigDecimal wholesalePrice = rs.getBigDecimal("WHOSALEPRICE");
                String unit = rs.getString("Unit");
                int quantity = rs.getInt("Quantity");
                int categoryId = rs.getInt("CategoryId");
                int supplierId = rs.getInt("SupplierID");
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("CreateAt") != null) {
                    createAt = rs.getTimestamp("CreateAt").toLocalDateTime();
                }
                
                LocalDateTime update = null;
                if (rs.getTimestamp("Update") != null) {
                    update = rs.getTimestamp("Update").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                Products pro = new Products(productId, productName, entryPrice, retailPrice, 
                        description, wholesalePrice, unit, quantity, categoryId, 
                        supplierId, createAt, update, status);
                
                vector.add(pro);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    public Products getProductById(int productId) {
        String sql = "SELECT * FROM Products WHERE ProductId = " + productId;
        Vector<Products> products = getProducts(sql);
        
        if (products.size() > 0) {
            return products.get(0);
        }
        
        return null;
    }
    
    public Vector<Products> getProductsByCategoryId(int categoryId) {
        String sql = "SELECT * FROM Products WHERE CategoryId = " + categoryId;
        return getProducts(sql);
    }
    
    public Vector<Products> searchProducts(String keyword) {
        String sql = "SELECT * FROM Products WHERE ProductName LIKE '%" + keyword + "%' OR Description LIKE '%" + keyword + "%'";
        return getProducts(sql);
    }
    
    public int updateProductQuantity(int productId, int newQuantity) {
        int n = 0;
        String sql = "UPDATE Products SET Quantity = ?, [Update] = ? WHERE ProductId = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, newQuantity);
            pre.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pre.setInt(3, productId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    public static void main(String[] args) {
        DAOProducts dao = new DAOProducts();
        
        // Test getting all products
        Vector<Products> products = dao.getProducts("SELECT * FROM Products");
        System.out.println("All products:");
        for (Products product : products) {
            System.out.println(product);
        }
        
    }
}
