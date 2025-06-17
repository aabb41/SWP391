/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.ExportProduct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DAOExportProduct extends DBConnect {
    
    /**
     * Updates an existing export product record in the database
     * @param exportProduct The export product object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateExportProduct(ExportProduct exportProduct) {
        int n = 0;
        String sql = "UPDATE [ExportProduct] SET "
                + "[ProductId] = ?, "
                + "[SupplierID] = ?, "
                + "[Quantity] = ?, "
                + "[date_of_export] = ?, "
                + "[status] = ? "
                + "WHERE exportProductId = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, exportProduct.getProductId());
            pre.setInt(2, exportProduct.getSupplierID());
            pre.setInt(3, exportProduct.getQuantity());
            pre.setTimestamp(4, exportProduct.getDate_of_export() != null ? 
                    Timestamp.valueOf(exportProduct.getDate_of_export()) : null);
            pre.setInt(5, exportProduct.getStatus());
            pre.setString(6, exportProduct.getExportProductId().toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new export product record into the database
     * @param exportProduct The export product object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertExportProduct(ExportProduct exportProduct) {
        int n = 0;
        String sql = "INSERT INTO [ExportProduct]("
                + "[exportProductId], "
                + "[ProductId], "
                + "[SupplierID], "
                + "[Quantity], "
                + "[CreataAt], "
                + "[date_of_export], "
                + "[status]) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (exportProduct.getExportProductId() == null) {
                exportProduct.setExportProductId(UUID.randomUUID());
            }
            
            pre.setString(1, exportProduct.getExportProductId().toString());
            pre.setInt(2, exportProduct.getProductId());
            pre.setInt(3, exportProduct.getSupplierID());
            pre.setInt(4, exportProduct.getQuantity());
            
            // Set current time if not provided
            LocalDateTime now = LocalDateTime.now();
            if (exportProduct.getCreataAt() == null) {
                exportProduct.setCreataAt(now);
            }
            pre.setTimestamp(5, Timestamp.valueOf(exportProduct.getCreataAt()));
            
            // Set date of export
            pre.setTimestamp(6, exportProduct.getDate_of_export() != null ? 
                    Timestamp.valueOf(exportProduct.getDate_of_export()) : null);
            
            pre.setInt(7, exportProduct.getStatus());
            
            n = pre.executeUpdate();
            
            // If successful, update product quantity
            if (n > 0) {
                updateProductQuantityAfterExport(exportProduct.getProductId(), exportProduct.getQuantity());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates product quantity after export
     * @param productId The ID of the product
     * @param exportQuantity The quantity being exported
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    private int updateProductQuantityAfterExport(int productId, int exportQuantity) {
        int n = 0;
        
        try {
            // First get current quantity
            String sqlSelect = "SELECT Quantity FROM Products WHERE ProductId = ?";
            PreparedStatement preSelect = conn.prepareStatement(sqlSelect);
            preSelect.setInt(1, productId);
            ResultSet rs = preSelect.executeQuery();
            
            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity - exportQuantity;
                
                // Ensure quantity doesn't go below zero
                if (newQuantity < 0) {
                    newQuantity = 0;
                }
                
                // Update product quantity
                String sqlUpdate = "UPDATE Products SET Quantity = ?, [Update] = ? WHERE ProductId = ?";
                PreparedStatement preUpdate = conn.prepareStatement(sqlUpdate);
                preUpdate.setInt(1, newQuantity);
                preUpdate.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preUpdate.setInt(3, productId);
                
                n = preUpdate.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes an export product record from the database
     * @param exportProductId The UUID of the export product to remove
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int removeExportProduct(UUID exportProductId) {
        int n = 0;
        String sql = "DELETE FROM ExportProduct WHERE exportProductId = ?";
        
        try {
            // First get the export product details to restore product quantity
            ExportProduct exportProduct = getExportProductById(exportProductId);
            
            if (exportProduct != null) {
                PreparedStatement pre = conn.prepareStatement(sql);
                pre.setString(1, exportProductId.toString());
                
                n = pre.executeUpdate();
                
                // If deletion successful, restore product quantity
                if (n > 0) {
                    restoreProductQuantityAfterExportRemoval(exportProduct.getProductId(), exportProduct.getQuantity());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Restores product quantity after export removal
     * @param productId The ID of the product
     * @param exportQuantity The quantity that was exported
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    private int restoreProductQuantityAfterExportRemoval(int productId, int exportQuantity) {
        int n = 0;
        
        try {
            // First get current quantity
            String sqlSelect = "SELECT Quantity FROM Products WHERE ProductId = ?";
            PreparedStatement preSelect = conn.prepareStatement(sqlSelect);
            preSelect.setInt(1, productId);
            ResultSet rs = preSelect.executeQuery();
            
            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity + exportQuantity;
                
                // Update product quantity
                String sqlUpdate = "UPDATE Products SET Quantity = ?, [Update] = ? WHERE ProductId = ?";
                PreparedStatement preUpdate = conn.prepareStatement(sqlUpdate);
                preUpdate.setInt(1, newQuantity);
                preUpdate.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                preUpdate.setInt(3, productId);
                
                n = preUpdate.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves export product records from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of ExportProduct objects
     */
    public Vector<ExportProduct> getExportProducts(String sql) {
        Vector<ExportProduct> vector = new Vector<ExportProduct>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                UUID exportProductId = UUID.fromString(rs.getString("exportProductId"));
                int productId = rs.getInt("ProductId");
                int supplierId = rs.getInt("SupplierID");
                int quantity = rs.getInt("Quantity");
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("CreataAt") != null) {
                    createAt = rs.getTimestamp("CreataAt").toLocalDateTime();
                }
                
                LocalDateTime dateOfExport = null;
                if (rs.getTimestamp("date_of_export") != null) {
                    dateOfExport = rs.getTimestamp("date_of_export").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                ExportProduct exportProduct = new ExportProduct(
                        exportProductId, productId, supplierId, quantity, 
                        createAt, dateOfExport, status);
                
                vector.add(exportProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all export product records from the database
     * @return Vector of all ExportProduct objects
     */
    public Vector<ExportProduct> getAllExportProducts() {
        return getExportProducts("SELECT * FROM ExportProduct");
    }
    
    /**
     * Retrieves an export product by its ID
     * @param exportProductId The UUID of the export product to retrieve
     * @return The ExportProduct object if found, null otherwise
     */
    public ExportProduct getExportProductById(UUID exportProductId) {
        String sql = "SELECT * FROM ExportProduct WHERE exportProductId = '" + exportProductId.toString() + "'";
        Vector<ExportProduct> exportProducts = getExportProducts(sql);
        
        if (exportProducts.size() > 0) {
            return exportProducts.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves export products by product ID
     * @param productId The ID of the product
     * @return Vector of ExportProduct objects for the specified product
     */
    public Vector<ExportProduct> getExportProductsByProductId(int productId) {
        String sql = "SELECT * FROM ExportProduct WHERE ProductId = " + productId;
        return getExportProducts(sql);
    }
    
    /**
     * Retrieves export products by supplier ID
     * @param supplierId The ID of the supplier
     * @return Vector of ExportProduct objects for the specified supplier
     */
    public Vector<ExportProduct> getExportProductsBySupplierId(int supplierId) {
        String sql = "SELECT * FROM ExportProduct WHERE SupplierID = " + supplierId;
        return getExportProducts(sql);
    }
    
    /**
     * Retrieves export products by date range
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return Vector of ExportProduct objects in the specified date range
     */
    public Vector<ExportProduct> getExportProductsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM ExportProduct WHERE date_of_export BETWEEN ? AND ?";
        Vector<ExportProduct> vector = new Vector<ExportProduct>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(startDate));
            pre.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID exportProductId = UUID.fromString(rs.getString("exportProductId"));
                int productId = rs.getInt("ProductId");
                int supplierId = rs.getInt("SupplierID");
                int quantity = rs.getInt("Quantity");
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("CreataAt") != null) {
                    createAt = rs.getTimestamp("CreataAt").toLocalDateTime();
                }
                
                LocalDateTime dateOfExport = null;
                if (rs.getTimestamp("date_of_export") != null) {
                    dateOfExport = rs.getTimestamp("date_of_export").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                ExportProduct exportProduct = new ExportProduct(
                        exportProductId, productId, supplierId, quantity, 
                        createAt, dateOfExport, status);
                
                vector.add(exportProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Updates the status of an export product
     * @param exportProductId The UUID of the export product to update
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateExportProductStatus(UUID exportProductId, int status) {
        int n = 0;
        String sql = "UPDATE ExportProduct SET status = ? WHERE exportProductId = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, exportProductId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets total exported quantity for a specific product
     * @param productId The ID of the product
     * @return Total exported quantity
     */
    public int getTotalExportedQuantityByProductId(int productId) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(Quantity) as TotalQuantity FROM ExportProduct WHERE ProductId = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                totalQuantity = rs.getInt("TotalQuantity");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalQuantity;
    }
    
    /**
     * Main method for testing the DAOExportProduct functionality
     */
    public static void main(String[] args) {
        DAOExportProduct dao = new DAOExportProduct();
        
        // Test getting all export products
        System.out.println("All export products:");
        Vector<ExportProduct> exportProducts = dao.getAllExportProducts();
        for (ExportProduct exportProduct : exportProducts) {
            System.out.println(exportProduct);
        }
        
        // Test inserting a new export product
        ExportProduct newExportProduct = new ExportProduct(
                UUID.randomUUID(), 1, 1, 5, 
                LocalDateTime.now(), LocalDateTime.now(), 1);
        
        int result = dao.insertExportProduct(newExportProduct);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting export products by product ID
        System.out.println("\nExport products for product ID 1:");
        Vector<ExportProduct> productExports = dao.getExportProductsByProductId(1);
        for (ExportProduct exportProduct : productExports) {
            System.out.println(exportProduct);
        }
        
        // Test getting total exported quantity
        int totalExported = dao.getTotalExportedQuantityByProductId(1);
        System.out.println("\nTotal exported quantity for product ID 1: " + totalExported);
    }
}
