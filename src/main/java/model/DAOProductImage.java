/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.ProductImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DAOProductImage extends DBConnect {
    
    /**
     * Updates an existing product image in the database
     * @param productImage The product image object with updated information
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductImage(ProductImage productImage) {
        int n = 0;
        String sql = "UPDATE [ProductImage] SET "
                + "[ProductID] = ?, "
                + "[imgUrl] = ?, "
                + "[status] = ? "
                + "WHERE Productimg = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productImage.getProductID());
            pre.setString(2, productImage.getImgUrl());
            pre.setInt(3, productImage.getStatus());
            pre.setInt(4, productImage.getProductimg());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Inserts a new product image into the database
     * @param productImage The product image object to insert
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int insertProductImage(ProductImage productImage) {
        int n = 0;
        String sql = "INSERT INTO [ProductImage]("
                + "[ProductID], "
                + "[imgUrl], "
                + "[status]) "
                + "VALUES(?, ?, ?)";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pre.setInt(1, productImage.getProductID());
            pre.setString(2, productImage.getImgUrl());
            pre.setInt(3, productImage.getStatus());
            
            n = pre.executeUpdate();
            
            // Get the generated ID
            if (n > 0) {
                ResultSet rs = pre.getGeneratedKeys();
                if (rs.next()) {
                    productImage.setProductimg(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Removes a product image from the database
     * @param productImgId The ID of the product image to remove
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int removeProductImage(int productImgId) {
        int n = 0;
        String sql = "DELETE FROM ProductImage WHERE Productimg = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productImgId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Logically deletes a product image by setting its status to 0
     * @param productImgId The ID of the product image to logically delete
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int logicalDeleteProductImage(int productImgId) {
        int n = 0;
        String sql = "UPDATE ProductImage SET status = 0 WHERE Productimg = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productImgId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Retrieves product images from the database based on the provided SQL query
     * @param sql The SQL query to execute
     * @return Vector of ProductImage objects
     */
    public Vector<ProductImage> getProductImages(String sql) {
        Vector<ProductImage> vector = new Vector<ProductImage>();
        
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            
            while (rs.next()) {
                int productImgId = rs.getInt("Productimg");
                int productId = rs.getInt("ProductID");
                String imgUrl = rs.getString("imgUrl");
                int status = rs.getInt("status");
                
                ProductImage productImage = new ProductImage(productImgId, productId, imgUrl, status);
                
                vector.add(productImage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return vector;
    }
    
    /**
     * Retrieves all product images from the database
     * @return Vector of all ProductImage objects
     */
    public Vector<ProductImage> getAllProductImages() {
        return getProductImages("SELECT * FROM ProductImage");
    }
    
    /**
     * Retrieves active product images from the database (status = 1)
     * @return Vector of active ProductImage objects
     */
    public Vector<ProductImage> getActiveProductImages() {
        return getProductImages("SELECT * FROM ProductImage WHERE status = 1");
    }
    
    /**
     * Retrieves a product image by its ID
     * @param productImgId The ID of the product image to retrieve
     * @return The ProductImage object if found, null otherwise
     */
    public ProductImage getProductImageById(int productImgId) {
        String sql = "SELECT * FROM ProductImage WHERE Productimg = " + productImgId;
        Vector<ProductImage> productImages = getProductImages(sql);
        
        if (productImages.size() > 0) {
            return productImages.get(0);
        }
        
        return null;
    }
    
    /**
     * Retrieves product images by product ID
     * @param productId The ID of the product
     * @return Vector of ProductImage objects for the specified product
     */
    public Vector<ProductImage> getProductImagesByProductId(int productId) {
        String sql = "SELECT * FROM ProductImage WHERE ProductID = " + productId;
        return getProductImages(sql);
    }
    
    /**
     * Retrieves active product images by product ID
     * @param productId The ID of the product
     * @return Vector of active ProductImage objects for the specified product
     */
    public Vector<ProductImage> getActiveProductImagesByProductId(int productId) {
        String sql = "SELECT * FROM ProductImage WHERE ProductID = " + productId + " AND status = 1";
        return getProductImages(sql);
    }
    
    /**
     * Updates the status of a product image
     * @param productImgId The ID of the product image to update
     * @param status The new status value
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductImageStatus(int productImgId, int status) {
        int n = 0;
        String sql = "UPDATE ProductImage SET status = ? WHERE Productimg = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, status);
            pre.setInt(2, productImgId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Gets the primary image for a product (first active image)
     * @param productId The ID of the product
     * @return The primary ProductImage object if found, null otherwise
     */
    public ProductImage getPrimaryProductImage(int productId) {
        String sql = "SELECT TOP 1 * FROM ProductImage WHERE ProductID = " + productId + " AND status = 1";
        Vector<ProductImage> productImages = getProductImages(sql);
        
        if (productImages.size() > 0) {
            return productImages.get(0);
        }
        
        return null;
    }
    
    /**
     * Gets the count of images for a product
     * @param productId The ID of the product
     * @return Count of images for the product
     */
    public int getProductImageCount(int productId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count FROM ProductImage WHERE ProductID = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Gets the count of active images for a product
     * @param productId The ID of the product
     * @return Count of active images for the product
     */
    public int getActiveProductImageCount(int productId) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count FROM ProductImage WHERE ProductID = ? AND status = 1";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            ResultSet rs = pre.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return count;
    }
    
    /**
     * Removes all images for a product
     * @param productId The ID of the product
     * @return Number of rows affected
     */
    public int removeAllProductImages(int productId) {
        int n = 0;
        String sql = "DELETE FROM ProductImage WHERE ProductID = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Logically deletes all images for a product by setting their status to 0
     * @param productId The ID of the product
     * @return Number of rows affected
     */
    public int logicalDeleteAllProductImages(int productId) {
        int n = 0;
        String sql = "UPDATE ProductImage SET status = 0 WHERE ProductID = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, productId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Checks if a product has at least one image
     * @param productId The ID of the product
     * @return true if the product has at least one image, false otherwise
     */
    public boolean hasProductImages(int productId) {
        return getProductImageCount(productId) > 0;
    }
    
    /**
     * Checks if a product has at least one active image
     * @param productId The ID of the product
     * @return true if the product has at least one active image, false otherwise
     */
    public boolean hasActiveProductImages(int productId) {
        return getActiveProductImageCount(productId) > 0;
    }
    
    /**
     * Updates the image URL of a product image
     * @param productImgId The ID of the product image
     * @param newImgUrl The new image URL
     * @return Number of rows affected (1 if successful, 0 if not)
     */
    public int updateProductImageUrl(int productImgId, String newImgUrl) {
        int n = 0;
        String sql = "UPDATE ProductImage SET imgUrl = ? WHERE Productimg = ?";
        
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, newImgUrl);
            pre.setInt(2, productImgId);
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOProductImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return n;
    }
    
    /**
     * Main method for testing the DAOProductImage functionality
     */
    public static void main(String[] args) {
        DAOProductImage dao = new DAOProductImage();
        
        // Test getting all product images
        System.out.println("All product images:");
        Vector<ProductImage> productImages = dao.getAllProductImages();
        for (ProductImage productImage : productImages) {
            System.out.println(productImage);
        }
        
        // Test inserting a new product image
        ProductImage newProductImage = new ProductImage(0, 1, "test_image.jpg", 1);
        int result = dao.insertProductImage(newProductImage);
        System.out.println("\nInsert result: " + (result > 0 ? "Success" : "Failed"));
        if (result > 0) {
            System.out.println("New product image ID: " + newProductImage.getProductimg());
        }
        
        // Test getting product images by product ID
        System.out.println("\nProduct images for product ID 1:");
        Vector<ProductImage> productImagesForProduct = dao.getProductImagesByProductId(1);
        for (ProductImage productImage : productImagesForProduct) {
            System.out.println(productImage);
        }
        
        // Test getting primary product image
        ProductImage primaryImage = dao.getPrimaryProductImage(1);
        System.out.println("\nPrimary image for product ID 1: " + (primaryImage != null ? primaryImage : "No primary image found"));
        
        // Test getting image count
        int imageCount = dao.getProductImageCount(1);
        System.out.println("\nImage count for product ID 1: " + imageCount);
        
        // Test updating image URL
        if (primaryImage != null) {
            int updateResult = dao.updateProductImageUrl(primaryImage.getProductimg(), "updated_image.jpg");
            System.out.println("\nUpdate image URL result: " + (updateResult > 0 ? "Success" : "Failed"));
        }
        
        // Test logical delete
        if (newProductImage.getProductimg() > 0) {
            int deleteResult = dao.logicalDeleteProductImage(newProductImage.getProductimg());
            System.out.println("\nLogical delete result: " + (deleteResult > 0 ? "Success" : "Failed"));
        }
    }
}
