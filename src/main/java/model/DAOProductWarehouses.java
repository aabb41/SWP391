/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.ProductWarehouses;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DAOProductWarehouses extends DBConnect {
    
    /**
     * Updates an existing product warehouse record in the database
     * @param productWarehouses The product warehouse object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductWarehouses(ProductWarehouses productWarehouses) {
        int n = 0;
        String sql = "UPDATE [ProductWarehouses] SET "
                + "[ProductID] = ?, "
                + "[warehousesID] = ?, "
                + "[quantity] = ? "
                + "WHERE ProductWarehouses = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productWarehouses.getProductID());
            pre.setInt(2, productWarehouses.getWarehousesID());
            pre.setInt(3, productWarehouses.getQuantity());
            pre.setString(4, productWarehouses.getProductWarehouses().toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new product warehouse record into the database
     * @param productWarehouses The product warehouse object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertProductWarehouses(ProductWarehouses productWarehouses) {
        int n = 0;
        String sql = "INSERT INTO [ProductWarehouses]("
                + "[ProductWarehouses], "
                + "[ProductID], "
                + "[warehousesID], "
                + "[quantity]) "
                + "VALUES(?, ?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (productWarehouses.getProductWarehouses() == null) {
                productWarehouses.setProductWarehouses(UUID.randomUUID());
            }
            
            pre.setString(1, productWarehouses.getProductWarehouses().toString());
            pre.setInt(2, productWarehouses.getProductID());
            pre.setInt(3, productWarehouses.getWarehousesID());
            pre.setInt(4, productWarehouses.getQuantity());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes a product warehouse record from the database
     * @param productWarehousesId The UUID of the product warehouse record to remove
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int removeProductWarehouses(UUID productWarehousesId) {
        int n = 0;
        String sql = "DELETE FROM ProductWarehouses WHERE ProductWarehouses = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, productWarehousesId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves product warehouse records from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of ProductWarehouses objects
     */
    public Vector<ProductWarehouses> getProductWarehouses(String sql) {
        Vector<ProductWarehouses> vector = new Vector<ProductWarehouses>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                UUID productWarehousesId = UUID.fromString(rs.getString("ProductWarehouses"));
                int productId = rs.getInt("ProductID");
                int warehousesId = rs.getInt("warehousesID");
                int quantity = rs.getInt("quantity");
                
                ProductWarehouses productWarehouses = new ProductWarehouses(
                        productWarehousesId, productId, warehousesId, quantity);
                
                vector.add(productWarehouses);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all product warehouse records from the database
     * @return Vector of all ProductWarehouses objects
     */
    public Vector<ProductWarehouses> getAllProductWarehouses() {
        return getProductWarehouses("SELECT * FROM ProductWarehouses");
    }
    
    /**
     * Retrieves a product warehouse record by its ID
     * @param productWarehousesId The UUID of the product warehouse record to retrieve
     * @return The ProductWarehouses object if found, null otherwise
     */
    public ProductWarehouses getProductWarehousesById(UUID productWarehousesId) {
        String sql = "SELECT * FROM ProductWarehouses WHERE ProductWarehouses = '" 
                + productWarehousesId.toString() + "'";
        Vector<ProductWarehouses> productWarehousesVector = getProductWarehouses(sql);
        
        if (productWarehousesVector.size() > 0) {
            return productWarehousesVector.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves product warehouse records by product ID
     * @param productId The ID of the product
     * @return Vector of ProductWarehouses objects for the specified product
     */
    public Vector<ProductWarehouses> getProductWarehousesByProductId(int productId) {
        String sql = "SELECT * FROM ProductWarehouses WHERE ProductID = " + productId;
        return getProductWarehouses(sql);
    }
    
    /**
     * Retrieves product warehouse records by warehouse ID
     * @param warehouseId The ID of the warehouse
     * @return Vector of ProductWarehouses objects for the specified warehouse
     */
    public Vector<ProductWarehouses> getProductWarehousesByWarehouseId(int warehouseId) {
        String sql = "SELECT * FROM ProductWarehouses WHERE warehousesID = " + warehouseId;
        return getProductWarehouses(sql);
    }
    
    /**
     * Retrieves a product warehouse record by product ID and warehouse ID
     * @param productId The ID of the product
     * @param warehouseId The ID of the warehouse
     * @return The ProductWarehouses object if found, null otherwise
     */
    public ProductWarehouses getProductWarehousesByProductAndWarehouse(int productId, int warehouseId) {
        String sql = "SELECT * FROM ProductWarehouses WHERE ProductID = " + productId 
                + " AND warehousesID = " + warehouseId;
        Vector<ProductWarehouses> productWarehousesVector = getProductWarehouses(sql);
        
        if (productWarehousesVector.size() > 0) {
            return productWarehousesVector.get(0);
        }
        
        return null;
    }
    
    /**
     * Updates the quantity of a product in a warehouse
     * @param productId The ID of the product
     * @param warehouseId The ID of the warehouse
     * @param quantity The new quantity
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductQuantity(int productId, int warehouseId, int quantity) {
        int n = 0;
        
        // First, check if the product-warehouse combination exists
        ProductWarehouses existingRecord = getProductWarehousesByProductAndWarehouse(productId, warehouseId);
        
        if (existingRecord != null) {
            // Update existing record
            existingRecord.setQuantity(quantity);
            n = updateProductWarehouses(existingRecord);
        } else {
            // Create new record
            ProductWarehouses newRecord = new ProductWarehouses(
                    UUID.randomUUID(), productId, warehouseId, quantity);
            n = insertProductWarehouses(newRecord);
        }
        
        return n;
    }
    
    /**
     * Increases the quantity of a product in a warehouse
     * @param productId The ID of the product
     * @param warehouseId The ID of the warehouse
     * @param quantityToAdd The quantity to add
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int increaseProductQuantity(int productId, int warehouseId, int quantityToAdd) {
        int n = 0;
        
        // First, check if the product-warehouse combination exists
        ProductWarehouses existingRecord = getProductWarehousesByProductAndWarehouse(productId, warehouseId);
        
        if (existingRecord != null) {
            // Update existing record
            int newQuantity = existingRecord.getQuantity() + quantityToAdd;
            existingRecord.setQuantity(newQuantity);
            n = updateProductWarehouses(existingRecord);
        } else {
            // Create new record
            ProductWarehouses newRecord = new ProductWarehouses(
                    UUID.randomUUID(), productId, warehouseId, quantityToAdd);
            n = insertProductWarehouses(newRecord);
        }
        
        return n;
    }
    
    /**
     * Decreases the quantity of a product in a warehouse
     * @param productId The ID of the product
     * @param warehouseId The ID of the warehouse
     * @param quantityToSubtract The quantity to subtract
     * @return Number of rows affected (1 if successful, 0 if not, -1 if insufficient quantity)
     */
    public int decreaseProductQuantity(int productId, int warehouseId, int quantityToSubtract) {
        int n = 0;
        
        // First, check if the product-warehouse combination exists
        ProductWarehouses existingRecord = getProductWarehousesByProductAndWarehouse(productId, warehouseId);
        
        if (existingRecord != null) {
            // Check if there's enough quantity
            if (existingRecord.getQuantity() >= quantityToSubtract) {
                // Update existing record
                int newQuantity = existingRecord.getQuantity() - quantityToSubtract;
                existingRecord.setQuantity(newQuantity);
                n = updateProductWarehouses(existingRecord);
            } else {
                // Insufficient quantity
                return -1;
            }
        } else {
            // Record doesn't exist, can't decrease
            return -1;
        }
        
        return n;
    }
    
    /**
     * Gets the total quantity of a product across all warehouses
     * @param productId The ID of the product
     * @return Total quantity of the product
     */
    public int getTotalProductQuantity(int productId) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(quantity) as totalQuantity FROM ProductWarehouses WHERE ProductID = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next() && rs.getObject("totalQuantity") != null) {
                totalQuantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalQuantity;
    }
    
    /**
     * Gets the quantity of a product in a specific warehouse
     * @param productId The ID of the product
     * @param warehouseId The ID of the warehouse
     * @return Quantity of the product in the warehouse, 0 if not found
     */
    public int getProductQuantityInWarehouse(int productId, int warehouseId) {
        ProductWarehouses record = getProductWarehousesByProductAndWarehouse(productId, warehouseId);
        
        if (record != null) {
            return record.getQuantity();
        }
        
        return 0;
    }
    
    /**
     * Checks if a product is available in a warehouse with sufficient quantity
     * @param productId The ID of the product
     * @param warehouseId The ID of the warehouse
     * @param requiredQuantity The required quantity
     * @return true if the product is available with sufficient quantity, false otherwise
     */
    public boolean isProductAvailableInWarehouse(int productId, int warehouseId, int requiredQuantity) {
        int availableQuantity = getProductQuantityInWarehouse(productId, warehouseId);
        return availableQuantity >= requiredQuantity;
    }
    
    /**
     * Checks if a product is available across all warehouses with sufficient quantity
     * @param productId The ID of the product
     * @param requiredQuantity The required quantity
     * @return true if the product is available with sufficient quantity, false otherwise
     */
    public boolean isProductAvailable(int productId, int requiredQuantity) {
        int totalQuantity = getTotalProductQuantity(productId);
        return totalQuantity >= requiredQuantity;
    }
    
    /**
     * Gets the warehouses where a product is available
     * @param productId The ID of the product
     * @return Vector of ProductWarehouses objects where the product has quantity > 0
     */
    public Vector<ProductWarehouses> getWarehousesWithProduct(int productId) {
        String sql = "SELECT * FROM ProductWarehouses WHERE ProductID = " + productId + " AND quantity > 0";
        return getProductWarehouses(sql);
    }
    
    /**
     * Gets the warehouses where a product is available with sufficient quantity
     * @param productId The ID of the product
     * @param requiredQuantity The required quantity
     * @return Vector of ProductWarehouses objects where the product has sufficient quantity
     */
    public Vector<ProductWarehouses> getWarehousesWithSufficientProduct(int productId, int requiredQuantity) {
        String sql = "SELECT * FROM ProductWarehouses WHERE ProductID = " + productId 
                + " AND quantity >= " + requiredQuantity;
        return getProductWarehouses(sql);
    }
    
    /**
     * Removes all warehouse records for a product
     * @param productId The ID of the product
     * @return Number of rows affected
     */
    public int removeAllProductWarehouseRecords(int productId) {
        int n = 0;
        String sql = "DELETE FROM ProductWarehouses WHERE ProductID = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes all product records for a warehouse
     * @param warehouseId The ID of the warehouse
     * @return Number of rows affected
     */
    public int removeAllWarehouseProductRecords(int warehouseId) {
        int n = 0;
        String sql = "DELETE FROM ProductWarehouses WHERE warehousesID = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, warehouseId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets the count of products in a warehouse
     * @param warehouseId The ID of the warehouse
     * @return Count of products in the warehouse
     */
    public int getProductCountInWarehouse(int warehouseId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count FROM ProductWarehouses WHERE warehousesID = ? AND quantity > 0";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, warehouseId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Gets the count of warehouses where a product is stored
     * @param productId The ID of the product
     * @return Count of warehouses where the product is stored
     */
    public int getWarehouseCountForProduct(int productId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count FROM ProductWarehouses WHERE ProductID = ? AND quantity > 0";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Transfers product quantity from one warehouse to another
     * @param productId The ID of the product
     * @param sourceWarehouseId The ID of the source warehouse
     * @param destinationWarehouseId The ID of the destination warehouse
     * @param quantity The quantity to transfer
     * @return 1 if successful, 0 if failed, -1 if insufficient quantity
     */
    public int transferProductBetweenWarehouses(int productId, int sourceWarehouseId, 
            int destinationWarehouseId, int quantity) {
        // Check if source has enough quantity
        if (!isProductAvailableInWarehouse(productId, sourceWarehouseId, quantity)) {
            return -1; // Insufficient quantity
        }
        
        // Begin transaction
        try {
            conn.setAutoCommit(false);
            
            // Decrease from source
            int decreaseResult = decreaseProductQuantity(productId, sourceWarehouseId, quantity);
            if (decreaseResult <= 0) {
                conn.rollback();
                conn.setAutoCommit(true);
                return 0;
            }
            
            // Increase in destination
            int increaseResult = increaseProductQuantity(productId, destinationWarehouseId, quantity);
            if (increaseResult <= 0) {
                conn.rollback();
                conn.setAutoCommit(true);
                return 0;
            }
            
            // Commit transaction
            conn.commit();
            conn.setAutoCommit(true);
            return 1;
            
        } catch (SQLException ex) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(DAOProductWarehouses.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    /**
     * Gets products with low stock in a warehouse (quantity below threshold)
     * @param warehouseId The ID of the warehouse
     * @param threshold The threshold quantity
     * @return Vector of ProductWarehouses objects with quantity below threshold
     */
    public Vector<ProductWarehouses> getLowStockProductsInWarehouse(int warehouseId, int threshold) {
        String sql = "SELECT * FROM ProductWarehouses WHERE warehousesID = " + warehouseId 
                + " AND quantity > 0 AND quantity < " + threshold;
        return getProductWarehouses(sql);
    }
    
    /**
     * Gets products with zero stock in a warehouse
     * @param warehouseId The ID of the warehouse
     * @return Vector of ProductWarehouses objects with zero quantity
     */
    public Vector<ProductWarehouses> getOutOfStockProductsInWarehouse(int warehouseId) {
        String sql = "SELECT * FROM ProductWarehouses WHERE warehousesID = " + warehouseId 
                + " AND quantity = 0";
        return getProductWarehouses(sql);
    }
    
    /**
     * Main method for testing the DAOProductWarehouses functionality
     */
    public static void main(String[] args) {
        DAOProductWarehouses dao = new DAOProductWarehouses();
        
        // Test getting all product warehouses
        System.out.println("All product warehouses:");
        Vector<ProductWarehouses> productWarehouses = dao.getAllProductWarehouses();
        for (ProductWarehouses pw : productWarehouses) {
            System.out.println(pw);
        }
        
        // Test inserting a new product warehouse record
        ProductWarehouses newProductWarehouse = new ProductWarehouses(
                UUID.randomUUID(), 1, 1, 100);
        int result = dao.insertProductWarehouses(newProductWarehouse);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting product warehouses by product ID
        System.out.println("\nProduct warehouses for product ID 1:");
        Vector<ProductWarehouses> productWarehousesForProduct = dao.getProductWarehousesByProductId(1);
        for (ProductWarehouses pw : productWarehousesForProduct) {
            System.out.println(pw);
        }
        
        // Test getting product warehouses by warehouse ID
        System.out.println("\nProduct warehouses for warehouse ID 1:");
        Vector<ProductWarehouses> productWarehousesForWarehouse = dao.getProductWarehousesByWarehouseId(1);
        for (ProductWarehouses pw : productWarehousesForWarehouse) {
            System.out.println(pw);
        }
        
        // Test getting total product quantity
        int totalQuantity = dao.getTotalProductQuantity(1);
        System.out.println("\nTotal quantity for product ID 1: " + totalQuantity);
        
        // Test increasing product quantity
        System.out.println("\nIncreasing quantity for product ID 1 in warehouse ID 1 by 50:");
        int increaseResult = dao.increaseProductQuantity(1, 1, 50);
        System.out.println("Increase result: " + (increaseResult > 0 ? "Success" : "Failed"));
        
        // Test decreasing product quantity
        System.out.println("\nDecreasing quantity for product ID 1 in warehouse ID 1 by 25:");
        int decreaseResult = dao.decreaseProductQuantity(1, 1, 25);
        System.out.println("Decrease result: " + (decreaseResult > 0 ? "Success" : "Failed"));
        
        // Test checking product availability
        boolean isAvailable = dao.isProductAvailableInWarehouse(1, 1, 50);
        System.out.println("\nIs product ID 1 available in warehouse ID 1 with quantity 50? " + isAvailable);
        
        // Test getting warehouses with product
        System.out.println("\nWarehouses with product ID 1:");
        Vector<ProductWarehouses> warehousesWithProduct = dao.getWarehousesWithProduct(1);
        for (ProductWarehouses pw : warehousesWithProduct) {
            System.out.println(pw);
        }
        
        // Test transferring product between warehouses
        System.out.println("\nTransferring 10 units of product ID 1 from warehouse ID 1 to warehouse ID 2:");
        int transferResult = dao.transferProductBetweenWarehouses(1, 1, 2, 10);
        System.out.println("Transfer result: " + 
                (transferResult > 0 ? "Success" : (transferResult == -1 ? "Insufficient quantity" : "Failed")));
    }
}
