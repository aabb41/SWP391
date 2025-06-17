/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Export_Batches;
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
public class DAOExportBatches extends DBConnect {
    
    /**
     * Updates an existing export batch in the database
     * @param exportBatch The export batch object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateExportBatch(Export_Batches exportBatch) {
        int n = 0;
        String sql = "UPDATE [Export_Batches] SET "
                + "[date_of_import] = ?, "
                + "[status] = ? "
                + "WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, exportBatch.getDate_of_import() != null ? 
                    Timestamp.valueOf(exportBatch.getDate_of_import()) : null);
            pre.setInt(2, exportBatch.getStatus());
            pre.setString(3, exportBatch.getBatch_id().toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new export batch into the database
     * @param exportBatch The export batch object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertExportBatch(Export_Batches exportBatch) {
        int n = 0;
        String sql = "INSERT INTO [Export_Batches]("
                + "[batch_id], "
                + "[createAT], "
                + "[date_of_import], "
                + "[status]) "
                + "VALUES(?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (exportBatch.getBatch_id() == null) {
                exportBatch.setBatch_id(UUID.randomUUID());
            }
            
            pre.setString(1, exportBatch.getBatch_id().toString());
            
            // Set current time if not provided
            LocalDateTime now = LocalDateTime.now();
            if (exportBatch.getCreateAT() == null) {
                exportBatch.setCreateAT(now);
            }
            pre.setTimestamp(2, Timestamp.valueOf(exportBatch.getCreateAT()));
            
            // Set date of import
            pre.setTimestamp(3, exportBatch.getDate_of_import() != null ? 
                    Timestamp.valueOf(exportBatch.getDate_of_import()) : null);
            
            pre.setInt(4, exportBatch.getStatus());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes an export batch from the database
     * @param batchId The UUID of the export batch to remove
     * @return Number of rows affected (1 if successful, 0 if not, -1 if batch has related products)
     */
    public int removeExportBatch(UUID batchId) {
        int n = 0;
        
        try {
            // Check if batch has related products in ExportProduct table
            String checkSql = "SELECT COUNT(*) as count FROM ExportProduct WHERE batch_id = ?";
            PreparedStatement checkPre = conn.prepareStatement(checkSql);
            checkPre.setString(1, batchId.toString());
            ResultSet checkRs = checkPre.executeQuery();
            
            if (checkRs.next() && checkRs.getInt("count") > 0) {
                return -1; // Batch has related products, can't delete
            }
            
            // If no related products, proceed with deletion
            String sql = "DELETE FROM Export_Batches WHERE batch_id = ?";
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves export batches from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Export_Batches objects
     */
    public Vector<Export_Batches> getExportBatches(String sql) {
        Vector<Export_Batches> vector = new Vector<Export_Batches>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                UUID batchId = UUID.fromString(rs.getString("batch_id"));
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("createAT") != null) {
                    createAt = rs.getTimestamp("createAT").toLocalDateTime();
                }
                
                LocalDateTime dateOfImport = null;
                if (rs.getTimestamp("date_of_import") != null) {
                    dateOfImport = rs.getTimestamp("date_of_import").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                Export_Batches exportBatch = new Export_Batches(
                        batchId, createAt, dateOfImport, status);
                
                vector.add(exportBatch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all export batches from the database
     * @return Vector of all Export_Batches objects
     */
    public Vector<Export_Batches> getAllExportBatches() {
        return getExportBatches("SELECT * FROM Export_Batches");
    }
    
    /**
     * Retrieves active export batches from the database (status = 1)
     * @return Vector of active Export_Batches objects
     */
    public Vector<Export_Batches> getActiveExportBatches() {
        return getExportBatches("SELECT * FROM Export_Batches WHERE status = 1");
    }
    
    /**
     * Retrieves an export batch by its ID
     * @param batchId The UUID of the export batch to retrieve
     * @return The Export_Batches object if found, null otherwise
     */
    public Export_Batches getExportBatchById(UUID batchId) {
        String sql = "SELECT * FROM Export_Batches WHERE batch_id = '" + batchId.toString() + "'";
        Vector<Export_Batches> exportBatches = getExportBatches(sql);
        
        if (exportBatches.size() > 0) {
            return exportBatches.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves export batches by date range
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return Vector of Export_Batches objects in the specified date range
     */
    public Vector<Export_Batches> getExportBatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM Export_Batches WHERE date_of_import BETWEEN ? AND ?";
        Vector<Export_Batches> vector = new Vector<Export_Batches>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(startDate));
            pre.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID batchId = UUID.fromString(rs.getString("batch_id"));
                
                LocalDateTime createAt = null;
                if (rs.getTimestamp("createAT") != null) {
                    createAt = rs.getTimestamp("createAT").toLocalDateTime();
                }
                
                LocalDateTime dateOfImport = null;
                if (rs.getTimestamp("date_of_import") != null) {
                    dateOfImport = rs.getTimestamp("date_of_import").toLocalDateTime();
                }
                
                int status = rs.getInt("status");
                
                Export_Batches exportBatch = new Export_Batches(
                        batchId, createAt, dateOfImport, status);
                
                vector.add(exportBatch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Updates the status of an export batch
     * @param batchId The UUID of the export batch to update
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateExportBatchStatus(UUID batchId, int status) {
        int n = 0;
        String sql = "UPDATE Export_Batches SET status = ? WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, batchId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets the count of export products in a batch
     * @param batchId The UUID of the batch
     * @return Count of export products in the batch
     */
    public int getExportProductCountInBatch(UUID batchId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count FROM ExportProduct WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Gets the total quantity of products exported in a batch
     * @param batchId The UUID of the batch
     * @return Total quantity of products exported in the batch
     */
    public int getTotalQuantityInBatch(UUID batchId) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(Quantity) as totalQuantity FROM ExportProduct WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                totalQuantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOExportBatches.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalQuantity;
    }
    
    /**
     * Gets the most recent export batch
     * @return The most recent Export_Batches object, or null if none exists
     */
    public Export_Batches getMostRecentBatch() {
        String sql = "SELECT TOP 1 * FROM Export_Batches ORDER BY createAT DESC";
        Vector<Export_Batches> exportBatches = getExportBatches(sql);
        
        if (exportBatches.size() > 0) {
            return exportBatches.get(0);
        }
        
        return null;
    }
    
    /**
     * Main method for testing the DAOExportBatches functionality
     */
    public static void main(String[] args) {
        DAOExportBatches dao = new DAOExportBatches();
        
        // Test getting all export batches
        System.out.println("All export batches:");
        Vector<Export_Batches> exportBatches = dao.getAllExportBatches();
        for (Export_Batches exportBatch : exportBatches) {
            System.out.println(exportBatch);
        }
        
        // Test inserting a new export batch
        Export_Batches newExportBatch = new Export_Batches(
                UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), 1);
        
        int result = dao.insertExportBatch(newExportBatch);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting active export batches
        System.out.println("\nActive export batches:");
        Vector<Export_Batches> activeBatches = dao.getActiveExportBatches();
        for (Export_Batches exportBatch : activeBatches) {
            System.out.println(exportBatch);
        }
        
        // Test getting the most recent batch
        Export_Batches recentBatch = dao.getMostRecentBatch();
        System.out.println("\nMost recent batch: " + (recentBatch != null ? recentBatch : "No batches found"));
    }
}
