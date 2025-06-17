/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
//Database Access Object

import entity.Product_Batches_Export;
import java.math.BigDecimal;
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
public class DAOProductBatchesExport extends DBConnect {

    /**
     * Removes a product batch export record from the database
     * @param productBatchesId The UUID of the product batch export to remove
     * @return Number of rows affected (1 if successful, 0 if not, -1 if foreign key constraint)
     */
    public int removeProductBatchExport(UUID productBatchesId) {
        int n = 0;
        String sql = "DELETE FROM Product_Batches_Export WHERE product_batches_id=?";
        try {
            // Check if there are any foreign key constraints before deleting
            // If needed, add code here to check foreign key constraints
            
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, productBatchesId.toString());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Updates a product batch export record in the database
     * @param productBatchExport The product batch export object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductBatchExport(Product_Batches_Export productBatchExport) {
        int n = 0;
        var sql ="UPDATE [Product_Batches_Export]\n" +
"                      SET [product_id] = ? ,\n" +
"                          [batch_id] = ?,\n" +
"                          [Quantity] = ?,\n" +
"                          [EntryPrice] = ?,\n" +
"                          [status] = ?\n" +
"                    WHERE product_batches_id=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productBatchExport.getProduct_id());
            pre.setString(2, productBatchExport.getBatch_id().toString());
            pre.setInt(3, productBatchExport.getQuantity());
            pre.setBigDecimal(4, productBatchExport.getEntryPrice());
            pre.setInt(5, productBatchExport.getStatus());
            pre.setString(6, productBatchExport.getProduct_batches_id().toString());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Inserts a new product batch export record into the database using PreparedStatement
     * @param productBatchExport The product batch export object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertProductBatchExport(Product_Batches_Export productBatchExport) {
        int n = 0;
        String sql = "INSERT INTO [Product_Batches_Export] ([product_batches_id],"
                + "[product_id],[batch_id]\n"
                + ",[Quantity],[EntryPrice],[status])\n"
                + "VALUES(?,?,?,?,?,?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Generate UUID if not provided
            if (productBatchExport.getProduct_batches_id() == null) {
                productBatchExport.setProduct_batches_id(UUID.randomUUID());
            }
            
            pre.setString(1, productBatchExport.getProduct_batches_id().toString());
            pre.setInt(2, productBatchExport.getProduct_id());
            pre.setString(3, productBatchExport.getBatch_id().toString());
            pre.setInt(4, productBatchExport.getQuantity());
            pre.setBigDecimal(5, productBatchExport.getEntryPrice());
            pre.setInt(6, productBatchExport.getStatus());

            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    /**
     * Adds a new product batch export record into the database using Statement
     * @param productBatchExport The product batch export object to add
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int addProductBatchExport(Product_Batches_Export productBatchExport) {
        int n = 0;
        
        // Generate UUID if not provided
        if (productBatchExport.getProduct_batches_id() == null) {
            productBatchExport.setProduct_batches_id(UUID.randomUUID());
        }
        
        String sql = "INSERT INTO [Product_Batches_Export] ([product_batches_id],[product_id],[batch_id]\n"
                + ",[Quantity],[EntryPrice],[status])\n"
                + "VALUES('" + productBatchExport.getProduct_batches_id().toString() + "'\n"
                + "," + productBatchExport.getProduct_id() + "\n"
                + ",'" + productBatchExport.getBatch_id().toString() + "'\n"
                + "," + productBatchExport.getQuantity() + "\n"
                + "," + productBatchExport.getEntryPrice() + "\n"
                + "," + productBatchExport.getStatus() + ")";
        
        System.out.println(sql);
        try {
            Statement state = conn.createStatement();
            n = state.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return n;
    }

    /**
     * Retrieves product batch export records from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of Product_Batches_Export objects
     */
    public Vector<Product_Batches_Export> getProductBatchExports(String sql) {
        Vector<Product_Batches_Export> vector = new Vector<Product_Batches_Export>();
        try {
            Statement state = conn.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                UUID productBatchesId = UUID.fromString(rs.getString("product_batches_id"));
                int productId = rs.getInt("product_id");
                UUID batchId = UUID.fromString(rs.getString("batch_id"));
                int quantity = rs.getInt("Quantity");
                BigDecimal entryPrice = rs.getBigDecimal("EntryPrice");
                int status = rs.getInt("status");
                
                Product_Batches_Export productBatchExport = new Product_Batches_Export(
                        productBatchesId, productId, batchId, quantity, entryPrice, status);
                vector.add(productBatchExport);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    
    /**
     * Retrieves all product batch export records from the database
     * @return Vector of all Product_Batches_Export objects
     */
    public Vector<Product_Batches_Export> getAllProductBatchExports() {
        return getProductBatchExports("SELECT * FROM Product_Batches_Export");
    }
    
    /**
     * Retrieves active product batch export records from the database (status = 1)
     * @return Vector of active Product_Batches_Export objects
     */
    public Vector<Product_Batches_Export> getActiveProductBatchExports() {
        return getProductBatchExports("SELECT * FROM Product_Batches_Export WHERE status = 1");
    }
    
    /**
     * Retrieves a product batch export record by its ID
     * @param productBatchesId The UUID of the product batch export record to retrieve
     * @return The Product_Batches_Export object if found, null otherwise
     */
    public Product_Batches_Export getProductBatchExportById(UUID productBatchesId) {
        String sql = "SELECT * FROM Product_Batches_Export WHERE product_batches_id = '" 
                + productBatchesId.toString() + "'";
        Vector<Product_Batches_Export> productBatchExports = getProductBatchExports(sql);
        
        if (productBatchExports.size() > 0) {
            return productBatchExports.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves product batch export records by product ID
     * @param productId The ID of the product
     * @return Vector of Product_Batches_Export objects for the specified product
     */
    public Vector<Product_Batches_Export> getProductBatchExportsByProductId(int productId) {
        String sql = "SELECT * FROM Product_Batches_Export WHERE product_id = " + productId;
        return getProductBatchExports(sql);
    }
    
    /**
     * Retrieves active product batch export records by product ID
     * @param productId The ID of the product
     * @return Vector of active Product_Batches_Export objects for the specified product
     */
    public Vector<Product_Batches_Export> getActiveProductBatchExportsByProductId(int productId) {
        String sql = "SELECT * FROM Product_Batches_Export WHERE product_id = " + productId + " AND status = 1";
        return getProductBatchExports(sql);
    }
    
    /**
     * Retrieves product batch export records by batch ID
     * @param batchId The UUID of the batch
     * @return Vector of Product_Batches_Export objects for the specified batch
     */
    public Vector<Product_Batches_Export> getProductBatchExportsByBatchId(UUID batchId) {
        String sql = "SELECT * FROM Product_Batches_Export WHERE batch_id = '" + batchId.toString() + "'";
        return getProductBatchExports(sql);
    }
    
    /**
     * Updates the status of a product batch export record
     * @param productBatchesId The UUID of the product batch export record
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductBatchExportStatus(UUID productBatchesId, int status) {
        int n = 0;
        String sql = "UPDATE Product_Batches_Export SET status = ? WHERE product_batches_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setString(2, productBatchesId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Updates the quantity of a product batch export record
     * @param productBatchesId The UUID of the product batch export record
     * @param quantity The new quantity value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductBatchExportQuantity(UUID productBatchesId, int quantity) {
        int n = 0;
        String sql = "UPDATE Product_Batches_Export SET Quantity = ? WHERE product_batches_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, quantity);
            pre.setString(2, productBatchesId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets the total quantity of a product across all batches
     * @param productId The ID of the product
     * @return Total quantity of the product
     */
    public int getTotalProductQuantity(int productId) {
        int totalQuantity = 0;
        String sql = "SELECT SUM(Quantity) as totalQuantity FROM Product_Batches_Export "
                + "WHERE product_id = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next() && rs.getObject("totalQuantity") != null) {
                totalQuantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return totalQuantity;
    }
    
    /**
     * Gets the average entry price of a product across all batches
     * @param productId The ID of the product
     * @return Average entry price of the product
     */
    public BigDecimal getAverageProductEntryPrice(int productId) {
        BigDecimal averagePrice = BigDecimal.ZERO;
        String sql = "SELECT AVG(EntryPrice) as avgPrice FROM Product_Batches_Export "
                + "WHERE product_id = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next() && rs.getObject("avgPrice") != null) {
                averagePrice = rs.getBigDecimal("avgPrice");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return averagePrice;
    }
    
    /**
     * Removes all batch records for a product
     * @param productId The ID of the product
     * @return Number of rows affected
     */
    public int removeAllProductBatchRecords(int productId) {
        int n = 0;
        String sql = "DELETE FROM Product_Batches_Export WHERE product_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Logically deletes all batch records for a product by setting status to 0
     * @param productId The ID of the product
     * @return Number of rows affected
     */
    public int logicalDeleteAllProductBatchRecords(int productId) {
        int n = 0;
        String sql = "UPDATE Product_Batches_Export SET status = 0 WHERE product_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes all product records for a batch
     * @param batchId The UUID of the batch
     * @return Number of rows affected
     */
    public int removeAllBatchProductRecords(UUID batchId) {
        int n = 0;
        String sql = "DELETE FROM Product_Batches_Export WHERE batch_id = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, batchId.toString());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductBatchesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Main method for testing the DAOProductBatchesExport functionality
     */
    public static void main(String[] args) {
        DAOProductBatchesExport dao = new DAOProductBatchesExport();
        
        // Test getting all product batch exports
        System.out.println("All product batch exports:");
        Vector<Product_Batches_Export> productBatchExports = dao.getAllProductBatchExports();
        for (Product_Batches_Export pbe : productBatchExports) {
            System.out.println(pbe);
        }
        
        // Test inserting a new product batch export
        UUID batchId = UUID.randomUUID();
        Product_Batches_Export newProductBatchExport = new Product_Batches_Export(
                UUID.randomUUID(), 1, batchId, 100, new BigDecimal("10.50"), 1);
        int result = dao.insertProductBatchExport(newProductBatchExport);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        
        // Test getting product batch exports by product ID
        System.out.println("\nProduct batch exports for product ID 1:");
        Vector<Product_Batches_Export> productBatchExportsForProduct = dao.getProductBatchExportsByProductId(1);
        for (Product_Batches_Export pbe : productBatchExportsForProduct) {
            System.out.println(pbe);
        }
        
        // Test getting product batch exports by batch ID
        System.out.println("\nProduct batch exports for batch ID " + batchId + ":");
        Vector<Product_Batches_Export> productBatchExportsForBatch = dao.getProductBatchExportsByBatchId(batchId);
        for (Product_Batches_Export pbe : productBatchExportsForBatch) {
            System.out.println(pbe);
        }
        
        // Test updating product batch export
        if (!productBatchExportsForProduct.isEmpty()) {
            Product_Batches_Export pbeToUpdate = productBatchExportsForProduct.get(0);
            pbeToUpdate.setQuantity(pbeToUpdate.getQuantity() + 50);
            int updateResult = dao.updateProductBatchExport(pbeToUpdate);
            System.out.println("\nUpdate result: " + (updateResult > 0 ? "Success" : "Failed"));
        }
        
        // Test getting total product quantity
        int totalQuantity = dao.getTotalProductQuantity(1);
        System.out.println("\nTotal quantity for product ID 1: " + totalQuantity);
        
        // Test getting average product entry price
        BigDecimal avgPrice = dao.getAverageProductEntryPrice(1);
        System.out.println("\nAverage entry price for product ID 1: " + avgPrice);
        
        // Test updating product batch export status
        if (!productBatchExportsForProduct.isEmpty()) {
            Product_Batches_Export pbeToUpdateStatus = productBatchExportsForProduct.get(0);
            int statusUpdateResult = dao.updateProductBatchExportStatus(
                    pbeToUpdateStatus.getProduct_batches_id(), 
                    pbeToUpdateStatus.getStatus() == 1 ? 0 : 1);
            System.out.println("\nStatus update result: " + (statusUpdateResult > 0 ? "Success" : "Failed"));
        }
        
        // Test getting active product batch exports
        System.out.println("\nActive product batch exports:");
        Vector<Product_Batches_Export> activeProductBatchExports = dao.getActiveProductBatchExports();
        for (Product_Batches_Export pbe : activeProductBatchExports) {
            System.out.println(pbe);
        }
        
        // Test removing a product batch export
        // Uncomment to test deletion
        /*
        if (!productBatchExportsForProduct.isEmpty()) {
            Product_Batches_Export pbeToRemove = productBatchExportsForProduct.get(0);
            int removeResult = dao.removeProductBatchExport(pbeToRemove.getProduct_batches_id());
            System.out.println("\nRemove result: " + (removeResult > 0 ? "Success" : "Failed"));
        }
        */
    }
}
