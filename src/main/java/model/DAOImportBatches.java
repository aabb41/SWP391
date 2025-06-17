/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Import_Batches;
import java.math.BigDecimal;
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
public class DAOImportBatches extends DBConnect {
    
    /**
     * Updates an existing import batch in the database
     * @param importBatch The import batch object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateImportBatch(Import_Batches importBatch) {
        int n = 0;
        String sql = "UPDATE [Import_Batches] SET "
                + "[SupplierID] = ?, "
                + "[TotalPrice] = ?, "
                + "[date_of_import] = ?, "
                + "[status] = ? "
                + "WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, importBatch.getSupplierID());
            pre.setBigDecimal(2, importBatch.getTotalPrice());
            pre.setTimestamp(3, importBatch.getDate_of_import() != null ? 
                    Timestamp.valueOf(importBatch.getDate_of_import()) : null);
            pre.setInt(4, importBatch.getStatus());
            pre.setString(5, importBatch.getBatch_id().toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new import batch into the database
     * @param importBatch The import batch object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertImportBatch(Import_Batches importBatch) {
        int n = 0;
        String sql = "INSERT INTO [Import_Batches]("
                + "[batch_id], "
                + "[SupplierID], "
                + "[createAt], "
                + "[TotalPrice], "
                + "[date_of_import], "
                + "[status]) "
                + "VALUES(?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (importBatch.getBatch_id() == null) {
                importBatch.setBatch_id(UUID.randomUUID());
            }
            
            pre.setString(1, importBatch.getBatch_id().toString());
            pre.setInt(2, importBatch.getSupplierID());
            
            // Set current time if not provided
            LocalDateTime now = LocalDateTime.now();
            if (importBatch.getCreateAt() == null) {
                importBatch.setCreateAt(now);
            }
            pre.setTimestamp(3, Timestamp.valueOf(importBatch.getCreateAt()));
            
            pre.setBigDecimal(4, importBatch.getTotalPrice());
            
            // Set date of import
            pre.setTimestamp(5, importBatch.getDate_of_import() != null ? 
                    Timestamp.valueOf(importBatch.getDate_of_import()) : null);
            
            pre.setInt(6, importBatch.getStatus());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes an import batch from the database
     * @param batchId The UUID of the import batch to remove
     * @return Number of rows affected (1 if successful, 0 if not, -1 if batch has related products)
     */
    public int removeImportBatch(UUID batchId) {
        int n = 0;
        
        try {
            // Check if batch has related products in ImportProduct table
            String checkSql = "SELECT COUNT(*) as count FROM ImportProduct WHERE batch_id = ?";
            PreparedStatement checkPre = conn.prepareStatement(checkSql);
            checkPre.setString(1, batchId.toString());
            ResultSet checkRs = checkPre.executeQuery();
            
            if (checkRs.next() && checkRs.getInt("count") > 0) {
                return -1; // Batch has related products, can't delete
            }
            
            // If no related products, proceed with deletion
            String sql = "DELETE FROM Import_Batches WHERE batch_id = ?";
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves import batches from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Import_Batches objects
     */
    public Vector<Import_Batches> getImportBatches(String sql) {
        Vector<Import_Batches> vector = new Vector<Import_Batches>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                UUID batchId = UUID.fromString(rs.getString("batch_id"));
                int supplierId = rs.getInt("SupplierID");
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("createAt") != null) {
                    createAt = rs.getTimestamp("createAt").toLocalDateTime();
                }
                
                BigDecimal totalPrice = rs.getBigDecimal("TotalPrice");
                
                LocalDateTime dateOfImport = null;
                if (rs.getTimestamp("date_of_import") != null) {
                    dateOfImport = rs.getTimestamp("date_of_import").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                Import_Batches importBatch = new Import_Batches(
                        batchId, supplierId, createAt, totalPrice, dateOfImport, status);
                
                vector.add(importBatch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all import batches from the database
     * @return Vector of all Import_Batches objects
     */
    public Vector<Import_Batches> getAllImportBatches() {
        return getImportBatches("SELECT * FROM Import_Batches");
    }
    
    /**
     * Retrieves active import batches from the database (status = 1)
     * @return Vector of active Import_Batches objects
     */
    public Vector<Import_Batches> getActiveImportBatches() {
        return getImportBatches("SELECT * FROM Import_Batches WHERE status = 1");
    }
    
    /**
     * Retrieves an import batch by its ID
     * @param batchId The UUID of the import batch to retrieve
     * @return The Import_Batches object if found, null otherwise
     */
    public Import_Batches getImportBatchById(UUID batchId) {
        String sql = "SELECT * FROM Import_Batches WHERE batch_id = '" + batchId.toString() + "'";
        Vector<Import_Batches> importBatches = getImportBatches(sql);
        
        if (importBatches.size() > 0) {
            return importBatches.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves import batches by supplier ID
     * @param supplierId The ID of the supplier
     * @return Vector of Import_Batches objects for the specified supplier
     */
    public Vector<Import_Batches> getImportBatchesBySupplierId(int supplierId) {
        String sql = "SELECT * FROM Import_Batches WHERE SupplierID = " + supplierId;
        return getImportBatches(sql);
    }
    
    /**
     * Retrieves import batches by date range
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return Vector of Import_Batches objects in the specified date range
     */
    public Vector<Import_Batches> getImportBatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM Import_Batches WHERE date_of_import BETWEEN ? AND ?";
        Vector<Import_Batches> vector = new Vector<Import_Batches>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(startDate));
            pre.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID batchId = UUID.fromString(rs.getString("batch_id"));
                int supplierId = rs.getInt("SupplierID");
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("createAt") != null) {
                    createAt = rs.getTimestamp("createAt").toLocalDateTime();
                }
                
                BigDecimal totalPrice = rs.getBigDecimal("TotalPrice");
                
                LocalDateTime dateOfImport = null;
                if (rs.getTimestamp("date_of_import") != null) {
                    dateOfImport = rs.getTimestamp("date_of_import").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                Import_Batches importBatch = new Import_Batches(
                        batchId, supplierId, createAt, totalPrice, dateOfImport, status);
                
                vector.add(importBatch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Updates the status of an import batch
     * @param batchId The UUID of the import batch to update
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateImportBatchStatus(UUID batchId, int status) {
        int n = 0;
        String sql = "UPDATE Import_Batches SET status = ? WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, batchId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets the count of import products in a batch
     * @param batchId The UUID of the batch
     * @return Count of import products in the batch
     */
    public int getImportProductCountInBatch(UUID batchId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count FROM ImportProduct WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Gets the total quantity of products imported in a batch
     * @param batchId The UUID of the batch
     * @return Total quantity of products imported in the batch
     */
    public int getTotalQuantityInBatch(UUID batchId) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(Quantity) as totalQuantity FROM ImportProduct WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                totalQuantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalQuantity;
    }
    
    /**
     * Gets the total value of products imported in a batch
     * @param batchId The UUID of the batch
     * @return Total value of products imported in the batch
     */
    public BigDecimal getTotalValueInBatch(UUID batchId) {
        BigDecimal totalValue = BigDecimal.ZERO;
        String sql = "SELECT SUM(Quantity * import_price) as totalValue FROM ImportProduct WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next() && rs.getBigDecimal("totalValue") != null) {
                totalValue = rs.getBigDecimal("totalValue");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalValue;
    }
    
    /**
     * Updates the total price of an import batch based on its import products
     * @param batchId The UUID of the batch
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateBatchTotalPrice(UUID batchId) {
        int n = 0;
        
        try {
            // Calculate total value from import products
            BigDecimal totalValue = getTotalValueInBatch(batchId);
            
            // Update batch total price
            String sql = "UPDATE Import_Batches SET TotalPrice = ? WHERE batch_id = ?";
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setBigDecimal(1, totalValue);
            pre.setString(2, batchId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets the most recent import batch
     * @return The most recent Import_Batches object, or null if none exists
     */
    public Import_Batches getMostRecentBatch() {
        String sql = "SELECT TOP 1 * FROM Import_Batches ORDER BY createAt DESC";
        Vector<Import_Batches> importBatches = getImportBatches(sql);
        
        if (importBatches.size() > 0) {
            return importBatches.get(0);
        }
        
        return null;
    }
    
    /**
     * Gets import batches with total value greater than the specified amount
     * @param minValue The minimum total value
     * @return Vector of Import_Batches objects with total value greater than minValue
     */
    public Vector<Import_Batches> getImportBatchesByMinValue(BigDecimal minValue) {
        String sql = "SELECT * FROM Import_Batches WHERE TotalPrice > ?";
        Vector<Import_Batches> vector = new Vector<Import_Batches>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setBigDecimal(1, minValue);
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID batchId = UUID.fromString(rs.getString("batch_id"));
                int supplierId = rs.getInt("SupplierID");
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("createAt") != null) {
                    createAt = rs.getTimestamp("createAt").toLocalDateTime();
                }
                
                BigDecimal totalPrice = rs.getBigDecimal("TotalPrice");
                
                LocalDateTime dateOfImport = null;
                if (rs.getTimestamp("date_of_import") != null) {
                    dateOfImport = rs.getTimestamp("date_of_import").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                Import_Batches importBatch = new Import_Batches(
                        batchId, supplierId, createAt, totalPrice, dateOfImport, status);
                
                vector.add(importBatch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOImportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Main method for testing the DAOImportBatches functionality
     */
    public static void main(String[] args) {
        DAOImportBatches dao = new DAOImportBatches();
        
        // Test getting all import batches
        System.out.println("All import batches:");
        Vector<Import_Batches> importBatches = dao.getAllImportBatches();
        for (Import_Batches importBatch : importBatches) {
            System.out.println(importBatch);
        }
        
        // Test inserting a new import batch
        Import_Batches newImportBatch = new Import_Batches(
                UUID.randomUUID(), 1, LocalDateTime.now(), new BigDecimal("1000.00"), 
                LocalDateTime.now(), 1);
        
        int result = dao.insertImportBatch(newImportBatch);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting active import batches
        System.out.println("\nActive import batches:");
        Vector<Import_Batches> activeBatches = dao.getActiveImportBatches();
        for (Import_Batches importBatch : activeBatches) {
            System.out.println(importBatch);
        }
        
        // Test getting import batches by supplier
        System.out.println("\nImport batches for supplier ID 1:");
        Vector<Import_Batches> supplierBatches = dao.getImportBatchesBySupplierId(1);
        for (Import_Batches importBatch : supplierBatches) {
            System.out.println(importBatch);
        }
        
        // Test getting the most recent batch
        Import_Batches recentBatch = dao.getMostRecentBatch();
        System.out.println("\nMost recent batch: " + (recentBatch != null ? recentBatch : "No batches found"));
    }
}
