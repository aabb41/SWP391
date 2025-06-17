/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.ImportProduct;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DAOImportProduct extends DBConnect {
    
    /**
     * Updates an existing import product record in the database
     * @param importProduct The import product object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateImportProduct(ImportProduct importProduct) {
        int n = 0;
        String sql = "UPDATE [ImportProduct] SET "
                + "[ProductId] = ?, "
                + "[Supplier] = ?, "
                + "[Quantity] = ?, "
                + "[EntryPrice] = ?, "
                + "[import_price] = ?, "
                + "[date_of_import] = ?, "
                + "[status] = ? "
                + "WHERE importProduct = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, importProduct.getProductId());
            pre.setInt(2, importProduct.getSupplier());
            pre.setInt(3, importProduct.getQuantity());
            pre.setBigDecimal(4, importProduct.getEntryPrice());
            pre.setBigDecimal(5, importProduct.getImport_price());
            pre.setTime(6, importProduct.getDate_of_import() != null ? 
                    Time.valueOf(importProduct.getDate_of_import()) : null);
            pre.setInt(7, importProduct.getStatus());
            pre.setString(8, importProduct.getImportProduct().toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new import product record into the database
     * @param importProduct The import product object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertImportProduct(ImportProduct importProduct) {
        int n = 0;
        String sql = "INSERT INTO [ImportProduct]("
                + "[importProduct], "
                + "[ProductId], "
                + "[Supplier], "
                + "[Quantity], "
                + "[EntryPrice], "
                + "[createAt], "
                + "[import_price], "
                + "[date_of_import], "
                + "[status]) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (importProduct.getImportProduct() == null) {
                importProduct.setImportProduct(UUID.randomUUID());
            }
            
            pre.setString(1, importProduct.getImportProduct().toString());
            pre.setInt(2, importProduct.getProductId());
            pre.setInt(3, importProduct.getSupplier());
            pre.setInt(4, importProduct.getQuantity());
            pre.setBigDecimal(5, importProduct.getEntryPrice());
            
            // Set current time if not provided
            LocalTime now = LocalTime.now();
            if (importProduct.getCreateAt() == null) {
                importProduct.setCreateAt(now);
            }
            pre.setTime(6, Time.valueOf(importProduct.getCreateAt()));
            
            pre.setBigDecimal(7, importProduct.getImport_price());
            
            // Set date of import
            pre.setTime(8, importProduct.getDate_of_import() != null ? 
                    Time.valueOf(importProduct.getDate_of_import()) : null);
            
            pre.setInt(9, importProduct.getStatus());
            
            n = pre.executeUpdate();
            
            // If successful, update product quantity
            if (n > 0) {
                updateProductQuantityAfterImport(importProduct.getProductId(), importProduct.getQuantity());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates product quantity after import
     * @param productId The ID of the product
     * @param importQuantity The quantity being imported
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    private int updateProductQuantityAfterImport(int productId, int importQuantity) {
        int n = 0;
        
        try {
            // First get current quantity
            String sqlSelect = "SELECT Quantity FROM Products WHERE ProductId = ?";
            PreparedStatement preSelect = conn.prepareStatement(sqlSelect);
            preSelect.setInt(1, productId);
            ResultSet rs = preSelect.executeQuery();
            
            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity + importQuantity;
                
                // Update product quantity
                String sqlUpdate = "UPDATE Products SET Quantity = ?, [Update] = GETDATE() WHERE ProductId = ?";
                PreparedStatement preUpdate = conn.prepareStatement(sqlUpdate);
                preUpdate.setInt(1, newQuantity);
                preUpdate.setInt(2, productId);
                
                n = preUpdate.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes an import product record from the database
     * @param importProductId The UUID of the import product to remove
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int removeImportProduct(UUID importProductId) {
        int n = 0;
        String sql = "DELETE FROM ImportProduct WHERE importProduct = ?";
        
        try {
            // First get the import product details to restore product quantity
            ImportProduct importProduct = getImportProductById(importProductId);
            
            if (importProduct != null) {
                PreparedStatement pre = conn.prepareStatement(sql);
                pre.setString(1, importProductId.toString());
                
                n = pre.executeUpdate();
                
                // If deletion successful, restore product quantity
                if (n > 0) {
                    restoreProductQuantityAfterImportRemoval(importProduct.getProductId(), importProduct.getQuantity());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Restores product quantity after import removal
     * @param productId The ID of the product
     * @param importQuantity The quantity that was imported
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    private int restoreProductQuantityAfterImportRemoval(int productId, int importQuantity) {
        int n = 0;
        
        try {
            // First get current quantity
            String sqlSelect = "SELECT Quantity FROM Products WHERE ProductId = ?";
            PreparedStatement preSelect = conn.prepareStatement(sqlSelect);
            preSelect.setInt(1, productId);
            ResultSet rs = preSelect.executeQuery();
            
            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity - importQuantity;
                
                // Ensure quantity doesn't go below zero
                if (newQuantity < 0) {
                    newQuantity = 0;
                }
                
                // Update product quantity
                String sqlUpdate = "UPDATE Products SET Quantity = ?, [Update] = GETDATE() WHERE ProductId = ?";
                PreparedStatement preUpdate = conn.prepareStatement(sqlUpdate);
                preUpdate.setInt(1, newQuantity);
                preUpdate.setInt(2, productId);
                
                n = preUpdate.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves import product records from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of ImportProduct objects
     */
    public Vector<ImportProduct> getImportProducts(String sql) {
        Vector<ImportProduct> vector = new Vector<ImportProduct>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                UUID importProductId = UUID.fromString(rs.getString("importProduct"));
                int productId = rs.getInt("ProductId");
                int supplier = rs.getInt("Supplier");
                int quantity = rs.getInt("Quantity");
                BigDecimal entryPrice = rs.getBigDecimal("EntryPrice");
                
                LocalTime createAt = null;
                if (rs.getTime("createAt") != null) {
                    createAt = rs.getTime("createAt").toLocalTime();
                }
                
                BigDecimal importPrice = rs.getBigDecimal("import_price");
                
                LocalTime dateOfImport = null;
                if (rs.getTime("date_of_import") != null) {
                    dateOfImport = rs.getTime("date_of_import").toLocalTime();
                }
                
                int status = rs.getInt("status");
                
                ImportProduct importProduct = new ImportProduct(
                        importProductId, productId, supplier, quantity, entryPrice,
                        createAt, importPrice, dateOfImport, status);
                
                vector.add(importProduct);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all import product records from the database
     * @return Vector of all ImportProduct objects
     */
    public Vector<ImportProduct> getAllImportProducts() {
        return getImportProducts("SELECT * FROM ImportProduct");
    }
    
    /**
     * Retrieves an import product by its ID
     * @param importProductId The UUID of the import product to retrieve
     * @return The ImportProduct object if found, null otherwise
     */
    public ImportProduct getImportProductById(UUID importProductId) {
        String sql = "SELECT * FROM ImportProduct WHERE importProduct = '" + importProductId.toString() + "'";
        Vector<ImportProduct> importProducts = getImportProducts(sql);
        
        if (importProducts.size() > 0) {
            return importProducts.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves import products by product ID
     * @param productId The ID of the product
     * @return Vector of ImportProduct objects for the specified product
     */
    public Vector<ImportProduct> getImportProductsByProductId(int productId) {
        String sql = "SELECT * FROM ImportProduct WHERE ProductId = " + productId;
        return getImportProducts(sql);
    }
    
    /**
     * Retrieves import products by supplier ID
     * @param supplierId The ID of the supplier
     * @return Vector of ImportProduct objects for the specified supplier
     */
    public Vector<ImportProduct> getImportProductsBySupplierId(int supplierId) {
        String sql = "SELECT * FROM ImportProduct WHERE Supplier = " + supplierId;
        return getImportProducts(sql);
    }
    
    /**
     * Updates the status of an import product
     * @param importProductId The UUID of the import product to update
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateImportProductStatus(UUID importProductId, int status) {
        int n = 0;
        String sql = "UPDATE ImportProduct SET status = ? WHERE importProduct = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, importProductId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets total imported quantity for a specific product
     * @param productId The ID of the product
     * @return Total imported quantity
     */
    public int getTotalImportedQuantityByProductId(int productId) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(Quantity) as TotalQuantity FROM ImportProduct WHERE ProductId = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                totalQuantity = rs.getInt("TotalQuantity");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalQuantity;
    }
    
    /**
     * Gets average import price for a specific product
     * @param productId The ID of the product
     * @return Average import price
     */
    public BigDecimal getAverageImportPriceByProductId(int productId) {
        BigDecimal averagePrice = BigDecimal.ZERO;
        String sql = "SELECT AVG(import_price) as AveragePrice FROM ImportProduct WHERE ProductId = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next() && rs.getBigDecimal("AveragePrice") != null) {
                averagePrice = rs.getBigDecimal("AveragePrice");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return averagePrice;
    }
    
    /**
     * Gets the most recent import price for a specific product
     * @param productId The ID of the product
     * @return Most recent import price
     */
    public BigDecimal getMostRecentImportPriceByProductId(int productId) {
        BigDecimal recentPrice = BigDecimal.ZERO;
        String sql = "SELECT TOP 1 import_price FROM ImportProduct WHERE ProductId = ? AND status = 1 ORDER BY date_of_import DESC";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                recentPrice = rs.getBigDecimal("import_price");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return recentPrice;
    }
    
    /**
     * Gets the total import value (quantity * import_price) for a specific product
     * @param productId The ID of the product
     * @return Total import value
     */
    public BigDecimal getTotalImportValueByProductId(int productId) {
        BigDecimal totalValue = BigDecimal.ZERO;
        String sql = "SELECT SUM(Quantity * import_price) as TotalValue FROM ImportProduct WHERE ProductId = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next() && rs.getBigDecimal("TotalValue") != null) {
                totalValue = rs.getBigDecimal("TotalValue");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalValue;
    }
    
    /**
     * Main method for testing the DAOImportProduct functionality
     */
    public static void main(String[] args) {
        DAOImportProduct dao = new DAOImportProduct();
        
        // Test getting all import products
        System.out.println("All import products:");
        Vector<ImportProduct> importProducts = dao.getAllImportProducts();
        for (ImportProduct importProduct : importProducts) {
            System.out.println(importProduct);
        }
        
        // Test inserting a new import product
        ImportProduct newImportProduct = new ImportProduct(
                UUID.randomUUID(), 1, 1, 10, new BigDecimal("100.00"), 
                LocalTime.now(), new BigDecimal("120.00"), LocalTime.now(), 1);
        
        int result = dao.insertImportProduct(newImportProduct);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting import products by product ID
        System.out.println("\nImport products for product ID 1:");
        Vector<ImportProduct> productImports = dao.getImportProductsByProductId(1);
        for (ImportProduct importProduct : productImports) {
            System.out.println(importProduct);
        }
        
        // Test getting total imported quantity
        int totalImported = dao.getTotalImportedQuantityByProductId(1);
        System.out.println("\nTotal imported quantity for product ID 1: " + totalImported);
        
        // Test getting average import price
        BigDecimal avgPrice = dao.getAverageImportPriceByProductId(1);
        System.out.println("\nAverage import price for product ID 1: " + avgPrice);
    }
}
