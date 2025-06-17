/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
//Database Access Object

import entity.Warehouses_Transfers;
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
public class DAOWarehousesTransfers extends DBConnect {

    /**
     * Removes a warehouse transfer from the database
     * @param transferId The UUID of the transfer to remove
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int removeTransfer(UUID transferId) {
        int n = 0;
        String sql = "DELETE FROM Warehouses_Transfers WHERE transfer_id=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, transferId.toString());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Updates a warehouse transfer in the database
     * @param transfer The transfer object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateTransfer(Warehouses_Transfers transfer) {
        int n = 0;
        String sql = "UPDATE [Warehouses_Transfers]\n" +
"                      SET [ProductId] = ?,\n" +
"                          [Quantity] = ?,\n" +
"                          [From_warehouse_Id] = ?,\n" +
"                          [To_warehouse_Id] = ?,\n" +
"                          [date_of_transfer] = ?,\n" +
"                          [status] = ?\n" +
"                    WHERE transfer_id=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, transfer.getProductId());
            pre.setInt(2, transfer.getQuantity());
            pre.setInt(3, transfer.getFrom_warehouse_Id());
            pre.setInt(4, transfer.getTo_warehouse_Id());
            pre.setTimestamp(5, Timestamp.valueOf(transfer.getDate_of_transfer()));
            pre.setInt(6, transfer.getStatus());
            pre.setString(7, transfer.getTransfer_id().toString());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Inserts a new warehouse transfer into the database using PreparedStatement
     * @param transfer The transfer object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertTransfer(Warehouses_Transfers transfer) {
        int n = 0;
        String sql = "INSERT INTO [Warehouses_Transfers] ([transfer_id], [ProductId], [Quantity], [createAt], "
                + "[From_warehouse_Id], [To_warehouse_Id], [date_of_transfer], [status]) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (transfer.getTransfer_id() == null) {
                transfer.setTransfer_id(UUID.randomUUID());
            }
            pre.setString(1, transfer.getTransfer_id().toString());
            
            pre.setInt(2, transfer.getProductId());
            pre.setInt(3, transfer.getQuantity());
            
            // Handle LocalDateTime conversion to Timestamp
            LocalDateTime createAt = transfer.getCreateAt();
            if (createAt == null) {
                createAt = LocalDateTime.now();
                transfer.setCreateAt(createAt);
            }
            pre.setTimestamp(4, Timestamp.valueOf(createAt));
            
            pre.setInt(5, transfer.getFrom_warehouse_Id());
            pre.setInt(6, transfer.getTo_warehouse_Id());
            
            // Handle date_of_transfer
            LocalDateTime dateOfTransfer = transfer.getDate_of_transfer();
            if (dateOfTransfer == null) {
                dateOfTransfer = LocalDateTime.now();
                transfer.setDate_of_transfer(dateOfTransfer);
            }
            pre.setTimestamp(7, Timestamp.valueOf(dateOfTransfer));
            
            pre.setInt(8, transfer.getStatus());

            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Adds a new warehouse transfer to the database using Statement
     * @param transfer The transfer object to add
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int addTransfer(Warehouses_Transfers transfer) {
        int n = 0;
        
        // Generate UUID if not provided
        if (transfer.getTransfer_id() == null) {
            transfer.setTransfer_id(UUID.randomUUID());
        }
        
        // Set createAt if not already set
        if (transfer.getCreateAt() == null) {
            transfer.setCreateAt(LocalDateTime.now());
        }
        
        // Set date_of_transfer if not already set
        if (transfer.getDate_of_transfer() == null) {
            transfer.setDate_of_transfer(LocalDateTime.now());
        }
        
        String sql = "INSERT INTO [Warehouses_Transfers] ([transfer_id], [ProductId], [Quantity], [createAt], "
                + "[From_warehouse_Id], [To_warehouse_Id], [date_of_transfer], [status])\n"
                + "VALUES('" + transfer.getTransfer_id().toString() + "'\n"
                + ", " + transfer.getProductId() + "\n"
                + ", " + transfer.getQuantity() + "\n"
                + ", '" + Timestamp.valueOf(transfer.getCreateAt()) + "'\n"
                + ", " + transfer.getFrom_warehouse_Id() + "\n"
                + ", " + transfer.getTo_warehouse_Id() + "\n"
                + ", '" + Timestamp.valueOf(transfer.getDate_of_transfer()) + "'\n"
                + ", " + transfer.getStatus() + ")";
        
        System.out.println(sql);
        try {
            Statement state = conn.createStatement();
            n = state.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Retrieves warehouse transfers from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Warehouses_Transfers objects
     */
    public Vector<Warehouses_Transfers> getTransfers(String sql) {
        Vector<Warehouses_Transfers> vector = new Vector<Warehouses_Transfers>();
        try {
            Statement state = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                UUID transferId = UUID.fromString(rs.getString("transfer_id"));
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int fromWarehouseId = rs.getInt("From_warehouse_Id");
                int toWarehouseId = rs.getInt("To_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                Warehouses_Transfers transfer = new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        fromWarehouseId, toWarehouseId, dateOfTransfer, status);
                vector.add(transfer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    
    /**
     * Retrieves all warehouse transfers from the database
     * @return Vector of all Warehouses_Transfers objects
     */
    public Vector<Warehouses_Transfers> getAllTransfers() {
        return getTransfers("SELECT * FROM Warehouses_Transfers");
    }
    
    /**
     * Retrieves active warehouse transfers from the database (status = 1)
     * @return Vector of active Warehouses_Transfers objects
     */
    public Vector<Warehouses_Transfers> getActiveTransfers() {
        return getTransfers("SELECT * FROM Warehouses_Transfers WHERE status = 1");
    }
    
    /**
     * Retrieves a warehouse transfer by ID
     * @param transferId The UUID of the transfer to retrieve
     * @return The Warehouses_Transfers object if found, null otherwise
     */
    public Warehouses_Transfers getTransferById(UUID transferId) {
        String sql = "SELECT * FROM Warehouses_Transfers WHERE transfer_id = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, transferId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int fromWarehouseId = rs.getInt("From_warehouse_Id");
                int toWarehouseId = rs.getInt("To_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                return new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        fromWarehouseId, toWarehouseId, dateOfTransfer, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Retrieves warehouse transfers by product ID
     * @param productId The product ID to search for
     * @return Vector of matching Warehouses_Transfers objects
     */
    public Vector<Warehouses_Transfers> getTransfersByProductId(int productId) {
        String sql = "SELECT * FROM Warehouses_Transfers WHERE ProductId = ?";
        Vector<Warehouses_Transfers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID transferId = UUID.fromString(rs.getString("transfer_id"));
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int fromWarehouseId = rs.getInt("From_warehouse_Id");
                int toWarehouseId = rs.getInt("To_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                Warehouses_Transfers transfer = new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        fromWarehouseId, toWarehouseId, dateOfTransfer, status);
                result.add(transfer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Retrieves warehouse transfers by source warehouse ID
     * @param warehouseId The source warehouse ID to search for
     * @return Vector of matching Warehouses_Transfers objects
     */
    public Vector<Warehouses_Transfers> getTransfersBySourceWarehouse(int warehouseId) {
        String sql = "SELECT * FROM Warehouses_Transfers WHERE From_warehouse_Id = ?";
        Vector<Warehouses_Transfers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, warehouseId);
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID transferId = UUID.fromString(rs.getString("transfer_id"));
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int toWarehouseId = rs.getInt("To_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                Warehouses_Transfers transfer = new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        warehouseId, toWarehouseId, dateOfTransfer, status);
                result.add(transfer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Retrieves warehouse transfers by destination warehouse ID
     * @param warehouseId The destination warehouse ID to search for
     * @return Vector of matching Warehouses_Transfers objects
     */
    public Vector<Warehouses_Transfers> getTransfersByDestinationWarehouse(int warehouseId) {
        String sql = "SELECT * FROM Warehouses_Transfers WHERE To_warehouse_Id = ?";
        Vector<Warehouses_Transfers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, warehouseId);
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID transferId = UUID.fromString(rs.getString("transfer_id"));
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int fromWarehouseId = rs.getInt("From_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                Warehouses_Transfers transfer = new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        fromWarehouseId, warehouseId, dateOfTransfer, status);
                result.add(transfer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Updates the status of a warehouse transfer
     * @param transferId The UUID of the transfer
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateTransferStatus(UUID transferId, int status) {
        int n = 0;
        String sql = "UPDATE Warehouses_Transfers SET status = ? WHERE transfer_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, transferId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates the quantity of a warehouse transfer
     * @param transferId The UUID of the transfer
     * @param quantity The new quantity value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateTransferQuantity(UUID transferId, int quantity) {
        int n = 0;
        String sql = "UPDATE Warehouses_Transfers SET Quantity = ? WHERE transfer_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, quantity);
            pre.setString(2, transferId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Checks if a transfer exists by ID
     * @param transferId The UUID of the transfer to check
     * @return true if the transfer exists, false otherwise
     */
    public boolean transferExists(UUID transferId) {
        String sql = "SELECT COUNT(*) AS count FROM Warehouses_Transfers WHERE transfer_id = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, transferId.toString());
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Gets transfers sorted by transfer date (newest first)
     * @param limit The maximum number of records to return (0 for all)
     * @return Vector of Warehouses_Transfers objects sorted by transfer date
     */
    public Vector<Warehouses_Transfers> getTransfersByTransferDate(int limit) {
        String sql = "SELECT * FROM Warehouses_Transfers ORDER BY date_of_transfer DESC";
        
        if (limit > 0) {
            sql += " FETCH FIRST " + limit + " ROWS ONLY";
        }
        
        return getTransfers(sql);
    }
    
    /**
     * Gets transfers created after a specific date
     * @param date The date to filter by
     * @return Vector of Warehouses_Transfers objects created after the specified date
     */
    public Vector<Warehouses_Transfers> getTransfersCreatedAfter(LocalDateTime date) {
        String sql = "SELECT * FROM Warehouses_Transfers WHERE createAt > ?";
        Vector<Warehouses_Transfers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID transferId = UUID.fromString(rs.getString("transfer_id"));
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int fromWarehouseId = rs.getInt("From_warehouse_Id");
                int toWarehouseId = rs.getInt("To_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                Warehouses_Transfers transfer = new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        fromWarehouseId, toWarehouseId, dateOfTransfer, status);
                result.add(transfer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Gets transfers with transfer date after a specific date
     * @param date The date to filter by
     * @return Vector of Warehouses_Transfers objects with transfer date after the specified date
     */
    public Vector<Warehouses_Transfers> getTransfersWithTransferDateAfter(LocalDateTime date) {
        String sql = "SELECT * FROM Warehouses_Transfers WHERE date_of_transfer > ?";
        Vector<Warehouses_Transfers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                UUID transferId = UUID.fromString(rs.getString("transfer_id"));
                int productId = rs.getInt("ProductId");
                int quantity = rs.getInt("Quantity");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int fromWarehouseId = rs.getInt("From_warehouse_Id");
                int toWarehouseId = rs.getInt("To_warehouse_Id");
                LocalDateTime dateOfTransfer = rs.getTimestamp("date_of_transfer").toLocalDateTime();
                int status = rs.getInt("status");
                
                Warehouses_Transfers transfer = new Warehouses_Transfers(
                        transferId, productId, quantity, createAt, 
                        fromWarehouseId, toWarehouseId, dateOfTransfer, status);
                result.add(transfer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Gets the total quantity transferred for a specific product
     * @param productId The product ID to check
     * @return Total quantity transferred
     */
    public int getTotalQuantityTransferred(int productId) {
        int total = 0;
        String sql = "SELECT SUM(Quantity) AS total FROM Warehouses_Transfers WHERE ProductId = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehousesTransfers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return total;
    }
    
    /**
     * Main method for testing the DAOWarehousesTransfers functionality
     */
    public static void main(String[] args) {
        DAOWarehousesTransfers dao = new DAOWarehousesTransfers();
        
        // Test getting all transfers
        System.out.println("All transfers:");
        Vector<Warehouses_Transfers> transfers = dao.getAllTransfers();
        for (Warehouses_Transfers transfer : transfers) {
            System.out.println(transfer);
        }
        
        // Test inserting a new transfer
        UUID newId = UUID.randomUUID();
        Warehouses_Transfers newTransfer = new Warehouses_Transfers(
                newId, 
                1, // ProductId 
                10, // Quantity
                LocalDateTime.now(), 
                1, // From_warehouse_Id
                2, // To_warehouse_Id
                LocalDateTime.now(), 
                1 // status
        );
        
        // Check if transfer already exists
        if (!dao.transferExists(newId)) {
            int result = dao.insertTransfer(newTransfer);
            System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        } else {
            System.out.println("\nTransfer with ID '" + newId + "' already exists.");
        }
        
        // Test getting transfer by ID
        System.out.println("\nTransfer by ID '" + newId + "':");
        Warehouses_Transfers transferById = dao.getTransferById(newId);
        if (transferById != null) {
            System.out.println(transferById);
            
            // Test updating transfer
            transferById.setQuantity(15);
            int updateResult = dao.updateTransfer(transferById);
            System.out.println("\nUpdate result: " + (updateResult > 0 ? "Success" : "Failed"));
            
            // Test updating just the quantity
            int quantityUpdateResult = dao.updateTransferQuantity(newId, 20);
            System.out.println("\nQuantity update result: " + (quantityUpdateResult > 0 ? "Success" : "Failed"));
        } else {
            System.out.println("Transfer not found.");
        }
        
        // Test getting transfers by product ID
        System.out.println("\nTransfers for product ID 1:");
        Vector<Warehouses_Transfers> productTransfers = dao.getTransfersByProductId(1);
        for (Warehouses_Transfers transfer : productTransfers) {
            System.out.println(transfer);
        }
        
        // Test getting transfers by source warehouse
        System.out.println("\nTransfers from warehouse ID 1:");
        Vector<Warehouses_Transfers> sourceTransfers = dao.getTransfersBySourceWarehouse(1);
        for (Warehouses_Transfers transfer : sourceTransfers) {
            System.out.println(transfer);
        }
        
        // Test getting transfers by destination warehouse
        System.out.println("\nTransfers to warehouse ID 2:");
        Vector<Warehouses_Transfers> destTransfers = dao.getTransfersByDestinationWarehouse(2);
        for (Warehouses_Transfers transfer : destTransfers) {
            System.out.println(transfer);
        }
        
        // Test getting total quantity transferred for a product
        int totalQty = dao.getTotalQuantityTransferred(1);
        System.out.println("\nTotal quantity transferred for product ID 1: " + totalQty);
        
        // Test getting newest transfers
        System.out.println("\nNewest 5 transfers:");
        Vector<Warehouses_Transfers> newestTransfers = dao.getTransfersByTransferDate(5);
        for (Warehouses_Transfers transfer : newestTransfers) {
            System.out.println(transfer);
        }
        
        // Test removing a transfer (commented out to avoid actual deletion)
        /*
        if (transferById != null) {
            int removeResult = dao.removeTransfer(newId);
            System.out.println("\nRemove result: " + 
                    (removeResult > 0 ? "Success" : "Failed"));
        }
        */
    }
}
