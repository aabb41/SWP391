/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
//Database Access Object

import entity.Supppliers;
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
public class DAOSuppliers extends DBConnect {

    /**
     * Removes a supplier from the database
     * @param supplierName The name of the supplier to remove
     * @return Number of rows affected (1 if successful, 0 if not, -1 if foreign key constraint)
     */
    public int removeSupplier(String supplierName) {
        int n = 0;
        String sql = "DELETE FROM Suppliers WHERE Supplier_name=?";
        try {
            // Check foreign key constraints
            String sqlSelect = "SELECT SupplierID FROM Products WHERE SupplierID IN "
                    + "(SELECT SupplierID FROM Suppliers WHERE Supplier_name=?)";
            PreparedStatement preSelect = conn.prepareStatement(sqlSelect);
            preSelect.setString(1, supplierName);
            ResultSet rs = preSelect.executeQuery();
            if (rs.next()) {
                return -1; // Foreign key constraint violation
            }
            
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, supplierName);
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Updates a supplier in the database
     * @param supplier The supplier object with updated information
     * @param oldSupplierName The original name of the supplier (primary key)
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateSupplier(Supppliers supplier, String oldSupplierName) {
        int n = 0;
        String sql ="UPDATE [Suppliers]\n" +
"                    SET [Supplier_name] = ?,\n" +
"                        [Phone] = ?,\n" +
"                        [Address] = ?,\n" +
"                        [Status] = ?\n" +
"                   WHERE Supplier_name=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, supplier.getSupplier_name());
            pre.setString(2, supplier.getPhone());
            pre.setString(3, supplier.getAddress());
            pre.setInt(4, supplier.getStatus());
            pre.setString(5, oldSupplierName);
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Inserts a new supplier into the database using PreparedStatement
     * @param supplier The supplier object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertSupplier(Supppliers supplier) {
        int n = 0;
        String sql = "INSERT INTO [Suppliers] ([Supplier_name], [Phone], [Address], [createAt], [Status]) "
                + "VALUES(?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, supplier.getSupplier_name());
            pre.setString(2, supplier.getPhone());
            pre.setString(3, supplier.getAddress());
            
            // Handle LocalDateTime conversion to Timestamp
            LocalDateTime createAt = supplier.getCreateAt();
            if (createAt == null) {
                createAt = LocalDateTime.now();
                supplier.setCreateAt(createAt);
            }
            pre.setTimestamp(4, Timestamp.valueOf(createAt));
            
            pre.setInt(5, supplier.getStatus());

            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Adds a new supplier to the database using Statement
     * @param supplier The supplier object to add
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int addSupplier(Supppliers supplier) {
        int n = 0;
        
        // Set createAt if not already set
        if (supplier.getCreateAt() == null) {
            supplier.setCreateAt(LocalDateTime.now());
        }
        
        String sql = "INSERT INTO [Suppliers] ([Supplier_name], [Phone], [Address], [createAt], [Status])\n"
                + "VALUES('" + supplier.getSupplier_name() + "'\n"
                + ", '" + supplier.getPhone() + "'\n"
                + ", '" + supplier.getAddress() + "'\n"
                + ", '" + Timestamp.valueOf(supplier.getCreateAt()) + "'\n"
                + ", " + supplier.getStatus() + ")";
        
        System.out.println(sql);
        try {
            Statement state = conn.createStatement();
            n = state.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Retrieves suppliers from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Suppliers objects
     */
    public Vector<Supppliers> getSuppliers(String sql) {
        Vector<Supppliers> vector = new Vector<Supppliers>();
        try {
            Statement state = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String supplierName = rs.getString("Supplier_name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Supppliers supplier = new Supppliers(supplierName, phone, address, createAt, status);
                vector.add(supplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    
    /**
     * Retrieves all suppliers from the database
     * @return Vector of all Suppliers objects
     */
    public Vector<Supppliers> getAllSuppliers() {
        return getSuppliers("SELECT * FROM Suppliers");
    }
    
    /**
     * Retrieves active suppliers from the database (status = 1)
     * @return Vector of active Suppliers objects
     */
    public Vector<Supppliers> getActiveSuppliers() {
        return getSuppliers("SELECT * FROM Suppliers WHERE Status = 1");
    }
    
    /**
     * Retrieves a supplier by name
     * @param supplierName The name of the supplier to retrieve
     * @return The Suppliers object if found, null otherwise
     */
    public Supppliers getSupplierByName(String supplierName) {
        String sql = "SELECT * FROM Suppliers WHERE Supplier_name = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, supplierName);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                return new Supppliers(supplierName, phone, address, createAt, status);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Searches for suppliers by name (partial match)
     * @param namePattern The pattern to search for in supplier names
     * @return Vector of matching Suppliers objects
     */
    public Vector<Supppliers> searchSuppliersByName(String namePattern) {
        String sql = "SELECT * FROM Suppliers WHERE Supplier_name LIKE ?";
        Vector<Supppliers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, "%" + namePattern + "%");
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                String supplierName = rs.getString("Supplier_name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Supppliers supplier = new Supppliers(supplierName, phone, address, createAt, status);
                result.add(supplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Searches for suppliers by phone number (partial match)
     * @param phonePattern The pattern to search for in supplier phone numbers
     * @return Vector of matching Suppliers objects
     */
    public Vector<Supppliers> searchSuppliersByPhone(String phonePattern) {
        String sql = "SELECT * FROM Suppliers WHERE Phone LIKE ?";
        Vector<Supppliers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, "%" + phonePattern + "%");
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                String supplierName = rs.getString("Supplier_name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Supppliers supplier = new Supppliers(supplierName, phone, address, createAt, status);
                result.add(supplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Updates the status of a supplier
     * @param supplierName The name of the supplier
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateSupplierStatus(String supplierName, int status) {
        int n = 0;
        String sql = "UPDATE Suppliers SET Status = ? WHERE Supplier_name = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, supplierName);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates the phone number of a supplier
     * @param supplierName The name of the supplier
     * @param phone The new phone number
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateSupplierPhone(String supplierName, String phone) {
        int n = 0;
        String sql = "UPDATE Suppliers SET Phone = ? WHERE Supplier_name = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, phone);
            pre.setString(2, supplierName);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates the address of a supplier
     * @param supplierName The name of the supplier
     * @param address The new address
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateSupplierAddress(String supplierName, String address) {
        int n = 0;
        String sql = "UPDATE Suppliers SET Address = ? WHERE Supplier_name = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, address);
            pre.setString(2, supplierName);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Checks if a supplier exists by name
     * @param supplierName The name of the supplier to check
     * @return true if the supplier exists, false otherwise
     */
    public boolean supplierExists(String supplierName) {
        String sql = "SELECT COUNT(*) AS count FROM Suppliers WHERE Supplier_name = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, supplierName);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Gets suppliers sorted by creation date (newest first)
     * @param limit The maximum number of records to return (0 for all)
     * @return Vector of Suppliers objects sorted by creation date
     */
    public Vector<Supppliers> getSuppliersByCreationDate(int limit) {
        String sql = "SELECT * FROM Suppliers ORDER BY createAt DESC";
        
        if (limit > 0) {
            sql += " FETCH FIRST " + limit + " ROWS ONLY";
        }
        
        return getSuppliers(sql);
    }
    
    /**
     * Gets suppliers created after a specific date
     * @param date The date to filter by
     * @return Vector of Suppliers objects created after the specified date
     */
    public Vector<Supppliers> getSuppliersCreatedAfter(LocalDateTime date) {
        String sql = "SELECT * FROM Suppliers WHERE createAt > ?";
        Vector<Supppliers> result = new Vector<>();
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = pre.executeQuery();
            
            while (rs.next()) {
                String supplierName = rs.getString("Supplier_name");
                String phone = rs.getString("Phone");
                String address = rs.getString("Address");
                LocalDateTime createAt = rs.getTimestamp("createAt").toLocalDateTime();
                int status = rs.getInt("Status");
                
                Supppliers supplier = new Supppliers(supplierName, phone, address, createAt, status);
                result.add(supplier);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOSuppliers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    /**
     * Main method for testing the DAOSuppliers functionality
     */
    public static void main(String[] args) {
        DAOSuppliers dao = new DAOSuppliers();
        
        // Test getting all suppliers
        System.out.println("All suppliers:");
        Vector<Supppliers> suppliers = dao.getAllSuppliers();
        for (Supppliers supplier : suppliers) {
            System.out.println(supplier);
        }
        
        // Test inserting a new supplier
        Supppliers newSupplier = new Supppliers(
                "Test Supplier", 
                "123-456-7890", 
                "123 Test St, Test City", 
                LocalDateTime.now(), 
                1);
        
        // Check if supplier already exists
        if (!dao.supplierExists(newSupplier.getSupplier_name())) {
            int result = dao.insertSupplier(newSupplier);
            System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        } else {
            System.out.println("\nSupplier '" + newSupplier.getSupplier_name() + "' already exists.");
        }
        
        // Test getting supplier by name
        System.out.println("\nSupplier by name 'Test Supplier':");
        Supppliers supplierByName = dao.getSupplierByName("Test Supplier");
        if (supplierByName != null) {
            System.out.println(supplierByName);
            
            // Test updating supplier
            supplierByName.setPhone("987-654-3210");
            supplierByName.setAddress("456 Updated St, New City");
            int updateResult = dao.updateSupplier(supplierByName, "Test Supplier");
            System.out.println("\nUpdate result: " + (updateResult > 0 ? "Success" : "Failed"));
            
            // Test updating just the phone
            int phoneUpdateResult = dao.updateSupplierPhone("Test Supplier", "555-555-5555");
            System.out.println("\nPhone update result: " + (phoneUpdateResult > 0 ? "Success" : "Failed"));
        } else {
            System.out.println("Supplier not found.");
        }
        
        // Test searching suppliers by name
        System.out.println("\nSuppliers with 'Test' in name:");
        Vector<Supppliers> searchResults = dao.searchSuppliersByName("Test");
        for (Supppliers supplier : searchResults) {
            System.out.println(supplier);
        }
        
        // Test getting suppliers by creation date
        System.out.println("\nNewest 5 suppliers:");
        Vector<Supppliers> newestSuppliers = dao.getSuppliersByCreationDate(5);
        for (Supppliers supplier : newestSuppliers) {
            System.out.println(supplier);
        }
        
        // Test removing a supplier (commented out to avoid actual deletion)
        /*
        if (supplierByName != null) {
            int removeResult = dao.removeSupplier("Test Supplier");
            System.out.println("\nRemove result: " + 
                    (removeResult > 0 ? "Success" : 
                     (removeResult == -1 ? "Failed - Foreign key constraint" : "Failed")));
        }
        */
    }
}
