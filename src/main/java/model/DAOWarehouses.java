/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
//Database Access Object

import entity.Warehouses;
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
public class DAOWarehouses extends DBConnect {

    /**
     * Removes a warehouse from the database
     * @param warehouseName The name of the warehouse to remove
     * @return Number of rows affected (1 if successful, 0 if not, -1 if foreign key constraint)
     */
    public int removeWarehouse(String warehouseName) {
        int n = 0;
        String sql = "DELETE FROM Warehouses WHERE warehouses_name=?";
        try {
            // Check foreign key constraints if needed
            // String sqlSelect = "SELECT ... FROM ... WHERE warehouses_name=?";
            // PreparedStatement preSelect = conn.prepareStatement(sqlSelect);
            // preSelect.setString(1, warehouseName);
            // ResultSet rs = preSelect.executeQuery();
            // if (rs.next()) {
            //     return -1; // Foreign key constraint violation
            // }
            
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, warehouseName);
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Updates a warehouse in the database
     * @param warehouse The warehouse object with updated information
     * @param oldWarehouseName The original name of the warehouse (primary key)
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateWarehouse(Warehouses warehouse, String oldWarehouseName) {
        int n = 0;
        String sql = "UPDATE [Warehouses]\n" +
"                      SET [warehouses_name] = ?,\n" +
"                          [location] = ?,\n" +
"                          [Status] = ?\n" +
"                    WHERE warehouses_name=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, warehouse.getWarehouses_name());
            pre.setString(2, warehouse.getLocation());
            pre.setInt(3, warehouse.getStatus());
            pre.setString(4, oldWarehouseName);
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Inserts a new warehouse into the database using PreparedStatement
     * @param warehouse The warehouse object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertWarehouse(Warehouses warehouse) {
        int n = 0;
        String sql = "INSERT INTO [Warehouses] ([warehouses_name], [location], [createAt], [Status]) "
                + "VALUES(?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, warehouse.getWarehouses_name());
            pre.setString(2, warehouse.getLocation());
            
            // Handle LocalDateTime conversion to Timestamp
            LocalDateTime createAt = warehouse.getCreateAt();
            if (createAt == null) {
                createAt = LocalDateTime.now();
                warehouse.setCreateAt(createAt);
            }
            pre.setTimestamp(3, Timestamp.valueOf(createAt));
            
            pre.setInt(4, warehouse.getStatus());

            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Adds a new warehouse to the database using Statement
     * @param warehouse The warehouse object to add
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int addWarehouse(Warehouses warehouse) {
        int n = 0;
        
        // Set createAt if not already set
        if (warehouse.getCreateAt() == null) {
            warehouse.setCreateAt(LocalDateTime.now());
        }
        
        String sql = "INSERT INTO [Warehouses] ([warehouses_name], [location], [createAt], [Status])\n"
                + "VALUES('" + warehouse.getWarehouses_name() + "'\n"
                + ", '" + warehouse.getLocation() + "'\n"
                + ", '" + Timestamp.valueOf(warehouse.getCreateAt()) + "'\n"
                + ", " + warehouse.getStatus() + ")";
        
        System.out.println(sql);
        try {
            Statement state = conn.createStatement();
            n = state.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Retrieves warehouses from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Warehouses objects
     */
    public Vector<Warehouses> getWarehouses(String sql) {
        Vector<Warehouses> vector = new Vector<Warehouses>();
        try {
            Statement state = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String warehouseName = rs.getString("warehouses_name");
                String location = rs.getString("location");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Warehouses warehouse = new Warehouses(warehouseName, location, createAt, status);
                vector.add(warehouse);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    
    /**
     * Retrieves all warehouses from the database
     * @return Vector of all Warehouses objects
     */
    public Vector<Warehouses> getAllWarehouses() {
        return getWarehouses("SELECT * FROM Warehouses");
    }
    
    /**
     * Retrieves active warehouses from the database (status = 1)
     * @return Vector of active Warehouses objects
     */
    public Vector<Warehouses> getActiveWarehouses() {
        return getWarehouses("SELECT * FROM Warehouses WHERE Status = 1");
    }
    
    /**
     * Retrieves a warehouse by name
     * @param warehouseName The name of the warehouse to retrieve
     * @return The Warehouses object if found, null otherwise
     */
    public Warehouses getWarehouseByName(String warehouseName) {
        String sql = "SELECT * FROM Warehouses WHERE warehouses_name = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, warehouseName);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                String location = rs.getString("location");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                return new Warehouses(warehouseName, location, createAt, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Searches for warehouses by name (partial match)
     * @param namePattern The pattern to search for in warehouse names
     * @return Vector of matching Warehouses objects
     */
    public Vector<Warehouses> searchWarehousesByName(String namePattern) {
        String sql = "SELECT * FROM Warehouses WHERE warehouses_name LIKE ?";
        Vector<Warehouses> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, "%" + namePattern + "%");
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                String warehouseName = rs.getString("warehouses_name");
                String location = rs.getString("location");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Warehouses warehouse = new Warehouses(warehouseName, location, createAt, status);
                result.add(warehouse);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Searches for warehouses by location (partial match)
     * @param locationPattern The pattern to search for in warehouse locations
     * @return Vector of matching Warehouses objects
     */
    public Vector<Warehouses> searchWarehousesByLocation(String locationPattern) {
        String sql = "SELECT * FROM Warehouses WHERE location LIKE ?";
        Vector<Warehouses> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, "%" + locationPattern + "%");
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                String warehouseName = rs.getString("warehouses_name");
                String location = rs.getString("location");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Warehouses warehouse = new Warehouses(warehouseName, location, createAt, status);
                result.add(warehouse);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Updates the status of a warehouse
     * @param warehouseName The name of the warehouse
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateWarehouseStatus(String warehouseName, int status) {
        int n = 0;
        String sql = "UPDATE Warehouses SET Status = ? WHERE warehouses_name = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, warehouseName);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates the location of a warehouse
     * @param warehouseName The name of the warehouse
     * @param location The new location
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateWarehouseLocation(String warehouseName, String location) {
        int n = 0;
        String sql = "UPDATE Warehouses SET location = ? WHERE warehouses_name = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, location);
            pre.setString(2, warehouseName);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Checks if a warehouse exists by name
     * @param warehouseName The name of the warehouse to check
     * @return true if the warehouse exists, false otherwise
     */
    public boolean warehouseExists(String warehouseName) {
        String sql = "SELECT COUNT(*) AS count FROM Warehouses WHERE warehouses_name = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, warehouseName);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Gets warehouses sorted by creation date (newest first)
     * @param limit The maximum number of records to return (0 for all)
     * @return Vector of Warehouses objects sorted by creation date
     */
    public Vector<Warehouses> getWarehousesByCreationDate(int limit) {
        String sql = "SELECT * FROM Warehouses ORDER BY createAt DESC";
        
        if (limit > 0) {
            sql += " FETCH FIRST " + limit + " ROWS ONLY";
        }
        
        return getWarehouses(sql);
    }
    
    /**
     * Gets warehouses created after a specific date
     * @param date The date to filter by
     * @return Vector of Warehouses objects created after the specified date
     */
    public Vector<Warehouses> getWarehousesCreatedAfter(LocalDateTime date) {
        String sql = "SELECT * FROM Warehouses WHERE createAt > ?";
        Vector<Warehouses> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                String warehouseName = rs.getString("warehouses_name");
                String location = rs.getString("location");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Warehouses warehouse = new Warehouses(warehouseName, location, createAt, status);
                result.add(warehouse);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Gets the count of active warehouses
     * @return Count of active warehouses
     */
    public int getActiveWarehouseCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS count FROM Warehouses WHERE Status = 1";
        
        try {
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Main method for testing the DAOWarehouses functionality
     */
    public static void main(String[] args) {
        DAOWarehouses dao = new DAOWarehouses();
        
        // Test getting all warehouses
        System.out.println("All warehouses:");
        Vector<Warehouses> warehouses = dao.getAllWarehouses();
        for (Warehouses warehouse : warehouses) {
            System.out.println(warehouse);
        }
        
        // Test inserting a new warehouse
        Warehouses newWarehouse = new Warehouses(
                "Test Warehouse", 
                "123 Test Location, Test City", 
                LocalDateTime.now(), 
                1);
        
        // Check if warehouse already exists
        if (!dao.warehouseExists(newWarehouse.getWarehouses_name())) {
            int result = dao.insertWarehouse(newWarehouse);
            System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        } else {
            System.out.println("\nWarehouse '" + newWarehouse.getWarehouses_name() + "' already exists.");
        }
        
        // Test getting warehouse by name
        System.out.println("\nWarehouse by name 'Test Warehouse':");
        Warehouses warehouseByName = dao.getWarehouseByName("Test Warehouse");
        if (warehouseByName != null) {
            System.out.println(warehouseByName);
            
            // Test updating warehouse
            warehouseByName.setLocation("456 Updated Location, New City");
            int updateResult = dao.updateWarehouse(warehouseByName, "Test Warehouse");
            System.out.println("\nUpdate result: " + (updateResult > 0 ? "Success" : "Failed"));
            
            // Test updating just the location
            int locationUpdateResult = dao.updateWarehouseLocation("Test Warehouse", "789 New Location, Another City");
            System.out.println("\nLocation update result: " + (locationUpdateResult > 0 ? "Success" : "Failed"));
        } else {
            System.out.println("Warehouse not found.");
        }
        
        // Test searching warehouses by name
        System.out.println("\nWarehouses with 'Test' in name:");
        Vector<Warehouses> searchResults = dao.searchWarehousesByName("Test");
        for (Warehouses warehouse : searchResults) {
            System.out.println(warehouse);
        }
        
        // Test getting warehouses by creation date
        System.out.println("\nNewest 5 warehouses:");
        Vector<Warehouses> newestWarehouses = dao.getWarehousesByCreationDate(5);
        for (Warehouses warehouse : newestWarehouses) {
            System.out.println(warehouse);
        }
        
        // Test getting count of active warehouses
        int activeCount = dao.getActiveWarehouseCount();
        System.out.println("\nNumber of active warehouses: " + activeCount);
        
        // Test removing a warehouse (commented out to avoid actual deletion)
        /*
        if (warehouseByName != null) {
            int removeResult = dao.removeWarehouse("Test Warehouse");
            System.out.println("\nRemove result: " + 
                    (removeResult > 0 ? "Success" : "Failed"));
        }
        */
    }
}
